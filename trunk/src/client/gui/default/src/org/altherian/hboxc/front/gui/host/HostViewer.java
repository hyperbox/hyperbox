/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxc.front.gui.host;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorConnectedEventOut;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorDisconnectedEventOut;
import org.altherian.hbox.comm.out.host.HostOut;
import org.altherian.hboxc.front.gui.ViewEventManager;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.hboxc.front.gui.worker.receiver._HostReceiver;
import org.altherian.hboxc.front.gui.workers.HostGetWorker;
import org.altherian.helper.swing.JTextFieldUtils;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class HostViewer implements _Refreshable, _HostReceiver {
   
   private final String srvId;
   
   private JLabel hostnameLabel;
   private JTextField hostnameValue;
   private JLabel memPercLabel;
   private JProgressBar memPercValue;
   private JLabel memUsedLabel;
   private JTextField memUsedValue;
   private JLabel memFreeLabel;
   private JTextField memFreeValue;
   private JLabel memTotalLabel;
   private JTextField memTotalValue;
   
   private JLabel status;
   private JPanel dataPanel;
   private JPanel panel;
   
   public HostViewer(String srvId) {
      this.srvId = srvId;
      
      hostnameLabel = new JLabel("Hostname");
      memPercLabel = new JLabel("Memory Usage");
      memUsedLabel = new JLabel("Memory Used");
      memFreeLabel = new JLabel("Memory Free");
      memTotalLabel = new JLabel("Memory Total");
      
      hostnameValue = JTextFieldUtils.createNonEditable();
      memPercValue = new JProgressBar(JProgressBar.HORIZONTAL, 0, 10000);
      memUsedValue = JTextFieldUtils.createNonEditable();
      memFreeValue = JTextFieldUtils.createNonEditable();
      memTotalValue = JTextFieldUtils.createNonEditable();
      
      dataPanel = new JPanel(new MigLayout("ins 0"));
      dataPanel.add(hostnameLabel);
      dataPanel.add(hostnameValue, "growx,pushx,wrap");
      dataPanel.add(memPercLabel);
      dataPanel.add(memPercValue, "growx,pushx,wrap");
      dataPanel.add(memUsedLabel);
      dataPanel.add(memUsedValue, "growx,pushx,wrap");
      dataPanel.add(memFreeLabel);
      dataPanel.add(memFreeValue, "growx,pushx,wrap");
      dataPanel.add(memTotalLabel);
      dataPanel.add(memTotalValue, "growx,pushx,wrap");
      
      status = new JLabel();
      status.setVisible(false);
      
      panel = new JPanel(new MigLayout());
      panel.add(status, "growx, pushx, wrap, hidemode 3");
      panel.add(dataPanel, "grow, push, wrap, hidemode 3");
      
      RefreshUtil.set(panel, this);
      refresh();
      ViewEventManager.register(this);
   }
   
   @Override
   public void refresh() {
      HostGetWorker.execute(this, srvId);
   }
   
   private void clear() {
      hostnameValue.setText(null);
      memPercValue.setValue(0);
      memPercValue.setString(null);
      memPercValue.setStringPainted(false);
      memUsedValue.setText(null);
      memFreeValue.setText(null);
      memTotalValue.setText(null);
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   @Override
   public void loadingStarted() {
      clear();
      dataPanel.setEnabled(false);
      dataPanel.setVisible(false);
      status.setText("Loading...");
   }
   
   @Override
   public void loadingFinished(boolean isSuccessful, String message) {
      setDataVisible(isSuccessful);
      status.setText(message);
   }
   
   @Override
   public void put(HostOut hostOut) {
      Long memUsed = hostOut.getMemorySize() - hostOut.getMemoryAvailable();
      hostnameValue.setText(hostOut.getHostname());
      memPercValue.setStringPainted(true);
      memPercValue.setValue((int) Math.ceil(((1 - (hostOut.getMemoryAvailable().doubleValue() / hostOut.getMemorySize().doubleValue())) * 10000)));
      
      memPercValue.setString(memUsed + "MB / " + hostOut.getMemorySize() + " MB (" + memPercValue.getString() + ")");
      memUsedValue.setText(memUsed.toString() + " MB");
      memFreeValue.setText(hostOut.getMemoryAvailable().toString() + " MB");
      memTotalValue.setText(hostOut.getMemorySize().toString() + " MB");
   }
   
   @Handler
   public void putHypervisorConnected(HypervisorConnectedEventOut ev) {
      if (srvId.contentEquals(ev.getServerId())) {
         refresh();
      }
   }
   
   @Handler
   public void putHypervisorDisconnected(HypervisorDisconnectedEventOut ev) {
      if (srvId.contentEquals(ev.getServerId())) {
         refresh();
      }
   }
   
   private void setDataVisible(boolean isVisible) {
      status.setVisible(!isVisible);
      status.setEnabled(!isVisible);
      dataPanel.setVisible(isVisible);
      dataPanel.setEnabled(isVisible);
   }
   
}
