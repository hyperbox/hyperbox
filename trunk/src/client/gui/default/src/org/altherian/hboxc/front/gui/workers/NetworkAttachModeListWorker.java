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

import org.altherian.hbox.comm.out.network.NetworkAttachModeOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AxSwingWorker;
import org.altherian.hboxc.front.gui.worker.receiver._NetworkAttachModeReceiver;
import java.util.List;

public class NetworkAttachModeListWorker extends AxSwingWorker<_NetworkAttachModeReceiver, Void, NetworkAttachModeOut> {
   
   private String srvId;
   
   public NetworkAttachModeListWorker(_NetworkAttachModeReceiver recv, String serverId) {
      super(recv);
      this.srvId = serverId;
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      for (NetworkAttachModeOut ostOut : Gui.getServer(srvId).listNetworkAttachModes()) {
         publish(ostOut);
      }
      
      return null;
   }
   
   @Override
   protected void process(List<NetworkAttachModeOut> ostOutList) {
      getReceiver().add(ostOutList);
   }
   
   public static void execute(_NetworkAttachModeReceiver recv, String serverId) {
      (new NetworkAttachModeListWorker(recv, serverId)).execute();
   }
   
}
