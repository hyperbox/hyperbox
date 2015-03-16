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

package org.altherian.hboxc.front.gui.vm.edit;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.in.ConsoleIn;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.PositiveNumberSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.hboxc.front.gui.Gui;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConsoleVmEdit {

   private MachineIn mIn;
   private MachineOut mOut;

   private JPanel panel;

   private JLabel enableLabel;
   private JLabel portLabel;
   private JLabel addressLabel;
   private JLabel authTypeLabel;
   private JLabel authTimeoutLabel;
   private JLabel allowMultiConnLabel;
   private JLabel vncPassLabel;

   private JCheckBox enableValue;
   private JTextField portValue;
   private JTextField addressValue;
   private JTextField authTypeValue;
   private JTextField authTimeoutValue;
   private JCheckBox allowMultiConnValue;
   private JTextField vncPassField;

   public ConsoleVmEdit() {
      enableLabel = new JLabel("Enabled");
      portLabel = new JLabel("Port");
      addressLabel = new JLabel("Address");
      authTypeLabel = new JLabel("Authentication Type");
      authTimeoutLabel = new JLabel("Authentication Timeout");
      allowMultiConnLabel = new JLabel("Allow Multi Connections");
      vncPassLabel = new JLabel("VNC Password");

      enableValue = new JCheckBox();
      portValue = new JTextField();
      addressValue = new JTextField();
      authTypeValue = new JTextField();
      authTimeoutValue = new JTextField();
      allowMultiConnValue = new JCheckBox();
      vncPassField = new JTextField();

      panel = new JPanel(new MigLayout());
      panel.add(enableLabel);
      panel.add(enableValue, "growx, pushx, wrap");
      panel.add(portLabel);
      panel.add(portValue, "growx, pushx, wrap");
      panel.add(addressLabel);
      panel.add(addressValue, "growx, pushx, wrap");
      panel.add(authTypeLabel);
      panel.add(authTypeValue, "growx, pushx, wrap");
      panel.add(authTimeoutLabel);
      panel.add(authTimeoutValue, "growx, pushx, wrap");
      panel.add(allowMultiConnLabel);
      panel.add(allowMultiConnValue, "growx, pushx, wrap");
   }

   public Component getComp() {
      return panel;
   }

   public void update(MachineOut mOut, MachineIn mIn) {
      this.mIn = mIn;
      this.mOut = mOut;

      enableValue.setSelected(mOut.getSetting(MachineAttribute.VrdeEnabled).getBoolean());
      portValue.setText(mOut.getSetting(MachineAttribute.VrdePort).getString());
      addressValue.setText(mOut.getSetting(MachineAttribute.VrdeAddress).getString());
      authTypeValue.setText(mOut.getSetting(MachineAttribute.VrdeAuthType).getString());
      authTimeoutValue.setText(mOut.getSetting(MachineAttribute.VrdeAuthTimeout).getString());
      allowMultiConnValue.setSelected(mOut.getSetting(MachineAttribute.VrdeMultiConnection).getBoolean());

      if ("VNC".equals(mOut.getSetting(MachineAttribute.VrdeModule).getString())) {
         panel.add(vncPassLabel);
         panel.add(vncPassField, "growx, pushx, wrap");
         if (Gui.getServer(mOut.getServerId()).getMachineReader(mOut.getUuid()).getConsole().hasSetting("VNCPassword")) {
            vncPassField.setText(Gui.getServer(mOut.getServerId()).getMachineReader(mOut.getUuid()).getConsole().getSetting("VNCPassword")
                  .getString());
         }
      }
   }

   public void save() {
      if (!mOut.getSetting(MachineAttribute.VrdeEnabled).getBoolean().equals(enableValue.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttribute.VrdeEnabled, enableValue.isSelected()));
      }
      if (!mOut.getSetting(MachineAttribute.VrdePort).getString().contentEquals(portValue.getText())) {
         mIn.setSetting(new PositiveNumberSettingIO(MachineAttribute.VrdePort, Long.parseLong(portValue.getText())));
      }
      if (!mOut.getSetting(MachineAttribute.VrdeAddress).getString().contentEquals(addressValue.getText())) {
         mIn.setSetting(new StringSettingIO(MachineAttribute.VrdeAddress, addressValue.getText()));
      }
      if (!mOut.getSetting(MachineAttribute.VrdeAuthType).getString().contentEquals(authTypeValue.getText())) {
         mIn.setSetting(new StringSettingIO(MachineAttribute.VrdeAuthType, authTypeValue.getText()));
      }
      if (!mOut.getSetting(MachineAttribute.VrdeAuthTimeout).getString().contentEquals(authTimeoutValue.getText())) {
         mIn.setSetting(new StringSettingIO(MachineAttribute.VrdeAuthTimeout, authTimeoutValue.getText()));
      }
      if (!mOut.getSetting(MachineAttribute.VrdeMultiConnection).getBoolean().equals(allowMultiConnValue.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttribute.VrdeMultiConnection, allowMultiConnValue.isSelected()));
      }
      if ("VNC".equals(mOut.getSetting(MachineAttribute.VrdeModule).getString()) && !vncPassField.getText().isEmpty()) {
         ConsoleIn conIn = new ConsoleIn();
         conIn.setSetting(new StringSettingIO("VNCPassword", vncPassField.getText()));
         mIn.addDevice(conIn);
      }
   }

}
