package org.altherian.hboxd.core.action.hypervisor;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hboxd.comm.io.factory.MediumIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.tool.logging.Logger;

import java.util.Arrays;
import java.util.List;

public class HypervisorToolsMediumGet extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.ToolsMediumGet.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      MediumOutput medOut = null;
      
      if (hbox.getHypervisor().hasToolsMedium()) {
         _RawMedium med = hbox.getHypervisor().getToolsMedium();
         Logger.debug("Hypervisor has tools medium - Location: " + med.getLocation());
         medOut = MediumIoFactory.get(med);
      } else {
         Logger.debug("Hypervisor does not have tools medium");
      }
      
      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, MediumOutput.class, medOut));
   }
   
}
