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
import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import java.util.Date;

public abstract class MachineEventOut extends EventOut {

   protected MachineEventOut() {
      // Used for serialization
   }

   public MachineEventOut(Date time, Enum<?> id, ServerOut srvOut, MachineOut mOut) {
      super(time, id, srvOut);
      set(MachineOut.class, mOut);
   }

   public MachineOut getMachine() {
      return get(MachineOut.class);
   }

   public String getUuid() {
      return getMachine().getUuid();
   }

   @Override
   public String toString() {
      return "Machine " + getUuid() + " on Server " + getServerId() + " | Event ID " + getId() + " occured @ " + getTime();
   }

}
