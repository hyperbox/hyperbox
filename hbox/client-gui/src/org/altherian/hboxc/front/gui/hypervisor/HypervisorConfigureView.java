package org.altherian.hboxc.front.gui.hypervisor;

import org.altherian.hbox.comm.input.HypervisorInput;
import org.altherian.hboxc.HyperboxClient;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.vbox.VBox4_2ConfigureView;
import org.altherian.hboxc.front.gui.vbox.VBox4_3ConfigureView;

public class HypervisorConfigureView {
   
   public static HypervisorInput getInput(String srvId) {
      String hypTypeId = Gui.getServer(srvId).getHypervisor().getType();
      // TODO make it generic
      if (hypTypeId.startsWith("vbox-4.3")) {
         return VBox4_3ConfigureView.getInput(srvId);
      }
      else if (hypTypeId.startsWith("vbox-4.2")) {
         return VBox4_2ConfigureView.getInput(srvId);
      }
      else {
         HyperboxClient.getView().postError("No Configuration GUI module for this hypervisor: " + hypTypeId);
         return null;
      }
      
   }
   
}
