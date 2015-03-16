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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.comm.utils;

import net.engio.mbassy.listener.Handler;
import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._AnswerReceiver;
import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.comm.out.event.task.TaskStateEventOut;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.states.TaskState;
import org.altherian.hboxc._EventReceiver;
import org.altherian.hboxc.back._Backend;
import org.altherian.hboxc.event.EventManager;
import org.altherian.hboxc.event.backend.BackendStateEvent;
import org.altherian.hboxc.exception.ServerDisconnectedException;
import org.altherian.tool.logging.Logger;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public final class Transaction implements _AnswerReceiver, _EventReceiver {

   private _Backend b;

   private Long startTime;
   private Long endTime;
   private volatile Answer start;
   private Deque<Answer> mainQ;
   private volatile Answer end;
   private volatile Long lastMessageTime;

   private final Request request;
   private volatile String taskId;
   private TaskStateEventOut evOut;
   private volatile List<String> finishedTaskId;

   private String internalError = "";

   public Transaction(_Backend b, Request request) {
      this.b = b;
      this.request = request;
      mainQ = new LinkedList<Answer>();
      start = null;
      end = null;
      taskId = null;
      finishedTaskId = new ArrayList<String>();
      evOut = null;
   }

   private void init() {
      if (startTime != null) {
         throw new HyperboxRuntimeException("Transaction " + request.getExchangeId() + " has already been run");
      }
      startTime = System.currentTimeMillis();
      lastMessageTime = startTime;
   }

   public Answer getHeader() {
      return start;
   }

   public Answer getFooter() {
      return end;
   }

   public Deque<Answer> getBody() {
      return new LinkedList<Answer>(mainQ);
   }

   public String getError() {
      // TODO improve, maybe use a special Enum for "system" bindings?
      if ((end != null) && end.has(Exception.class.getName())) {
         return (String) end.get(Exception.class.getName());
      } else {
         return internalError;
      }
   }

   public <T> List<T> extractItems(Class<T> toExtract) {
      List<T> list = new ArrayList<T>();
      for (Answer ans : mainQ) {
         if (ans.has(toExtract)) {
            list.add(ans.get(toExtract));
         }
      }
      return list;
   }

   public <T> T extractItem(Class<T> toExtract) {
      if (mainQ.size() == 0) {
         throw new HyperboxRuntimeException("No data was sent by the server");
      }
      return mainQ.getFirst().get(toExtract);
   }

   /**
    * Will wait until the end of the transaction and return the final status. It will contain the body being all answers except for the leading &
    * trailing ones.<br/>
    * More precisely, will include all Answers where <code>(isExchangeInProgress() && !isExchangeStarted() && !isExchangedFinished())</code><br/>
    * See getBody() for the data.
    *
    * @return boolean True if the transaction was successful, false if not.
    * @throws ServerDisconnectedException If the server disconnected during the transaction
    */
   public boolean sendAndWait() throws ServerDisconnectedException {

      try {
         init();
         b.setAnswerReceiver(request.getExchangeId(), this);
         b.putRequest(request);
         synchronized (this) {
            while (end == null) {
               if (!b.isConnected()) {
                  throw new ServerDisconnectedException();
               }
               try {
                  wait(500);
                  if ((start != null) && (System.currentTimeMillis() > (lastMessageTime + 15000))) {
                     throw new HyperboxRuntimeException("Transaction " + request.getExchangeId() + " [" + request.getName() + "] has timeout after 15 sec");
                  }
               } catch (InterruptedException e) {
                  throw new HyperboxRuntimeException("Transaction " + request.getExchangeId() + " has been canceled");
               }
            }
         }
         return !hasFailed();
      } finally {
         endTime = System.currentTimeMillis();
         Logger.debug("Transaction ID " + request.getName() + " took " + (endTime - startTime) + "ms");
      }
   }

   public boolean sendAndWaitForTask() throws ServerDisconnectedException {

      Logger.verbose("Waiting until task is finished");
      EventManager.get().register(this);
      try {
         if (!sendAndWait()) {
            return false;
         }
         taskId = extractItem(TaskOut.class).getId();
         synchronized (this) {
            while (!finishedTaskId.contains(taskId)) {
               if (!b.isConnected()) {
                  throw new ServerDisconnectedException();
               }
               try {
                  wait(500);
               } catch (InterruptedException e) {
                  throw new HyperboxRuntimeException("Transaction " + request.getExchangeId() + " has been canceled");
               }
            }
         }
         Logger.verbose("Task seems finished");
         // TODO improve and add tag to the TaskState
         if (evOut.getTask().getState().equals(TaskState.Canceled) || evOut.getTask().getState().equals(TaskState.Failed)) {
            return false;
         } else {
            return true;
         }
      } finally {
         endTime = System.currentTimeMillis();
         Logger.debug("Transaction took " + (endTime - startTime) + "ms");
         EventManager.get().unregister(this);
      }
   }

   public boolean hasFailed() {
      return (end != null) && end.hasFailed();
   }

   @Override
   public void putAnswer(Answer ans) {

      if (ans.getExchangeId().contentEquals(request.getExchangeId())) {
         lastMessageTime = System.currentTimeMillis();
         if (ans.isExchangeStarted()) {
            Logger.debug("Got start message for ExchangeID " + ans.getExchangeId());
            start = ans;
         }
         if (ans.isExchangeInProgress()) {
            Logger.debug("Got progress message for ExchangeID " + ans.getExchangeId());
            mainQ.offer(ans);
         }
         if (ans.isExchangedFinished()) {
            Logger.debug("Got final message for ExchangeID " + ans.getExchangeId());
            end = ans;
         }
         synchronized (this) {
            notifyAll();
         }
      } else {
         Logger.error("Received message from another request : " + request.getExchangeId() + " vs " + ans.getExchangeId());
      }
   }

   @Handler
   public void putBackendDisconnect(BackendStateEvent ev) {

      if (ev.getBackend().equals(b)) {
         synchronized (this) {
            notifyAll();
         }
      }
   }

   @Handler
   @Override
   public void post(EventOut evOut) {

      Logger.verbose(evOut.toString());
      if (evOut instanceof TaskStateEventOut) {
         TaskStateEventOut tsEvOut = (TaskStateEventOut) evOut;
         Logger.debug("Got event for TaskState: " + tsEvOut.getTask().getState());
         if (tsEvOut.getTask().getState().isFinishing()) {
            finishedTaskId.add(tsEvOut.getTask().getId());
            if (tsEvOut.getTask().getId().contentEquals(taskId)) {
               this.evOut = tsEvOut;
               synchronized (this) {
                  notifyAll();
               }
            }
         }
      }
   }

}
