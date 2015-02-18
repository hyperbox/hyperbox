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
import org.altherian.hbox.hypervisor.net._NetService_IP6;

public class NetService_IP6_IO extends NetServiceIO implements _NetService_IP6 {
   
   private String ip;
   private long mask;
   private String gw;
   
   protected NetService_IP6_IO() {
      // serial
   }
   
   public NetService_IP6_IO(NetService_IP6_IO svc) {
      this(svc.isEnabled(), svc.getAddress(), svc.getMask());
   }
   
   public NetService_IP6_IO(boolean enabled) {
      super(NetServiceType.IPv6.getId(), enabled);
   }
   
   public NetService_IP6_IO(boolean enabled, String ip, long mask) {
      this(enabled);
      setIP(ip);
      setMask(mask);
   }
   
   @Override
   public String getAddress() {
      return ip;
   }
   
   @Override
   public long getMask() {
      return mask;
   }
   
   @Override
   public String getGateway() {
      return gw;
   }
   
   @Override
   public void setIP(String ip) {
      this.ip = ip;
   }
   
   @Override
   public void setMask(long mask) {
      this.mask = mask;
   }
   
   @Override
   public void setGateway(String gw) {
      this.gw = gw;
   }
   
}
