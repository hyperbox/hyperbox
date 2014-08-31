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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.Entity;
import org.altherian.hbox.constant.GuestNetworkInterfaceAttribute;

public class GuestNetworkInterfaceIn extends ObjectIn<Entity> {
   
   public GuestNetworkInterfaceIn() {
      super(Entity.GuestNetworkInterface);
   }
   
   public GuestNetworkInterfaceIn(String id) {
      super(Entity.GuestNetworkInterface, id);
   }
   
   public boolean isUp() {
      return getSetting(GuestNetworkInterfaceAttribute.IsUp).getBoolean();
   }
   
   public void setUp(boolean isUp) {
      setSetting(new BooleanSettingIO(GuestNetworkInterfaceAttribute.IsUp, isUp));
   }
   
   public String getMacAddress() {
      return getSetting(GuestNetworkInterfaceAttribute.MacAddress).getString();
   }
   
   public GuestNetworkInterfaceIn setMacAddress(String macAddress) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.MacAddress, macAddress));
      return this;
   }
   
   public String getIp4Address() {
      return getSetting(GuestNetworkInterfaceAttribute.IP4Address).getString();
   }
   
   public void setIp4Address(String ip4Address) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.IP4Address, ip4Address));
   }
   
   public String getIp4Subnet() {
      return getSetting(GuestNetworkInterfaceAttribute.IP4Subnet).getString();
   }
   
   public void setIp4Subnet(String ip4Subnet) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.IP4Subnet, ip4Subnet));
   }
   
   public String getIp6Address() {
      return getSetting(GuestNetworkInterfaceAttribute.IP6Address).getString();
   }
   
   public void setIp6Address(String ip6Address) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.IP6Address, ip6Address));
   }
   
   public String getIp6Subnet() {
      return getSetting(GuestNetworkInterfaceAttribute.IP6Subnet).getString();
   }
   
   public void setIp6Subnet(String ip6Subnet) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.IP6Subnet, ip6Subnet));
   }
   
}
