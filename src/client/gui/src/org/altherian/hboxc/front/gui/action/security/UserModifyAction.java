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

package org.altherian.hboxc.front.gui.action.security;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.comm.out.security.UserOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.security.user.UserEditor;
import org.altherian.hboxc.front.gui.security.user._UserSelector;
import org.altherian.tool.logging.Logger;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class UserModifyAction extends AbstractAction {
   
   private _UserSelector selector;
   
   public UserModifyAction(_UserSelector selector) {
      this(selector, "Edit");
   }
   
   public UserModifyAction(_UserSelector selector, String label) {
      super(label, IconBuilder.getTask(HyperboxTasks.UserModify));
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      List<String> selection = selector.getSelection();
      if (!selection.isEmpty()) {
         UserOut usrOut = Gui.getReader().getServerReader(selector.getServerId()).getUser(new UserIn(selector.getSelection().get(0)));
         UserIn usrIn = UserEditor.getInput(selector.getServerId(), usrOut);
         if (usrIn != null) {
            Logger.debug("Got user input");
            Gui.post(new Request(Command.HBOX, HyperboxTasks.UserModify, new ServerIn(selector.getServerId()), usrIn));
         }
         
      }
   }
   
}
