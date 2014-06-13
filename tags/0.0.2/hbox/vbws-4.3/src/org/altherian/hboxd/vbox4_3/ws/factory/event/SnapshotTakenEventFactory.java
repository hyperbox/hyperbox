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

package org.altherian.hboxd.vbox4_3.ws.factory.event;

import org.altherian.hbox.event._Event;
import org.altherian.hboxd.event.machine.MachineSnapshotDataChangedEvent;
import org.altherian.hboxd.event.snapshot.SnapshotTakenEvent;
import org.altherian.hboxd.vbox4_3.ws.factory.ConnectionManager;
import org.altherian.hboxd.vbox4_3.ws.factory._PreciseEventFactory;
import org.altherian.tool.logging.Logger;

import org.virtualbox_4_3.IEvent;
import org.virtualbox_4_3.ISnapshotTakenEvent;
import org.virtualbox_4_3.VBoxEventType;

public class SnapshotTakenEventFactory implements _PreciseEventFactory {
   
   @Override
   public VBoxEventType getType() {
      return VBoxEventType.OnSnapshotTaken;
   }
   
   @Override
   public ISnapshotTakenEvent getRaw(IEvent vbEvent) {
      Logger.track();
      
      return ISnapshotTakenEvent.queryInterface(vbEvent);
   }
   
   @Override
   public _Event getEvent(IEvent vbEvent) {
      Logger.track();
      
      ISnapshotTakenEvent snapEv = (ISnapshotTakenEvent) vbEvent;
      
      // Generic event might be used due to Webservices bug, depending on revision - See Javadoc of HyperboxEvents.MachineSnapshotDataChange
      // This revision is only valid for 4.2 branch
      if (ConnectionManager.getBox().getRevision() >= 90983) {
         return new SnapshotTakenEvent(snapEv.getMachineId(), snapEv.getSnapshotId());
      } else {
         return new MachineSnapshotDataChangedEvent(snapEv.getMachineId());
      }
   }
   
}