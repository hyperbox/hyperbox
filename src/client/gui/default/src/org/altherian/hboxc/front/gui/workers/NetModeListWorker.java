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

package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.out.network.NetModeOut;
import org.altherian.hbox.exception.HypervisorNotConnectedException;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AxSwingWorker;
import org.altherian.hboxc.front.gui.worker.receiver._NetModeListReceiver;
import java.util.List;

public class NetModeListWorker extends AxSwingWorker<_NetModeListReceiver, Void, NetModeOut> {

   private String srvId;

   public NetModeListWorker(_NetModeListReceiver recv, String srvId) {
      super(recv);
      this.srvId = srvId;
   }

   @Override
   protected Void doInBackground() throws Exception {
      if (!Gui.getServer(srvId).isHypervisorConnected()) {
         throw new HypervisorNotConnectedException();
      }

      for (NetModeOut mode : Gui.getServer(srvId).getHypervisor().listNetworkModes()) {
         publish(mode);
      }

      return null;
   }

   @Override
   protected void process(List<NetModeOut> modes) {
      getReceiver().add(modes);
   }

   public static void execute(_NetModeListReceiver recv, String srvId) {
      (new NetModeListWorker(recv, srvId)).execute();
   }

}
