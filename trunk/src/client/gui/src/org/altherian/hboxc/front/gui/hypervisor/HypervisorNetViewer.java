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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.altherian.hboxc.front.gui.hypervisor;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.out.network.NetModeOut;
import org.altherian.hboxc.event.connector.ConnectorStateChangedEvent;
import org.altherian.hboxc.event.server.ServerConnectionStateEvent;
import org.altherian.hboxc.front.gui.FrontEventManager;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.tool.logging.Logger;
import java.awt.Component;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;


public class HypervisorNetViewer implements _Refreshable {
   
   private String conId;
   private String srvId;

   private JPanel panel;
   
   public HypervisorNetViewer(String conId, String srvId) {
      this.conId = conId;
      this.srvId = srvId;
      panel = new JPanel(new MigLayout("ins 0"));
      RefreshUtil.set(panel, this);
      refresh();
      FrontEventManager.register(this);
   }

   public JComponent getComponent() {
      return panel;
   }

   @Override
   public void refresh() {
      for (Component c : panel.getComponents()) {
         panel.remove(c);
      }
      if (Gui.getReader().getConnector(conId).isConnected() && Gui.getServer(srvId).isHypervisorConnected()) {
         List<NetModeOut> modesOut = Gui.getServer(srvId).getHypervisor().listNetworkModes();
         for (NetModeOut modeOut : modesOut) {
            if (modeOut.canLinkAdaptor()) {
               HypervisorNetModeViewer viewer = new HypervisorNetModeViewer(srvId, modeOut);
               viewer.getComponent().setBorder(BorderFactory.createTitledBorder(modeOut.getLabel()));
               panel.add(viewer.getComponent(), "growx, pushx, wrap");
            } else {
               Logger.debug("Skipped Net mode " + modeOut.getLabel() + ": does not support linking to adaptors");
            }
         }
      }
      panel.repaint();
      panel.revalidate();
   }
   
   @Handler
   private void putConnectorStateEvent(ConnectorStateChangedEvent ev) {
      if (ev.getConnector().getId().equals(conId)) {
         refresh();
      }
   }

   @Handler
   private void putServerConnectionStateEvent(ServerConnectionStateEvent ev) {
      if (ev.getServer().getId().equals(srvId)) {
         refresh();
      }
   }
   
}
