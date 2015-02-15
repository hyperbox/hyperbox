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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.io.NetServiceIO;
import org.altherian.hbox.comm.io.NetService_DHCP_IP4_IO;
import org.altherian.hbox.comm.io.NetService_IP4_CIDR_IO;
import org.altherian.hbox.comm.io.NetService_IP4_IO;
import org.altherian.hbox.comm.io.NetService_IP6_Gateway_IO;
import org.altherian.hbox.comm.io.NetService_IP6_IO;
import org.altherian.hbox.comm.io.NetService_NAT_IP4_IO;
import org.altherian.hbox.comm.io.NetService_NAT_IP6_IO;
import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.hypervisor.net._NetService;

public class NetServiceIoFactory {

   private NetServiceIoFactory() {
      // only static
   }

   public static NetServiceIO get(_NetService svc) {
      return svc.getIO();
   }

   public static _NetService get(NetServiceIO svcIn) {
      if (NetServiceType.IPv4.is(svcIn.getType())) {
         return new NetService_IP4_IO((NetService_IP4_IO) svcIn);
      }
      
      if (NetServiceType.IPv4_NetCIDR.is(svcIn.getType())) {
         return new NetService_IP4_CIDR_IO((NetService_IP4_CIDR_IO) svcIn);
      }

      if (NetServiceType.IPv6.is(svcIn.getType())) {
         return new NetService_IP6_IO((NetService_IP6_IO) svcIn);
      }

      if (NetServiceType.IPv6_Gateway.is(svcIn.getType())) {
         return new NetService_IP6_Gateway_IO((NetService_IP6_Gateway_IO) svcIn);
      }

      if (NetServiceType.DHCP_IPv4.is(svcIn.getType())) {
         return new NetService_DHCP_IP4_IO((NetService_DHCP_IP4_IO) svcIn);
      }
      
      if (NetServiceType.NAT_IPv4.is(svcIn.getType())) {
         return new NetService_NAT_IP4_IO((NetService_NAT_IP4_IO) svcIn);
      }

      if (NetServiceType.NAT_IPv6.is(svcIn.getType())) {
         return new NetService_NAT_IP6_IO((NetService_NAT_IP6_IO) svcIn);
      }
      
      throw new HyperboxRuntimeException(svcIn.getType() + " is not supported for network operations");
   }

}
