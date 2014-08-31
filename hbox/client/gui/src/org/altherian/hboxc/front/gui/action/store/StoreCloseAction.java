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
import java.util.List;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public final class StoreCloseAction extends AbstractAction {
   
   private _StoreSelector selector;
   
   public StoreCloseAction(_StoreSelector selector) {
      this(selector, "Close");
   }
   
   public StoreCloseAction(_StoreSelector selector, String label) {
      super(label, IconBuilder.getTask(HyperboxTasks.StoreClose));
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      List<String> selection = selector.getSelection();
      if (!selection.isEmpty()) {
         for (String storeId : selection) {
            Gui.post(new Request(Command.HBOX, HyperboxTasks.StoreClose, new ServerIn(selector.getServer()), new StoreIn(storeId)));
         }
      }
   }
   
}
