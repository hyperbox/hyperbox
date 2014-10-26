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
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.comm.out.hypervisor.OsTypeOut;
import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.hboxc.HyperboxClient;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.workers.KeyboardTypeListWorker;
import org.altherian.hboxc.front.gui.workers.OsTypeListWorker;
import org.altherian.hboxc.front.gui.workers._KeyboardTypeListReceiver;
import org.altherian.hboxc.front.gui.workers._OsTypeListReceiver;

import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GeneralVmEdit {
   
   private JPanel panel;
   private JLabel nameLabel;
   private JTextField nameField;
   private JLabel osTypeLabel;
   private JComboBox osTypeField;
   private JLabel snapshotFolderLabel;
   private JTextField snapshotFolderField;
   private JLabel keyboardTypeLabel;
   private JComboBox keyboardTypeBox;
   private JLabel mouseTypeLabel;
   private JComboBox mouseTypeBox;
   private JLabel descLabel;
   private JTextArea descArea;
   
   private MachineOut mOut;
   private MachineIn mIn;
   
   public GeneralVmEdit() {
      nameLabel = new JLabel("Name");
      nameField = new JTextField();
      
      osTypeLabel = new JLabel("OS Type");
      osTypeField = new JComboBox();
      
      snapshotFolderLabel = new JLabel("Snapshot Folder");
      snapshotFolderField = new JTextField();
      
      keyboardTypeLabel = new JLabel("Keyboard Type");
      keyboardTypeBox = new JComboBox();
      
      mouseTypeLabel = new JLabel("Mouse Type");
      mouseTypeBox = new JComboBox();
      
      descLabel = new JLabel("Description");
      descArea = new JTextArea();
      
      panel = new JPanel(new MigLayout());
      panel.add(nameLabel);
      panel.add(nameField, "growx,pushx,wrap");
      panel.add(osTypeLabel);
      panel.add(osTypeField, "growx,pushx,wrap");
      panel.add(snapshotFolderLabel);
      panel.add(snapshotFolderField, "growx,pushx,wrap");
      panel.add(keyboardTypeLabel);
      panel.add(keyboardTypeBox, "growx,pushx,wrap");
      panel.add(mouseTypeLabel);
      panel.add(mouseTypeBox, "growx,pushx,wrap");
      panel.add(descLabel);
      panel.add(descArea, "growx,pushx,wrap");
   }
   
   public Component getComp() {
      return panel;
   }
   
   public void update(MachineOut mOut, MachineIn mIn) {
      this.mIn = mIn;
      this.mOut = mOut;
      
      nameField.setText(mOut.getName());
      descArea.setText(mOut.getSetting(MachineAttribute.Description).getString());
      
      KeyboardTypeListWorker.get(new KeyboardListReceiver(), mOut.getServerId(), mOut.getUuid());
      
      try {
         mouseTypeBox.removeAllItems();
         for (String mouse : Gui.getServer(mOut.getServerId()).listMouseMode(new MachineIn(mOut))) {
            mouseTypeBox.addItem(mouse);
         }
         mouseTypeBox.setSelectedItem(mOut.getSetting(MachineAttribute.MouseMode).getRawValue());
      } catch (Throwable t) {
         HyperboxClient.getView().postError("Unable to retrieve list of Mouse modes", t);
      }
      OsTypeListWorker.run(new OsTypeLoader(), mOut);
   }
   
   public void save() {
      if (!nameField.getText().contentEquals(mOut.getName())) {
         mIn.setName(nameField.getText());
      }
      if (!osTypeField.getSelectedItem().toString().contentEquals(mOut.getSetting(MachineAttribute.OsType).getString())) {
         mIn.setSetting(new StringSettingIO(MachineAttribute.OsType, osTypeField.getSelectedItem().toString()));
      }
      if (!keyboardTypeBox.getSelectedItem().toString().contentEquals(mOut.getSetting(MachineAttribute.KeyboardMode).getString())) {
         mIn.setSetting(new StringSettingIO(MachineAttribute.KeyboardMode, keyboardTypeBox.getSelectedItem().toString()));
      }
      if (!mouseTypeBox.getSelectedItem().toString().contentEquals(mOut.getSetting(MachineAttribute.MouseMode).getString())) {
         mIn.setSetting(new StringSettingIO(MachineAttribute.MouseMode, mouseTypeBox.getSelectedItem().toString()));
      }
      if (!descArea.getText().contentEquals(mOut.getSetting(MachineAttribute.Description).getString())) {
         mIn.setSetting(new StringSettingIO(MachineAttribute.Description, descArea.getText()));
      }
   }
   
   private class OsTypeLoader implements _OsTypeListReceiver {
      
      @Override
      public void loadingStarted() {
         osTypeField.removeAllItems();
         osTypeField.addItem("Loading...");
         osTypeField.setSelectedItem("Loading...");
         osTypeField.setEnabled(false);
      }
      
      @Override
      public void loadingFinished(boolean isSuccess, String message) {
         osTypeField.setEnabled(true);
         
         if (isSuccess) {
            osTypeField.setSelectedItem(mOut.getSetting(MachineAttribute.OsType).getRawValue());
            osTypeField.removeItem("Loading...");
         } else {
            osTypeField.removeAllItems();
            osTypeField.addItem("Failed to load: " + message);
         }
      }
      
      @Override
      public void add(List<OsTypeOut> ostOuttList) {
         for (OsTypeOut osOut : ostOuttList) {
            osTypeField.addItem(osOut.getId());
         }
      }
      
   }
   
   private class KeyboardListReceiver implements _KeyboardTypeListReceiver {
      
      @Override
      public void loadingStarted() {
         keyboardTypeBox.setEnabled(false);
         keyboardTypeBox.removeAllItems();
         keyboardTypeBox.addItem("Loading...");
         keyboardTypeBox.setSelectedItem("Loading...");
      }
      
      @Override
      public void loadingFinished(boolean isSuccessful, String message) {
         keyboardTypeBox.removeItem("Loading...");
         keyboardTypeBox.setEnabled(isSuccessful);
         if (isSuccessful) {
            keyboardTypeBox.setSelectedItem(mOut.getSetting(MachineAttribute.KeyboardMode).getRawValue());
         } else {
            keyboardTypeBox.removeAllItems();
            keyboardTypeBox.addItem("Failed to load Keyboard Types list: " + message);
         }
      }
      
      @Override
      public void add(List<String> keyboardList) {
         for (String keyboard : keyboardList) {
            keyboardTypeBox.addItem(keyboard);
         }
      }
      
   }
   
}
