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

package org.altherian.hboxc.front.gui.vm.edit;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.NetworkInterfaceIn;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.comm.out.network.NetworkInterfaceOut;
import org.altherian.hboxc.front.gui.network.NetworkInterfaceViewer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public final class NetworkVmEdit {
   
   private String srvId;
   private MachineIn mIn;
   private JTabbedPane nicTabs;
   private List<NetworkInterfaceViewer> viewers;
   private JPanel panel;
   
   public NetworkVmEdit() {
      viewers = new ArrayList<NetworkInterfaceViewer>();
      nicTabs = new JTabbedPane();
      panel = new JPanel(new MigLayout());
      panel.add(nicTabs, "grow,push");
   }
   
   public Component getComp() {
      return panel;
   }
   
   public void update(MachineOut mOut, MachineIn mIn) {
      this.srvId = mOut.getServerId();
      this.mIn = mIn;
      
      nicTabs.removeAll();
      viewers.clear();
      for (NetworkInterfaceOut nicOut : mOut.listNetworkInterface()) {
         NetworkInterfaceViewer viewer = NetworkInterfaceViewer.show(srvId, nicOut, new NetworkInterfaceIn(mOut.getUuid(), nicOut.getNicId()));
         viewers.add(viewer);
         nicTabs.addTab("NIC " + (nicOut.getNicId() + 1), viewer.getPanel());
      }
   }
   
   public void save() {
      if (mIn != null) {
         for (NetworkInterfaceViewer viewer : viewers) {
            NetworkInterfaceIn nicIn = viewer.save();
            if (nicIn != null) {
               mIn.addNetworkInterface(nicIn);
            }
         }
      }
   }
   
}
