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

package org.altherian.hbox.comm.out.network;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.out.ObjectOut;
import org.altherian.hbox.constant.Entity;
import org.altherian.hbox.constant.NetworkInterfaceAttribute;

import java.util.List;

public final class NetworkInterfaceOut extends ObjectOut {
   
   private long nicId;
   
   @SuppressWarnings("unused")
   private NetworkInterfaceOut() {
      // stub
   }
   
   public NetworkInterfaceOut(Long nicId, List<SettingIO> settings) {
      super(Entity.NetworkInterace, nicId.toString(), settings);
      this.nicId = nicId;
   }
   
   public long getNicId() {
      return nicId;
   }
   
   public boolean isEnabled() {
      return getSetting(NetworkInterfaceAttribute.Enabled).getBoolean();
   }
   
   public boolean isCableConnected() {
      return getSetting(NetworkInterfaceAttribute.CableConnected).getBoolean();
   }
   
   public String getAttachMode() {
      return getSetting(NetworkInterfaceAttribute.AttachMode).getString();
   }
   
   public String getAttachName() {
      return getSetting(NetworkInterfaceAttribute.AttachName).getString();
   }
   
   public String getAdapterType() {
      return getSetting(NetworkInterfaceAttribute.AdapterType).getString();
   }
   
   public String getMacAddress() {
      return getSetting(NetworkInterfaceAttribute.MacAddress).getString();
   }
   
}
