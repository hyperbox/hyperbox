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

package org.altherian.hboxc.front.gui.vm.view;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.event.machine.MachineDataChangedEvent;
import org.altherian.hboxc.event.machine.MachineRemovedEvent;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.snapshot.SnapshotManagementView;
import org.altherian.hboxc.front.gui.workers.MachineGetWorker;
import org.altherian.hboxc.front.gui.workers._MachineReceiver;
import org.altherian.tool.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public final class VmDetailedView implements _MachineReceiver, _Refreshable {
   
   private MachineOutput mOut;
   
   private VmSummaryView summaryTab;
   private SnapshotManagementView snapTab;
   private JTabbedPane tabs;
   private JLabel loadingLabel;
   private JPanel panel;
   private JLabel errorLabel;
   
   public VmDetailedView() {
      Logger.track();
      
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
      
      FrontEventManager.register(this);
   }
   
   private void update() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               update();
            }
         });
      }
      
      if (mOut.isAvailable()) {
         summaryTab.show(mOut);
         tabs.setEnabledAt(tabs.indexOfComponent(summaryTab.getComponent()), true);
         snapTab.show(mOut);
         tabs.setEnabledAt(tabs.indexOfComponent(snapTab.getComponent()), true);
      }
   }
   
   public void setUserSelection(MachineOutput mOut) {
      Logger.track();
      
      this.mOut = mOut;
      refresh();
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   @Handler
   public void getMachineUpdate(MachineDataChangedEvent ev) {
      Logger.track();
      
      if (ev.getUuid().contentEquals(mOut.getUuid())) {
         Logger.track();
         put(ev.getMachine());
      }
   }
   
   @Handler
   public void getMachineRemove(MachineRemovedEvent ev) {
      Logger.track();
      
      if (ev.getUuid().contentEquals(mOut.getUuid())) {
         Logger.track();
         clear();
      }
   }
   
   private void clear() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clear();
            }
         });
      }
      
      Logger.track();
      errorLabel.setVisible(false);
      tabs.setVisible(false);
      summaryTab.clear();
   }
   
   @Override
   public void loadingStarted() {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               loadingStarted();
            }
         });
      }
      
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
      Logger.track();
      
      loadingLabel.setVisible(false);
      tabs.setEnabled(isSuccessful);
      tabs.setVisible(isSuccessful && mOut.isAvailable());
      if (!isSuccessful) {
         errorLabel.setText("Unable to retrieve VM information: " + message);
         errorLabel.setVisible(true);
      }
   }
   
   @Override
   public void put(MachineOutput mOut) {
      Logger.track();
      
      this.mOut = mOut;
      update();
   }
   
   @Override
   public void refresh() {
      MachineGetWorker.get(this, mOut);
   }
   
}
