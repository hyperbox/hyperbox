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
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.in.StoreItemIn;
import org.altherian.hbox.comm.io.factory.StoreItemIoFactory;
import org.altherian.hbox.comm.out.StoreItemOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.server._SingleServerSelector;
import org.altherian.hboxc.front.gui.store.utils.StoreItemChooser;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public final class MachineRegisterAction extends AbstractAction {

   private _SingleServerSelector select;

   public MachineRegisterAction(_SingleServerSelector select, String label) {
      super(label, IconBuilder.getTask(HypervisorTasks.MachineRegister));
      this.select = select;
   }

   public MachineRegisterAction(_SingleServerSelector select) {
      this(select, "Register");
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      StoreItemOut vboxFilePathOut = StoreItemChooser.getExisitingFile(select.getServer().getId());
      if (vboxFilePathOut != null) {
         StoreItemIn vboxFilePathIn = StoreItemIoFactory.get(vboxFilePathOut);
         Gui.post(new Request(Command.VBOX, HypervisorTasks.MachineRegister, new ServerIn(select.getServer().getId()), vboxFilePathIn));
      }
   }

}
