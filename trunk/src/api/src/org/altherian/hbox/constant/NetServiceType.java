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

package org.altherian.hbox.constant;

import org.altherian.hbox.hypervisor.net._NetService;

public enum NetServiceType {
   
   /**
    * Can IPv4 be enabled/disable
    */
   IPv4,
   
   /**
    * Can IPv6 be enabled/disable
    */
   IPv6,
   
   /**
    * Can a DHCP server be enabled/disable
    */
   DHCP_IPv4,
   
   /**
    * Can an NAT engine for IPv4 be enabled/disabled
    */
   NAT_IPv4,
   
   /**
    * Can an NAT engine for IPv6 be enabled/disabled
    */
   NAT_IPv6,
   
   ;
   
   public String getId() {
      return toString();
   }
   
   public boolean typeOf(_NetService svc) {
      return this.getId() == svc.getType();
   }
   
   public boolean is(Object o) {
      return this.getId().equalsIgnoreCase(o.toString());
   }
   
}
