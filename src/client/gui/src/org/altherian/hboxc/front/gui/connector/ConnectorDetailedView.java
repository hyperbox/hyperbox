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

package org.altherian.hboxc.front.gui.connector;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.event.connector.ConnectorStateChangedEvent;
import org.altherian.hboxc.front.gui.FrontEventManager;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.host.HostViewer;
import org.altherian.hboxc.front.gui.module.ModuleListView;
import org.altherian.hboxc.front.gui.security.user.UserListView;
import org.altherian.hboxc.front.gui.store.StoreListView;
import org.altherian.hboxc.front.gui.tasks.ServerTaskListView;
import org.altherian.tool.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;

public class ConnectorDetailedView implements _Refreshable {
   
   private ConnectorOutput conOut;
   private JTabbedPane tabs;
   private JLabel loadingLabel;
   private JPanel panel;
   
   private ConnectorSummaryViewer summaryView;
   private HostViewer hostView;
   private ServerTaskListView taskView;
   private StoreListView storeView;
   private UserListView userView;
   private ModuleListView modView;
   
   public ConnectorDetailedView(ConnectorOutput conOut) {
      this.conOut = conOut;
      summaryView = new ConnectorSummaryViewer();
      hostView = new HostViewer();
      taskView = new ServerTaskListView();
      storeView = new StoreListView();
      userView = new UserListView();
      modView = new ModuleListView();
      
      tabs = new JTabbedPane();
      tabs.addTab("Summary", IconBuilder.getEntityType(EntityType.Server), summaryView.getComponent());
      tabs.addTab("Host", IconBuilder.getEntityType(EntityType.Server), hostView.getComponent());
      tabs.addTab("Tasks", IconBuilder.getEntityType(EntityType.Task), taskView.getComponent());
      tabs.addTab("Stores", IconBuilder.getEntityType(EntityType.Store), storeView.getComponent());
      tabs.addTab("Users", IconBuilder.getEntityType(EntityType.User), userView.getComponent());
      tabs.addTab("Modules", IconBuilder.getEntityType(EntityType.Module), modView.getComponent());
      
      loadingLabel = new JLabel("Loading...");
      loadingLabel.setVisible(false);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(loadingLabel, "growx,pushx,wrap,hidemode 3");
      panel.add(tabs, "grow,push,wrap");
      
      FrontEventManager.register(this);

      update();
   }

   private void update(ConnectorOutput conOut) {
      this.conOut = conOut;
      update();
   }
   
   private void update() {
      Logger.track();

      summaryView.show(conOut);
      tabs.setEnabledAt(tabs.indexOfComponent(hostView.getComponent()), conOut.isConnected());
      tabs.setEnabledAt(tabs.indexOfComponent(taskView.getComponent()), conOut.isConnected());
      tabs.setEnabledAt(tabs.indexOfComponent(storeView.getComponent()), conOut.isConnected());
      tabs.setEnabledAt(tabs.indexOfComponent(userView.getComponent()), conOut.isConnected());
      tabs.setEnabledAt(tabs.indexOfComponent(modView.getComponent()), conOut.isConnected());
      if (conOut.isConnected()) {
         hostView.show(conOut.getServerId());
         taskView.show(conOut.getServer());
         storeView.show(conOut.getServer());
         userView.show(conOut.getServer());
         modView.show(conOut.getServer());
      } else {
         tabs.setSelectedComponent(summaryView.getComponent());
      }
   }
   
   @Override
   public void refresh() {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            update(Gui.getReader().getConnector(conOut.getId()));
            return null;
         }
         
      }.execute();
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   @Handler
   private void putConnectorStateEvent(ConnectorStateChangedEvent ev) {
      Logger.track();
      
      refresh();
   }
   
}
