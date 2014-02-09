package org.altherian.hboxd.core.action.guest;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.GuestNetworkInterfaceInput;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.output.hypervisor.GuestNetworkInterfaceOutput;
import org.altherian.hboxd.comm.io.factory.GuestNetworkInterfaceIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.hypervisor.vm.guest._RawGuestNetworkInterface;
import org.altherian.hboxd.session.SessionContext;

import java.util.Arrays;
import java.util.List;

public class GuestNetworkInterfaceFindAction extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.GuestNetworkInterfaceFind.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      MachineInput mIn = request.get(MachineInput.class);
      GuestNetworkInterfaceInput gNicIn = request.get(GuestNetworkInterfaceInput.class);
      
      _RawGuestNetworkInterface rawGNic = hbox.getHypervisor().getMachine(mIn.getUuid()).getGuest().getNetworkInterfaceByMac(gNicIn.getMacAddress());
      GuestNetworkInterfaceOutput gNicOut = GuestNetworkInterfaceIoFactory.get(rawGNic);

      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, gNicOut));
   }
   
}
