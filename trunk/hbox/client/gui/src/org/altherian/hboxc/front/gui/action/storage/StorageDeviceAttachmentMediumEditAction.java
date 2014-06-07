package org.altherian.hboxc.front.gui.action.storage;

import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.output.storage.StorageDeviceAttachmentOutput;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.builder.PopupMenuBuilder;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class StorageDeviceAttachmentMediumEditAction extends AbstractAction {
   
   private String serverId;
   private StorageDeviceAttachmentOutput sdaOut;
   
   public StorageDeviceAttachmentMediumEditAction(String serverId, StorageDeviceAttachmentOutput sdaOut) {
      super("", IconBuilder.getTask(HypervisorTasks.MediumModify));
      this.serverId = serverId;
      this.sdaOut = sdaOut;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      JPopupMenu menuActions = PopupMenuBuilder.get(serverId, sdaOut);
      if (ae.getSource() instanceof JComponent) {
         JComponent component = (JComponent) ae.getSource();
         menuActions.show(component, 0, component.getHeight());
      }
   }
   
}
