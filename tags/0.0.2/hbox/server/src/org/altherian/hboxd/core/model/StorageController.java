package org.altherian.hboxd.core.model;

import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;
import org.altherian.hboxd.settings._Setting;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StorageController implements _StorageController {
   
   private _Machine vm;
   private _Hypervisor hypervisor;
   private _RawStorageController rawSto;
   
   public StorageController(_Machine vm, _Hypervisor hypervisor, _RawStorageController rawSto) {
      this.vm = vm;
      this.hypervisor = hypervisor;
      this.rawSto = rawSto;
   }
   
   @Override
   public _Machine getMachine() {
      return vm;
   }
   
   @Override
   public EntityTypes getType() {
      return EntityTypes.StorageController;
   }
   
   @Override
   public List<_Setting> getSettings() {
      return rawSto.listSettings();
   }
   
   @Override
   public _Setting getSetting(String settingId) {
      return rawSto.getSetting(settingId);
   }
   
   @Override
   public void setSetting(_Setting setting) {
      rawSto.setSetting(setting);
   }
   
   @Override
   public boolean hasSetting(String settingId) {
      try {
         rawSto.getSetting(settingId);
         return true;
      } catch (Throwable t) {
         // TODO catch better
         return false;
      }
   }
   
   @Override
   public String getId() {
      return vm.getUuid() + "/" + rawSto.getName();
   }
   
   @Override
   public String getMachineUuid() {
      return vm.getUuid();
   }
   
   @Override
   public String getName() {
      return rawSto.getName();
   }
   
   @Override
   public void setName(String name) {
      rawSto.setName(name);
   }
   
   @Override
   public String getControllerType() {
      return rawSto.getType();
   }
   
   @Override
   public String getControllerSubType() {
      return rawSto.getSubType();
   }
   
   @Override
   public void setSubType(String subType) {
      rawSto.setSubType(subType);
   }
   
   @Override
   public long getPortCount() {
      return rawSto.getPortCount();
   }
   
   @Override
   public void setPortCount(long portCount) {
      rawSto.setPortCount(portCount);
   }
   
   @Override
   public long getMaxPortCount() {
      return rawSto.getMaxPortCount();
   }
   
   @Override
   public long getMaxDeviceCount() {
      return rawSto.getMaxDeviceCount();
   }
   
   @Override
   public void attachDevice(String deviceId, long portNb, long deviceNb) {
      rawSto.attachDevice(deviceId, portNb, deviceNb);
   }
   
   @Override
   public void detachDevice(long portNb, long deviceNb) {
      rawSto.detachDevice(portNb, deviceNb);
   }
   
   @Override
   public Set<_Medium> listMedium() {
      Set<_Medium> mediumList = new HashSet<_Medium>();
      for (_RawMedium med : vm.getServer().getHypervisor().listMediums()) {
         mediumList.add(new Medium(vm.getServer(), hypervisor, med));
      }
      return mediumList;
   }
   
   @Override
   public Set<_MediumAttachment> listMediumAttachment() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public void attachMedium(_Medium medium) {
      rawSto.attachMedium(vm.getServer().getHypervisor().getMedium(medium.getId()));
   }
   
   @Override
   public void attachMedium(_Medium medium, long portNb, long deviceNb) {
      rawSto.attachMedium(vm.getServer().getHypervisor().getMedium(medium.getId()), portNb, deviceNb);
   }
   
   @Override
   public void detachMedium(_Medium medium) {
      rawSto.detachMedium(vm.getServer().getHypervisor().getMedium(medium.getId()));
   }
   
   @Override
   public void detachMedium(long portNb, long deviceNb) {
      rawSto.detachMedium(portNb, deviceNb);
   }
   
   @Override
   public boolean isSlotTaken(long portNb, long deviceNb) {
      return rawSto.isSlotTaken(portNb, deviceNb);
   }
   
}
