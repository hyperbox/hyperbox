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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.StorageControllerAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageControllerIn extends DeviceIn {

   private Map<String, StorageDeviceAttachmentIn> attachments = new HashMap<String, StorageDeviceAttachmentIn>();

   @SuppressWarnings("unused")
   private StorageControllerIn() {
      // used only for (de)serialisation
   }

   public StorageControllerIn(String machineUuid, String name) {
      super(name);
      setName(name);
      setMachineUuid(machineUuid);
   }

   public StorageControllerIn(String machineUuid, String name, String type) {
      this(machineUuid, name);
      setType(type);
   }

   public StorageControllerIn(String machineUuid, String name, String type, List<SettingIO> settings) {
      super(name, EntityType.StorageController.getId(), settings);
      setName(name);
      setMachineUuid(machineUuid);
   }

   public String getName() {
      return getSetting(StorageControllerAttribute.Name).getString();
   }

   public void setName(String name) {
      setSetting(new StringSettingIO(StorageControllerAttribute.Name, name));
   }

   public String getType() {
      return getSetting(StorageControllerAttribute.Type).getString();
   }

   public String getSubType() {
      return getSetting(StorageControllerAttribute.SubType).getString();
   }

   public void setType(String type) {
      setSetting(new StringSettingIO(StorageControllerAttribute.Type, type));
   }

   public void setSubType(String subType) {
      setSetting(new StringSettingIO(StorageControllerAttribute.SubType, subType));
   }

   public List<StorageDeviceAttachmentIn> listAttachments() {
      return new ArrayList<StorageDeviceAttachmentIn>(attachments.values());
   }

   public boolean addMediumAttachment(StorageDeviceAttachmentIn matIn) {
      if (attachments.containsKey(matIn.getId())) {
         return false;
      }

      attachments.put(matIn.getId(), matIn);
      return true;
   }

   public boolean removeAttachment(StorageDeviceAttachmentIn sadIn) {
      if (!attachments.containsKey(sadIn.getId())) {
         return false;
      }

      StorageDeviceAttachmentIn properSadIn = attachments.get(sadIn.getId());
      if (properSadIn.getAction().equals(Action.Create)) {
         attachments.remove(sadIn.getId());
      } else {
         properSadIn.setAction(Action.Delete);
      }

      return true;
   }

}
