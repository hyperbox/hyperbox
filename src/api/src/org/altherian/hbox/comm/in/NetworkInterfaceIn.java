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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.NetworkInterfaceAttribute;

public class NetworkInterfaceIn extends DeviceInput {
   
   private long nicId;
   
   @SuppressWarnings("unused")
   private NetworkInterfaceIn() {
      // used only for (de)serialisation
   }
   
   public NetworkInterfaceIn(String machineUuid, long nicId) {
      setMachineUuid(machineUuid);
      this.nicId = nicId;
   }
   
   public long getNicId() {
      return nicId;
   }
   
   public boolean isEnabled() {
      return getSetting(NetworkInterfaceAttribute.Enabled).getBoolean();
   }
   
   public void setEnabled(boolean isEnabled) {
      setSetting(new BooleanSettingIO(NetworkInterfaceAttribute.Enabled, isEnabled));
   }
   
   public boolean isCableConnected() {
      return getSetting(NetworkInterfaceAttribute.CableConnected).getBoolean();
   }
   
   public void setCableConnected(boolean connected) {
      setSetting(new BooleanSettingIO(NetworkInterfaceAttribute.CableConnected, connected));
   }
   
   public String getAttachMode() {
      return getSetting(NetworkInterfaceAttribute.AttachMode).getString();
   }
   
   public void setAttachMode(String modeId) {
      setSetting(new StringSettingIO(NetworkInterfaceAttribute.AttachMode, modeId));
   }
   
   public String getAttachName() {
      if (hasSetting(NetworkInterfaceAttribute.AttachName)) {
         return getSetting(NetworkInterfaceAttribute.AttachName).getString();
      } else {
         return "";
      }
   }
   
   public void setAttachName(String nameId) {
      setSetting(new StringSettingIO(NetworkInterfaceAttribute.AttachName, nameId));
   }
   
   public String getAdapterType() {
      if (getSetting(NetworkInterfaceAttribute.AdapterType) != null) {
         return getSetting(NetworkInterfaceAttribute.AdapterType).getString();
      } else {
         return "";
      }
   }
   
   public void setAdapterType(String adapterTypeId) {
      setSetting(new StringSettingIO(NetworkInterfaceAttribute.AdapterType, adapterTypeId));
   }
   
   public String getMacAddress() {
      return getSetting(NetworkInterfaceAttribute.MacAddress).getString();
   }
   
   public void setMacAddress(String addr) {
      setSetting(new StringSettingIO(NetworkInterfaceAttribute.MacAddress, addr));
   }
   
}
