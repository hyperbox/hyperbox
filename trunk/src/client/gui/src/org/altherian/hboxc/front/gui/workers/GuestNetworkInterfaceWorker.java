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

package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.out.hypervisor.GuestNetworkInterfaceOut;
import org.altherian.hbox.comm.out.network.NetworkInterfaceOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AxSwingWorker;
import java.util.concurrent.ExecutionException;

public class GuestNetworkInterfaceWorker extends AxSwingWorker<_GuestNetworkInterfaceReceiver, GuestNetworkInterfaceOut, Void> {
   
   private String srvId;
   private String vmId;
   private NetworkInterfaceOut nicOut;
   
   public GuestNetworkInterfaceWorker(_GuestNetworkInterfaceReceiver recv, String srvId, String vmId, NetworkInterfaceOut nicOut) {
      super(recv);
      this.srvId = srvId;
      this.vmId = vmId;
      this.nicOut = nicOut;
   }
   
   @Override
   protected GuestNetworkInterfaceOut doInBackground() throws Exception {
      return Gui.getServer(srvId).getGuest(vmId).findNetworkInterface(nicOut.getMacAddress());
   }
   
   @Override
   protected void innerDone() throws InterruptedException, ExecutionException {
      GuestNetworkInterfaceOut gNicOut = get();
      getReceiver().put(gNicOut);
   }
   
   public static void execute(_GuestNetworkInterfaceReceiver recv, String srvId, String vmId, NetworkInterfaceOut nicOut) {
      (new GuestNetworkInterfaceWorker(recv, srvId, vmId, nicOut)).execute();
   }
   
}
