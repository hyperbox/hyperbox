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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.action.machine;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.vm._VmSelector;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public final class MachineDeleteAction extends AbstractAction {
   
   private _VmSelector selector;
   
   public MachineDeleteAction(_VmSelector selector) {
      super("Delete", IconBuilder.getTask(HypervisorTasks.MachineDelete));
      setEnabled(true);
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent ev) {
      int info = JOptionPane.showConfirmDialog(
            null,
            "This will delete the selected VM and all its disks.\nThis cannot be canceled or rolled back!\nAre you sure?",
            "Delete confirmation",
            JOptionPane.WARNING_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION);
      if (info == JOptionPane.YES_OPTION) {
         for (MachineOutput mOut : selector.getMachines()) {
            Gui.post(new Request(Command.VBOX, HypervisorTasks.MachineDelete, new MachineInput(mOut)));
         }
      }
   }
   
}
