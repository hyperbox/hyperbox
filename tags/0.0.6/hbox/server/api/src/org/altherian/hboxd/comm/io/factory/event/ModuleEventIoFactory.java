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

import org.altherian.hbox.comm.output.ModuleOutput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.event.module.ModuleDisabledEventOutput;
import org.altherian.hbox.comm.output.event.module.ModuleEnabledEventOutput;
import org.altherian.hbox.comm.output.event.module.ModuleLoadedEventOutput;
import org.altherian.hbox.comm.output.event.module.ModuleRegisteredEventOutput;
import org.altherian.hbox.comm.output.event.module.ModuleUnloadedEventOutput;
import org.altherian.hbox.comm.output.event.module.ModuleUnregisteredEventOutput;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.event._Event;
import org.altherian.hboxd.comm.io.factory.ModuleIoFactory;
import org.altherian.hboxd.comm.io.factory.ServerIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.event.module.ModuleEvent;
import org.altherian.hboxd.event.module.ModuleUnregisteredEvent;

import java.util.Date;

public class ModuleEventIoFactory implements _EventIoFactory {
   
   @Override
   public Enum<?>[] getHandles() {
      return new Enum<?>[] {
            HyperboxEvents.ModuleDisabled,
            HyperboxEvents.ModuleEnabled,
            HyperboxEvents.ModuleLoaded,
            HyperboxEvents.ModuleRegistered,
            HyperboxEvents.ModuleUnloaded,
            HyperboxEvents.ModuleUnregistered
      };
   }
   
   @Override
   public EventOutput get(_Hyperbox hbox, _Event ev) {
      if (!(ev instanceof ModuleEvent)) {
         return null;
      }
      
      ModuleEvent modEv = (ModuleEvent) ev;
      Date time = modEv.getTime();
      ServerOutput srvOut = ServerIoFactory.get();
      
      switch ((HyperboxEvents) ev.getEventId()) {
         case ModuleDisabled:
            return new ModuleDisabledEventOutput(time, srvOut, ModuleIoFactory.get(modEv.getModule()));
         case ModuleEnabled:
            return new ModuleEnabledEventOutput(time, srvOut, ModuleIoFactory.get(modEv.getModule()));
         case ModuleLoaded:
            return new ModuleLoadedEventOutput(time, srvOut, ModuleIoFactory.get(modEv.getModule()));
         case ModuleRegistered:
            return new ModuleRegisteredEventOutput(time, srvOut, ModuleIoFactory.get(modEv.getModule()));
         case ModuleUnloaded:
            return new ModuleUnloadedEventOutput(time, srvOut, ModuleIoFactory.get(modEv.getModule()));
         case ModuleUnregistered:
            return new ModuleUnregisteredEventOutput(time, srvOut, new ModuleOutput(((ModuleUnregisteredEvent) modEv).getModuleId()));
         default:
            return null;
      }
   }
   
}
