/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
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

package org.altherian.hbox.comm.output.event.task;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.states.TaskQueueEvents;

import java.util.Date;

public final class TaskQueueEventOutput extends TaskEventOutput {
   
   private TaskQueueEvents ev;
   
   @SuppressWarnings("unused")
   private TaskQueueEventOutput() {
      // Used for serialization
   }
   
   public TaskQueueEventOutput(Date time, ServerOutput srvOut, TaskOutput tOut, TaskQueueEvents ev) {
      super(time, HyperboxEvents.TaskQueue, srvOut, tOut);
      this.ev = ev;
   }
   
   public TaskQueueEvents getQueueEvent() {
      return ev;
   }
   
   @Override
   public String toString() {
      return "Task Queue performed " + getQueueEvent() + " on Task ID #" + getTaskId() + " @ " + getTime();
   }
   
}
