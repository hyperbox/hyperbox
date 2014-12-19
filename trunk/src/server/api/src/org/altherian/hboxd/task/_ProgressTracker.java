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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.task;

public interface _ProgressTracker {
   
   public String getId();
   
   public String getType();
   
   public String getState();
   
   public String getDescription();
   
   public boolean isCancelable();
   
   public long getPercent();
   
   /**
    * In seconds
    * 
    * @return seconds remaining
    */
   public long timeRemaining();
   
   public boolean isRunning();
   
   public boolean hasBeenCanceled();
   
   public long returnCode();
   
   public boolean hasFailed();
   
   public long getErrorCode();
   
   public String getError();
   
   public long getOperationCount();
   
   public long getCurrentOperation();
   
   public String getOperationDescription();
   
   public long getOperationPercent();
   
}
