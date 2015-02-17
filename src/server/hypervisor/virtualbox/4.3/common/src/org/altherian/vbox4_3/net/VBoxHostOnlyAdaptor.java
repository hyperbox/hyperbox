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

package org.altherian.vbox4_3.net;

import org.altherian.hbox.comm.io.NetService_DHCP_IP4_IO;
import org.altherian.hbox.comm.io.NetService_IP4_IO;
import org.altherian.hbox.comm.io.NetService_IP6_IO;
import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.hypervisor.net._NetService;
import org.altherian.hbox.hypervisor.net._NetService_IP4;
import org.altherian.hbox.hypervisor.net._NetService_IP4_DHCP;
import org.altherian.hbox.hypervisor.net._NetService_IP6;
import org.altherian.vbox.net.VBoxAdaptor;
import org.altherian.vbox4_3.VBox;
import org.virtualbox_4_3.HostNetworkInterfaceStatus;
import org.virtualbox_4_3.IDHCPServer;
import org.virtualbox_4_3.IHostNetworkInterface;
import org.virtualbox_4_3.VBoxException;

public class VBoxHostOnlyAdaptor extends VBoxAdaptor {
   
   public VBoxHostOnlyAdaptor(IHostNetworkInterface nic) {
      super(nic.getId(), nic.getName(), VBoxNetMode.HostOnly, nic.getStatus().equals(HostNetworkInterfaceStatus.Up));
   }
   
   @Override
   protected void process(_NetService service) {
      IHostNetworkInterface nic = VBox.get().getHost().findHostNetworkInterfaceById(getId());
      if (NetServiceType.IPv4.is(service.getType())) {
         _NetService_IP4 ip4Svc = (_NetService_IP4) service;
         if (ip4Svc.isEnabled()) {
            nic.enableStaticIPConfig(ip4Svc.getAddress(), ip4Svc.getMask());
         } else {
            nic.enableStaticIPConfig("", "");
         }
      } else if (NetServiceType.IPv6.is(service.getType())) {
         _NetService_IP6 ip6Svc = (_NetService_IP6) service;
         if (nic.getIPV6Supported()) {
            nic.enableStaticIPConfigV6(ip6Svc.getAddress(), ip6Svc.getMask());
         }
      } else if (NetServiceType.DHCP_IPv4.is(service.getType())) {
         _NetService_IP4_DHCP dhcpSvc = (_NetService_IP4_DHCP) service;
         IDHCPServer dhcpSrv;
         try {
            dhcpSrv = VBox.get().findDHCPServerByNetworkName(nic.getNetworkName());
         } catch (VBoxException e) {
            if (!dhcpSvc.isEnabled()) {
               return;
            }

            dhcpSrv = VBox.get().createDHCPServer(nic.getNetworkName());
         }
         dhcpSrv.setEnabled(dhcpSvc.isEnabled());
         if (dhcpSvc.isEnabled()) {
            dhcpSrv.setConfiguration(dhcpSvc.getAddress(), dhcpSvc.getMask(), dhcpSvc.getStartAddress(), dhcpSvc.getEndAddress());
         }
      } else {
         throw new HyperboxRuntimeException("Service type " + service.getType() + " is not supported on " + getMode().getId() + " adaptor");
      }
   }
   
   @Override
   public _NetService getService(String serviceTypeId) {
      IHostNetworkInterface nic = VBox.get().getHost().findHostNetworkInterfaceById(getId());
      if (NetServiceType.IPv4.is(serviceTypeId)) {
         return new NetService_IP4_IO(true, nic.getIPAddress(), nic.getNetworkMask());
      } else if (NetServiceType.IPv6.is(serviceTypeId)) {
         return new NetService_IP6_IO(nic.getIPV6Supported(), nic.getIPV6Address(), nic.getIPV6NetworkMaskPrefixLength());
      } else if (NetServiceType.DHCP_IPv4.is(serviceTypeId)) {
         IDHCPServer dhcpSrv;
         try {
            dhcpSrv = VBox.get().findDHCPServerByNetworkName(nic.getNetworkName());
         } catch (VBoxException e) {
            return new NetService_DHCP_IP4_IO(false);
         }
         _NetService_IP4_DHCP svc = new NetService_DHCP_IP4_IO(dhcpSrv.getEnabled());
         svc.setAddress(dhcpSrv.getIPAddress());
         svc.setNetmask(dhcpSrv.getNetworkMask());
         svc.setStartAddress(dhcpSrv.getLowerIP());
         svc.setEndAddress(dhcpSrv.getUpperIP());
         return svc;
      } else {
         throw new HyperboxRuntimeException("Service type " + serviceTypeId + " is not supported on " + getMode().getId() + " adaptor");
      }
   }
   
}
