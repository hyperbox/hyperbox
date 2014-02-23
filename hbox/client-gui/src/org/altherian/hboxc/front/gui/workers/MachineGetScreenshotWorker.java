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
import org.altherian.hbox.comm.output.hypervisor.ScreenshotOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import javax.swing.SwingWorker;

public class MachineGetScreenshotWorker extends SwingWorker<ScreenshotOutput, Void> {
   
   private _MachineScreenshotReceiver recv;
   private MachineOutput mOut;
   
   public MachineGetScreenshotWorker(_MachineScreenshotReceiver recv, MachineOutput mOut) {
      this.recv = recv;
      this.mOut = mOut;
   }
   
   @Override
   protected ScreenshotOutput doInBackground() throws Exception {
      return Gui.getServer(mOut.getServerId()).getScreenshot(new MachineInput(mOut));
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         ScreenshotOutput scrOut = get();
         recv.put(scrOut);
         recv.loadingFinished(true, null);
      } catch (Throwable e) {
         recv.loadingFinished(false, e.getMessage());
      }
   }
   
   public static void get(_MachineScreenshotReceiver recv, MachineOutput mOut) {
      new MachineGetScreenshotWorker(recv, mOut).execute();
   }
   
}
