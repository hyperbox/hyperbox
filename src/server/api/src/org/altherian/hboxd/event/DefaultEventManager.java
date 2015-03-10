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

package org.altherian.hboxd.event;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import org.altherian.hbox.event._Event;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxd.security.SecurityContext;
import org.altherian.tool.logging.Logger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class DefaultEventManager implements _EventManager, Runnable {
   
   private BlockingQueue<_Event> eventsQueue;
   private MBassador<_Event> eventBus;
   private boolean running = false;
   private Thread worker;
   
   @Override
   public void start() throws HyperboxException {
      
      Logger.debug("Event Manager Starting");
      eventBus = new MBassador<_Event>(BusConfiguration.Default());
      eventsQueue = new LinkedBlockingQueue<_Event>();
      worker = new Thread(this, "EvMgrWT");
      worker.setDaemon(true);
      SecurityContext.addAdminThread(worker);
      
      worker.start();
      Logger.verbose("Event Manager Started");
   }
   
   @Override
   public void stop() {
      
      Logger.debug("Event Manager Stopping");
      running = false;
      if (worker != null) {
         worker.interrupt();
         try {
            worker.join(1000);
         } catch (InterruptedException e) {
            Logger.exception(e);
         }
      }
      eventsQueue = null;
      Logger.verbose("Event Manager Stopped");
   }
   
   @Override
   public void register(Object o) {
      
      eventBus.subscribe(o);
      Logger.debug(o + " has registered for all events.");
   }
   
   @Override
   public void unregister(Object o) {
      
      eventBus.unsubscribe(o);
      Logger.debug(o + " has unregistered for all events.");
   }
   
   @Override
   public void post(_Event ev) {
      
      Logger.debug("Received Event ID [" + ev.getEventId() + "] fired @ " + ev.getTime());
      if ((eventsQueue != null) && !eventsQueue.offer(ev)) {
         Logger.error("Event queue is full (" + eventsQueue.size() + "), cannot add " + ev.getEventId());
      }
   }
   
   @Override
   public void run() {
      
      Logger.verbose("Event Manager Worker Started");
      running = true;
      while (running) {
         try {
            _Event event = eventsQueue.take();
            Logger.debug("Processing Event: " + event.toString());
            eventBus.publish(event);
         } catch (InterruptedException e) {
            Logger.debug("Got interupted, halting...");
            running = false;
         } catch (Throwable t) {
            Logger.error("Error when processing event: " + t.getMessage());
            Logger.exception(t);
         }
      }
      Logger.verbose("Event Manager Worker halted.");
   }
   
}
