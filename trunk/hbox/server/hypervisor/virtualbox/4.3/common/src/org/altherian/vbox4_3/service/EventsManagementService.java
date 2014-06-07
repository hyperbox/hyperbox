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

package org.altherian.vbox4_3.service;

import org.altherian.hbox.event._Event;
import org.altherian.hboxd.event._EventManager;
import org.altherian.hboxd.service.SimpleLoopService;
import org.altherian.tool.logging.Logger;
import org.altherian.vbox.utils.EventBusFactory;
import org.altherian.vbox4_3.ErrorInterpreter;
import org.altherian.vbox4_3.VBox;
import org.altherian.vbox4_3.factory.EventFactory;

import java.util.Arrays;

import org.virtualbox_4_3.IEvent;
import org.virtualbox_4_3.IEventListener;
import org.virtualbox_4_3.VBoxEventType;
import org.virtualbox_4_3.VBoxException;

/**
 * Recommended way to handle events is the pasive implementation, which required polling to get new events.<br/>
 * This service will keep polling, transform events and feed them into hyperbox.
 * 
 * @author noteirak
 */
public final class EventsManagementService extends SimpleLoopService {
   
   private _EventManager evMgr;
   private IEventListener el;
   
   public EventsManagementService(_EventManager evMgr) {
      this.evMgr = evMgr;
   }
   
   @Override
   protected void beforeLooping() {
      setSleepingTime(0);
      
      el = VBox.get().getEventSource().createListener();
      VBox.get().getEventSource().registerListener(el, Arrays.asList(VBoxEventType.Any), false);
      Logger.debug("Virtualbox Event Manager Server started.");
   }
   
   @Override
   protected void afterLooping() {
      if (el != null) {
         try {
            VBox.get().getEventSource().unregisterListener(el);
         } catch (Throwable t) {
            Logger.debug("Exception when trying to unregister listener on event source: " + t.getMessage());
         }
         el = null;
      }
      Logger.debug("Virtualbox Event Manager Server stopped.");
   }
   
   @Override
   protected void doLoop() {
      try {
         IEvent rawEvent = VBox.get().getEventSource().getEvent(el, 1000);
         if (rawEvent != null) {
            Logger.debug("Got an event from Virtualbox: " + rawEvent.getClass().getName() + " - " + rawEvent.getType() + " - " + rawEvent);
            IEvent preciseRawEvent = EventFactory.getRaw(rawEvent);
            if (preciseRawEvent != null) {
               Logger.debug("Event was processed to " + preciseRawEvent.getClass().getName() + " - " + preciseRawEvent.getType() + " - "
                     + preciseRawEvent);
               EventBusFactory.post(preciseRawEvent);
               _Event ev = EventFactory.get(preciseRawEvent);
               if (ev != null) {
                  evMgr.post(ev);
               }
            }
            VBox.get().getEventSource().eventProcessed(el, rawEvent);
         }
      } catch (VBoxException e) {
         throw ErrorInterpreter.transform(e);
      } catch (RuntimeException t) {
         if ((t.getMessage() != null) && t.getMessage().contains("Connection refused")) {
            Logger.error("Virtualbox broke the connection with us");
            stop();
         } else {
            throw t;
         }
      }
   }
   
}
