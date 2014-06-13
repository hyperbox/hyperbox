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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.action.store;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.input.StoreInput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.server._SingleServerSelector;
import org.altherian.hboxc.front.gui.store.StoreEditor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class StoreRegisterAction extends AbstractAction {
   
   private _SingleServerSelector select;
   
   public StoreRegisterAction(_SingleServerSelector select) {
      this(select, "Register");
   }
   
   public StoreRegisterAction(_SingleServerSelector select, String label) {
      super(label, IconBuilder.getTask(HyperboxTasks.StoreRegister));
      putValue(SHORT_DESCRIPTION, "Register the target as a new Store\nThe target MUST exist.");
      this.select = select;
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      StoreInput stoIn = StoreEditor.getInputRegister(select.getServer().getId());
      if (stoIn != null) {
         Gui.post(new Request(Command.HBOX, HyperboxTasks.StoreRegister, new ServerInput(select.getId()), stoIn));
      }
   }
   
}
