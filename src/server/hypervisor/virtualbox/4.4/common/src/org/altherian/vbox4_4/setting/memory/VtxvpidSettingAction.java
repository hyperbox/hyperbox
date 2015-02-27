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

package org.altherian.vbox4_4.setting.memory;

import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.setting.BooleanSetting;
import org.altherian.setting._Setting;
import org.altherian.vbox.settings.memory.VtxvpidSetting;
import org.altherian.vbox4_4.setting._MachineSettingAction;
import org.virtualbox_4_4.HWVirtExPropertyType;
import org.virtualbox_4_4.IMachine;
import org.virtualbox_4_4.LockType;

public class VtxvpidSettingAction implements _MachineSettingAction {
   
   @Override
   public LockType getLockType() {
      return LockType.Write;
   }
   
   @Override
   public String getSettingName() {
      return MachineAttribute.Vtxvpid.toString();
   }
   
   @Override
   public void set(IMachine machine, _Setting setting) {
      machine.setHWVirtExProperty(HWVirtExPropertyType.VPID, ((BooleanSetting) setting).getValue());
   }
   
   @Override
   public _Setting get(IMachine machine) {
      return new VtxvpidSetting(machine.getHWVirtExProperty(HWVirtExPropertyType.VPID));
   }
   
}
