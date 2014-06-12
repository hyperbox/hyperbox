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

package org.altherian.hboxd.core.model;

import org.altherian.hboxd.hypervisor.storage._RawMediumAttachment;
import org.altherian.tool.StringTools;

public class MediumAttachment implements _MediumAttachment {
   
   private String machineId;
   private String mediumId;
   private String controllerId;
   private long portId;
   private long deviceId;
   private String deviceType;
   private boolean isPassThrough;
   
   public MediumAttachment(_RawMediumAttachment raw) {
      this(raw.getMachine().getUuid(), raw.hasMedium() ? raw.getMedium().getUuid() : null, raw.getController().getName(), raw.getPortId(), raw
            .getDeviceId(), raw
            .getDeviceType(), raw.isPassThrough());
   }
   
   public MediumAttachment(String machineId, String mediumId, String controllerId, long portId, long deviceId, String deviceType,
         boolean isPassThrough) {
      this.machineId = machineId;
      this.mediumId = mediumId;
      this.controllerId = controllerId;
      this.portId = portId;
      this.deviceId = deviceId;
      this.deviceType = deviceType;
      this.isPassThrough = isPassThrough;
   }
   
   @Override
   public String getMachineId() {
      return machineId;
   }
   
   @Override
   public String getMediumId() {
      return mediumId;
   }
   
   @Override
   public String getControllerId() {
      return controllerId;
   }
   
   @Override
   public long getPortId() {
      return portId;
   }
   
   @Override
   public long getDeviceId() {
      return deviceId;
   }
   
   @Override
   public String getDeviceType() {
      return deviceType;
   }
   
   @Override
   public boolean isPassThrough() {
      return isPassThrough;
   }
   
   @Override
   public boolean hasMedium() {
      return !StringTools.isEmpty(mediumId);
   }
   
}
