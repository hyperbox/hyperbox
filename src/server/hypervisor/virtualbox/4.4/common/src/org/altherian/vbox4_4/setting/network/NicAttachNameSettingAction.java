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

package org.altherian.vbox4_4.setting.network;

import org.altherian.hbox.constant.NetworkInterfaceAttribute;
import org.altherian.setting._Setting;
import org.altherian.vbox.settings.network.NicAttachNameSetting;
import org.altherian.vbox4_4.setting._NetworkInterfaceSettingAction;
import org.virtualbox_4_4.INetworkAdapter;
import org.virtualbox_4_4.LockType;

public class NicAttachNameSettingAction implements _NetworkInterfaceSettingAction {

   @Override
   public LockType getLockType() {
      return LockType.Shared;
   }

   @Override
   public String getSettingName() {
      return NetworkInterfaceAttribute.AttachName.toString();
   }

   @Override
   public void set(INetworkAdapter nic, _Setting setting) {
      switch (nic.getAttachmentType()) {
         case Bridged:
            nic.setBridgedInterface(setting.getString());
            break;
         case Generic:
            nic.setGenericDriver(setting.getString());
            break;
         case HostOnly:
            nic.setHostOnlyInterface(setting.getString());
            break;
         case Internal:
            nic.setInternalNetwork(setting.getString());
            break;
         default:
            break;
      }
   }

   @Override
   public _Setting get(INetworkAdapter nic) {
      String value = "";
      switch (nic.getAttachmentType()) {
         case Bridged:
            value = nic.getBridgedInterface();
            break;
         case Generic:
            value = nic.getGenericDriver();
            break;
         case HostOnly:
            value = nic.getHostOnlyInterface();
            break;
         case Internal:
            value = nic.getInternalNetwork();
            break;
         default:
            break;
      }
      return new NicAttachNameSetting(value);
   }

}
