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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.constant.NetServiceType;

public class NetServiceIP4In extends NetServiceIn {
   
   private String ip;
   private String mask;
   
   public NetServiceIP4In() {
      super(NetServiceType.IPv4.getId());
   }
   
   public NetServiceIP4In(boolean isEnabled, String ip, String mask) {
      this();
      setEnabled(isEnabled);
      setIP(ip);
      setMask(mask);
   }
   
   public String getIP() {
      return ip;
   }
   
   public void setIP(String ip) {
      this.ip = ip;
   }
   
   public String getMask() {
      return mask;
   }
   
   public void setMask(String mask) {
      this.mask = mask;
   }
   
}
