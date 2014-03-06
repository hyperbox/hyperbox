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

package org.altherian.hboxd.task;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.states.TaskState;
import org.altherian.hboxd.core.action._HyperboxAction;
import org.altherian.hboxd.security._User;

import java.util.Date;

public interface _Task {
   
   public String getId();
   
   public Throwable getError();
   
   public _HyperboxAction getAction();
   
   public Request getRequest();
   
   public _ProgressTracker getProgress();
   
   public _User getUser();
   
   public TaskState getState();
   
   public boolean isCancelable();
   
   public void queue();
   
   public void start();
   
   public void cancel();
   
   public Date getCreateTime();
   
   public Date getQueueTime();
   
   public Date getStartTime();
   
   public Date getStopTime();
   
}
