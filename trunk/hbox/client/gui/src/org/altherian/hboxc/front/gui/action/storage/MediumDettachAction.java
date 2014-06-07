package org.altherian.hboxc.front.gui.action.storage;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.input.StorageDeviceAttachmentInput;
import org.altherian.hbox.comm.output.storage.StorageDeviceAttachmentOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class MediumDettachAction extends AbstractAction {
   
   private String serverId;
   private StorageDeviceAttachmentOutput sdaOut;
   
   public MediumDettachAction(String serverId, StorageDeviceAttachmentOutput sdaOut, boolean isEnabled) {
      this(serverId, sdaOut, "Detach Medium", IconBuilder.getTask(HypervisorTasks.MediumUnmount), isEnabled);
   }
   
   public MediumDettachAction(String serverId, StorageDeviceAttachmentOutput sdaOut, String label, ImageIcon icon, boolean isEnabled) {
      super(label, icon);
      setEnabled(isEnabled);
      this.serverId = serverId;
      this.sdaOut = sdaOut;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      Request req = new Request(Command.VBOX, HypervisorTasks.MediumUnmount);
      req.set(new ServerInput(serverId));
      req.set(new MachineInput(sdaOut.getMachineUuid()));
      req.set(new StorageDeviceAttachmentInput(sdaOut.getControllerName(), sdaOut.getPortId(), sdaOut.getDeviceId()));
      Gui.post(req);
   }
   
}
