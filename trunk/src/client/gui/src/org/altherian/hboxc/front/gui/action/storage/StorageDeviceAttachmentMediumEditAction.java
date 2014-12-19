/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
 * 
 * http://hyperbox.altherian.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.front.gui.action.storage;

import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.out.storage.StorageDeviceAttachmentOut;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.builder.PopupMenuBuilder;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class StorageDeviceAttachmentMediumEditAction extends AbstractAction {
   
   private String serverId;
   private StorageDeviceAttachmentOut sdaOut;
   
   public StorageDeviceAttachmentMediumEditAction(String serverId, StorageDeviceAttachmentOut sdaOut) {
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
