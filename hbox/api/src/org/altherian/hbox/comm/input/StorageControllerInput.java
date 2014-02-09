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

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.StorageControllerSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageControllerInput extends DeviceInput {
   
   private Map<String, StorageDeviceAttachmentInput> attachments = new HashMap<String, StorageDeviceAttachmentInput>();
   
   @SuppressWarnings("unused")
   private StorageControllerInput() {
      // used only for (de)serialisation
   }
   
   public StorageControllerInput(String machineUuid, String name) {
      super(name);
      setName(name);
      setMachineUuid(machineUuid);
   }
   
   public StorageControllerInput(String machineUuid, String name, String type) {
      this(machineUuid, name);
      setType(type);
   }
   
   public StorageControllerInput(String machineUuid, String name, String type, List<SettingIO> settings) {
      super(name, settings);
      setName(name);
      setMachineUuid(machineUuid);
   }
   
   public String getName() {
      return getSetting(StorageControllerSettings.Name).getString();
   }
   
   public void setName(String name) {
      setSetting(new StringSettingIO(StorageControllerSettings.Name, name));
   }
   
   public String getType() {
      return getSetting(StorageControllerSettings.Type).getString();
   }
   
   public String getSubType() {
      return getSetting(StorageControllerSettings.SubType).getString();
   }
   
   public void setType(String type) {
      setSetting(new StringSettingIO(StorageControllerSettings.Type, type));
   }
   
   public void setSubType(String subType) {
      setSetting(new StringSettingIO(StorageControllerSettings.SubType, subType));
   }
   
   public List<StorageDeviceAttachmentInput> listAttachments() {
      return new ArrayList<StorageDeviceAttachmentInput>(attachments.values());
   }
   
   public boolean addMediumAttachment(StorageDeviceAttachmentInput matIn) {
      if (attachments.containsKey(matIn.getId())) {
         return false;
      }
      
      attachments.put(matIn.getId(), matIn);
      return true;
   }
   
   public boolean removeAttachment(StorageDeviceAttachmentInput sadIn) {
      if (!attachments.containsKey(sadIn.getId())) {
         return false;
      }
      
      StorageDeviceAttachmentInput properSadIn = attachments.get(sadIn.getId());
      if (properSadIn.getAction().equals(Action.Create)) {
         attachments.remove(sadIn.getId());
      } else {
         properSadIn.setAction(Action.Delete);
      }
      
      return true;
   }
   
}
