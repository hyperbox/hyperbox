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
import org.altherian.hbox.comm.in.ModuleIn;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.out.StoreItemOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.module._ModuleSelector;
import org.altherian.hboxc.front.gui.store.utils.StoreItemChooser;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class ModuleRegisterAction extends AbstractAction {
   
   private _ModuleSelector selector;
   
   public ModuleRegisterAction(_ModuleSelector selector) {
      super("Register", IconBuilder.getTask(HyperboxTasks.ModuleRegister));
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      StoreItemOut stoOut = StoreItemChooser.getExisitingFile(selector.getServerId());
      if (stoOut != null) {
         ModuleIn modIn = new ModuleIn();
         modIn.setDescriptorFile(stoOut.getPath());
         Gui.post(new Request(Command.HBOX, HyperboxTasks.ModuleRegister, new ServerIn(selector.getServerId()), modIn));
      }
   }
   
}
