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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.output.storage.StorageDeviceAttachmentOutput;
import org.altherian.hboxd.business._MediumAttachment;
import org.altherian.hboxd.hypervisor.storage._RawMediumAttachment;

public class MediumAttachmentIoFactory {
   
   private MediumAttachmentIoFactory() {
      // static class
   }
   
   public static StorageDeviceAttachmentOutput get(_MediumAttachment ma) {
      
      return new StorageDeviceAttachmentOutput(
            ma.getMachineId(),
            ma.getControllerId(),
            ma.getMediumId(),
            ma.getPortId(),
            ma.getDeviceId(),
            ma.getDeviceType());
   }
   
   public static StorageDeviceAttachmentOutput get(_RawMediumAttachment ma) {
      
      String mediumId = null;
      if (ma.getMedium() != null) {
         mediumId = ma.getMedium().getUuid();
      }
      
      return new StorageDeviceAttachmentOutput(
            ma.getMachine().getUuid(),
            ma.getController().getName(),
            mediumId,
            ma.getPortId(),
            ma.getDeviceId(),
            ma.getDeviceType());
   }
   
}
