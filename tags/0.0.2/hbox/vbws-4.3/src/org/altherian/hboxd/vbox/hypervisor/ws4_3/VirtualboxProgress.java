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

package org.altherian.hboxd.vbox.hypervisor.ws4_3;

import org.altherian.hboxd.task._ProgressTracker;

import org.virtualbox_4_3.IProgress;

public class VirtualboxProgress implements _ProgressTracker {
   
   private IProgress rawProgress;
   
   public VirtualboxProgress(IProgress rawProgress) {
      this.rawProgress = rawProgress;
   }
   
   @Override
   public String getId() {
      return rawProgress.getId();
   }
   
   @Override
   public String getType() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public String getState() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public String getDescription() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public boolean isCancelable() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public long getPercent() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public long timeRemaining() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public boolean isRunning() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public boolean hasBeenCanceled() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public long returnCode() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public boolean hasFailed() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public long getErrorCode() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public String getError() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public long getOperationCount() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public long getCurrentOperation() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public String getOperationDescription() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public long getOperationPercent() {
      // TODO Auto-generated method stub
      return 0;
   }
   
}
