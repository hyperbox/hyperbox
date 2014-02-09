package org.altherian.hboxc.front.gui.action.storage;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.MediumInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.input.StorageDeviceAttachmentInput;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hbox.comm.output.storage.StorageDeviceAttachmentOutput;
import org.altherian.hboxc.HyperboxClient;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class HypervisorToolsMediumAttachAction extends AbstractAction {
   
   private String serverId;
   private StorageDeviceAttachmentOutput sdaOut;
   
   public HypervisorToolsMediumAttachAction(String serverId, StorageDeviceAttachmentOutput sdaOut) {
      this(serverId, sdaOut, "Attach Hypervisor Tools", IconBuilder.getTask(HypervisorTasks.MediumMount), true);
   }
   
   public HypervisorToolsMediumAttachAction(String serverId, StorageDeviceAttachmentOutput sdaOut, String label, ImageIcon icon, boolean isEnabled) {
      super(label, icon);
      setEnabled(isEnabled);
      this.serverId = serverId;
      this.sdaOut = sdaOut;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      MediumOutput medOut = Gui.getReader().getServerReader(serverId).getHypervisor().getToolsMedium();
      if (medOut != null) {
         Request req = new Request(Command.VBOX, HypervisorTasks.MediumMount);
         req.set(new ServerInput(serverId));
         req.set(new MachineInput(sdaOut.getMachineUuid()));
         req.set(new StorageDeviceAttachmentInput(sdaOut.getControllerName(), sdaOut.getPortId(), sdaOut.getDeviceId(), sdaOut.getDeviceType()));
         req.set(new MediumInput(medOut.getLocation(), medOut.getDeviceType()));
         Gui.post(req);
      } else {
         HyperboxClient.getView().postError("Cannot attach - No Hypervisor Tools available");
      }
   }
   
}
