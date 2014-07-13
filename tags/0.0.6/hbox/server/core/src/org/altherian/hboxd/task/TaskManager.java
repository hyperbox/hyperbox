/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * 
 * http://hyperbox.altherian.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxd.task;

import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.exception.HyperboxCommunicationException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.states.ServerState;
import org.altherian.hbox.states.TaskQueueEvents;
import org.altherian.hbox.states.TaskState;
import org.altherian.hboxd.comm.io.factory.TaskIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action._HyperboxAction;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.system.SystemStateEvent;
import org.altherian.hboxd.event.task.TaskQueueEvent;
import org.altherian.hboxd.security.SecurityContext;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public final class TaskManager implements _TaskManager {
   
   private volatile boolean running;
   private long taskId = 1;
   private volatile _Task currentTask;
   
   private Map<String, _Task> tasks;
   private BlockingQueue<_Task> taskQueue;
   private BlockingDeque<_Task> finishedTaskDeque;
   
   private TaskQueueWorker queueWorker = new TaskQueueWorker();
   private Thread worker;
   
   private _Hyperbox hbox;
   
   @Override
   public void start(_Hyperbox hbox) {
      Logger.track();
      
      this.hbox = hbox;
      
      taskQueue = new LinkedBlockingQueue<_Task>();
      finishedTaskDeque = new LinkedBlockingDeque<_Task>(10);
      Logger.debug("Task history size: " + finishedTaskDeque.remainingCapacity());
      tasks = new HashMap<String, _Task>();
      worker = new Thread(queueWorker, "TaskMgrQW");
      SecurityContext.addAdminThread(worker);
      
      EventManager.register(this);
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      running = false;
      if ((worker != null) && !Thread.currentThread().equals(worker)) {
         worker.interrupt();
         try {
            worker.join(1000);
         } catch (InterruptedException e) {
            Logger.debug("Error while waiting for Task Manager worker thread to finish : " + e.getMessage());
            Logger.exception(e);
         }
      }
   }
   
   @Override
   public void process(Request req) {
      Logger.track();
      try {
         Logger.debug("Received Request #" + req.getExchangeId() + " [" + req.getCommand() + ":" + req.getName() + "] "
               + "from Client #" + SessionContext.getClient().getId() + " ("
               + SessionContext.getClient().getAddress() + ")"
               + " under " + SecurityContext.getUser().getName());
         hbox.getSecurityManager().authorize(req);
         _HyperboxAction ac = hbox.getActionManager().get(req);
         if (ac.isQueueable() && req.isQueueable()) {
            queueWorker.queue(req, ac);
         } else {
            Logger.debug("Immediate execute of Request #" + req.getExchangeId() + " [" + req.getCommand() + ":" + req.getName() + "]");
            TaskWorker.execute(req, ac, hbox);
         }
      } catch (HyperboxCommunicationException e) {
         Logger.debug("Communication error : " + e.getMessage());
         SessionContext.getClient().putAnswer(new Answer(req, AnswerType.UNKNOWN, e));
      }
   }
   
   @Override
   public List<_Task> list() {
      List<_Task> taskList = new ArrayList<_Task>();
      if ((currentTask != null) && !taskQueue.contains(currentTask)) {
         taskList.add(currentTask);
      }
      for (_Task task : taskQueue) {
         taskList.add(task);
      }
      for (_Task task : finishedTaskDeque) {
         taskList.add(task);
      }
      return taskList;
   }
   
   @Override
   public void remove(String taskId) {
      Logger.track();
      
      _Task task = get(taskId);
      task.cancel();
      queueWorker.remove(task);
   }
   
   @Override
   public _Task get(String taskId) {
      if (!tasks.containsKey(taskId)) {
         throw new HyperboxRuntimeException("Unknown Task ID: " + taskId);
      }
      
      return tasks.get(taskId);
   }
   
   private static class TaskWorker {
      
      public static void execute(Request req, _HyperboxAction ca, _Hyperbox hbox) {
         try {
            Logger.debug("Processing Request #" + req.getExchangeId());
            
            SessionContext.getClient().putAnswer(new Answer(req, ca.getStartReturn()));
            try {
               ca.run(req, hbox);
               Logger.debug("Request #" + req.getExchangeId() + " [" + req.getCommand() + ":" + req.getName() + "]" + " succeeded.");
               SessionContext.getClient().putAnswer(new Answer(req, ca.getFinishReturn()));
            } catch (HyperboxRuntimeException e) {
               Logger.debug("Request #" + req.getExchangeId() + " [" + req.getCommand() + ":" + req.getName() + "]" + " failed: " + e.getMessage());
               SessionContext.getClient().putAnswer(new Answer(req, ca.getFailReturn(), e));
            }
         } catch (Throwable e) {
            Logger.debug("Server Error when executing #" + req.getExchangeId() + " [" + req.getCommand() + ":" + req.getName() + "]" + ": "
                  + e.getMessage());
            Logger.exception(e);
            SessionContext.getClient().putAnswer(new Answer(req, AnswerType.SERVER_ERROR, e));
         }
      }
      
   }
   
   private class TaskQueueWorker implements Runnable {
      
      private boolean add(_Task t) {
         Logger.track();
         
         if (!taskQueue.offer(t)) {
            return false;
         }
         tasks.put(t.getId(), t);
         t.queue();
         Logger.debug("Added Request #" + t.getRequest().getExchangeId() + " [" + t.getRequest().getCommand() + ":" + t.getRequest().getName() + "] to queue.");
         EventManager.post(new TaskQueueEvent(TaskQueueEvents.TaskAdded, t));
         return true;
      }
      
      // TODO use events to clean up the queues
      private void remove(_Task t) {
         Logger.track();
         
         if (t != null) {
            taskQueue.remove(t);
            if (!finishedTaskDeque.contains(t) && (finishedTaskDeque.remainingCapacity() == 0)) {
               _Task oldTask = finishedTaskDeque.pollLast();
               tasks.remove(oldTask.getId());
               Logger.debug("Removed Request #" + oldTask.getRequest().getExchangeId() + " [" + oldTask.getRequest().getCommand() + ":" + oldTask.getRequest().getName() + "] from queue.");
               EventManager.post(new TaskQueueEvent(TaskQueueEvents.TaskRemoved, oldTask));
            }
            finishedTaskDeque.offerFirst(t);
            Logger.debug("Archived Request #" + t.getRequest().getExchangeId() + " [" + t.getRequest().getCommand() + ":" + t.getRequest().getName()
                  + "]");
         }
      }
      
      public void queue(Request req, _HyperboxAction ca) {
         Logger.track();
         
         Logger.debug("Queueing Request #" + req.getExchangeId());
         SessionContext.getClient().putAnswer(new Answer(req, AnswerType.STARTED));
         // TODO do a better Task ID generator
         _Task t = new HyperboxTask(Long.toString(taskId++), ca, req, SecurityContext.getUser(), SessionContext.getClient(), hbox);
         if (add(t)) {
            SessionContext.getClient().putAnswer(new Answer(req, AnswerType.DATA, TaskIoFactory.get(t)));
            SessionContext.getClient().putAnswer(new Answer(req, AnswerType.QUEUED));
         } else {
            Logger.debug("Failed to queue Request - Queue is full");
            SessionContext.getClient().putAnswer(new Answer(req, AnswerType.SERVER_ERROR));
         }
      }
      
      @Override
      public void run() {
         Logger.track();
         
         running = true;
         Logger.verbose("Task Queue Worker started");
         
         while (running) {
            try {
               while ((taskQueue.peek() == null) || taskQueue.peek().getState().equals(TaskState.Created)) {
                  Thread.sleep(100);
               }
               currentTask = taskQueue.take();
               currentTask.start();
            } catch (InterruptedException e) {
               Logger.debug("Task Queue Worker was interupted, halting...");
               running = false;
            } catch (Throwable e) {
               Logger.error("Exception in Task #" + currentTask.getId() + " : " + e.getMessage());
            } finally {
               remove(currentTask);
               currentTask = null;
            }
         }
         Logger.info("Task Queue Worker halted");
      }
   }
   
   @Handler
   public void putSystemEvent(SystemStateEvent ev) {
      Logger.track();
      
      if (ServerState.Running.equals(ev.getState())) {
         worker.start();
      }
   }
   
   @Override
   public long getHistorySize() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public void setHistorySize(long size) {
      // TODO Auto-generated method stub
      
   }
   
}
