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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.network;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.output.hypervisor.GuestNetworkInterfaceOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceOutput;
import org.altherian.helper.swing.JTextFieldUtils;
import org.altherian.tool.AxStrings;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NetworkInterfaceSummary {
   
   private NetworkInterfaceSummary() {
      
   }
   
   public static JPanel get(NetworkInterfaceOutput nicOut, GuestNetworkInterfaceOutput gNicOut) {
      JPanel panel = new JPanel(new MigLayout("ins 0"));
      
      JTextField nicValue = JTextFieldUtils.createAsLabel(nicOut.getAdapterType() + " using " + nicOut.getAttachMode()
            + (AxStrings.isEmpty(nicOut.getAttachName()) ? "" : " on " + nicOut.getAttachName()));
      JTextField ipv4Value = JTextFieldUtils.createAsLabel(nicOut.getMacAddress());
      if (gNicOut != null) {
         ipv4Value.setText(ipv4Value.getText() + " / " + gNicOut.getIp4Address() + " / " + gNicOut.getIp4Subnet());
      } else {
         ipv4Value.setText(ipv4Value.getText() + " / IP information not available");
      }
      
      panel.add(new JLabel("Adapter " + (nicOut.getNicId() + 1)), "wrap");
      panel.add(new JLabel(""));
      panel.add(nicValue, "wrap");
      panel.add(new JLabel(""));
      panel.add(ipv4Value, "wrap");
      
      return panel;
   }
   
}
