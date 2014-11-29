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

package org.altherian.hbox.comm.out.storage;

import org.altherian.hbox.comm.out.ObjectOut;
import org.altherian.hbox.constant.EntityType;

public class StorageDeviceAttachmentOut extends ObjectOut {
   
   private String machineUuid;
   private String controllerName;
   private String mediumUuid;
   private long portId;
   private long deviceId;
   private String deviceType;
   
   @SuppressWarnings("unused")
   private StorageDeviceAttachmentOut() {
      // Used for serialization
   }
   
   public StorageDeviceAttachmentOut(String machineUuid, String controllerName, String mediumUuid, long portId, long deviceId, String deviceType) {
      super(EntityType.StorageAttachment, machineUuid + "|" + controllerName + "|" + portId + "|" + deviceId);
      this.machineUuid = machineUuid;
      this.controllerName = controllerName;
      this.mediumUuid = mediumUuid;
      this.portId = portId;
      this.deviceId = deviceId;
      this.deviceType = deviceType;
   }
   
   /**
    * @return the machineUuid
    */
   public String getMachineUuid() {
      return machineUuid;
   }
   
   /**
    * @return the controllerName
    */
   public String getControllerName() {
      return controllerName;
   }
   
   /**
    * @return the mediumUuid or null if no medium is present
    * @see #hasMediumInserted()
    */
   public String getMediumUuid() {
      return mediumUuid;
   }
   
   /**
    * @return the portId
    */
   public long getPortId() {
      return portId;
   }
   
   /**
    * @return the deviceId
    */
   public long getDeviceId() {
      return deviceId;
   }
   
   /**
    * @return the deviceType
    */
   public String getDeviceType() {
      return deviceType;
   }
   
   public boolean hasMediumInserted() {
      return (getMediumUuid() != null);
   }
   
   @Override
   public String toString() {
      if (hasMediumInserted()) {
         return getMediumUuid();
      } else {
         return getDeviceType() + " Drive";
      }
   }
   
}
