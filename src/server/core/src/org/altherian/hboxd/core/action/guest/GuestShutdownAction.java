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

package org.altherian.hboxd.core.action.guest;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.states.ACPI;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.AbstractHyperboxMultiTaskAction;
import org.altherian.hboxd.exception.ActionCanceledException;
import org.altherian.hboxd.exception.task.TaskInvalidStateException;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.tool.logging.Logger;
import java.util.Arrays;
import java.util.List;

public class GuestShutdownAction extends AbstractHyperboxMultiTaskAction {
   
   private Thread runningThread;
   private boolean canceled = false;
   
   private void init() {
      runningThread = Thread.currentThread();
      canceled = false;
   }
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.HBOX.getId() + HyperboxTasks.GuestShutdown.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return true;
   }
   
   @Override
   public boolean isCancelable() {
      return true;
   }
   
   @Override
   public void cancel() {
      if (runningThread == null) {
         throw new TaskInvalidStateException("Cannot cancel a non-running task");
      }
      
      canceled = true;
      runningThread.interrupt();
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      init();
      try {
         MachineIn mIn = request.get(MachineIn.class);
         _RawVM m = hbox.getHypervisor().getMachine(mIn.getUuid());
         while (!m.getState().equals(MachineStates.PoweredOff)) {
            if (canceled) {
               Logger.debug("Action has been canceled, interupting");
               throw new ActionCanceledException();
            }
            if (m.getState().equals(MachineStates.Running)) {
               m.sendAcpi(ACPI.PowerButton);
            }
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e) {
               throw new ActionCanceledException();
            }
         }
      } finally {
         runningThread = null;
      }
   }
   
}
