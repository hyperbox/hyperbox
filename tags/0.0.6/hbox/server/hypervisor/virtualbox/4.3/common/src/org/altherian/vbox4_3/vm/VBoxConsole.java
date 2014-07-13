/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.vbox4_3.vm;

import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hboxd.hypervisor.vm.device._RawConsole;
import org.altherian.hboxd.settings.BooleanSetting;
import org.altherian.hboxd.settings.PositiveNumberSetting;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.vbox4_3.manager.VBoxSettingManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VBoxConsole implements _RawConsole {
   
   private VBoxMachine machine;
   
   public VBoxConsole(VBoxMachine machine) {
      this.machine = machine;
   }
   
   @Override
   public List<_Setting> listSettings() {
      List<_Setting> settings = new ArrayList<_Setting>();
      for (MachineAttributes setting : MachineAttributes.values()) {
         if (setting.getDeviceType().equals(EntityTypes.Console)) {
            getSetting(setting);
         }
      }
      return settings;
   }
   
   @Override
   public _Setting getSetting(Object getName) {
      return VBoxSettingManager.get(machine, getName);
   }
   
   @Override
   public void setSetting(_Setting s) {
      machine.setSetting(Arrays.asList(s));
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      machine.setSetting(s);
   }
   
   @Override
   public Boolean isEnabled() {
      return getSetting(MachineAttributes.VrdeEnabled).getBoolean();
   }
   
   @Override
   public void setEnabled(Boolean enable) {
      setSetting(new BooleanSetting(MachineAttributes.VrdeEnabled, enable));
   }
   
   @Override
   public String getAuthType() {
      return getSetting(MachineAttributes.VrdeAuthType).getString();
   }
   
   @Override
   public void setAuthType(String authType) {
      setSetting(new StringSetting(MachineAttributes.VrdeAuthType, authType));
   }
   
   @Override
   public String getAuthLibrary() {
      return getSetting(MachineAttributes.VrdeAuthLibrary).getString();
   }
   
   @Override
   public void setAuthLibrary(String library) {
      setSetting(new StringSetting(MachineAttributes.VrdeAuthLibrary, library));
   }
   
   @Override
   public Long getAuthTimeout() {
      return getSetting(MachineAttributes.VrdeAuthTimeout).getNumber();
   }
   
   @Override
   public void setAuthTimeout(Long timeout) {
      setSetting(new PositiveNumberSetting(MachineAttributes.VrdeAuthTimeout, timeout));
   }
   
   @Override
   public Boolean getAllowMultiConnection() {
      return getSetting(MachineAttributes.VrdeMultiConnection).getBoolean();
   }
   
   @Override
   public void setAllowMultiConnection(Boolean allow) {
      setSetting(new BooleanSetting(MachineAttributes.VrdeMultiConnection, allow));
   }
   
   /*
   @Override
   public Boolean getReuseSingleConnection() {
      return getSetting(MachineAttributes.VrdeReuseSingleConnection).getBoolean();
   }
   
   @Override
   public void setReuseSingleConnection(Boolean reuse) {
      setSetting(new BooleanSetting(MachineAttributes.VrdeReuseSingleConnection, reuse));
   }
   
   @Override
   public String getModule() {
      return getSetting(MachineAttributes.VrdeExtPack).getString();
   }
   
   @Override
   public void setModule(String module) {
      setSetting(new StringSetting(MachineAttributes.VrdeExtPack, module));
   }
    */
   
}
