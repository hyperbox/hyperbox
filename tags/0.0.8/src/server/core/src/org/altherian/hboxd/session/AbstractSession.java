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

package org.altherian.hboxd.session;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._Client;
import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.states.SessionStates;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.session.SessionStateEvent;
import org.altherian.hboxd.security.SecurityContext;
import org.altherian.hboxd.security._User;
import org.altherian.hboxd.task._TaskManager;
import org.altherian.tool.logging.Logger;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractSession implements _Session {
   
   private final String id;
   private Date createTime;
   private SessionStates state;
   
   private _Client client;
   protected _User user;
   
   private Thread worker;
   private volatile boolean running;
   private BlockingQueue<Request> msgQueue;
   
   private _TaskManager taskMgr;
   
   public AbstractSession(String id, _Client client, _User user, _TaskManager taskMgr) {
      Logger.track();
      
      this.id = id;
      createTime = new Date();
      
      msgQueue = new LinkedBlockingQueue<Request>();
      this.client = client;
      this.user = user;
      this.taskMgr = taskMgr;
      
      init();
      setState(SessionStates.Created);
   }
   
   private void init() {
      Logger.track();
      
      running = true;
      worker = new Thread(this, "SessWT - Session #" + id + " - Connection #" + client.getId());
      worker.start();
   }
   
   protected void setState(SessionStates state) {
      this.state = state;
      EventManager.post(new SessionStateEvent(this));
   }
   
   @Override
   public String getId() {
      return id;
   }
   
   @Override
   public SessionStates getState() {
      return state;
   }
   
   @Override
   public _User getUser() {
      return user;
   }
   
   @Override
   public void login() {
      user = SecurityContext.getUser();
   }
   
   @Override
   public void logout() {
      close();
   }
   
   @Override
   public void close() {
      Logger.track();
      
      running = false;
      
      if (!Thread.currentThread().equals(worker)) {
         worker.interrupt();
         try {
            worker.join(1000);
         } catch (InterruptedException e) {
            Logger.debug("Session worker thread ID: " + worker.getId());
            Logger.debug("Thread ID #" + Thread.currentThread().getId() + " [" + Thread.currentThread().getName() + "] forcefully destroyed for Session ID #" + getId());
         }
      }
      
   }
   
   @Override
   public void putRequest(Request req) {
      Logger.track();
      
      if (!msgQueue.offer(req)) {
         throw new HyperboxRuntimeException("Message queue is full, try again later.");
      }
   }
   
   protected void process(Request req) {
      taskMgr.process(req);
   }
   
   @Override
   public void post(EventOut ev) {
      Logger.track();
      
      client.post(ev);
   }
   
   @Override
   public void run() {
      Logger.track();
      
      SecurityContext.setUser(user);
      SessionContext.setClient(client);
      running = true;
      
      Logger.debug("Session ID #" + getId() + " | " + getUser().getDomainLogonName() + " | Message Queue Runner started");
      
      while (running) {
         try {
            Request r = msgQueue.take();
            taskMgr.process(r);
         } catch (InterruptedException e) {
            Logger.debug("Session ID #" + getId() + " | " + getUser().getDomainLogonName() + " | Message Queue Runner interupted, halting...");
            running = false;
         } catch (Throwable e) {
            Logger.error("Fatal error while trying to process a client request : " + e.getMessage());
            Logger.exception(e);
         }
      }
      Logger.debug("Session ID #" + getId() + " | " + getUser().getDomainLogonName() + " | Message Queue Runner halted");
      setState(SessionStates.Destroyed);
   }
   
   @Override
   public Date getCreateTime() {
      return createTime;
   }
   
   protected _Client getClient() {
      return client;
   }

}
