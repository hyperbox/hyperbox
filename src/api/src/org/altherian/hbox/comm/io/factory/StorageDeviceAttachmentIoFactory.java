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

package org.altherian.hbox.comm.io.factory;

import org.altherian.hbox.comm.in.MediumIn;
import org.altherian.hbox.comm.in.StorageDeviceAttachmentIn;
import org.altherian.hbox.comm.out.storage.StorageDeviceAttachmentOut;

public final class StorageDeviceAttachmentIoFactory {
   
   private StorageDeviceAttachmentIoFactory() {
      // Factory class
   }
   
   public static String getId(String machineId, String controllerId, Long portId, Long deviceId) {
      return machineId + "|" + controllerId + "|" + portId.toString() + "|" + deviceId.toString();
   }
   
   public static StorageDeviceAttachmentIn get(StorageDeviceAttachmentOut maOut) {
      if (maOut.hasMediumInserted()) {
         return new StorageDeviceAttachmentIn(maOut.getControllerName(), maOut.getPortId(), maOut.getDeviceId(), maOut.getDeviceType(), new MediumIn(
               maOut.getMediumUuid()));
      } else {
         return new StorageDeviceAttachmentIn(maOut.getControllerName(), maOut.getPortId(), maOut.getDeviceId(), maOut.getDeviceType());
      }
      
   }
   
}
