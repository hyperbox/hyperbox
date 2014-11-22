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

package org.altherian.vbox4_3.setting.storage;

import org.altherian.hbox.constant.StorageControllerAttribute;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.setting._Setting;
import org.altherian.vbox.settings.storage.ControllerNameSetting;
import org.altherian.vbox4_3.setting._StorageControllerSettingAction;

import org.virtualbox_4_3.IStorageController;
import org.virtualbox_4_3.LockType;

public final class ControllerNameSettingAction implements _StorageControllerSettingAction {
   
   @Override
   public LockType getLockType() {
      return LockType.Write;
   }
   
   @Override
   public String getSettingName() {
      return StorageControllerAttribute.Name.toString();
   }
   
   @Override
   public void set(IStorageController sct, _Setting setting) {
      if (!setting.getString().contentEquals(get(sct).getString())) {
         throw new HyperboxRuntimeException("Read-only setting");
      }
   }
   
   @Override
   public _Setting get(IStorageController sct) {
      return new ControllerNameSetting(sct.getName());
   }
   
}
