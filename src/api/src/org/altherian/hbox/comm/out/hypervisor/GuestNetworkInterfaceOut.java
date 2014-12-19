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

package org.altherian.hbox.comm.out.hypervisor;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.ObjectOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.GuestNetworkInterfaceAttribute;

public class GuestNetworkInterfaceOut extends ObjectOut {
   
   protected GuestNetworkInterfaceOut() {
      
   }
   
   public GuestNetworkInterfaceOut(String id, boolean isUp, String macAddress) {
      super(EntityType.GuestNetworkInterface, id);
      setSetting(new BooleanSettingIO(GuestNetworkInterfaceAttribute.IsUp, isUp));
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.MacAddress, macAddress));
   }
   
   public GuestNetworkInterfaceOut(String id, boolean isUp, String macAddress, String ip4Address, String ip4Subnet) {
      this(id, isUp, macAddress);
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.IP4Address, ip4Address));
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.IP4Subnet, ip4Subnet));
   }
   
   public GuestNetworkInterfaceOut(String id, boolean isUp, String macAddress, String ip4Address, String ip4Subnet, String ip6Address,
         String ip6Subnet) {
      this(id, isUp, macAddress, ip4Address, ip4Subnet);
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.IP6Address, ip6Address));
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttribute.IP6Subnet, ip6Subnet));
   }
   
   public boolean isUp() {
      return getSetting(GuestNetworkInterfaceAttribute.IsUp).getBoolean();
   }
   
   public String getMacAddress() {
      return getSetting(GuestNetworkInterfaceAttribute.MacAddress).getString();
   }
   
   public String getIp4Address() {
      return getSetting(GuestNetworkInterfaceAttribute.IP4Address).getString();
   }
   
   public String getIp4Subnet() {
      return getSetting(GuestNetworkInterfaceAttribute.IP4Subnet).getString();
   }
   
   public String getIp6Address() {
      return getSetting(GuestNetworkInterfaceAttribute.IP6Address).getString();
   }
   
   public String getIp6Subnet() {
      return getSetting(GuestNetworkInterfaceAttribute.IP6Subnet).getString();
   }
   
}
