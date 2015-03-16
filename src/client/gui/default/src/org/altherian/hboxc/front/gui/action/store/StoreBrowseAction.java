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

package org.altherian.hboxc.front.gui.action.store;

import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.store._StoreSelector;
import org.altherian.hboxc.front.gui.store.utils.StoreItemChooser;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public final class StoreBrowseAction extends AbstractAction {

   private _StoreSelector selector;

   public StoreBrowseAction(_StoreSelector selector) {
      this(selector, "Browse");
   }

   public StoreBrowseAction(_StoreSelector selector, String label) {
      super(label, IconBuilder.getTask(HyperboxTasks.StoreGet));
      this.selector = selector;
   }

   @Override
   public void actionPerformed(ActionEvent ev) {
      List<String> selection = selector.getSelection();
      if (!selection.isEmpty()) {
         StoreItemChooser.browse(selector.getServer().getId());
      }
   }

}
