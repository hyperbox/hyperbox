package org.altherian.hboxc.front.gui.action.hypervisor;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.HypervisorInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hboxc.controller.MessageInput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.hypervisor.HypervisorConfigureView;
import org.altherian.hboxc.front.gui.server._SingleServerSelector;
import org.altherian.tool.logging.Logger;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class HypervisorConfigureAction extends AbstractAction {
   
   private _SingleServerSelector selector;
   
   public HypervisorConfigureAction(_SingleServerSelector selector) {
      this(selector, "Configure");
   }
   
   public HypervisorConfigureAction(_SingleServerSelector selector, String label) {
      super(label);
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      Logger.track();
      
      ServerOutput srvOut = selector.getServer();
      if (srvOut != null) {
         HypervisorInput hypIn = HypervisorConfigureView.getInput(srvOut.getId());
         if (hypIn != null) {
            Logger.debug("Got user input to configure hypervisor");
            Request req = new Request(Command.HBOX, HyperboxTasks.HypervisorConfigure);
            req.set(new ServerInput(srvOut.getId()));
            req.set(hypIn);
            Gui.post(new MessageInput(req));
         }
      } else {
         Logger.debug("No server was selected");
      }
   }
   
}
