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

package org.altherian.hbox.comm.out.event.machine;

import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.states.MachineStates;
import java.util.Date;

public final class MachineStateEventOut extends MachineEventOut {

   private MachineStates state;

   @SuppressWarnings("unused")
   private MachineStateEventOut() {
      // Used for serialization
   }

   public MachineStateEventOut(Date time, ServerOut srvOut, MachineOut mOut, MachineStates state) {
      super(time, HyperboxEvents.MachineState, srvOut, mOut);
      this.state = state;
   }

   public MachineStates getState() {
      return state;
   }

   @Override
   public String toString() {
      return "Machine " + getUuid() + " | State changed to " + getState() + " @ " + getTime();
   }

}
