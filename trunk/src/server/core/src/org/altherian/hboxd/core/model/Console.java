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

import org.altherian.hbox.constant.EntityType;
import org.altherian.hboxd.hypervisor.vm.device._RawConsole;
import org.altherian.setting._Setting;

import java.util.List;

public class Console implements _Console {
   
   private _Machine machine;
   private _RawConsole console;
   
   public Console(_Machine machine, _RawConsole console) {
      this.machine = machine;
      this.console = console;
   }
   
   @Override
   public List<_Setting> getSettings() {
      return console.listSettings();
   }
   
   @Override
   public _Setting getSetting(String settingId) {
      return console.getSetting(settingId);
   }
   
   @Override
   public void setSetting(_Setting setting) {
      console.setSetting(setting);
   }
   
   @Override
   public void setSetting(List<_Setting> settings) {
      console.setSetting(settings);
   }
   
   @Override
   public boolean hasSetting(String settingId) {
      try {
         console.getSetting(settingId);
         return true;
      } catch (Throwable t) {
         // TODO catch better
         return false;
      }
   }
   
   @Override
   public String getAddress() {
      return console.getAddress();
   }
   
   @Override
   public Long getPort() {
      return console.getPort();
   }
   
   @Override
   public String getProtocol() {
      return console.getProtocol();
   }
   
   @Override
   public boolean isEnable() {
      return console.isEnabled();
   }
   
   @Override
   public void setEnable(boolean isEnable) {
      console.setEnabled(isEnable);
   }
   
   @Override
   public boolean isActive() {
      return console.isActive();
   }
   
   @Override
   public _Machine getMachine() {
      return machine;
   }
   
   @Override
   public String getId() {
      return EntityType.Console.getId();
   }
   
   @Override
   public EntityType getType() {
      return EntityType.Console;
   }
   
}
