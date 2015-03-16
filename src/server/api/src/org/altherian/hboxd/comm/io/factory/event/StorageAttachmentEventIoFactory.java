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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.comm.io.factory.event;

import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.comm.out.event.storage.StorageControllerAttachmentDataModifiedEventOut;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.comm.out.storage.StorageControllerOut;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.event._Event;
import org.altherian.hboxd.comm.io.factory.MachineIoFactory;
import org.altherian.hboxd.comm.io.factory.ServerIoFactory;
import org.altherian.hboxd.comm.io.factory.StorageControllerIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.model._Machine;
import org.altherian.hboxd.event.storage.StorageAttachmentEvent;

public class StorageAttachmentEventIoFactory implements _EventIoFactory {

   @Override
   public Enum<?>[] getHandles() {
      return new Enum<?>[] {
            HyperboxEvents.StorageAttachmentAdded,
            HyperboxEvents.StorageAttachmentModified,
            HyperboxEvents.StorageAttachmentRemoved
      };
   }

   @Override
   public EventOut get(_Hyperbox hbox, _Event ev) {
      StorageAttachmentEvent event = (StorageAttachmentEvent) ev;

      _Machine vm = hbox.getServer().getMachine(event.getMachineId());

      ServerOut srvOut = ServerIoFactory.get();
      MachineOut vmOut = MachineIoFactory.getSimple(vm);
      StorageControllerOut stoOut = StorageControllerIoFactory.get(vm.getStorageController(event.getControllerId()));

      switch ((HyperboxEvents) event.getEventId()) {
         case StorageAttachmentAdded:
            return new StorageControllerAttachmentDataModifiedEventOut(ev.getTime(), srvOut, vmOut, stoOut);
         case StorageAttachmentModified:
            return new StorageControllerAttachmentDataModifiedEventOut(ev.getTime(), srvOut, vmOut, stoOut);
         case StorageAttachmentRemoved:
            return new StorageControllerAttachmentDataModifiedEventOut(ev.getTime(), srvOut, vmOut, stoOut);
         default:
            return null;
      }
   }

}
