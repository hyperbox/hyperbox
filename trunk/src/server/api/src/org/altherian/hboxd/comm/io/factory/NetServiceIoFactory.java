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

import org.altherian.hbox.comm.in.NetServiceIP4In;
import org.altherian.hbox.comm.in.NetServiceIn;
import org.altherian.hbox.comm.out.network.NetServiceIP4Out;
import org.altherian.hbox.comm.out.network.NetServiceOut;
import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.hypervisor.net._NetService;
import org.altherian.hbox.hypervisor.net._NetServiceIP4;
import org.altherian.hboxd.hypervisor.net.NetServiceIPv4;

public class NetServiceIoFactory {
   
   private NetServiceIoFactory() {
      // only static
   }
   
   public static NetServiceOut get(_NetService svc) {
      if (NetServiceType.IPv4.is(svc.getType())) {
         _NetServiceIP4 ip4 = (_NetServiceIP4) svc;
         return new NetServiceIP4Out(ip4.isEnabled(), ip4.getAddress(), ip4.getMask());
      } else {
         throw new HyperboxRuntimeException(svc.getType() + " is not supported");
      }
   }
   
   public static _NetService get(NetServiceIn svcIn) {
      if (NetServiceType.IPv4.is(svcIn.getServiceTypeId())) {
         NetServiceIP4In ip4In = (NetServiceIP4In) svcIn;
         return new NetServiceIPv4(ip4In.isEnabled(), ip4In.getIP(), ip4In.getMask());
      } else {
         throw new HyperboxRuntimeException(svcIn.getServiceTypeId() + " is not supported");
      }
   }
   
}
