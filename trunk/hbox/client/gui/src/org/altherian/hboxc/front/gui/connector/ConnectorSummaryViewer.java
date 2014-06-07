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

package org.altherian.hboxc.front.gui.connector;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.event.connector.ConnectorEvent;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.server.ServerViewer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ConnectorSummaryViewer implements _Refreshable {
   
   private ConnectorOutput conOut;
   
   private ServerViewer srvView;
   
   private JLabel labelLabel;
   private JTextField labelValue;
   private JLabel addressLabel;
   private JTextField addressValue;
   private JLabel backendLabel;
   private JTextField backendValue;
   private JLabel stateLabel;
   private JTextField stateValue;
   
   private JPanel conPanel;
   private JPanel panel;
   
   public ConnectorSummaryViewer() {
      labelLabel = new JLabel("Label");
      addressLabel = new JLabel("Address");
      backendLabel = new JLabel("Backend");
      stateLabel = new JLabel("State");
      
      labelValue = new JTextField();
      labelValue.setEditable(false);
      addressValue = new JTextField();
      addressValue.setEditable(false);
      backendValue = new JTextField();
      backendValue.setEditable(false);
      stateValue = new JTextField();
      stateValue.setEditable(false);
      
      srvView = new ServerViewer();
      
      conPanel = new JPanel(new MigLayout());
      conPanel.setBorder(BorderFactory.createTitledBorder("Connector"));
      conPanel.add(labelLabel);
      conPanel.add(labelValue, "growx,pushx,wrap");
      conPanel.add(addressLabel);
      conPanel.add(addressValue, "growx,pushx,wrap");
      conPanel.add(backendLabel);
      conPanel.add(backendValue, "growx,pushx,wrap");
      conPanel.add(stateLabel);
      conPanel.add(stateValue, "growx,pushx,wrap");
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(conPanel,"growx, pushx, wrap");
      panel.add(srvView.getComponent(),"growx, pushx, wrap");
      srvView.getComponent().setVisible(true);
      
      FrontEventManager.register(this);
   }
   
   @Override
   public void refresh() {
      conOut = Gui.getReader().getConnector(conOut.getId());
      update();
   }
   
   public void show(ConnectorOutput conOut) {
      this.conOut = conOut;
      refresh();
   }
   
   protected void update() {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               update();
            }
         });
      } else {
         labelValue.setText(conOut.getLabel());
         addressValue.setText(conOut.getAddress());
         backendValue.setText(conOut.getBackendId());
         stateValue.setText(conOut.getState().toString());
         if (conOut.isConnected()) {
            srvView.getComponent().setVisible(true);
            srvView.show(conOut.getServer());
         } else {
            srvView.getComponent().setVisible(false);
         }
      }
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   @Handler
   protected void putConnectorEvent(ConnectorEvent ev) {
      show(ev.getConnector());
   }
   
}
