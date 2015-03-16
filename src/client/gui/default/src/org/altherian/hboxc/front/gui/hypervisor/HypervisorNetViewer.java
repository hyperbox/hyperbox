/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.hboxc.front.gui.hypervisor;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorConnectionStateEventOut;
import org.altherian.hbox.comm.out.network.NetModeOut;
import org.altherian.hboxc.event.connector.ConnectorStateChangedEvent;
import org.altherian.hboxc.front.gui.ViewEventManager;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.hboxc.front.gui.worker.receiver._NetModeListReceiver;
import org.altherian.hboxc.front.gui.workers.NetModeListWorker;
import org.altherian.tool.logging.Logger;
import java.awt.Component;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HypervisorNetViewer implements _Refreshable, _NetModeListReceiver {

   private String srvId;

   private JLabel status;
   private JPanel dataPanel;
   private JPanel panel;

   public HypervisorNetViewer(String srvId) {
      this.srvId = srvId;

      status = new JLabel();
      dataPanel = new JPanel(new MigLayout("ins 0"));
      dataPanel.setVisible(false);
      panel = new JPanel(new MigLayout());
      panel.add(status, "growx, pushx, wrap, hidemode 3");
      panel.add(dataPanel, "grow, push, wrap, hidemode 3");

      RefreshUtil.set(panel, this);
      refresh();
      ViewEventManager.register(this);
   }

   public JComponent getComponent() {
      return panel;
   }

   @Override
   public void refresh() {
      NetModeListWorker.execute(this, srvId);
   }

   @Handler
   private void putHypervisorConnectionEvent(HypervisorConnectionStateEventOut ev) {
      if (ev.getServerId().equals(srvId)) {
         refresh();
      }
   }

   @Handler
   private void putConnectorConnectionStateEvent(ConnectorStateChangedEvent ev) {
      if (ev.getConnector().getServerId().equals(srvId)) {
         refresh();
      }
   }

   private void clear() {
      for (Component c : dataPanel.getComponents()) {
         dataPanel.remove(c);
      }
   }

   private void setDataVisible(boolean isVisible) {
      status.setVisible(!isVisible);
      dataPanel.setVisible(isVisible);
      dataPanel.setEnabled(isVisible);
      if (!isVisible) {
         clear();
      }
   }

   @Override
   public void loadingStarted() {
      setDataVisible(false);
      status.setText("Loading...");
   }

   @Override
   public void loadingFinished(boolean isSuccessful, String message) {
      status.setText(message);
      setDataVisible(isSuccessful);
   }

   @Override
   public void add(List<NetModeOut> modesOut) {
      for (NetModeOut modeOut : modesOut) {
         if (modeOut.canLinkAdaptor()) {
            HypervisorNetModeViewer viewer = new HypervisorNetModeViewer(srvId, modeOut);
            viewer.getComponent().setBorder(BorderFactory.createTitledBorder(modeOut.getLabel()));
            dataPanel.add(viewer.getComponent(), "growx, pushx, wrap");
         } else {
            Logger.debug("Skipped Net mode " + modeOut.getLabel() + ": does not support linking to adaptors");
         }
      }
   }

}
