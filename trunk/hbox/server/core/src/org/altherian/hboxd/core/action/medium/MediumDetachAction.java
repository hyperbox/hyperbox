package org.altherian.hboxd.core.action.medium;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.StorageDeviceAttachmentInput;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;

import java.util.Arrays;
import java.util.List;

public class MediumDetachAction extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.MediumUnmount.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      MachineInput mIn = request.get(MachineInput.class);
      StorageDeviceAttachmentInput sdaIn = request.get(StorageDeviceAttachmentInput.class);
      
      hbox.getHypervisor().getMachine(mIn.getUuid()).getStorageController(sdaIn.getControllerId())
      .detachMedium(sdaIn.getPortId(), sdaIn.getDeviceId());
   }
   
}
