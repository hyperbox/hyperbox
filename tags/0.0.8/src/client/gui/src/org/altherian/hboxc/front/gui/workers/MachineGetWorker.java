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

import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AxSwingWorker;

import java.util.concurrent.ExecutionException;

public class MachineGetWorker extends AxSwingWorker<_MachineReceiver, MachineOut, Void> {
   
   private MachineOut mOut;
   
   public MachineGetWorker(_MachineReceiver recv, MachineOut mOut) {
      super(recv);
      this.mOut = mOut;
   }
   
   @Override
   protected MachineOut doInBackground() throws Exception {
      MachineOut newMachineOut = Gui.getServer(mOut.getServerId()).getMachine(mOut.getUuid());
      return newMachineOut;
   }
   
   @Override
   protected void innerDone() throws InterruptedException, ExecutionException {
      MachineOut mOut = get();
      getReceiver().put(mOut);
   }
   
   public static void execute(_MachineReceiver recv, MachineOut mOut) {
      (new MachineGetWorker(recv, mOut)).execute();
   }
   
}
