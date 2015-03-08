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

import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.OsTypeOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import java.util.List;

import javax.swing.SwingWorker;

public class OsTypeListWorker extends SwingWorker<Void, OsTypeOutput> {
   
   private _OsTypeListReceiver recv;
   private MachineOutput mOut;
   
   public OsTypeListWorker(_OsTypeListReceiver recv, MachineOutput mOut) {
      this.recv = recv;
      this.mOut = mOut;
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      recv.loadingStarted();
      for (OsTypeOutput ostOut : Gui.getServer(mOut.getServerId()).listOsType(new MachineInput(mOut))) {
         publish(ostOut);
      }
      
      return null;
   }
   
   @Override
   protected void process(List<OsTypeOutput> ostOutList) {
      recv.add(ostOutList);
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
   
   public static void run(_OsTypeListReceiver recv, MachineOutput mOut) {
      new OsTypeListWorker(recv, mOut).execute();
   }
   
}