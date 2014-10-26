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

import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.comm.out.event.machine.MachineDataChangeEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineRegistrationEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineSnapshotDataChangedEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineStateEventOut;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
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
import org.altherian.tool.logging.Logger;

public final class MachineEventIoFactory implements _EventIoFactory {
   
   private MachineOut getObjOut(String id) {
      try {
         return MachineIoFactory.get(HBoxServer.get().getMachine(id));
      } catch (HyperboxRuntimeException e) {
         Logger.exception(e);
         return MachineIoFactory.get(id, MachineStates.Unknown.getId());
      }
   }
   
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
   public EventOut get(_Hyperbox hbox, _Event ev) {
      MachineEvent mEv = (MachineEvent) ev;
      MachineOut mOut = MachineIoFactory.get(mEv.getMachineId(), MachineStates.Unknown.getId());
      try {
         mOut = MachineIoFactory.get(HBoxServer.get().getMachine(mEv.getMachineId()));
      } catch (HyperboxRuntimeException e) {
         Logger.exception(e);
      }
      switch ((HyperboxEvents) ev.getEventId()) {
         case MachineState:
            return new MachineStateEventOut(mEv.getTime(), ServerIoFactory.get(), getObjOut(mEv.getMachineId()), ((MachineStateEvent) ev).getState());
         case MachineRegistration:
            return new MachineRegistrationEventOut(mEv.getTime(), ServerIoFactory.get(), mOut,
                  ((MachineRegistrationEvent) mEv).isRegistrated());
         case MachineDataChange:
            return new MachineDataChangeEventOut(mEv.getTime(), ServerIoFactory.get(), mOut);
         case MachineSnapshotDataChange:
            return new MachineSnapshotDataChangedEventOut(mEv.getTime(), ServerIoFactory.get(), mOut);
         default:
            return null;
      }
   }
   
}
