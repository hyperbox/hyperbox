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

package org.altherian.vbox4_2.vm;

import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.hboxd.hypervisor.vm.device._RawCPU;
import org.altherian.setting._Setting;
import org.altherian.vbox.settings.cpu.CpuCountSetting;
import org.altherian.vbox4_2.manager.VBoxSettingManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VBoxCPU implements _RawCPU {

   private VBoxMachine machine;

   public VBoxCPU(VBoxMachine machine) {
      this.machine = machine;
   }

   @Override
   public long getAmount() {
      return ((CpuCountSetting) getSetting(MachineAttribute.CpuCount)).getValue();
   }

   @Override
   public void setAmount(long amount) {
      setSetting(new CpuCountSetting(amount));
   }

   @Override
   public List<_Setting> listSettings() {
      List<_Setting> settings = new ArrayList<_Setting>();
      for (MachineAttribute setting : MachineAttribute.values()) {
         if (setting.getDeviceType().equals(EntityType.CPU)) {
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
      VBoxSettingManager.set(machine, Arrays.asList(s));
   }

   @Override
   public void setSetting(List<_Setting> s) {
      VBoxSettingManager.set(machine, s);
   }

}
