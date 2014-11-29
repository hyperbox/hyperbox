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

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.out.ObjectOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.StorageControllerAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageControllerOut extends ObjectOut {
   
   private String vmId;
   private List<StorageDeviceAttachmentOut> attachments;
   
   @SuppressWarnings("unused")
   private StorageControllerOut() {
      // stub
   }
   
   public StorageControllerOut(String vmId, String name, List<SettingIO> settings) {
      this(vmId, name, settings, Collections.<StorageDeviceAttachmentOut> emptyList());
   }
   
   public StorageControllerOut(String vmId, String name, List<SettingIO> settings, List<StorageDeviceAttachmentOut> attachments) {
      super(EntityType.StorageController, name, settings);
      this.vmId = vmId;
      this.attachments = attachments;
   }
   
   public String getMachineUuid() {
      return vmId;
   }
   
   public String getName() {
      return getSetting(StorageControllerAttribute.Name).getString();
   }
   
   /**
    * @return the type
    */
   public String getType() {
      return getSetting(StorageControllerAttribute.Type).getString();
   }
   
   /**
    * @return the subType
    */
   public String getSubType() {
      return getSetting(StorageControllerAttribute.SubType).getString();
   }
   
   public Long getMinPortCount() {
      return getSetting(StorageControllerAttribute.MinPortCount).getNumber();
   }
   
   /**
    * @return the maxPortCount
    */
   public Long getMaxPortCount() {
      return getSetting(StorageControllerAttribute.MaxPortCount).getNumber();
   }
   
   /**
    * @return the maxDeviceCount
    */
   public Long getMaxDeviceCount() {
      return getSetting(StorageControllerAttribute.MaxDeviceCount).getNumber();
   }
   
   public boolean hasAttachments() {
      return !attachments.isEmpty();
   }
   
   public List<StorageDeviceAttachmentOut> getAttachments() {
      return new ArrayList<StorageDeviceAttachmentOut>(attachments);
   }
   
   @Override
   public String toString() {
      return getName();
   }
   
}
