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

package org.altherian.hboxd.vbox.hypervisor.ws4_2.vm;

import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hboxd.hypervisor.vm.device._RawMemory;
import org.altherian.hboxd.settings.BooleanSetting;
import org.altherian.hboxd.settings.PositiveNumberSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.hboxd.vbox4_2.ws.manager.VbSettingManager;
import org.altherian.vbox.settings.memory.LargePagesSetting;
import org.altherian.vbox.settings.memory.MemorySetting;
import org.altherian.vbox.settings.memory.NestedPagingSetting;
import org.altherian.vbox.settings.memory.PagefusionSetting;
import org.altherian.vbox.settings.memory.VtxvpidSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VBoxMemory implements _RawMemory {
   
   private VBoxMachine machine;
   
   public VBoxMemory(VBoxMachine machine) {
      this.machine = machine;
   }
   
   @Override
   public long getAmount() {
      return ((PositiveNumberSetting) machine.getSetting(MachineAttributes.Memory)).getValue();
   }
   
   @Override
   public void setAmount(long amount) {
      setSetting(new MemorySetting(amount));
   }
   
   @Override
   public boolean isLargePageEnabled() {
      return ((BooleanSetting) machine.getSetting(MachineAttributes.LargePages)).getValue();
   }
   
   @Override
   public void setLargePage(boolean isEnabled) {
      setSetting(new LargePagesSetting(isEnabled));
   }
   
   @Override
   public boolean isPageFusionEnabled() {
      return ((BooleanSetting) machine.getSetting(MachineAttributes.PageFusion)).getValue();
   }
   
   @Override
   public void setPageFusion(boolean isEnabled) {
      setSetting(new PagefusionSetting(isEnabled));
   }
   
   @Override
   public boolean isNestedPagingEnabled() {
      return ((BooleanSetting) machine.getSetting(MachineAttributes.NestedPaging)).getValue();
   }
   
   @Override
   public void setNestedPaging(boolean isEnabled) {
      setSetting(new NestedPagingSetting(isEnabled));
   }
   
   @Override
   public boolean isVTxvpidEnabled() {
      return ((BooleanSetting) machine.getSetting(MachineAttributes.Vtxvpid)).getValue();
   }
   
   @Override
   public void setVtxvpid(boolean isEnabled) {
      setSetting(new VtxvpidSetting(isEnabled));
   }
   
   @Override
   public List<_Setting> listSettings() {
      List<_Setting> settings = new ArrayList<_Setting>();
      for (MachineAttributes setting : MachineAttributes.values()) {
         if (setting.getDeviceType().equals(EntityTypes.Memory)) {
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
