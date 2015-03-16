/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.vbox4_4.factory.event;

import org.altherian.hbox.event._Event;
import org.altherian.tool.logging.Logger;
import org.altherian.vbox4_4.factory._PreciseEventFactory;
import org.virtualbox_4_4.IEvent;
import org.virtualbox_4_4.IExtraDataChangedEvent;
import org.virtualbox_4_4.VBoxEventType;

public class ExtraDataChangeEventFactory implements _PreciseEventFactory {

   @Override
   public VBoxEventType getType() {
      return VBoxEventType.OnExtraDataChanged;
   }

   @Override
   public IEvent getRaw(IEvent vbEvent) {
      return IExtraDataChangedEvent.queryInterface(vbEvent);
   }

   @Override
   public _Event getEvent(IEvent vbEvent) {
      IExtraDataChangedEvent ev = (IExtraDataChangedEvent) vbEvent;
      Logger.debug("ExtraDataChanged event for " + (ev.getMachineId() == null ? "global data" : "Machine ID ") + ev.getMachineId() + " | Key: " + ev.getKey()
            + " | Value: " + ev.getValue());
      return null;
   }

}
