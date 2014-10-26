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

package org.altherian.hbox.comm.out;

import org.altherian.hbox.constant.Entity;

public class ProgressTrackerOut extends ObjectOut {
   
   private String id;
   private String type;
   private String state;
   
   @SuppressWarnings("unused")
   private ProgressTrackerOut() {
      // used for (de)serialisation
   }
   
   public ProgressTrackerOut(String id, String type, String state) {
      super(Entity.ProgressTracker, id);
      this.type = type;
      this.state = state;
   }
   
   @Override
   public String getId() {
      return id;
   }
   
   public String getType() {
      return type;
   }
   
   public String getState() {
      return state;
   }
   
   public String getDescription() {
      return null;
      
   }
   
   public boolean isCancelable() {
      return false;
      
   }
   
   public long getPercent() {
      return 0;
      
   }
   
   /**
    * In seconds
    * 
    * @return seconds remaining
    */
   public long timeRemaining() {
      return 0;
      
   }
   
   public boolean isRunning() {
      return false;
      
   }
   
   public boolean hasBeenCanceled() {
      return false;
      
   }
   
   public long returnCode() {
      return 0;
      
   }
   
   public boolean hasFailed() {
      return false;
      
   }
   
   public long getErrorCode() {
      return 0;
      
   }
   
   public String getError() {
      return null;
      
   }
   
   public long getOperationCount() {
      return 0;
      
   }
   
   public long getCurrentOperation() {
      return 0;
      
   }
   
   public String getOperationDescription() {
      return null;
      
   }
   
   public long getOperationPercent() {
      return 0;
      
   }
   
}
