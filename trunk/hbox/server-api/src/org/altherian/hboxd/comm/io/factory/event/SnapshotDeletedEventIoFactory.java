package org.altherian.hboxd.comm.io.factory.event;

import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotDeletedEventOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.SnapshotOutput;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.event._Event;
import org.altherian.hboxd.comm.io.factory.MachineIoFactory;
import org.altherian.hboxd.comm.io.factory.ServerIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.event.snapshot.SnapshotEvent;
import org.altherian.hboxd.hypervisor.vm._RawVM;

public class SnapshotDeletedEventIoFactory implements _EventIoFactory {
   
   @Override
   public Enum<?>[] getHandles() {
      return new Enum<?>[] {
            HyperboxEvents.SnapshotDeleted,
      };
   }
   
   @Override
   public EventOutput get(_Hyperbox hbox, _Event ev) {
      SnapshotEvent sEv = (SnapshotEvent) ev;
      
      _RawVM vm = hbox.getHypervisor().getMachine(sEv.getMachineUuid());
      MachineOutput mOut = MachineIoFactory.get(vm);
      SnapshotOutput snapOut = new SnapshotOutput(sEv.getSnapshotUuid());
      return new SnapshotDeletedEventOutput(sEv.getTime(), ServerIoFactory.get(), mOut, snapOut);
   }
   
}
