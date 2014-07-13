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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hbox.comm.input;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.GuestNetworkInterfaceAttributes;

public class GuestNetworkInterfaceInput extends ObjectInput {
   
   public GuestNetworkInterfaceInput() {
      
   }
   
   public GuestNetworkInterfaceInput(String id) {
      super(id);
   }
   
   public boolean isUp() {
      return getSetting(GuestNetworkInterfaceAttributes.IsUp).getBoolean();
   }
   
   public void setUp(boolean isUp) {
      setSetting(new BooleanSettingIO(GuestNetworkInterfaceAttributes.IsUp, isUp));
   }
   
   public String getMacAddress() {
      return getSetting(GuestNetworkInterfaceAttributes.MacAddress).getString();
   }
   
   public GuestNetworkInterfaceInput setMacAddress(String macAddress) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.MacAddress, macAddress));
      return this;
   }
   
   public String getIp4Address() {
      return getSetting(GuestNetworkInterfaceAttributes.IP4Address).getString();
   }
   
   public void setIp4Address(String ip4Address) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP4Address, ip4Address));
   }
   
   public String getIp4Subnet() {
      return getSetting(GuestNetworkInterfaceAttributes.IP4Subnet).getString();
   }
   
   public void setIp4Subnet(String ip4Subnet) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP4Subnet, ip4Subnet));
   }
   
   public String getIp6Address() {
      return getSetting(GuestNetworkInterfaceAttributes.IP6Address).getString();
   }
   
   public void setIp6Address(String ip6Address) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP6Address, ip6Address));
   }
   
   public String getIp6Subnet() {
      return getSetting(GuestNetworkInterfaceAttributes.IP6Subnet).getString();
   }
   
   public void setIp6Subnet(String ip6Subnet) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP6Subnet, ip6Subnet));
   }
   
}
