/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxc.front.gui.action;

import org.altherian.hbox.comm.Request;
import org.altherian.hboxc.comm.input.ConsoleViewerInput;
import org.altherian.hboxc.comm.output.ConsoleViewerOutput;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.controller.MessageInput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.vm.console.viewer._ConsoleViewerSelector;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class ConsoleViewerRemoveAction extends AbstractAction {

   private _ConsoleViewerSelector selector;

   public ConsoleViewerRemoveAction(_ConsoleViewerSelector selector) {
      super("-");
      this.selector = selector;
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      for (ConsoleViewerOutput cvOut : selector.getConsoleViewers()) {
         Gui.post(new MessageInput(new Request(ClientTasks.ConsoleViewerRemove, new ConsoleViewerInput(cvOut.getId()))));
      }
   }

}
