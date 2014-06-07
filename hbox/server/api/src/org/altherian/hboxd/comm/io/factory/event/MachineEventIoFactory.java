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

package org.altherian.hboxd.comm.io.factory.event;

import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineDataChangeEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineRegistrationEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineSnapshotDataChangedEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineStateEventOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.event._Event;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.comm.io.factory.MachineIoFactory;
import org.altherian.hboxd.comm.io.factory.ServerIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.event.machine.MachineEvent;
import org.altherian.hboxd.event.machine.MachineRegistrationEvent;
import org.altherian.hboxd.event.machine.MachineStateEvent;

public final class MachineEventIoFactory implements _EventIoFactory {
   
   @Override
   public Enum<?>[] getHandles() {
      return new Enum<?>[] {
            HyperboxEvents.MachineState,
            HyperboxEvents.MachineRegistration,
            HyperboxEvents.MachineDataChange,
            HyperboxEvents.MachineSnapshotDataChange
      };
   }
   
   @Override
   public EventOutput get(_Hyperbox hbox, _Event ev) {
      MachineEvent mEv = (MachineEvent) ev;
      MachineOutput mOut = null;
      try {
         mOut = MachineIoFactory.get(HBoxServer.get().getMachine(mEv.getMachineUuid()));
      } catch (HyperboxRuntimeException e) {
         mOut = MachineIoFactory.get(mEv.getMachineUuid(), MachineStates.UNKNOWN.getId());
      }
      switch ((HyperboxEvents) ev.getEventId()) {
         case MachineState:
            return new MachineStateEventOutput(mEv.getTime(), ServerIoFactory.get(), mOut, ((MachineStateEvent) ev).getState());
         case MachineRegistration:
            return new MachineRegistrationEventOutput(mEv.getTime(), ServerIoFactory.get(), mOut,
                  ((MachineRegistrationEvent) mEv).isRegistrated());
         case MachineDataChange:
            return new MachineDataChangeEventOutput(mEv.getTime(), ServerIoFactory.get(), mOut);
         case MachineSnapshotDataChange:
            return new MachineSnapshotDataChangedEventOutput(mEv.getTime(), ServerIoFactory.get(), mOut);
         default:
            return null;
      }
   }
   
}
