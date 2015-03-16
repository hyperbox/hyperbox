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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.vbox4_3.setting.storage;

import org.altherian.hbox.constant.MediumAttribute;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.setting.StringSetting;
import org.altherian.setting._Setting;
import org.altherian.vbox4_3.setting._MediumSettingAction;
import org.virtualbox_4_3.IMedium;

public class MediumParentUuidSettingAction implements _MediumSettingAction {

   @Override
   public String getSettingName() {
      return MediumAttribute.ParentUUID.toString();
   }

   @Override
   public void set(IMedium medium, _Setting setting) {
      throw new HyperboxRuntimeException("Read-only setting");
   }

   @Override
   public _Setting get(IMedium medium) {
      if (medium.getParent() != null) {
         return new StringSetting(MediumAttribute.ParentUUID, medium.getParent().getId());
      } else {
         return new StringSetting(MediumAttribute.ParentUUID, "");
      }
   }

}
