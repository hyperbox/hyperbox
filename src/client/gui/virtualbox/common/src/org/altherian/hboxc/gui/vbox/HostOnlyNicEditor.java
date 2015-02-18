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

package org.altherian.hboxc.gui.vbox;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.in.NetAdaptorIn;
import org.altherian.hbox.comm.io.NetServiceIO;
import org.altherian.hbox.comm.io.NetService_DHCP_IP4_IO;
import org.altherian.hbox.comm.io.NetService_IP4_IO;
import org.altherian.hbox.comm.io.NetService_IP6_IO;
import org.altherian.hbox.comm.out.network.NetAdaptorOut;
import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.hypervisor._NetAdaptorConfigureView;
import org.altherian.helper.swing.JCheckBoxUtils;
import org.altherian.helper.swing.JTextFieldShadow;
import org.altherian.helper.swing.JTextFieldUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class HostOnlyNicEditor implements _NetAdaptorConfigureView {
   
   private String srvId;
   private String modeId;
   private String adaptId;
   
   private JTabbedPane tabs;
   private JPanel ipPanel;
   private JPanel dhcpPanel;
   
   private JLabel ip4AddrLabel;
   private JLabel ip4NetmaskLabel;
   private JLabel ip6addrLabel;
   private JLabel ip6maskLabel;
   private JLabel dhcpEnableLabel;
   private JLabel dhcpAddrLabel;
   private JLabel dhcpMaskLabel;
   private JLabel startAddrLabel;
   private JLabel endAddrLabel;
   
   private JTextField ip4AddrValue;
   private JTextField ip4NetmaskValue;
   private JTextField ip6addrValue;
   private JTextField ip6maskValue;
   private JCheckBox dhcpEnableValue;
   private JTextField dhcpAddrValue;
   private JTextField dhcpMaskValue;
   private JTextField startAddrValue;
   private JTextField endAddrValue;
   
   private boolean ip6Supported = false;
   
   public HostOnlyNicEditor(String srvId, String modeId, String adaptId) {
      this.srvId = srvId;
      this.modeId = modeId;
      this.adaptId = adaptId;
      
      ip4AddrLabel = new JLabel("IPv4 Address:");
      ip4NetmaskLabel = new JLabel("IPv4 Netmask:");
      ip6addrLabel = new JLabel("IPv6 Address:");
      ip6maskLabel = new JLabel("IPv6 Netmask Length");
      dhcpEnableLabel = new JLabel("Enable Server");
      dhcpAddrLabel = new JLabel("Server Address");
      dhcpMaskLabel = new JLabel("Server Mask");
      startAddrLabel = new JLabel("Start Address");
      endAddrLabel = new JLabel("End Address");
      
      ip4AddrValue = new JTextFieldShadow("1.2.3.4");
      ip4AddrValue.setColumns(16);
      ip4NetmaskValue = new JTextFieldShadow("a.b.c.d");
      ip4NetmaskValue.setColumns(16);
      ip6addrValue = new JTextField();
      ip6addrValue.setColumns(24);
      ip6maskValue = new JTextField();
      ip6maskValue.setColumns(6);
      dhcpEnableValue = new JCheckBox();
      dhcpAddrValue = new JTextField();
      dhcpAddrValue.setColumns(16);
      dhcpMaskValue = new JTextField();
      dhcpMaskValue.setColumns(16);
      startAddrValue = new JTextField();
      startAddrValue.setColumns(16);
      endAddrValue = new JTextField();
      endAddrValue.setColumns(16);
      
      JCheckBoxUtils.link(dhcpEnableValue, dhcpAddrValue, dhcpMaskValue, startAddrValue, endAddrValue);
      
      ipPanel = new JPanel(new MigLayout());
      ipPanel.add(ip4AddrLabel);
      ipPanel.add(ip4AddrValue, "growx, pushx, wrap");
      ipPanel.add(ip4NetmaskLabel);
      ipPanel.add(ip4NetmaskValue, "growx, pushx, wrap");
      ipPanel.add(ip6addrLabel);
      ipPanel.add(ip6addrValue, "growx, pushx, wrap");
      ipPanel.add(ip6maskLabel);
      ipPanel.add(ip6maskValue, "growx, pushx, wrap");
      
      dhcpPanel = new JPanel(new MigLayout());
      dhcpPanel.add(dhcpEnableLabel);
      dhcpPanel.add(dhcpEnableValue, "growx, pushx, wrap");
      dhcpPanel.add(dhcpAddrLabel);
      dhcpPanel.add(dhcpAddrValue, "growx, pushx, wrap");
      dhcpPanel.add(dhcpMaskLabel);
      dhcpPanel.add(dhcpMaskValue, "growx, pushx, wrap");
      dhcpPanel.add(startAddrLabel);
      dhcpPanel.add(startAddrValue, "growx, pushx, wrap");
      dhcpPanel.add(endAddrLabel);
      dhcpPanel.add(endAddrValue, "growx, pushx, wrap");
      
      tabs = new JTabbedPane();
      tabs.addTab("Adapter", ipPanel);
      tabs.addTab("DHCP Server", dhcpPanel);
   }
   
   @Override
   public JComponent getComponent() {
      return tabs;
   }
   
   @Override
   public void update(NetAdaptorOut nicOut) {
      new SwingWorker<NetService_IP4_IO, Void>() {
         
         @Override
         protected NetService_IP4_IO doInBackground() throws Exception {
            return (NetService_IP4_IO) Gui.getServer(srvId).getHypervisor().getNetService(modeId, adaptId, NetServiceType.IPv4.getId());
         }
         
         @Override
         protected void done() {
            try {
               NetService_IP4_IO svc = get();
               ip4AddrValue.setText(svc.getAddress());
               ip4NetmaskValue.setText(svc.getMask());
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
               ip6Supported = svc.isEnabled();
               ip6addrValue.setEnabled(ip6Supported);
               ip6maskValue.setEnabled(ip6Supported);
               if (ip6Supported) {
                  ip6addrValue.setText(svc.getAddress());
                  ip6maskValue.setText(Long.toString(svc.getMask()));
               }
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
               if (svc.isEnabled()) {
                  dhcpEnableValue.doClick();
                  dhcpAddrValue.setText(svc.getAddress());
                  dhcpMaskValue.setText(svc.getMask());
                  startAddrValue.setText(svc.getStartAddress());
                  endAddrValue.setText(svc.getEndAddress());
               }
            } catch (InterruptedException e) {
               Gui.showError("Operation was canceled");
            } catch (ExecutionException e) {
               Gui.showError(e.getCause());
            }
         }
         
      }.execute();
      
   }
   
   @Override
   public NetAdaptorIn getInput() {
      NetAdaptorIn naIn = new NetAdaptorIn(modeId, adaptId);
      
      NetService_IP4_IO ip4 = new NetService_IP4_IO(true, ip4AddrValue.getText(), ip4NetmaskValue.getText());
      if (JTextFieldUtils.hasValue(ip6addrValue, ip6maskValue)) {
         NetService_IP6_IO ip6 = new NetService_IP6_IO(true, ip6addrValue.getText(), Long.parseLong(ip6maskValue.getText()));
         naIn.setService(ip6);
      }
      NetService_DHCP_IP4_IO dhcp = new NetService_DHCP_IP4_IO(dhcpEnableValue.isSelected());
      dhcp.setAddress(dhcpAddrValue.getText());
      dhcp.setNetmask(dhcpMaskValue.getText());
      dhcp.setStartAddress(startAddrValue.getText());
      dhcp.setEndAddress(endAddrValue.getText());
      naIn.setServices(new ArrayList<NetServiceIO>(Arrays.asList(ip4, dhcp)));
      
      return naIn;
   }
   
}
