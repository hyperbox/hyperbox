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

import org.altherian.hbox.comm._Actionnable;
import org.altherian.hbox.comm.io.SettingIO;

import java.util.List;

public class DeviceInput extends ObjectInput implements _Actionnable {
   
   private Action action = Action.Create;
   private String machineUuid;
   private String devType;
   
   public DeviceInput() {
      super();
   }
   
   public DeviceInput(String id) {
      super(id);
   }
   
   public DeviceInput(String id, List<SettingIO> settings) {
      super(id, settings);
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
