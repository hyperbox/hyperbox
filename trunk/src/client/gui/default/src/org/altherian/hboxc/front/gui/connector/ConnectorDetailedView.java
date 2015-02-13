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
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.ViewEventManager;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.host.HostViewer;
import org.altherian.hboxc.front.gui.hypervisor.HypervisorNetViewer;
import org.altherian.hboxc.front.gui.module.ModuleListView;
import org.altherian.hboxc.front.gui.security.user.UserListView;
import org.altherian.hboxc.front.gui.store.StoreListView;
import org.altherian.hboxc.front.gui.tasks.ServerTaskListView;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.tool.logging.Logger;
import java.util.concurrent.ExecutionException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;

public class ConnectorDetailedView implements _Refreshable {
   
   private String conId;
   private JTabbedPane tabs;
   private JLabel loadingLabel;
   private JPanel panel;
   
   private ConnectorSummaryViewer summaryView;
   private StoreListView storeView;
   private UserListView userView;
   private ModuleListView modView;
   
   public ConnectorDetailedView(ConnectorOutput conOut) {
      this.conId = conOut.getId();
      summaryView = new ConnectorSummaryViewer(conOut);
      storeView = new StoreListView();
      userView = new UserListView();
      modView = new ModuleListView();
      
      tabs = new JTabbedPane();
      tabs.addTab("Summary", IconBuilder.getEntityType(EntityType.Server), summaryView.getComponent());
      
      loadingLabel = new JLabel("Loading...");
      loadingLabel.setVisible(false);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(loadingLabel, "growx,pushx,wrap,hidemode 3");
      panel.add(tabs, "grow,push,wrap");
      
      RefreshUtil.set(panel, this);
      refresh();
      ViewEventManager.register(this);
   }
   
   private void update(ConnectorOutput conOut) {
      tabs.setSelectedComponent(summaryView.getComponent());
      if (conOut.isConnected()) {
         tabs.addTab("Host", IconBuilder.getEntityType(EntityType.Server), new HostViewer(conOut.getServerId()).getComponent());
         tabs.addTab("Network", IconBuilder.getEntityType(EntityType.Network), new HypervisorNetViewer(conOut.getServerId()).getComponent());
         tabs.addTab("Tasks", IconBuilder.getEntityType(EntityType.Task), new ServerTaskListView(conOut.getServerId()).getComponent());
         tabs.addTab("Stores", IconBuilder.getEntityType(EntityType.Store), storeView.getComponent());
         tabs.addTab("Users", IconBuilder.getEntityType(EntityType.User), userView.getComponent());
         tabs.addTab("Modules", IconBuilder.getEntityType(EntityType.Module), modView.getComponent());
         storeView.show(conOut.getServer());
         userView.show(conOut.getServer());
         modView.show(conOut.getServer());
      } else {
         if (tabs.getTabCount() > 1) {
            tabs.removeTabAt(tabs.indexOfTab("Host"));
            tabs.removeTabAt(tabs.indexOfTab("Network"));
            tabs.removeTabAt(tabs.indexOfTab("Tasks"));
            tabs.removeTabAt(tabs.indexOfTab("Stores"));
            tabs.removeTabAt(tabs.indexOfTab("Users"));
            tabs.removeTabAt(tabs.indexOfTab("Modules"));
         }
      }
   }
   
   @Override
   public void refresh() {
      Logger.track();
      
      new SwingWorker<ConnectorOutput, Void>() {
         
         @Override
         protected ConnectorOutput doInBackground() throws Exception {
            return Gui.getReader().getConnector(conId);
         }
         
         @Override
         protected void done() {
            try {
               update(get());
            } catch (InterruptedException e) {
               Gui.showError(e);
            } catch (ExecutionException e) {
               Gui.showError(e);
            }
         }
         
      }.execute();
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   @Handler
   private void putConnectorStateEvent(ConnectorStateChangedEvent ev) {
      if (conId.equals(ev.getConnector().getId())) {
         refresh();
      }
   }
   
}
