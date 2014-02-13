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

package org.altherian.hboxd.vbox.hypervisor.xpcom4_3.vm;

import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.KeyboardModes;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hbox.settings.vbox.general.KeyboardModeSetting;
import org.altherian.hboxd.hypervisor.vm.device._RawKeyboard;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.hboxd.vbox4_3.xpcom.manager.VbSettingManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VBoxKeyboard implements _RawKeyboard {
   
   private VBoxMachine machine;
   
   public VBoxKeyboard(VBoxMachine machine) {
      this.machine = machine;
   }
   
   @Override
   public String getMode() {
      return ((StringSetting) machine.getSetting(MachineAttributes.KeyboardMode)).getValue();
   }
   
   @Override
   public void setMode(String mode) {
      setSetting(new KeyboardModeSetting(mode));
   }
   
   @Override
   public void setMode(KeyboardModes mode) {
      setMode(mode.toString());
   }
   
   @Override
   public List<_Setting> listSettings() {
      List<_Setting> settings = new ArrayList<_Setting>();
      for (MachineAttributes setting : MachineAttributes.values()) {
         if (setting.getDeviceType().equals(EntityTypes.Keyboard)) {
            getSetting(setting);
         }
      }
      return settings;
   }
   
   @Override
   public _Setting getSetting(Object getName) {
      return VbSettingManager.get(machine, getName);
   }
   
   @Override
   public void setSetting(_Setting s) {
      machine.setSetting(Arrays.asList(s));
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      machine.setSetting(s);
   }
   
}
