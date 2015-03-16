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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.front.gui.net;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.in.NetworkInterfaceIn;
import org.altherian.hbox.comm.out.network.NetworkAttachModeOut;
import org.altherian.hbox.comm.out.network.NetworkAttachNameOut;
import org.altherian.hbox.comm.out.network.NetworkInterfaceOut;
import org.altherian.hbox.comm.out.network.NetworkInterfaceTypeOut;
import org.altherian.hboxc.front.gui.worker.receiver._NetworkAttachModeReceiver;
import org.altherian.hboxc.front.gui.worker.receiver._NetworkAttachNameReceiver;
import org.altherian.hboxc.front.gui.worker.receiver._NetworkInterfaceTypeReceiver;
import org.altherian.hboxc.front.gui.workers.NetworkAttachModeListWorker;
import org.altherian.hboxc.front.gui.workers.NetworkAttachNameListWorker;
import org.altherian.hboxc.front.gui.workers.NetworkInterfaceTypeListWorker;
import org.altherian.tool.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NetworkInterfaceViewer {

   private String srvId;
   private NetworkInterfaceOut nicOut;
   private NetworkInterfaceIn nicIn;

   private JLabel enableNicLabel = new JLabel();
   private JCheckBox enableNicValue = new JCheckBox();
   private JLabel connectedLabel = new JLabel();
   private JCheckBox connectedValue = new JCheckBox();
   private JLabel attachToLabel = new JLabel();
   private JComboBox attachModeValue = new JComboBox();
   private JLabel attachNameLabel = new JLabel();
   private JComboBox attachNameValue = new JComboBox();
   private JLabel adapterTypeLabel = new JLabel();
   private JComboBox adapaterTypeValue = new JComboBox();
   private JLabel macAddrLabel = new JLabel();
   private JTextField macAddrValue = new JTextField(30);

   private JPanel mainPanel = new JPanel(new MigLayout());

   public static NetworkInterfaceViewer show(String srvId, NetworkInterfaceOut nicOut) {
      NetworkInterfaceViewer viewer = new NetworkInterfaceViewer(srvId, nicOut);
      return viewer;
   }

   public static NetworkInterfaceViewer show(String srvId, NetworkInterfaceOut nicOut, NetworkInterfaceIn nicIn) {
      NetworkInterfaceViewer viewer = new NetworkInterfaceViewer(srvId, nicOut, nicIn);
      return viewer;
   }

   public NetworkInterfaceViewer(String srvId, NetworkInterfaceOut nicOut) {
      this.srvId = srvId;
      this.nicOut = nicOut;

      setLabels();
      init();
      apply(nicOut);

      mainPanel.add(enableNicLabel);
      mainPanel.add(enableNicValue, "growx, pushx, wrap");
      mainPanel.add(connectedLabel);
      mainPanel.add(connectedValue, "growx, pushx, wrap");
      mainPanel.add(attachToLabel);
      mainPanel.add(attachModeValue, "growx, pushx, wrap");
      mainPanel.add(attachNameLabel);
      mainPanel.add(attachNameValue, "growx, pushx, wrap");
      mainPanel.add(adapterTypeLabel);
      mainPanel.add(adapaterTypeValue, "growx, pushx, wrap");
      mainPanel.add(macAddrLabel);
      mainPanel.add(macAddrValue, "growx, pushx, wrap");
   }

   public NetworkInterfaceViewer(String srvId, NetworkInterfaceOut nicOut, NetworkInterfaceIn nicIn) {
      this(srvId, nicOut);
      this.nicIn = nicIn;
   }

   private void init() {
      enableNicValue.setSelected(false);
      connectedValue.setSelected(false);
      attachModeValue.addActionListener(new AttachTypeListener());
      attachModeValue.removeAllItems();
      attachNameValue.setEditable(true);
      attachNameValue.removeAllItems();
      adapaterTypeValue.removeAllItems();
      macAddrValue.setText(null);
   }

   private void setLabels() {
      enableNicLabel.setText("Enabled");
      connectedLabel.setText("Cable Connected");
      attachToLabel.setText("Attach Mode");
      attachNameLabel.setText("Attach To");
      adapterTypeLabel.setText("Adapter Type");
      macAddrLabel.setText("MAC Address");
   }

   private void apply(NetworkInterfaceOut nicOut) {
      NetworkAttachModeListWorker.execute(new NetworkModeReceiver(), srvId);
      NetworkInterfaceTypeListWorker.execute(new NetworkInterfaceTypeReceiver(), srvId);

      enableNicValue.setSelected(nicOut.isEnabled());
      connectedValue.setSelected(nicOut.isCableConnected());

      attachNameValue.setSelectedItem(nicOut.getAttachName());
      adapaterTypeValue.setSelectedItem(nicOut.getAdapterType());
      macAddrValue.setText(nicOut.getMacAddress());
   }

   public JPanel getPanel() {
      return mainPanel;
   }

   public NetworkInterfaceIn save() {
      if (nicIn != null) {
         if (enableNicValue.isEnabled() && (enableNicValue.isSelected() != nicOut.isEnabled())) {
            nicIn.setEnabled(enableNicValue.isSelected());
         }
         if (enableNicValue.isEnabled()) {
            if (connectedValue.isSelected() != nicOut.isCableConnected()) {
               nicIn.setCableConnected(connectedValue.isSelected());
            }
            if (!attachModeValue.getSelectedItem().toString().contentEquals(nicOut.getAttachMode())) {
               nicIn.setAttachMode(attachModeValue.getSelectedItem().toString());
            }

            if (attachNameValue.isEnabled() && (attachNameValue.getSelectedItem() != null)
                  && !attachNameValue.getSelectedItem().toString().contentEquals(nicOut.getAttachName())) {
               nicIn.setAttachName(attachNameValue.getSelectedItem().toString());
            }

            if (!adapaterTypeValue.getSelectedItem().toString().contentEquals(nicOut.getAdapterType())) {
               nicIn.setAdapterType(adapaterTypeValue.getSelectedItem().toString());
            }

            if (!macAddrValue.getText().contentEquals(nicOut.getMacAddress())) {
               nicIn.setMacAddress(macAddrValue.getText());
            }
         }
      }

      if (nicIn.hasNewData()) {
         return nicIn;
      } else {
         return null;
      }
   }

   private class AttachTypeListener implements ActionListener {

      @Override
      public void actionPerformed(ActionEvent e) {
         if (attachModeValue.isEnabled()) {
            String attachTypeId = attachModeValue.getSelectedItem().toString();
            if ((attachTypeId != null) && !attachTypeId.isEmpty()) {
               Logger.debug(attachTypeId + " was selected as attachment type, fetching list of attachment names");
               NetworkAttachNameListWorker.execute(new NetworkAttachNameReceiver(), srvId, attachTypeId);
            }
         }
      }
   }

   private class NetworkInterfaceTypeReceiver implements _NetworkInterfaceTypeReceiver {

      @Override
      public void loadingStarted() {
         adapaterTypeValue.setEnabled(false);
         adapaterTypeValue.removeAllItems();
         adapaterTypeValue.addItem("Loading...");
      }

      @Override
      public void loadingFinished(boolean isSuccessful, String message) {
         if (isSuccessful) {
            adapaterTypeValue.removeItem("Loading...");
            adapaterTypeValue.setSelectedItem(nicOut.getAdapterType());
         } else {
            adapaterTypeValue.removeAllItems();
            adapaterTypeValue.addItem("Error loading network interface types: " + message);
         }
         adapaterTypeValue.setEnabled(true);
      }

      @Override
      public void add(List<NetworkInterfaceTypeOut> objOutList) {
         for (NetworkInterfaceTypeOut adapterType : objOutList) {
            adapaterTypeValue.addItem(adapterType.getId());
         }
      }

   }

   private class NetworkModeReceiver implements _NetworkAttachModeReceiver {

      @Override
      public void loadingStarted() {
         attachModeValue.setEnabled(false);
         attachModeValue.removeAllItems();
         attachModeValue.addItem("Loading...");
      }

      @Override
      public void loadingFinished(boolean isSuccessful, String message) {
         if (isSuccessful) {
            attachModeValue.removeItem("Loading...");
            attachModeValue.setSelectedItem(nicOut.getAttachMode());
         } else {
            attachModeValue.removeAllItems();
            attachModeValue.addItem("Error loading attach modes: " + message);
         }
         attachModeValue.setEnabled(true);
      }

      @Override
      public void add(List<NetworkAttachModeOut> attachModes) {
         for (NetworkAttachModeOut attachMode : attachModes) {
            attachModeValue.addItem(attachMode.getId());
         }
      }

   }

   private class NetworkAttachNameReceiver implements _NetworkAttachNameReceiver {

      @Override
      public void loadingStarted() {
         attachModeValue.setEnabled(false);
         attachNameValue.setEnabled(false);
         attachNameValue.removeAllItems();
         attachNameValue.addItem("Loading...");
      }

      @Override
      public void loadingFinished(boolean isSuccessful, String message) {
         if (isSuccessful) {
            attachNameValue.removeItem("Loading...");
            if (attachModeValue.getSelectedItem().equals(nicOut.getAttachMode())) {
               attachNameValue.setSelectedItem(nicOut.getAttachName());
            } else {
               attachNameValue.setSelectedIndex(-1);
            }
         } else {
            attachNameValue.removeAllItems();
            attachNameValue.addItem("Error loading attach names: " + message);
         }
         attachNameValue.setEnabled(true);
         attachModeValue.setEnabled(true);
      }

      @Override
      public void add(List<NetworkAttachNameOut> nanOut) {
         for (NetworkAttachNameOut attachName : nanOut) {
            Logger.debug("Adding attachment name: " + attachName.getId());
            attachNameValue.addItem(attachName.getId());
         }
      }

   }

}
