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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.altherian.vbox4_3.net;

import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.hypervisor.net._NetService;
import org.altherian.hbox.hypervisor.net._NetServiceIP4;
import org.altherian.hboxd.hypervisor.net.NetServiceIPv4;
import org.altherian.vbox.net.VBoxAdaptor;
import org.altherian.vbox4_3.VBox;
import org.virtualbox_4_3.IHostNetworkInterface;


public class VBoxHostOnlyAdaptor extends VBoxAdaptor {
   
   public VBoxHostOnlyAdaptor(IHostNetworkInterface nic) {
      super(nic.getId(), nic.getName(), VBoxNetMode.HostOnly);
   }

   @Override
   protected void process(_NetService service) {
      if (NetServiceType.IPv4.is(service.getType())) {
         _NetServiceIP4 ip4Svc = (_NetServiceIP4) service;
         IHostNetworkInterface nic = VBox.get().getHost().findHostNetworkInterfaceById(getId());
         if (ip4Svc.isEnabled()) {
            nic.enableStaticIPConfig(ip4Svc.getIP(), ip4Svc.getMask());
         } else {
            nic.enableStaticIPConfig("", "");
         }
      } else {
         throw new HyperboxRuntimeException("Service type " + service.getType() + " is not supported on " + getMode().getId() + " adaptor");
      }
   }
   
   @Override
   public _NetService getService(String serviceTypeId) {
      IHostNetworkInterface nic = VBox.get().getHost().findHostNetworkInterfaceById(getId());
      if (NetServiceType.IPv4.is(serviceTypeId)) {
         return new NetServiceIPv4(true, nic.getIPAddress(), nic.getNetworkMask());
      } else {
         throw new HyperboxRuntimeException("Service type " + serviceTypeId + " is not supported on " + getMode().getId() + " adaptor");
      }
   }
   
}
