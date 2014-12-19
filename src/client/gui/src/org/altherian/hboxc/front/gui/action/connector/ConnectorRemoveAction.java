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

package org.altherian.hboxc.front.gui.action.connector;

import org.altherian.hbox.comm.Request;
import org.altherian.hboxc.comm.input.ConnectorInput;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.connector._ConnectorSelector;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class ConnectorRemoveAction extends AbstractAction {
   
   private _ConnectorSelector select;
   
   public ConnectorRemoveAction(_ConnectorSelector select) {
      this(select, "Remove");
   }
   
   public ConnectorRemoveAction(_ConnectorSelector select, String label) {
      super(label);
      this.select = select;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      for (ConnectorOutput conOut : select.listConnectors()) {
         Gui.post(new Request(ClientTasks.ConnectorRemove, new ConnectorInput(conOut.getId())));
      }
   }
   
}
