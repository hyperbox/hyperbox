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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.output.ExceptionOutput;
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hbox.comm.output.UserOutput;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.task._Task;

public class TaskIoFactory {
   
   private TaskIoFactory() {
      // will not be used
   }
   
   public static TaskOutput get(_Task t) {
      UserOutput uOut = UserIoFactory.get(t.getUser());
      
      ExceptionOutput eOut = null;
      if (t.getError() != null) {
         eOut = ExceptionIoFactory.get(t.getError());
      }
      
      TaskOutput tOut = new TaskOutput(
            HBoxServer.get().getId(),
            t.getId(),
            t.getRequest().getName(),
            t.getRequest().getExchangeId(),
            t.getState(),
            uOut,
            t.getCreateTime(),
            t.getQueueTime(),
            t.getStartTime(),
            t.getStopTime(),
            eOut);
      
      return tOut;
   }
   
}
