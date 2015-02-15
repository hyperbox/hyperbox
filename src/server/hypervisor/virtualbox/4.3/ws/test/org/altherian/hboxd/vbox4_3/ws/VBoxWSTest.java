/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxd.vbox4_3.ws;

import org.altherian.hbox.comm.io.NetService_DHCP_IP4_IO;
import org.altherian.hbox.comm.io.NetService_IP4_IO;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.hypervisor.net._NetAdaptor;
import org.altherian.hbox.hypervisor.net._NetService;
import org.altherian.hbox.hypervisor.net._NetService_IP4_DHCP;
import org.altherian.hboxd.hypervisor.DummyEventManager;
import org.altherian.hboxd.hypervisor.HypervisorTest;
import org.altherian.vbox4_3.ws.VBoxWSHypervisor;
import org.junit.BeforeClass;
import org.junit.Test;

public class VBoxWSTest extends HypervisorTest {
   
   @BeforeClass
   public static void beforeClass() throws HyperboxException {
      hypervisor = new VBoxWSHypervisor();
      hypervisor.setEventManager(new DummyEventManager());
      
      HypervisorTest.init();
   }
   
   @Test
   public void configureHostOnly() {
      _NetAdaptor adapt = hypervisor.createAdaptor("HostOnly", "");
      try {
         _NetService ip4 = new NetService_IP4_IO(true, "10.50.0.254", "255.255.255.0");
         _NetService_IP4_DHCP dhcp = new NetService_DHCP_IP4_IO(true);
         dhcp.setAddress("10.50.0.2");
         dhcp.setNetmask("255.255.255.0");
         dhcp.setStartAddress("10.50.0.10");
         dhcp.setEndAddress("10.50.0.20");
         adapt.setService(ip4);
         adapt.setService(dhcp);
      } finally {
         //hypervisor.removeAdaptor(adapt.getMode().getId(), adapt.getId());
      }
   }
   
}
