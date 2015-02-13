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

package org.altherian.hboxd.hypervisor.net;

import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.hypervisor.net._NetServiceIP4;

public class NetServiceIPv4 extends NetService implements _NetServiceIP4 {
   
   private String ip;
   private String mask;
   
   public NetServiceIPv4(boolean enabled) {
      super(NetServiceType.IPv4.getId(), NetServiceType.IPv4.getId(), NetServiceType.IPv4.getId(), enabled);
   }
   
   public NetServiceIPv4(boolean enabled, String ip, String mask) {
      this(enabled);
      setIP(ip);
      setMask(mask);
   }
   
   @Override
   public String getAddress() {
      return ip;
   }
   
   @Override
   public String getMask() {
      return mask;
   }
   
   @Override
   public String getGateway() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public String getNetwork() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public String getBroadcast() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public void setIP(String ip) {
      this.ip = ip;
   }
   
   @Override
   public void setMask(String mask) {
      this.mask = mask;
   }
   
   @Override
   public void setGateway(String gw) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public void setNetwork(String net) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public void setBroadcast(String bc) {
      // TODO Auto-generated method stub
      
   }
   
}
