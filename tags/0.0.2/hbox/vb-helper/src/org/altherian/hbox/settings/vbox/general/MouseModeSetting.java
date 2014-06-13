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

package org.altherian.hbox.settings.vbox.general;

import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hbox.constant.MouseModes;
import org.altherian.hboxd.settings.StringSetting;

public class MouseModeSetting extends StringSetting {
   
   public MouseModeSetting(String mode) {
      super(MachineAttributes.MouseMode, mode);
   }
   
   public MouseModeSetting(MouseModes mode) {
      this(mode.toString());
   }
   
}