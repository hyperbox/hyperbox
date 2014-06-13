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

package org.altherian.hboxc.front.gui.action.server;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hboxc.exception.ServerDisconnectedException;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.server._ServerSelector;
import org.altherian.tool.logging.Logger;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class ServerShutdownAction extends AbstractAction {
   
   private _ServerSelector selector;
   
   public ServerShutdownAction(_ServerSelector selector) {
      super("Shutdown");
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      Logger.verbose("Will send shutdown signal to " + selector.getServers().size() + " servers");
      for (ServerOutput srvOut : selector.getServers()) {
         Logger.verbose("Sending shutdown signal to Server ID " + srvOut.getId());
         try {
            Gui.post(new Request(Command.HBOX, HyperboxTasks.ServerShutdown, new ServerInput(srvOut.getId())));
         } catch (ServerDisconnectedException e) {
            // we ignore this exception as it can happen if the connection is terminated before the "ServerShutdown" packet reaches us.
            // TODO make sure the server sends finishing signals to everything so the disconnect is clean
         }
      }
   }
   
}
