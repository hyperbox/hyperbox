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

package org.altherian.hboxc.front.gui.net;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.in.NetServiceIP4In;
import org.altherian.hbox.comm.in.NetServiceIn;
import org.altherian.hbox.comm.out.network.NetServiceIP4Out;
import org.altherian.hbox.comm.out.network.NetServiceOut;
import org.altherian.hbox.constant.NetServiceType;
import org.altherian.helper.swing.JTextFieldShadow;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// TODO disable/enable field depending on the enable value
public class NetServiceIPv4EditPanel implements _NetServiceEditor {
   
   private JLabel enableLabel;
   private JCheckBox enableValue;
   private JLabel ipLabel;
   private JTextField ipValue;
   private JLabel maskLabel;
   private JTextField maskValue;
   private JPanel panel;
   
   public NetServiceIPv4EditPanel(NetServiceOut netSvcOut) {
      ipLabel = new JLabel("IP Address");
      ipValue = new JTextFieldShadow("1.2.3.4");
      ipValue.setColumns(16);
      ipValue.setEnabled(false);
      
      maskLabel = new JLabel("Subnet Mask");
      maskValue = new JTextFieldShadow("a.b.c.d");
      maskValue.setColumns(16);
      maskValue.setEnabled(false);
      
      enableLabel = new JLabel("Enable");
      enableValue = new JCheckBox();
      enableValue.addItemListener(new ItemListener() {
         
         @Override
         public void itemStateChanged(ItemEvent e) {
            ipValue.setEnabled(enableValue.isSelected());
            maskValue.setEnabled(enableValue.isSelected());
         }
         
      });
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(enableLabel);
      panel.add(enableValue, "growx, pushx, wrap");
      panel.add(ipLabel);
      panel.add(ipValue, "growx, pushx, wrap");
      panel.add(maskLabel);
      panel.add(maskValue, "growx, pushx, wrap");
      
      if (netSvcOut != null) {
         NetServiceIP4Out ip4Out = (NetServiceIP4Out) netSvcOut;
         enableValue.setSelected(ip4Out.isEnabled());
         ipValue.setText(ip4Out.getIP());
         maskValue.setText(ip4Out.getMask());
      }
   }
   
   @Override
   public String getServiceId() {
      return NetServiceType.IPv4_Address.getId();
   }
   
   @Override
   public JComponent getComponent() {
      return panel;
   }
   
   @Override
   public NetServiceIn getInput() {
      return new NetServiceIP4In(
            enableValue.isSelected(),
            enableValue.isSelected() ? ipValue.getText() : null,
            enableValue.isSelected() ? maskValue.getText() : null);
   }
   
}
