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

package org.altherian.vbox4_2.net;

import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.hypervisor.net._NetService;
import org.altherian.vbox.net.VBoxAdaptor;
import org.virtualbox_4_2.HostNetworkInterfaceStatus;
import org.virtualbox_4_2.IHostNetworkInterface;

public class VBoxBridgedAdaptor extends VBoxAdaptor {

   public VBoxBridgedAdaptor(IHostNetworkInterface nic) {
      super(nic.getId(), nic.getName(), VBoxNetMode.Bridged, nic.getStatus().equals(HostNetworkInterfaceStatus.Up));
   }

   @Override
   protected void process(_NetService service) {
      throw new HyperboxRuntimeException(service.getType() + " is not supported by Bridged adaptor");
   }

}
