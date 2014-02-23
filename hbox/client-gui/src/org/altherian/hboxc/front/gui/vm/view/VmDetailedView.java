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
   private VmConsoleView displayTab;
   private JTabbedPane tabs;
   private JLabel loadingLabel;
   private JPanel panel;
   
   public VmDetailedView() {
      Logger.track();
      
      summaryTab = new VmSummaryView();
      snapTab = new SnapshotManagementView();
      displayTab = new VmConsoleView();
      
      tabs = new JTabbedPane();
      tabs.addTab("Summary", summaryTab.getComponent());
      tabs.addTab("Snapshots", snapTab.getComponent());
      tabs.addTab("Console", displayTab.getComponent());
      
      loadingLabel = new JLabel("Loading...");
      loadingLabel.setVisible(false);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(loadingLabel, "growx,pushx,wrap");
      panel.add(tabs, "grow,push,wrap");
      
      FrontEventManager.register(this);
   }
   
   private void update() {
      summaryTab.show(mOut);
      tabs.setEnabledAt(tabs.indexOfComponent(summaryTab.getComponent()), true);
      snapTab.show(mOut);
      tabs.setEnabledAt(tabs.indexOfComponent(snapTab.getComponent()), true);
      displayTab.show(mOut);
      tabs.setEnabledAt(tabs.indexOfComponent(displayTab.getComponent()), true);
   }
   
   public void setUserSelection(MachineOutput mOut) {
      this.mOut = mOut;
      refresh();
   }
   
   public JComponent getComponent() {
      return tabs;
   }
   
   @Handler
   public void getMachineUpdate(MachineDataChangedEvent ev) {
      if (ev.getUuid().contentEquals(mOut.getUuid())) {
         put(ev.getMachine());
      }
   }
   
   @Handler
   public void getMachineRemove(MachineRemovedEvent ev) {
      if (ev.getUuid().contentEquals(mOut.getUuid())) {
         clear();
      }
   }
   
   private void clear() {
      summaryTab.clear();
      displayTab.clear();
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
      } else {
         loadingLabel.setVisible(true);
         tabs.setEnabledAt(tabs.indexOfComponent(summaryTab.getComponent()), false);
         tabs.setEnabledAt(tabs.indexOfComponent(snapTab.getComponent()), false);
         tabs.setEnabledAt(tabs.indexOfComponent(displayTab.getComponent()), false);
         clear();
      }
   }
   
   @Override
   public void loadingFinished(boolean isSuccessful, String message) {
      loadingLabel.setVisible(false);
      tabs.setEnabled(true);
   }
   
   @Override
   public void put(MachineOutput mOut) {
      this.mOut = mOut;
      update();
   }
   
   @Override
   public void refresh() {
      clear();
      MachineGetWorker.get(this, mOut);
   }
   
}
