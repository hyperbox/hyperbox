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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.in.NetworkAttachModeIn;
import org.altherian.hbox.comm.out.network.NetworkAttachNameOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import java.util.List;

import javax.swing.SwingWorker;

public class NetworkAttachNameListWorker extends SwingWorker<Void, NetworkAttachNameOut> {
   
   private _NetworkAttachNameReceiver recv;
   private String serverId;
   private String netAttachModeId;
   
   public NetworkAttachNameListWorker(_NetworkAttachNameReceiver recv, String serverId, String netAttachModeId) {
      this.recv = recv;
      this.serverId = serverId;
      this.netAttachModeId = netAttachModeId;
      recv.loadingStarted();
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      for (NetworkAttachNameOut nanOut : Gui.getServer(serverId).listNetworkAttachNames(new NetworkAttachModeIn(netAttachModeId))) {
         publish(nanOut);
      }
      
      return null;
   }
   
   @Override
   protected void process(List<NetworkAttachNameOut> nanOut) {
      recv.add(nanOut);
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         get();
         recv.loadingFinished(true, null);
      } catch (Throwable e) {
         recv.loadingFinished(false, e.getMessage());
      }
   }
   
   public static void run(_NetworkAttachNameReceiver recv, String serverId, String netAttachModeId) {
      new NetworkAttachNameListWorker(recv, serverId, netAttachModeId).execute();
   }
   
}
