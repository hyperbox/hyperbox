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
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AnSwingWorker;
import org.altherian.tool.logging.Logger;

import java.util.List;

public class TaskListWorker extends AnSwingWorker<Void, TaskOutput, _TaskListReceiver> {
   
   private ServerOutput srvOut;
   
   public TaskListWorker(_TaskListReceiver recv, ServerOutput srvOut) {
      super(recv);
      this.srvOut = srvOut;
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      
      for (TaskOutput tOut : Gui.getServer(srvOut).listTasks()) {
         publish(tOut);
      }
      
      return null;
   }
   
   @Override
   protected void process(List<TaskOutput> tOutList) {
      getReceiver().add(tOutList);
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         get();
         getReceiver().loadingFinished(true, null);
      } catch (Throwable e) {
         Logger.exception(e);
         getReceiver().loadingFinished(false, e.getMessage());
      }
   }
   
   public static void run(_TaskListReceiver recv, ServerOutput srvOut) {
      new TaskListWorker(recv, srvOut).execute();
   }
   
}
