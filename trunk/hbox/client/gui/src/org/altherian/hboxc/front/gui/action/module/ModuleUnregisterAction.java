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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.action.module;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.ModuleInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.output.ModuleOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.module._ModuleSelector;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class ModuleUnregisterAction extends AbstractAction {
   
   private _ModuleSelector selector;
   
   public ModuleUnregisterAction(_ModuleSelector selector) {
      super("Unregister", IconBuilder.getTask(HyperboxTasks.ModuleUnregister));
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      for (ModuleOutput mod : selector.getModuleSelection()) {
         Gui.post(new Request(Command.HBOX, HyperboxTasks.ModuleUnregister, new ServerInput(selector.getServerId()), new ModuleInput(mod.getId())));
      }
   }
   
}
