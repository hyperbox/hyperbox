package org.altherian.hboxc.core.server;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.GuestNetworkInterfaceInput;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.output.hypervisor.GuestNetworkInterfaceOutput;
import org.altherian.hboxc.comm.utils.Transaction;
import org.altherian.hboxc.server._GuestReader;
import org.altherian.hboxc.server._Server;

public class GuestReader implements _GuestReader {
   
   private _Server srv;
   private String machineUuid;
   
   public GuestReader(_Server srv, String machineUuid) {
      this.srv = srv;
      this.machineUuid = machineUuid;
   }

   @Override
   public GuestNetworkInterfaceOutput findNetworkInterface(String macAddress) {
      Request req = new Request(Command.VBOX,HypervisorTasks.GuestNetworkInterfaceFind);
      req.set(new MachineInput(machineUuid));
      req.set(new GuestNetworkInterfaceInput().setMacAddress(macAddress));
      Transaction t = srv.sendRequest(req);
      return t.extractItem(GuestNetworkInterfaceOutput.class);
   }
   
}
