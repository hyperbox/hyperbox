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

package org.altherian.hboxd.vbox4_4.xpcom.setting;

import org.altherian.hboxd.settings._Setting;

import org.virtualbox_4_4.INetworkAdapter;
import org.virtualbox_4_4.LockType;

public interface _NetworkInterfaceSettingAction {
   
   public LockType getLockType();
   
   public String getSettingName();
   
   public void set(INetworkAdapter nic, _Setting setting);
   
   public _Setting get(INetworkAdapter nic);
   
}
