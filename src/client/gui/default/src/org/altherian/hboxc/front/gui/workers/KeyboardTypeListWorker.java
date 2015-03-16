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

import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AxSwingWorker;
import org.altherian.hboxc.front.gui.worker.receiver._KeyboardTypeListReceiver;
import java.util.List;

public class KeyboardTypeListWorker extends AxSwingWorker<_KeyboardTypeListReceiver, Void, String> {

   private String serverId;
   private String machineId;

   public KeyboardTypeListWorker(_KeyboardTypeListReceiver recv, String serverId, String machineId) {
      super(recv);
      this.serverId = serverId;
      this.machineId = machineId;
   }

   @Override
   protected Void doInBackground() throws Exception {
      for (String type : Gui.getServer(serverId).listKeyboardMode(new MachineIn(machineId))) {
         publish(type);
      }

      return null;
   }

   @Override
   protected void process(List<String> typeList) {
      getReceiver().add(typeList);
   }

   public static void execute(_KeyboardTypeListReceiver recv, String serverId, String machineId) {
      (new KeyboardTypeListWorker(recv, serverId, machineId)).execute();
   }

}
