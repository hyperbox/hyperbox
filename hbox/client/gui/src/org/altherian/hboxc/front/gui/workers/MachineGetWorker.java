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
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import javax.swing.SwingWorker;

public class MachineGetWorker extends SwingWorker<MachineOutput, Void> {
   
   private _MachineReceiver recv;
   private MachineOutput mOut;
   
   public MachineGetWorker(_MachineReceiver recv, MachineOutput mOut) {
      this.recv = recv;
      this.mOut = mOut;
   }
   
   @Override
   protected MachineOutput doInBackground() throws Exception {
      recv.loadingStarted();
      MachineOutput newMachineOut = Gui.getServer(mOut.getServerId()).getMachine(new MachineInput(mOut));
      return newMachineOut;
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         MachineOutput mOut = get();
         recv.put(mOut);
         recv.loadingFinished(true, null);
      } catch (Throwable e) {
         Logger.exception(e);
         recv.loadingFinished(false, e.getMessage());
      }
   }
   
   public static void get(_MachineReceiver recv, MachineOutput mOut) {
      new MachineGetWorker(recv, mOut).execute();
   }
   
}
