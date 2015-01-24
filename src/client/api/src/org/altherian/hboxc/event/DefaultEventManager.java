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

package org.altherian.hboxc.event;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.tool.logging.Logger;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DefaultEventManager implements _EventManager, Runnable, UncaughtExceptionHandler {
   
   private String label;
   
   private Set<_EventProcessor> postProcessors = new HashSet<_EventProcessor>();
   protected MBassador<Object> eventBus;
   private BlockingQueue<Object> eventsQueue;
   private boolean running;
   private Thread worker;
   
   public DefaultEventManager() {
      this("EMW");
   }
   
   public DefaultEventManager(String label) {
      this.label = label;
   }
   
   private void stopWorker() {
      running = false;
      worker.interrupt();
      try {
         worker.join(1000);
      } catch (InterruptedException e) {
         Logger.exception(e);
      }
   }
   
   private void startWorker() {
      worker = new Thread(this);
      worker.setUncaughtExceptionHandler(this);
      worker.setName(label);
      worker.start();
   }
   
   @Override
   public void uncaughtException(Thread arg0, Throwable arg1) {
      Logger.error("Event Manager " + label + " Worker Thread has crashed: " + arg1.getMessage());
      stopWorker();
      startWorker();
   }
   
   @Override
   public void start() throws HyperboxException {
      Logger.track();
      
      Logger.verbose("Event Manager - " + label + " - is starting");
      eventBus = new MBassador<Object>(BusConfiguration.Default());
      eventsQueue = new LinkedBlockingQueue<Object>();
      startWorker();
      Logger.verbose("Event Manager - " + label + " - has started");
   }
   
   @Override
   public void start(_EventProcessor postProcessor) throws HyperboxException {
      postProcessors.add(postProcessor);
      start();
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      if (running) {
         Logger.verbose("Event Manager - " + label + " - is stopping");
         stopWorker();
         eventsQueue = null;
         Logger.verbose("Event Manager - " + label + " - has stopped");
      }
   }
   
   @Override
   public void register(Object o) {
      Logger.track();
      
      eventBus.subscribe(o);
   }
   
   @Override
   public void unregister(Object o) {
      eventBus.unsubscribe(o);
   }
   
   @Override
   public void post(Object o) {
      Logger.track();
      
      if (eventsQueue != null) {
         if (!eventsQueue.offer(o)) {
            Logger.error("Event Manager - " + label + " queue is full, cannot add " + o.getClass().getSimpleName());
         }
      } else {
         Logger.error("Event Manager - " + label + " was not started, event ignored");
      }
   }
   
   protected void publish(Object event) throws Throwable {
      send(event);
   }
   
   protected final void send(Object event) {
      eventBus.publish(event);
   }
   
   @Override
   public void run() {
      Logger.track();
      
      Logger.debug("Event Manager - " + label + " Worker Started");
      running = true;
      while (running) {
         try {
            Object event = eventsQueue.take();
            Logger.debug("Processing Event " + event.getClass().getSimpleName() + ": " + event.toString());
            publish(event);
            for (_EventProcessor postProcessor : postProcessors) {
               postProcessor.post(event);
            }
         } catch (InterruptedException e) {
            Logger.debug("Interupted, halting...");
         } catch (Throwable e) {
            Logger.error("Error while trying to dispatch event");
            Logger.exception(e);
         }
      }
      Logger.debug("Event Manager - " + label + " Worker halted.");
   }
   
   @Override
   public void add(_EventProcessor postProcessor) {
      postProcessors.add(postProcessor);
   }
   
}
