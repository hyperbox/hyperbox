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

package org.altherian.hboxc.front.gui.session;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.front.gui.FrontEventManager;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public final class SessionListView {
   
   private ServerOut srvOut;
   private JPanel panel;
   private SessionListTableModel sessionListModel;
   private JTable sessionList;
   
   public void init() throws HyperboxException {
      Logger.track();
      
      sessionListModel = new SessionListTableModel();
      sessionList = new JTable(sessionListModel);
      sessionList.setFillsViewportHeight(true);
      
      JScrollPane scrollPane = new JScrollPane(sessionList);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(scrollPane, "grow,push");
      
      FrontEventManager.register(this);
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   private void update() {
      sessionListModel.put(Gui.getServer(srvOut).listSessions());
   }
   
   public void update(ServerOut srvOut) {
      this.srvOut = srvOut;
      update();
   }
   
}
