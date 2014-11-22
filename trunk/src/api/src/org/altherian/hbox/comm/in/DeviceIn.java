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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.comm._Actionnable;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.constant.EntityType;

import java.util.List;

public class DeviceIn extends ObjectIn<EntityType> implements _Actionnable {
   
   private Action action = Action.Create;
   private String machineUuid;
   private String devType;
   
   public DeviceIn() {
      super(EntityType.Device);
   }
   
   public DeviceIn(String id) {
      super(EntityType.Device, id);
   }
   
   public DeviceIn(String id, String devType) {
      super(EntityType.Device, id);
      setDevType(devType);
   }
   
   public DeviceIn(String id, String devType, List<SettingIO> settings) {
      super(EntityType.Device, id, settings);
   }
   
   public String getMachineUuid() {
      return machineUuid;
   }
   
   public void setMachineUuid(String uuid) {
      machineUuid = uuid;
   }
   
   /**
    * @return the devType
    */
   public String getDevType() {
      return devType;
   }
   
   /**
    * @param devType the devType to set
    */
   public void setDevType(String devType) {
      this.devType = devType;
   }
   
   @Override
   public Action getAction() {
      return action;
   }
   
   @Override
   public void setAction(Action action) {
      this.action = action;
   }
   
}
