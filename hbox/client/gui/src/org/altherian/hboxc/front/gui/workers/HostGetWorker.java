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

import org.altherian.hbox.comm.output.host.HostOutput;
import org.altherian.hboxc.front.gui.Gui;

import javax.swing.SwingWorker;

public class HostGetWorker extends SwingWorker<HostOutput, Void> {
   
   private _HostReceiver recv;
   private String srvId;
   
   private HostGetWorker(_HostReceiver recv, String srvId) {
      this.recv = recv;
      this.srvId = srvId;
      recv.loadingStarted();
   }
   
   @Override
   protected HostOutput doInBackground() throws Exception {
      HostOutput hostOut = Gui.getServer(srvId).getHost();
      return hostOut;
   }
   
   @Override
   protected void done() {
      try {
         HostOutput hostOut = get();
         recv.put(hostOut);
         recv.loadingFinished(true, null);
      } catch (Throwable t) {
         recv.loadingFinished(false, t.getMessage());
      }
   }
   
   public static void get(_HostReceiver recv, String srvId) {
      new HostGetWorker(recv, srvId).execute();
   }

}
