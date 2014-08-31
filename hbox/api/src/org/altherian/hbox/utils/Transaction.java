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

package org.altherian.hbox.utils;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._Client;
import org.altherian.hbox.comm._RequestReceiver;
import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.comm.out.event.task.TaskStateEventOut;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Transaction implements _Client {
   
   private _RequestReceiver recv;
   
   private volatile Answer start;
   private Deque<Answer> mainQ = new LinkedList<Answer>();
   private volatile Answer end;
   
   private volatile String requestId;
   private volatile String taskId;
   private volatile TaskStateEventOut eventOut;
   
   private volatile long lastMessage;
   
   public void setRecv(_RequestReceiver recv) {
      this.recv = recv;
   }
   
   private void init() {
      mainQ.clear();
      start = null;
      end = null;
      requestId = "";
      taskId = "";
      eventOut = null;
      lastMessage = System.currentTimeMillis();
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
   
   public String getError() {
      // TODO improve, maybe use a special Enum for "system" bindings?
      return (String) end.get(Exception.class.getName());
   }
   
   public boolean sendAndWait(Request r) {
      init();
      requestId = r.getExchangeId();
      recv.putRequest(r);
      synchronized (this) {
         while (end == null) {
            try {
               wait(1000);
               Logger.debug("Waited 1000 ms. Last message : " + lastMessage + " | Current time : " + System.currentTimeMillis());
               if ((System.currentTimeMillis() - lastMessage) > 15000) {
                  throw new HyperboxRuntimeException("Server did not answer in time - Timeout of 15 sec");
               }
            } catch (InterruptedException e) {
               throw new HyperboxRuntimeException("Transaction " + r.getExchangeId() + " has been canceled");
            }
         }
      }
      return !hasFailed();
   }
   
   public boolean sendAndWaitForTask(Request r) {
      Logger.verbose("Waiting until task is finished");
      // TODO add period check to see if the task is still running
      if (!sendAndWait(r)) {
         return false;
      }
      if (extractItem(TaskOut.class) != null) {
         taskId = extractItem(TaskOut.class).getId();
         synchronized (this) {
            while ((eventOut == null) || !eventOut.getTask().getState().isFinishing()) {
               try {
                  wait();
               } catch (InterruptedException e) {
                  throw new HyperboxRuntimeException("Transaction " + r.getExchangeId() + " has been canceled");
               }
            }
         }
         Logger.debug("Task seems finished");
         if (eventOut.getTask().getState().isFailing()) {
            return false;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
   
   public boolean hasFailed() {
      return ((end != null) && end.hasFailed()) || ((eventOut != null) && eventOut.getTask().getState().isFailing());
   }
   
   @Override
   public void putAnswer(Answer ans) {
      if (ans.getExchangeId().contentEquals(requestId)) {
         Logger.track();
         
         Logger.debug("Received answer " + ans.getName() + " : " + ans.getType());
         lastMessage = System.currentTimeMillis();
         if (ans.isExchangeStarted()) {
            start = ans;
         }
         if (ans.isExchangeInProgress()) {
            mainQ.offer(ans);
         }
         if (ans.isExchangedFinished()) {
            end = ans;
         }
         synchronized (this) {
            notifyAll();
         }
      }
   }
   
   @Override
   public String getId() {
      return "0";
   }
   
   @Override
   public String getAddress() {
      return "Transaction";
   }
   
   @Override
   public void post(EventOut evOut) {
      Logger.track();
      Logger.verbose(evOut.toString());
      if (evOut instanceof TaskStateEventOut) {
         TaskStateEventOut tsEvOut = (TaskStateEventOut) evOut;
         if (tsEvOut.getTask().getId().contentEquals(taskId)) {
            eventOut = tsEvOut;
         }
      }
      synchronized (this) {
         notifyAll();
      }
   }
   
}
