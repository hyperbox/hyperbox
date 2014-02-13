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

package org.altherian.hboxd.vbox.hypervisor.xpcom4_4.storage;

import org.altherian.hbox.constant.MediumAttribute;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.settings.vbox.medium.MediumDescriptionSetting;
import org.altherian.hbox.settings.vbox.medium.MediumLocationSetting;
import org.altherian.hbox.settings.vbox.medium.MediumTypeSetting;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.settings.BooleanSetting;
import org.altherian.hboxd.settings.PositiveNumberSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.hboxd.task._ProgressTracker;
import org.altherian.hboxd.vbox.hypervisor.xpcom4_4.vm.VBoxMachine;
import org.altherian.hboxd.vbox4_4.xpcom.factory.ConnectionManager;
import org.altherian.hboxd.vbox4_4.xpcom.manager.VbSettingManager;
import org.altherian.tool.logging.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.virtualbox_4_4.AccessMode;
import org.virtualbox_4_4.DeviceType;
import org.virtualbox_4_4.IMedium;
import org.virtualbox_4_4.VBoxException;

public final class VirtualboxMedium implements _RawMedium {
   
   private String uuid;
   private String file;
   private DeviceType devType;
   
   private IMedium rawMedium;
   
   public VirtualboxMedium(IMedium medium) {
      medium.refreshState();
      uuid = medium.getId();
      file = medium.getLocation();
      devType = medium.getDeviceType();
   }
   
   private void getRead() {
      Logger.track();
      
      rawMedium = ConnectionManager.getBox().openMedium(file, devType, AccessMode.ReadOnly, false);
   }
   
   @Override
   public String getUuid() {
      return uuid;
   }
   
   @Override
   public void setUuid(String newUuid) {
      throw new UnsupportedOperationException("This is not a supported operation.");
   }
   
   @Override
   public void generateUuid() {
      throw new UnsupportedOperationException("This is not a supported operation.");
   }
   
   @Override
   public String getDescription() {
      return VbSettingManager.get(this, MediumAttribute.Description).getString();
   }
   
   @Override
   public void setDescription(String desc) {
      setSetting(new MediumDescriptionSetting(desc));
   }
   
   @Override
   public String getState() {
      return VbSettingManager.get(this, MediumAttribute.State).getRawValue().toString();
   }
   
   @Override
   public String getVariant() {
      return VbSettingManager.get(this, MediumAttribute.Variant).getRawValue().toString();
   }
   
   @Override
   public String getLocation() {
      return file;
   }
   
   @Override
   public void setLocation(String path) {
      setSetting(new MediumLocationSetting(path));
   }
   
   @Override
   public String getName() {
      return VbSettingManager.get(this, MediumAttribute.Name).getRawValue().toString();
   }
   
   @Override
   public String getDeviceType() {
      return devType.toString();
   }
   
   @Override
   public long getSize() {
      return ((PositiveNumberSetting) VbSettingManager.get(this, MediumAttribute.Size)).getValue();
   }
   
   @Override
   public String getFormat() {
      return VbSettingManager.get(this, MediumAttribute.Format).getRawValue().toString();
   }
   
   @Override
   public String getMediumFormat() {
      return VbSettingManager.get(this, MediumAttribute.MediumFormat).getRawValue().toString();
   }
   
   @Override
   public String getType() {
      return VbSettingManager.get(this, MediumAttribute.Type).getRawValue().toString();
   }
   
   @Override
   public void setType(String type) {
      setSetting(new MediumTypeSetting(type));
   }
   
   @Override
   public boolean hasParent() {
      getRead();
      try {
         if (rawMedium.getParent() != null) {
            return true;
         } else {
            return false;
         }
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public _RawMedium getParent() {
      getRead();
      try {
         if (rawMedium.getParent() != null) {
            return new VirtualboxMedium(rawMedium.getParent());
         } else {
            return null;
         }
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public boolean hasChild() {
      getRead();
      try {
         if (rawMedium.getChildren().isEmpty()) {
            return false;
         } else {
            return true;
         }
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public Set<_RawMedium> getChild() {
      Set<_RawMedium> childs = new HashSet<_RawMedium>();
      getRead();
      try {
         for (IMedium rawChild : rawMedium.getChildren()) {
            childs.add(new VirtualboxMedium(rawChild));
         }
         return childs;
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public _RawMedium getBase() {
      getRead();
      try {
         if (rawMedium.getBase() != null) {
            return new VirtualboxMedium(rawMedium.getBase());
         } else {
            return null;
         }
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public boolean isReadOnly() {
      return ((BooleanSetting) VbSettingManager.get(this, MediumAttribute.ReadOnly)).getValue();
   }
   
   @Override
   public long getLogicalSize() {
      return ((PositiveNumberSetting) VbSettingManager.get(this, MediumAttribute.LogicalSize)).getValue();
   }
   
   @Override
   public boolean isAutoReset() {
      return ((BooleanSetting) VbSettingManager.get(this, MediumAttribute.AutoReset)).getValue();
   }
   
   @Override
   public String lastAccessError() {
      getRead();
      try {
         return rawMedium.getLastAccessError();
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public Set<_RawVM> getLinkedMachines() {
      getRead();
      try {
         Set<_RawVM> linkedMachines = new HashSet<_RawVM>();
         for (String machineUuid : rawMedium.getMachineIds()) {
            linkedMachines.add(new VBoxMachine(machineUuid));
         }
         return linkedMachines;
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public void close() {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public void refresh() {
      rawMedium.refreshState();
   }
   
   @Override
   public _ProgressTracker clone(String path) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public _ProgressTracker clone(_RawMedium toMedium) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public _ProgressTracker clone(String path, String variantType) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public _ProgressTracker clone(_RawMedium toMedium, String variantType) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public _ProgressTracker compact() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public _ProgressTracker create(long size) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public _ProgressTracker create(long size, String variantType) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public _ProgressTracker resize(long size) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public void reset() {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public List<_Setting> listSettings() {
      return VbSettingManager.list(this);
   }
   
   @Override
   public _Setting getSetting(Object getName) {
      return VbSettingManager.get(this, getName);
   }
   
   @Override
   public void setSetting(_Setting s) {
      VbSettingManager.set(this, Arrays.asList(s));
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      VbSettingManager.set(this, s);
   }
   
}
