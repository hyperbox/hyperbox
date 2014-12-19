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

package org.altherian.hbox.comm.out.storage;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.ObjectOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.MediumAttribute;
import org.altherian.tool.AxStrings;
import java.util.List;

public class MediumOut extends ObjectOut {
   
   @SuppressWarnings("unused")
   private MediumOut() {
      // Used for serialization
   }
   
   public MediumOut(String uuid, List<SettingIO> settings) {
      super(EntityType.Medium, uuid, settings);
      setSetting(new StringSettingIO(MediumAttribute.UUID, uuid));
      setSetting(settings);
   }
   
   public String getUuid() {
      return getSetting(MediumAttribute.UUID).getString();
   }
   
   /**
    * Return the filename of this medium or its UUID if the filename was not provided
    * 
    * @return a String containing the filename or the UUID for this medium
    */
   public String getName() {
      if (!hasSetting(MediumAttribute.Name)) {
         setSetting(new StringSettingIO(MediumAttribute.Name, getUuid()));
      }
      
      return getSetting(MediumAttribute.Name).getString();
   }
   
   public boolean hasParent() {
      return !AxStrings.isEmpty(getSetting(MediumAttribute.ParentUUID).getString());
   }
   
   public String getParentUuid() {
      return getSetting(MediumAttribute.ParentUUID).getString();
   }
   
   public String getLocation() {
      return getSetting(MediumAttribute.Location).getString();
   }
   
   /**
    * Get the UUID for the base medium of this medium, if any
    * 
    * @return UUID if this medium has a base medium, or an empty String if not
    */
   public String getBaseUuid() {
      if (hasSetting(MediumAttribute.BaseUUID)) {
         return getSetting(MediumAttribute.BaseUUID).getString();
      } else {
         return "";
      }
   }
   
   public boolean isReadOnly() {
      return getSetting(MediumAttribute.ReadOnly).getBoolean();
   }
   
   public long getLogicalSize() {
      return getSetting(MediumAttribute.LogicalSize).getNumber();
   }
   
   public String getType() {
      return getSetting(MediumAttribute.Type).getString();
   }
   
   public String getDeviceType() {
      return getSetting(MediumAttribute.DeviceType).getString();
   }
   
   @Override
   public String toString() {
      return getSetting(MediumAttribute.Name).getString();
   }
   
}
