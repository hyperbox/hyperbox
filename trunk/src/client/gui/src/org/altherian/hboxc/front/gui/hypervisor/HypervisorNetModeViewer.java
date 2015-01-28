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
import org.altherian.hbox.comm.out.event.net.NetAdaptorEventOut;
import org.altherian.hbox.comm.out.network.NetAdaptorOut;
import org.altherian.hbox.comm.out.network.NetModeOut;
import org.altherian.hboxc.front.gui.FrontEventManager;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.network.NetAdaptorAddAction;
import org.altherian.hboxc.front.gui.action.network.NetAdaptorRemoveAction;
import org.altherian.tool.logging.Logger;
import java.awt.Component;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class HypervisorNetModeViewer implements _Refreshable {

   private String srvId;
   private NetModeOut mode;
   
   private JPanel panel = new JPanel(new MigLayout());
   
   public HypervisorNetModeViewer(String srvId, NetModeOut mode) {
      this.srvId = srvId;
      this.mode = mode;
      FrontEventManager.register(this);
      refresh();
   }
   
   @Override
   public void refresh() {
      for (Component c : panel.getComponents()) {
         panel.remove(c);
      }
      List<NetAdaptorOut> adaptors = Gui.getServer(srvId).getHypervisor().listAdaptors(mode.getId());
      if (adaptors.isEmpty()) {
         panel.add(new JLabel("No adaptors"), "growx, pushx, wrap");
      } else {
         for (NetAdaptorOut adapt : adaptors) {
            panel.add(new JLabel(adapt.getLabel()), "growx, pushx");
            if (mode.canRemoveAdaptor()) {
               panel.add(new JButton(new NetAdaptorRemoveAction(srvId, mode.getId(), adapt.getId())), "wrap");
            } else {
               panel.add(new JLabel(), "wrap");
            }
         }
      }
      
      Logger.debug(mode.getId() + " net mode can add adaptator? " + mode.canAddAdaptor());
      if (mode.canAddAdaptor()) {
         panel.add(new JButton(new NetAdaptorAddAction(srvId, mode.getId())), "wrap");
      }
      
      panel.repaint();
      panel.revalidate();
   }

   public JComponent getComponent() {
      return panel;
   }

   @Handler
   private void putNetAdaptorEvent(NetAdaptorEventOut ev) {
      if (mode.getId().equals(ev.getNetMode())) {
         refresh();
      }
   }
   
}
