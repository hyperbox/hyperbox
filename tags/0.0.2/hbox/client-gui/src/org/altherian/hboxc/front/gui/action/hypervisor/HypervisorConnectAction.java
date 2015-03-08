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

package org.altherian.hboxc.front.gui.action.hypervisor;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.HypervisorInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hboxc.controller.MessageInput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.hypervisor.HypervisorConnectView;
import org.altherian.hboxc.front.gui.server._SingleServerSelector;
import org.altherian.tool.logging.Logger;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class HypervisorConnectAction extends AbstractAction {
   
   private _SingleServerSelector selector;
   
   public HypervisorConnectAction(_SingleServerSelector selector) {
      this(selector, "Connect");
   }
   
   public HypervisorConnectAction(_SingleServerSelector selector, String label) {
      super(label);
      this.selector = selector;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      Logger.track();
      
      ServerOutput srvOut = selector.getServer();
      if (srvOut != null) {
         HypervisorInput hypIn = HypervisorConnectView.getInput(srvOut);
         if (hypIn != null) {
            Logger.debug("Got user input to connect hypervisor");
            Request req = new Request(Command.HBOX,HyperboxTasks.HypervisorConnect);
            req.set(new ServerInput(srvOut.getId()));
            req.set(hypIn);
            Gui.post(new MessageInput(req));
         }
      } else {
         Logger.debug("No server was selected");
      }
   }
   
}