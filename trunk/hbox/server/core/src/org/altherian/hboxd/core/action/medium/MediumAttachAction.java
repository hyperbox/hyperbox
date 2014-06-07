package org.altherian.hboxd.core.action.medium;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.MediumInput;
import org.altherian.hbox.comm.input.StorageDeviceAttachmentInput;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.hypervisor.storage._RawMedium;

import java.util.Arrays;
import java.util.List;

public class MediumAttachAction extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.MediumMount.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      MachineInput mIn = request.get(MachineInput.class);
      StorageDeviceAttachmentInput sdaIn = request.get(StorageDeviceAttachmentInput.class);
      MediumInput medIn = request.get(MediumInput.class);
      
      _RawMedium medium = hbox.getHypervisor().getMedium(medIn.getLocation(), sdaIn.getDeviceType());
      hbox.getHypervisor().getMachine(mIn.getUuid()).getStorageController(sdaIn.getControllerId())
      .attachMedium(medium, sdaIn.getPortId(), sdaIn.getDeviceId());
   }
   
}
