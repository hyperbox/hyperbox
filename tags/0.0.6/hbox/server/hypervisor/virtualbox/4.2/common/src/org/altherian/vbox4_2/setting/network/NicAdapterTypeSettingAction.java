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

package org.altherian.vbox4_2.setting.network;

import org.altherian.hbox.constant.NetworkInterfaceSettings;
import org.altherian.hbox.exception.ConfigurationException;
import org.altherian.hboxd.settings._Setting;
import org.altherian.vbox.settings.network.NicAdapterTypeSetting;
import org.altherian.vbox4_2.setting._NetworkInterfaceSettingAction;

import org.virtualbox_4_2.INetworkAdapter;
import org.virtualbox_4_2.LockType;
import org.virtualbox_4_2.NetworkAdapterType;
import org.virtualbox_4_2.VBoxException;

public class NicAdapterTypeSettingAction implements _NetworkInterfaceSettingAction {
   
   @Override
   public LockType getLockType() {
      return LockType.Write;
   }
   
   @Override
   public String getSettingName() {
      return NetworkInterfaceSettings.AdapterType.getId();
   }
   
   @Override
   public void set(INetworkAdapter nic, _Setting setting) {
      try {
         nic.setAdapterType(NetworkAdapterType.valueOf(setting.getRawValue().toString()));
      } catch (VBoxException e) {
         throw new ConfigurationException(e.getMessage());
      } catch (IllegalArgumentException e) {
         throw new ConfigurationException("Unkown adapter type [" + setting.getRawValue().toString() + "]");
      }
   }
   
   @Override
   public _Setting get(INetworkAdapter nic) {
      return new NicAdapterTypeSetting(nic.getAdapterType().toString());
   }
   
}
