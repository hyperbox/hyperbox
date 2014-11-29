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

package org.altherian.hbox.comm.input;

import org.altherian.hbox.comm._Actionnable;

public final class StorageDeviceAttachmentInput extends ObjectInput implements _Actionnable {
   
   private String controllerId;
   private String deviceType;
   private Long portId;
   private Long deviceId;
   
   private MediumInput medIn;
   
   private Action action = Action.Create;
   
   @SuppressWarnings("unused")
   private StorageDeviceAttachmentInput() {
      // not used
   }
   
   public StorageDeviceAttachmentInput(Long portId, Long deviceId) {
      this.portId = portId;
      this.deviceId = deviceId;
   }
   
   public StorageDeviceAttachmentInput(String storageControllerId, Long portId, Long deviceId) {
      super(storageControllerId + portId.toString() + deviceId.toString());
      this.controllerId = storageControllerId;
      this.portId = portId;
      this.deviceId = deviceId;
   }
   
   public StorageDeviceAttachmentInput(String storageControllerId, Long portId, Long deviceId, String deviceType) {
      this(storageControllerId, portId, deviceId);
      this.deviceType = deviceType;
   }
   
   public StorageDeviceAttachmentInput(String storageControllerId, Long portId, Long deviceId, String deviceType, MediumInput medIn) {
      this(storageControllerId, portId, deviceId, deviceType);
      this.medIn = medIn;
   }
   
   @Override
   public void setAction(Action action) {
      this.action = action;
   }
   
   @Override
   public Action getAction() {
      return action;
   }
   
   public String getControllerId() {
      return controllerId;
   }
   
   public String getDeviceType() {
      return deviceType;
   }
   
   public Long getPortId() {
      return portId;
   }
   
   public Long getDeviceId() {
      return deviceId;
   }
   
   public boolean hasMedium() {
      return medIn != null;
   }
   
   public MediumInput getMedium() {
      return medIn;
   }
   
   public void attachMedium(MediumInput medIn) {
      this.medIn = medIn;
   }
   
   public void detachMedium() {
      medIn = null;
   }
   
   @Override
   public String toString() {
      return getDeviceType() + " Drive";
   }
   
}