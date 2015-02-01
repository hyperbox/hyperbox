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

package org.altherian.hboxc.front.gui.action.machine;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.vm._MachineSelector;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class MachineResumeAction extends AbstractAction {
   
   private _MachineSelector selector;
   
   public MachineResumeAction(_MachineSelector selector) {
      super("Resume", IconBuilder.getTask(HypervisorTasks.MachineResume));
      setEnabled(true);
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent ev) {
      for (MachineOut mOut : selector.getMachines()) {
         Gui.post(new Request(Command.VBOX, HypervisorTasks.MachineResume, new MachineIn(mOut)));
      }
   }
   
}
