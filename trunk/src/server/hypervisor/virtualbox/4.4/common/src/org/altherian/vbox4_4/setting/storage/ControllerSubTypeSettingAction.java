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

package org.altherian.vbox4_4.setting.storage;

import org.altherian.hbox.constant.StorageControllerAttribute;
import org.altherian.setting._Setting;
import org.altherian.vbox.settings.storage.ControllerSubTypeSetting;
import org.altherian.vbox4_4.setting._StorageControllerSettingAction;
import org.virtualbox_4_4.IStorageController;
import org.virtualbox_4_4.LockType;
import org.virtualbox_4_4.StorageControllerType;

public class ControllerSubTypeSettingAction implements _StorageControllerSettingAction {
   
   @Override
   public LockType getLockType() {
      return LockType.Write;
   }
   
   @Override
   public String getSettingName() {
      return StorageControllerAttribute.SubType.toString();
   }
   
   @Override
   public void set(IStorageController sct, _Setting setting) {
      StorageControllerType type = StorageControllerType.valueOf(setting.getValue().toString());
      sct.setControllerType(type);
   }
   
   @Override
   public _Setting get(IStorageController sct) {
      return new ControllerSubTypeSetting(sct.getControllerType().toString());
   }
   
}
