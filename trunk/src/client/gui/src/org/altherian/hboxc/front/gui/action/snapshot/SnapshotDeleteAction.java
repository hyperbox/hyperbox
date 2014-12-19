/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
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

package org.altherian.hboxc.front.gui.action.snapshot;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.SnapshotIn;
import org.altherian.hbox.comm.out.hypervisor.SnapshotOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.MainView;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.snapshot._SnapshotSelector;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class SnapshotDeleteAction extends AbstractAction {
   
   private _SnapshotSelector selector;
   
   public SnapshotDeleteAction(String label, Icon icon, String toolTip, _SnapshotSelector selector) {
      super(label, icon);
      putValue(SHORT_DESCRIPTION, toolTip);
      this.selector = selector;
   }
   
   public SnapshotDeleteAction(_SnapshotSelector selector) {
      this(null, IconBuilder.getTask(HypervisorTasks.SnapshotDelete), "Delete the selected snapshot(s)", selector);
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      if (selector.getSelection().size() > 0) {
         int info = JOptionPane
               .showConfirmDialog(
                     MainView.getMainFrame(),
                     "This will delete the selected snapshot(s), merging the data with the previous state(s).\nThis cannot be canceled or rolled back!\nAre you sure?",
                     "Delete confirmation",
                     JOptionPane.WARNING_MESSAGE,
                     JOptionPane.OK_CANCEL_OPTION);
         if (info == JOptionPane.YES_OPTION) {
            for (SnapshotOut snapOut : selector.getSelection()) {
               Request req = new Request(Command.VBOX, HypervisorTasks.SnapshotDelete);
               req.set(new MachineIn(selector.getMachine()));
               req.set(new SnapshotIn(snapOut.getUuid()));
               Gui.post(req);
            }
         }
      }
   }
   
}
