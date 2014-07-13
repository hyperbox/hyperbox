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

package org.altherian.hbox.comm.output.event.storage;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.event.machine.MachineEventOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerOutput;

import java.util.Date;

public abstract class StorageControllerEventOutput extends MachineEventOutput {
   
   protected StorageControllerEventOutput() {
      // Used for serialization
   }
   
   public StorageControllerEventOutput(Date time, Enum<?> id, ServerOutput srvOut, MachineOutput vmOut, StorageControllerOutput stoOut) {
      super(time, id, srvOut,vmOut);
      set(StorageControllerOutput.class, stoOut);
   }
   
   public StorageControllerOutput getStorageController() {
      return get(StorageControllerOutput.class);
   }
   
}
