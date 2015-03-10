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

package org.altherian.hboxc.front.gui.vm.view;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hboxc.event.machine.MachineDataChangedEvent;
import org.altherian.hboxc.event.machine.MachineRemovedEvent;
import org.altherian.hboxc.front.gui.ViewEventManager;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.snapshot.SnapshotManagementView;
import org.altherian.hboxc.front.gui.worker.receiver._MachineReceiver;
import org.altherian.hboxc.front.gui.workers.MachineGetWorker;
import org.altherian.tool.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public final class VmDetailedView implements _MachineReceiver, _Refreshable {
   
   private MachineOut mOut;
   
   private VmSummaryView summaryTab;
   private SnapshotManagementView snapTab;
   private JTabbedPane tabs;
   private JLabel loadingLabel;
   private JPanel panel;
   private JLabel errorLabel;
   
   public VmDetailedView(MachineOut mOut) {
      
      
      this.mOut = mOut;
      
      summaryTab = new VmSummaryView();
      snapTab = new SnapshotManagementView();
      
      tabs = new JTabbedPane();
      tabs.addTab("Summary", summaryTab.getComponent());
      tabs.addTab("Snapshots", snapTab.getComponent());
      
      loadingLabel = new JLabel("Loading...");
      errorLabel = new JLabel();
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(loadingLabel, "growx, pushx, wrap, hidemode 3");
      panel.add(errorLabel, "growx, pushx, wrap, hidemode 3");
      panel.add(tabs, "grow, push, wrap, hidemode 3");
      
      ViewEventManager.register(this);
      
      refresh();
   }
   
   private void update() {
      
      
      if (mOut.isAvailable()) {
         summaryTab.show(mOut, true);
         tabs.setEnabledAt(tabs.indexOfComponent(summaryTab.getComponent()), true);
         snapTab.show(mOut);
         tabs.setEnabledAt(tabs.indexOfComponent(snapTab.getComponent()), true);
      }
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   @Handler
   public void getMachineUpdate(MachineDataChangedEvent ev) {
      
      
      if ((mOut != null) && ev.getUuid().contentEquals(mOut.getUuid())) {
         
         put(ev.getMachine());
      }
   }
   
   @Handler
   public void getMachineRemove(MachineRemovedEvent ev) {
      
      
      if ((mOut != null) && ev.getUuid().contentEquals(mOut.getUuid())) {
         
         clear();
      }
   }
   
   private void clear() {
      
      
      errorLabel.setVisible(false);
      tabs.setVisible(false);
      summaryTab.clear();
   }
   
   @Override
   public void loadingStarted() {
      clear();
      loadingLabel.setVisible(true);
      if (tabs.indexOfComponent(summaryTab.getComponent()) > -1) {
         tabs.setEnabledAt(tabs.indexOfComponent(summaryTab.getComponent()), false);
      }
      if (tabs.indexOfComponent(snapTab.getComponent()) > -1) {
         tabs.setEnabledAt(tabs.indexOfComponent(snapTab.getComponent()), false);
      }
   }
   
   @Override
   public void loadingFinished(final boolean isSuccessful, final String message) {
      
      
      loadingLabel.setVisible(false);
      tabs.setEnabled(isSuccessful);
      if (!isSuccessful) {
         errorLabel.setText("Unable to retrieve VM information: " + message);
         errorLabel.setVisible(true);
      } else {
         tabs.setVisible(mOut.isAvailable());
      }
   }
   
   @Override
   public void put(MachineOut mOut) {
      
      
      this.mOut = mOut;
      update();
   }
   
   @Override
   public void refresh() {
      MachineGetWorker.execute(this, mOut);
   }
   
}
