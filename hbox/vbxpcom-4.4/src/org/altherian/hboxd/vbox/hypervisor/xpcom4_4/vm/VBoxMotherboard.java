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

package org.altherian.hboxd.vbox.hypervisor.xpcom4_4.vm;

import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hbox.settings.vbox.motherboard.ACPISetting;
import org.altherian.hbox.settings.vbox.motherboard.HardwareUuidSetting;
import org.altherian.hbox.settings.vbox.motherboard.IoAPICSetting;
import org.altherian.hboxd.hypervisor.vm.device._RawMotherboard;
import org.altherian.hboxd.settings.BooleanSetting;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.hboxd.vbox4_4.xpcom.manager.VbSettingManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VBoxMotherboard implements _RawMotherboard {
   
   private VBoxMachine machine;
   
   public VBoxMotherboard(VBoxMachine machine) {
      this.machine = machine;
   }
   
   @Override
   public boolean isAcpiEnabled() {
      return ((BooleanSetting) machine.getSetting(MachineAttributes.ACPI)).getValue();
   }
   
   @Override
   public void setAcpi(boolean isEnabled) {
      setSetting(new ACPISetting(isEnabled));
   }
   
   @Override
   public boolean isIoApicEnabled() {
      return ((BooleanSetting) machine.getSetting(MachineAttributes.IoAPIC)).getValue();
   }
   
   @Override
   public void setIoApic(boolean isEnabled) {
      setSetting(new IoAPICSetting(isEnabled));
   }
   
   @Override
   public String getHardwareUuid() {
      return ((StringSetting) machine.getSetting(MachineAttributes.HardwareUuid)).getValue();
   }
   
   @Override
   public void setHardwareUuid(String uuid) {
      setSetting(new HardwareUuidSetting(uuid));
   }
   
   @Override
   public List<_Setting> listSettings() {
      List<_Setting> settings = new ArrayList<_Setting>();
      for (MachineAttributes setting : MachineAttributes.values()) {
         if (setting.getDeviceType().equals(EntityTypes.Motherboard)) {
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
