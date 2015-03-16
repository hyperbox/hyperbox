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

package org.altherian.vbox4_4.net;

import org.altherian.hbox.comm.io.NATRuleIO;
import org.altherian.hbox.comm.io.NetService_DHCP_IP4_IO;
import org.altherian.hbox.comm.io.NetService_IP4_CIDR_IO;
import org.altherian.hbox.comm.io.NetService_IP6_Gateway_IO;
import org.altherian.hbox.comm.io.NetService_IP6_IO;
import org.altherian.hbox.comm.io.NetService_NAT_IP4_IO;
import org.altherian.hbox.comm.io.NetService_NAT_IP6_IO;
import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.hypervisor.net._NetService;
import org.altherian.tool.logging.Logger;
import org.altherian.vbox.net.VBoxAdaptor;
import org.altherian.vbox4_4.VBox;
import org.virtualbox_4_4.INATNetwork;

public class VBoxNatNetworkAdaptor extends VBoxAdaptor {

   public VBoxNatNetworkAdaptor(INATNetwork natNet) {
      super(natNet.getNetworkName(), natNet.getNetworkName(), VBoxNetMode.NATNetwork, natNet.getEnabled());
   }

   @Override
   public void setLabel(String label) {
      INATNetwork natNet = VBox.get().findNATNetworkByName(getId());
      natNet.setNetworkName(label);
   }

   @Override
   protected void process(_NetService svc) {
      NetServiceType svcType = NetServiceType.valueOf(svc.getType());
      INATNetwork natNet = VBox.get().findNATNetworkByName(getId());
      switch (svcType) {
         case IPv4_NetCIDR:
            natNet.setNetwork(((NetService_IP4_CIDR_IO) svc).getCIDR());
            break;
         case DHCP_IPv4:
            natNet.setNeedDhcpServer(svc.isEnabled());
            break;
         case IPv6:
            natNet.setIPv6Enabled(svc.isEnabled());
            break;
         case IPv6_Gateway:
            natNet.setAdvertiseDefaultIPv6RouteEnabled(svc.isEnabled());
            break;
         case NAT_IPv4:
            Logger.warning(svcType + " is not implemented for " + getMode().getId());
            break;
         case NAT_IPv6:
            Logger.warning(svcType + " is not implemented for " + getMode().getId());
            break;
         default:
            throw new IllegalArgumentException("Service type " + svc.getType() + " is not supported on " + getMode().getId() + " adaptor");
      }
   }

   @Override
   public _NetService getService(String serviceTypeId) {
      INATNetwork natNet = VBox.get().findNATNetworkByName(getId());

      if (NetServiceType.IPv4_NetCIDR.is(serviceTypeId)) {
         return new NetService_IP4_CIDR_IO(natNet.getNetwork());
      }

      if (NetServiceType.DHCP_IPv4.is(serviceTypeId)) {
         return new NetService_DHCP_IP4_IO(natNet.getNeedDhcpServer());
      }

      if (NetServiceType.IPv6.is(serviceTypeId)) {
         return new NetService_IP6_IO(natNet.getIPv6Enabled());
      }

      if (NetServiceType.IPv6_Gateway.is(serviceTypeId)) {
         return new NetService_IP6_Gateway_IO(natNet.getAdvertiseDefaultIPv6RouteEnabled());
      }

      if (NetServiceType.NAT_IPv4.is(serviceTypeId)) {
         NetService_NAT_IP4_IO svc = new NetService_NAT_IP4_IO(true);
         for (String ruleRaw : natNet.getPortForwardRules4()) {
            String[] ruleRawSplit = ruleRaw.split(":");
            svc.addRule(new NATRuleIO(ruleRawSplit[0], ruleRawSplit[1], ruleRawSplit[2].replace("[", "").replace("]", ""), ruleRawSplit[3], ruleRawSplit[4]
                  .replace("[", "").replace("]", ""), ruleRawSplit[5]));
         }
         return svc;
      }

      if (NetServiceType.NAT_IPv6.is(serviceTypeId)) {
         NetService_NAT_IP6_IO svc = new NetService_NAT_IP6_IO(true);
         for (String ruleRaw : natNet.getPortForwardRules6()) {
            String[] ruleRawSplit = ruleRaw.split(":");
            svc.addRule(new NATRuleIO(ruleRawSplit[0], ruleRawSplit[1], ruleRawSplit[2].replace("[", "").replace("]", ""), ruleRawSplit[3], ruleRawSplit[4]
                  .replace("[", "").replace("]", ""), ruleRawSplit[5]));
         }
         return svc;
      }

      throw new IllegalArgumentException("Service type " + serviceTypeId + " is not supported on " + getMode().getId() + " adaptor");

   }

}
