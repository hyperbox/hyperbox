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

package org.altherian.hboxc.front.gui.action.task;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.input.TaskInput;
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.tasks._TaskSelector;
import org.altherian.tool.logging.Logger;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public final class TaskCancelAction extends AbstractAction {
   
   private _TaskSelector selector;
   
   public TaskCancelAction(_TaskSelector selector) {
      super("Cancel");
      setEnabled(true);
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent arg0) {
      for (TaskOutput tOut : selector.getSelection()) {
         TaskInput tIn = new TaskInput(tOut.getId());
         Logger.debug("Canceling Task #" + tIn.getId());
         Gui.post(new Request(Command.HBOX, HyperboxTasks.TaskCancel, new ServerInput(tOut.getServerId()), tIn));
      }
   }
   
}
