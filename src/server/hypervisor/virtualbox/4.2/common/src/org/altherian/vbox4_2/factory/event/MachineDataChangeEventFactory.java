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

package org.altherian.vbox4_2.factory.event;

import org.altherian.hbox.event._Event;
import org.altherian.hboxd.event.machine.MachineDataChangeEvent;
import org.altherian.vbox4_2.factory._PreciseEventFactory;
import org.virtualbox_4_2.IEvent;
import org.virtualbox_4_2.IMachineDataChangedEvent;
import org.virtualbox_4_2.VBoxEventType;

public final class MachineDataChangeEventFactory implements _PreciseEventFactory {
   
   @Override
   public VBoxEventType getType() {
      return VBoxEventType.OnMachineDataChanged;
   }
   
   @Override
   public IMachineDataChangedEvent getRaw(IEvent vbEvent) {
      
      return IMachineDataChangedEvent.queryInterface(vbEvent);
   }
   
   @Override
   public _Event getEvent(IEvent vbEvent) {
      
      _Event ev = new MachineDataChangeEvent(getRaw(vbEvent).getMachineId());
      return ev;
   }
   
}
