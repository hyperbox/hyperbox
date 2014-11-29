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

import org.altherian.hbox.comm.in.TaskIn;
import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AxSwingWorker;

import java.util.concurrent.ExecutionException;

public class TaskGetWorker extends AxSwingWorker<_TaskReceiver, TaskOut, Void> {
   
   private TaskOut objOut;
   
   public TaskGetWorker(_TaskReceiver recv, TaskOut objOut) {
      super(recv);
      this.objOut = objOut;
   }
   
   @Override
   protected TaskOut doInBackground() throws Exception {
      return Gui.getServer(objOut.getServerId()).getTask(new TaskIn(objOut));
   }
   
   @Override
   protected void innerDone() throws InterruptedException, ExecutionException {
      TaskOut tOut = get();
      getReceiver().put(tOut);
   }
   
   public static void execute(_TaskReceiver recv, TaskOut objOut) {
      (new TaskGetWorker(recv, objOut)).execute();
   }
   
}
