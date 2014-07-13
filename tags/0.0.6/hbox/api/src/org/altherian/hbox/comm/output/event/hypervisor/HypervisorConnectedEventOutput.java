/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hbox.comm.output.event.hypervisor;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.hypervisor.HypervisorOutput;
import org.altherian.hbox.event.HyperboxEvents;

import java.util.Date;

public class HypervisorConnectedEventOutput extends HypervisorEventOutput {
   
   protected HypervisorConnectedEventOutput() {
      // Used for serialization
   }
   
   public HypervisorConnectedEventOutput(Date time, ServerOutput srvOut, HypervisorOutput hypOut) {
      super(time, HyperboxEvents.HypervisorConnected, srvOut, hypOut);
   }
   
   @Override
   public String toString() {
      return "Server " + getServer().getName() + " connected to Hypervisor " + getHypervisor().getId() + " @ " + getTime();
   }
   
}
