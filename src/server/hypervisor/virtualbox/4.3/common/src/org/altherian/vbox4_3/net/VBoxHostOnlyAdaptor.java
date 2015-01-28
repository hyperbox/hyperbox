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
import org.altherian.hbox.hypervisor.net._NetMode;
import org.altherian.hbox.hypervisor.net._NetService;
import org.altherian.hbox.hypervisor.net._NetServiceIP4;
import org.altherian.vbox.net.VBoxAdaptor;
import org.altherian.vbox4_3.VBox;
import java.util.ArrayList;
import java.util.List;
import org.virtualbox_4_3.IHostNetworkInterface;

public class VBoxHostOnlyAdaptor extends VBoxAdaptor {

   public VBoxHostOnlyAdaptor(String id, String label, _NetMode mode) {
      this(id, label, mode, new ArrayList<_NetService>());
   }

   public VBoxHostOnlyAdaptor(String id, String label, _NetMode mode, List<_NetService> services) {
      super(id, label, mode, services);
   }
   
   @Override
   protected void process(_NetService service) {
      IHostNetworkInterface hostNic = VBox.get().getHost().findHostNetworkInterfaceById(getId());
      if (NetServiceType.IPv4.typeOf(service)) {
         _NetServiceIP4 svcIp4 = (_NetServiceIP4) service;
         if (service.isEnabled()) {
            hostNic.enableStaticIPConfig(svcIp4.getIP(), svcIp4.getMask());
         } else {
            hostNic.enableDynamicIPConfig();
         }
      }
      
   }

}
