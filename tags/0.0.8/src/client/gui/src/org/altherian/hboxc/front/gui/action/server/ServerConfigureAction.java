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

package org.altherian.hboxc.front.gui.action.server;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.server.ServerEditorDialog;
import org.altherian.hboxc.front.gui.server._ServerSelector;
import org.altherian.tool.logging.Logger;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class ServerConfigureAction extends AbstractAction {
   
   private _ServerSelector selector;
   
   public ServerConfigureAction(_ServerSelector selector) {
      super("Configure");
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      Logger.info("Action: Server Configure for Server #" + selector.getServer().getId());
      ServerIn srvIn = ServerEditorDialog.getInput(selector.getServer().getId());
      if (srvIn == null) {
         Logger.info("No server info was returned");
      } else {
         Logger.info("Server info was returned, sending data");
         Gui.post(new Request(Command.HBOX, HyperboxTasks.ServerConfigure, srvIn));
      }
   }
   
}
