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

package org.altherian.hboxc.core.server;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.TaskIn;
import org.altherian.hboxc.server._Server;
import org.altherian.hboxc.server.task._Task;

public class Task implements _Task {
   
   private _Server srv;
   private String taskId;
   
   public Task(_Server srv, String taskId) {
      this.srv = srv;
      this.taskId = taskId;
   }
   
   @Override
   public void cancel() {
      srv.sendRequest(new Request(Command.HBOX, HyperboxTasks.TaskCancel, new TaskIn(taskId)));
   }
   
}
