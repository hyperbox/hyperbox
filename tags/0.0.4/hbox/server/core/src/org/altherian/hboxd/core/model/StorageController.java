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

import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.storage.StorageAttachmentAddedEvent;
import org.altherian.hboxd.event.storage.StorageAttachmentModifiedEvent;
import org.altherian.hboxd.event.storage.StorageAttachmentRemovedEvent;
import org.altherian.hboxd.event.storage.StorageControllerAttachmentDataModifiedEvent;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.storage._RawMediumAttachment;
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
   public void setSetting(List<_Setting> settings) {
      rawSto.setSetting(settings);
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
      return rawSto.getName();
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
      boolean slotTaken = isSlotTaken(portNb, deviceNb);
      rawSto.attachDevice(deviceId, portNb, deviceNb);
      if (slotTaken) {
         EventManager.post(new StorageAttachmentModifiedEvent(getMachineUuid(), getId(), portNb, deviceNb));
      } else {
         EventManager.post(new StorageAttachmentAddedEvent(getMachineUuid(), getId(), portNb, deviceNb));
      }
   }
   
   @Override
   public void detachDevice(long portNb, long deviceNb) {
      rawSto.detachDevice(portNb, deviceNb);
      EventManager.post(new StorageAttachmentRemovedEvent(getMachineUuid(), getId(), portNb, deviceNb));
   }
   
   @Override
   public Set<_Medium> listMedium() {
      Set<_Medium> mediumList = new HashSet<_Medium>();
      for (_RawMedium med : hypervisor.listMediums()) {
         mediumList.add(new Medium(vm.getServer(), hypervisor, med));
      }
      return mediumList;
   }
   
   @Override
   public Set<_MediumAttachment> listMediumAttachment() {
      Set<_MediumAttachment> maSet = new HashSet<_MediumAttachment>();
      for (_RawMediumAttachment rawMa : rawSto.listMediumAttachment()) {
         maSet.add(new MediumAttachment(rawMa));
      }
      return maSet;
   }
   
   @Override
   public void attachMedium(_Medium medium) {
      rawSto.attachMedium(hypervisor.getMedium(medium.getUuid()));
      EventManager.post(new StorageControllerAttachmentDataModifiedEvent(getMachineUuid(), getId()));
   }
   
   @Override
   public void attachMedium(_Medium medium, long portNb, long deviceNb) {
      boolean slotTaken = isSlotTaken(portNb, deviceNb);
      rawSto.attachMedium(hypervisor.getMedium(medium.getUuid()), portNb, deviceNb);
      if (slotTaken) {
         EventManager.post(new StorageAttachmentModifiedEvent(getMachineUuid(), getId(), portNb, deviceNb));
      } else {
         EventManager.post(new StorageAttachmentAddedEvent(getMachineUuid(), getId(), portNb, deviceNb));
      }
   }
   
   @Override
   public void detachMedium(_Medium medium) {
      rawSto.detachMedium(hypervisor.getMedium(medium.getUuid()));
      EventManager.post(new StorageControllerAttachmentDataModifiedEvent(getMachineUuid(), getId()));
   }
   
   @Override
   public void detachMedium(long portNb, long deviceNb) {
      rawSto.detachMedium(portNb, deviceNb);
      EventManager.post(new StorageAttachmentRemovedEvent(vm.getUuid(), rawSto.getName(), portNb, deviceNb));
   }
   
   @Override
   public boolean isSlotTaken(long portNb, long deviceNb) {
      return rawSto.isSlotTaken(portNb, deviceNb);
   }
   
   @Override
   public _MediumAttachment getMediumAttachment(long portNb, long deviceNb) {
      _RawMediumAttachment rawMedAtt = rawSto.getMediumAttachment(portNb, deviceNb);
      _MediumAttachment medAtt = new MediumAttachment(
            vm.getUuid(),
            rawMedAtt.hasMedium() ? rawMedAtt.getMedium().getUuid() : null,
                  rawSto.getName(),
                  rawMedAtt.getPortId(),
                  rawMedAtt.getDeviceId(), rawMedAtt.getDeviceType(), rawMedAtt.isPassThrough());
      
      return medAtt;
   }
   
}
