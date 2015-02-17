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

package org.altherian.hboxc.gui.vbox4_3;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.in.NetAdaptorIn;
import org.altherian.hbox.comm.io.NetService_DHCP_IP4_IO;
import org.altherian.hbox.comm.io.NetService_IP4_CIDR_IO;
import org.altherian.hbox.comm.io.NetService_IP6_Gateway_IO;
import org.altherian.hbox.comm.io.NetService_IP6_IO;
import org.altherian.hbox.comm.out.network.NetAdaptorOut;
import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.hypervisor._NetAdaptorConfigureView;
import org.altherian.helper.swing.JCheckBoxUtils;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;


public class NATNetworkNicEditor implements _NetAdaptorConfigureView {

   private String srvId;
   private String modeId;
   private String adaptId;
   
   private JLabel enableLabel;
   private JCheckBox enableValue;
   private JLabel nameLabel;
   private JTextField nameValue;
   private JLabel cidrLabel;
   private JTextField cidrValue;
   private JLabel netOptionsLabel;
   private JLabel dhcpEnableLabel;
   private JCheckBox dhcpEnableValue;
   private JLabel ip6EnableLabel;
   private JCheckBox ip6EnableValue;
   private JLabel ip6GwLabel;
   private JCheckBox ip6GwValue;
   private JButton natButton;
   private JPanel panel;

   public NATNetworkNicEditor(String srvId, String modeId, String adaptId) {
      this.srvId = srvId;
      this.modeId = modeId;
      this.adaptId = adaptId;

      nameLabel = new JLabel("Network Name:");
      nameValue = new JTextField(16);
      
      if (adaptId != null) {
         enableLabel = new JLabel("Enable Network:");
         enableValue = new JCheckBox();
         cidrLabel = new JLabel("Network CIDR:");
         cidrValue = new JTextField(16);
         netOptionsLabel = new JLabel("Network Options:");
         dhcpEnableLabel = new JLabel("Supports DHCP");
         dhcpEnableValue = new JCheckBox();
         ip6EnableLabel = new JLabel("Supports IPv6");
         ip6EnableValue = new JCheckBox();
         ip6GwLabel = new JLabel("Advertise default IPv6 Route");
         ip6GwValue = new JCheckBox();
         natButton = new JButton("Port Forwarding");
      }

      panel = new JPanel(new MigLayout("ins 0"));
      if (adaptId != null) {
         panel.add(enableLabel);
         panel.add(enableValue, "growx,pushx,wrap");
         panel.add(nameLabel);
         panel.add(nameValue, "growx,pushx,wrap");
         panel.add(cidrLabel);
         panel.add(cidrValue, "growx,pushx,wrap");
         panel.add(dhcpEnableLabel);
         panel.add(dhcpEnableValue, "growx,pushx,wrap");
         panel.add(ip6EnableLabel);
         panel.add(ip6EnableValue, "growx,pushx,wrap");
         panel.add(ip6GwLabel);
         panel.add(ip6GwValue, "growx,pushx,wrap");
         panel.add(natButton, "center,span");
         
         JCheckBoxUtils.link(enableValue, nameValue, cidrValue, dhcpEnableValue, ip6EnableValue, ip6GwValue, natButton);
         JCheckBoxUtils.link(ip6EnableValue, ip6GwValue);
      } else {
         panel.add(nameLabel);
         panel.add(nameValue, "growx,pushx,wrap");
      }
   }

   @Override
   public JComponent getComponent() {
      return panel;
   }

   @Override
   public void update(NetAdaptorOut nicOut) {
      enableValue.setSelected(false);
      if (nicOut.isEnabled()) {
         enableValue.doClick();
         nameValue.setText(nicOut.getLabel());
         
         new SwingWorker<NetService_IP4_CIDR_IO, Void>() {
            
            @Override
            protected NetService_IP4_CIDR_IO doInBackground() throws Exception {
               return (NetService_IP4_CIDR_IO) Gui.getServer(srvId).getHypervisor().getNetService(modeId, adaptId, NetServiceType.IPv4_NetCIDR.getId());
            }

            @Override
            protected void done() {
               try {
                  NetService_IP4_CIDR_IO svc = get();
                  cidrValue.setText(svc.getCIDR());
               } catch (InterruptedException e) {
                  Gui.showError("Operation was canceled");
               } catch (ExecutionException e) {
                  Gui.showError(e.getCause());
               }
            }

         }.execute();

         new SwingWorker<NetService_DHCP_IP4_IO, Void>() {

            @Override
            protected NetService_DHCP_IP4_IO doInBackground() throws Exception {
               return (NetService_DHCP_IP4_IO) Gui.getServer(srvId).getHypervisor().getNetService(modeId, adaptId, NetServiceType.DHCP_IPv4.getId());
            }
            
            @Override
            protected void done() {
               try {
                  NetService_DHCP_IP4_IO svc = get();
                  dhcpEnableValue.setSelected(svc.isEnabled());
               } catch (InterruptedException e) {
                  Gui.showError("Operation was canceled");
               } catch (ExecutionException e) {
                  Gui.showError(e.getCause());
               }
            }

         }.execute();
         
         new SwingWorker<NetService_IP6_IO, Void>() {
            
            @Override
            protected NetService_IP6_IO doInBackground() throws Exception {
               return (NetService_IP6_IO) Gui.getServer(srvId).getHypervisor().getNetService(modeId, adaptId, NetServiceType.IPv6.getId());
            }

            @Override
            protected void done() {
               try {
                  NetService_IP6_IO svc = get();
                  ip6EnableValue.setSelected(svc.isEnabled());
               } catch (InterruptedException e) {
                  Gui.showError("Operation was canceled");
               } catch (ExecutionException e) {
                  Gui.showError(e.getCause());
               }
            }

         }.execute();
         
         new SwingWorker<NetService_IP6_Gateway_IO, Void>() {
            
            @Override
            protected NetService_IP6_Gateway_IO doInBackground() throws Exception {
               return (NetService_IP6_Gateway_IO) Gui.getServer(srvId).getHypervisor().getNetService(modeId, adaptId, NetServiceType.IPv6_Gateway.getId());
            }

            @Override
            protected void done() {
               try {
                  NetService_IP6_Gateway_IO svc = get();
                  ip6GwValue.setSelected(svc.isEnabled());
               } catch (InterruptedException e) {
                  Gui.showError("Operation was canceled");
               } catch (ExecutionException e) {
                  Gui.showError(e.getCause());
               }
            }
            
         }.execute();
         
      }
   }

   @Override
   public NetAdaptorIn getInput() {
      NetAdaptorIn naIn = new NetAdaptorIn(modeId, adaptId);
      naIn.setLabel(nameValue.getText());
      if (adaptId != null) {
         naIn.setService(new NetService_IP4_CIDR_IO(cidrValue.getText()));
         naIn.setService(new NetService_DHCP_IP4_IO(dhcpEnableValue.isSelected()));
         naIn.setService(new NetService_IP6_IO(ip6EnableValue.isSelected()));
         naIn.setService(new NetService_IP6_Gateway_IO(ip6GwValue.isSelected()));
      }
      return naIn;
   }

}
