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

package org.altherian.hbox.comm.output.storage;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.output.hypervisor.DeviceOutput;
import org.altherian.hbox.constant.StorageControllerSettings;

import java.util.List;

public class StorageControllerOutput extends DeviceOutput {
   
   private String uuid;
   
   @SuppressWarnings("unused")
   private StorageControllerOutput() {
      // Used for serialization
   }
   
   public StorageControllerOutput(String machineUuid, String name, List<SettingIO> settings) {
      super(name, settings);
      uuid = machineUuid;
   }
   
   public String getMachineUuid() {
      return uuid;
   }
   
   public String getName() {
      return getSetting(StorageControllerSettings.Name).getString();
   }
   
   /**
    * @return the type
    */
   public String getType() {
      return getSetting(StorageControllerSettings.Type).getString();
   }
   
   /**
    * @return the subType
    */
   public String getSubType() {
      return getSetting(StorageControllerSettings.SubType).getString();
   }
   
   public Long getMinPortCount() {
      return getSetting(StorageControllerSettings.MinPortCount).getNumber();
   }
   
   /**
    * @return the maxPortCount
    */
   public Long getMaxPortCount() {
      return getSetting(StorageControllerSettings.MaxPortCount).getNumber();
   }
   
   /**
    * @return the maxDeviceCount
    */
   public Long getMaxDeviceCount() {
      return getSetting(StorageControllerSettings.MaxDeviceCount).getNumber();
   }
   
   @Override
   public String toString() {
      return getName();
   }
   
}