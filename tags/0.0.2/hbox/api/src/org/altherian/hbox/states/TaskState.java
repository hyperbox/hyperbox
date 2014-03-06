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

package org.altherian.hbox.states;

public enum TaskState {
   
   Created(false, false),
   Pending(false, false),
   Running(false, false),
   Paused(false, false),
   Completed(true, false),
   Canceled(true, true),
   Failed(true, true),
   CriticalFailure(true, true);
   
   private boolean isFinishing;
   private boolean isFailing;
   
   private TaskState(boolean isFinishing, boolean isFailing) {
      this.isFinishing = isFinishing;
      this.isFailing = isFailing;
   }
   
   public boolean isFinishing() {
      return isFinishing;
   }
   
   public boolean isFailing() {
      return isFailing;
   }

   public String getId() {
      return toString();
   }
   
}
