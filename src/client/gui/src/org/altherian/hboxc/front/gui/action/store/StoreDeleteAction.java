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
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.in.StoreIn;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.store._StoreSelector;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class StoreDeleteAction extends AbstractAction {
   
   private _StoreSelector selector;
   
   public StoreDeleteAction(_StoreSelector selector) {
      this(selector, "Delete");
   }
   
   public StoreDeleteAction(_StoreSelector selector, String label) {
      super(label, IconBuilder.getTask(HyperboxTasks.StoreDelete));
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      int info = JOptionPane.showConfirmDialog(
            null,
            "This will delete the store and all its content.\nThis cannot be canceled or rolled back!\nAre you sure?",
            "Delete confirmation",
            JOptionPane.WARNING_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION);
      if (info == JOptionPane.YES_OPTION) {
         for (String storeId : selector.getSelection()) {
            Gui.post(new Request(Command.HBOX, HyperboxTasks.StoreDelete, new ServerIn(selector.getServer()), new StoreIn(storeId)));
         }
      }
   }
   
}
