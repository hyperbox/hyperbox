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

package org.altherian.hbox.comm.output.event.snapshot;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.event.machine.MachineEventOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.SnapshotOutput;

import java.util.Date;

public abstract class SnapshotEventOutput extends MachineEventOutput {
   
   protected SnapshotEventOutput() {
      // Used for serialization
   }
   
   public SnapshotEventOutput(Date time, Enum<?> id, ServerOutput srvOut, MachineOutput mOut, SnapshotOutput snapOut) {
      super(time, id, srvOut, mOut);
      set(SnapshotOutput.class, snapOut);
   }
   
   public SnapshotOutput getSnapshot() {
      return get(SnapshotOutput.class);
   }
   
   public String getSnapshotUuid() {
      return getSnapshot().getUuid();
   }
   
   @Override
   public String toString() {
      return "Event ID " + getId() + " occured @ " + getTime() + " for snapshot " + getSnapshotUuid() + " on machine " + getMachine().getId();
   }
   
}
