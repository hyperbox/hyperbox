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

package org.altherian.hboxd.comm.io.factory.event;

import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.event.task.TaskQueueEventOutput;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.event._Event;
import org.altherian.hbox.states.TaskQueueEvents;
import org.altherian.hboxd.comm.io.factory.ServerIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.tool.logging.Logger;

public final class TaskQueueEventIoFactory implements _EventIoFactory {
   
   @Override
   public Enum<?>[] getHandles() {
      return new Enum<?>[] {
            HyperboxEvents.TaskQueue
      };
   }
   
   @Override
   public EventOutput get(_Hyperbox hbox, _Event ev) {
      Logger.track();
      
      switch ((HyperboxEvents) ev.getEventId()) {
         case TaskQueue:
            Logger.debug("Creating a new TaskQueueEvent of type " + ev.get(TaskQueueEvents.class));
            return new TaskQueueEventOutput(ev.getTime(), ServerIoFactory.get(), ev.get(TaskOutput.class), ev.get(TaskQueueEvents.class));
         default:
            return null;
      }
   }
   
}
