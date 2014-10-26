/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxd.comm.io.factory.event;

import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.comm.out.event.server.ServerConnectionStateEventOut;
import org.altherian.hbox.comm.out.event.server.ServerPropertyChangedEventOut;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.event._Event;
import org.altherian.hbox.states.ServerConnectionState;
import org.altherian.hboxd.comm.io.factory.ServerIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.event.server.ServerConnectionStateEvent;
import org.altherian.hboxd.event.server.ServerEvent;
import org.altherian.hboxd.event.server.ServerPropertyChangedEvent;

public class ServerEventIoFactory implements _EventIoFactory {
   
   @Override
   public Enum<?>[] getHandles() {
      return new Enum<?>[] {
            HyperboxEvents.ServerConnectionState,
            HyperboxEvents.ServerPropertyChanged
      };
   }
   
   @Override
   public EventOut get(_Hyperbox hbox, _Event ev) {
      if (!(ev instanceof ServerEvent)) {
         return null;
      }
      
      ServerOut srvOut = ServerIoFactory.get();
      switch ((HyperboxEvents) ev.getEventId()) {
         case ServerConnectionState:
            ServerConnectionState state = ((ServerConnectionStateEvent) ev).getState();
            return new ServerConnectionStateEventOut(ev.getTime(), srvOut, state);
         case ServerPropertyChanged:
            Object property = ((ServerPropertyChangedEvent) ev).getProperty();
            Object value = ((ServerPropertyChangedEvent) ev).getValue();
            return new ServerPropertyChangedEventOut(ev.getTime(), srvOut, property, value.toString());
         default:
            return null;
      }
   }
   
}
