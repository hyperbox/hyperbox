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

import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class KeyboardTypeListWorker extends SwingWorker<Void, String> {
   
   private _KeyboardTypeListReceiver recv;
   private String serverId;
   private String machineId;
   
   public KeyboardTypeListWorker(_KeyboardTypeListReceiver recv, String serverId, String machineId) {
      this.recv = recv;
      this.serverId = serverId;
      this.machineId = machineId;
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      SwingUtilities.invokeLater(new Runnable() {
         
         @Override
         public void run() {
            recv.loadingStarted();
         }
         
      });
      
      for (String type : Gui.getServer(serverId).listKeyboardMode(new MachineIn(machineId))) {
         publish(type);
      }
      
      return null;
   }
   
   @Override
   protected void process(List<String> typeList) {
      recv.add(typeList);
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
   
   public static void get(_KeyboardTypeListReceiver recv, String serverId, String machineId) {
      new KeyboardTypeListWorker(recv, serverId, machineId).execute();
   }
   
}
