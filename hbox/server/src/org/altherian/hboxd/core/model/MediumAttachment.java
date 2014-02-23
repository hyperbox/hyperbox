package org.altherian.hboxd.core.model;

public class MediumAttachment implements _MediumAttachment {
   
   private String machineId;
   private String mediumId;
   private String controllerId;
   private long portId;
   private long deviceId;
   private String deviceType;
   private boolean isPassThrough;
   
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
   
}
