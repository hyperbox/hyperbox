/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import java.util.List;

import javax.swing.SwingWorker;

public class MachineListWorker extends SwingWorker<Void, MachineOutput> {
   
   private _MachineListReceiver recv;
   private String serverId;
   
   public MachineListWorker(_MachineListReceiver recv, String serverId) {
      this.recv = recv;
      this.serverId = serverId;
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      recv.loadingStarted();
      for (MachineOutput mOut : Gui.getServer(serverId).listMachines()) {
         publish(mOut);
      }
      
      return null;
   }
   
   @Override
   protected void process(List<MachineOutput> mOutList) {
      recv.add(mOutList);
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
   
   public static void get(_MachineListReceiver recv, ServerOutput srvOut) {
      get(recv, srvOut.getId());
   }
   
   public static void get(_MachineListReceiver recv, String serverId) {
      new MachineListWorker(recv, serverId).execute();
   }
   
}
