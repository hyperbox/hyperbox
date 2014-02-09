/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorConfiguredEventOutput;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorConnectedEventOutput;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorDisconnectedEventOutput;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.event._Event;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.event.hypervisor.HypervisorEvent;

public class HypervisorIoEvent implements _EventIoFactory {
   
   @Override
   public Enum<?>[] getHandles() {
      return new Enum<?>[] {
            HyperboxEvents.HypervisorConfigured,
            HyperboxEvents.HypervisorConnected,
            HyperboxEvents.HypervisorDisconnected
      };
   }
   
   @Override
   public EventOutput get(_Hyperbox hbox, _Event ev) {
      HypervisorEvent hypEv = (HypervisorEvent) ev;
      switch ((HyperboxEvents) hypEv.getEventId()) {
         case HypervisorConfigured:
            return new HypervisorConfiguredEventOutput(hypEv.getTime(), hypEv.getServer(), hypEv.getHypervisor());
         case HypervisorConnected:
            return new HypervisorConnectedEventOutput(hypEv.getTime(), hypEv.getServer(), hypEv.getHypervisor());
         case HypervisorDisconnected:
            return new HypervisorDisconnectedEventOutput(hypEv.getTime(), hypEv.getServer(), hypEv.getHypervisor());
         default:
            return null;
      }
      
   }
   
}
