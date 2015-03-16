/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.hbox.comm.io;

import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.hypervisor.net._NetService_IP4_DHCP;

public class NetService_DHCP_IP4_IO extends NetServiceIO implements _NetService_IP4_DHCP {

   private String addr;
   private String netmask;
   private String startAddr;
   private String endAddr;

   protected NetService_DHCP_IP4_IO() {
      // serial
   }

   public NetService_DHCP_IP4_IO(NetService_DHCP_IP4_IO svc) {
      this(svc.isEnabled());
      setAddress(svc.getAddress());
      setNetmask(svc.getMask());
      setStartAddress(svc.getStartAddress());
      setEndAddress(svc.getEndAddress());
   }

   public NetService_DHCP_IP4_IO(boolean enabled) {
      super(NetServiceType.DHCP_IPv4.getId(), enabled);
   }

   @Override
   public String getAddress() {
      return addr;
   }

   @Override
   public String getMask() {
      return netmask;
   }

   @Override
   public String getStartAddress() {
      return startAddr;
   }

   @Override
   public String getEndAddress() {
      return endAddr;
   }

   @Override
   public void setAddress(String addr) {
      this.addr = addr;
   }

   @Override
   public void setNetmask(String netmask) {
      this.netmask = netmask;
   }

   @Override
   public void setStartAddress(String startAddr) {
      this.startAddr = startAddr;
   }

   @Override
   public void setEndAddress(String endAddr) {
      this.endAddr = endAddr;
   }

}
