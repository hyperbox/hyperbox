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

package org.altherian.hboxc.front.gui.snapshot;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.in.SnapshotIn;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.MainView;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SnapshotCreateDialog {
   
   private static SnapshotCreateDialog instance;
   private JDialog mainDialog;
   
   private JPanel mainPanel;
   private JLabel nameLabel;
   private JTextField nameField;
   private JLabel descLabel;
   private JTextArea descArea;
   
   private JPanel buttonsPanel;
   private JButton saveButton;
   private JButton cancelButton;
   
   private MachineOut mOut;
   private SnapshotIn snapIn;
   
   private void init(MachineOut mOut) {
      this.mOut = mOut;
      
      mainDialog = new JDialog(MainView.getMainFrame());
      mainDialog.setModalityType(ModalityType.APPLICATION_MODAL);
      mainDialog.setTitle("Take new Snapshot");
      
      nameLabel = new JLabel("Name");
      nameField = new JTextField(40);
      descLabel = new JLabel("Description");
      descArea = new JTextArea();
      descArea.setLineWrap(true);
      descArea.setRows(10);
      
      mainPanel = new JPanel(new MigLayout());
      mainPanel.add(nameLabel);
      mainPanel.add(nameField, "growx,pushx,wrap");
      mainPanel.add(descLabel);
      mainPanel.add(descArea, "growx,pushx,wrap");
      
      saveButton = new JButton(new SaveAction());
      cancelButton = new JButton(new CancelAction());
      
      buttonsPanel = new JPanel(new MigLayout());
      buttonsPanel.add(saveButton);
      buttonsPanel.add(cancelButton);
      
      mainDialog.getContentPane().setLayout(new MigLayout());
      mainDialog.getContentPane().add(mainPanel, "grow,push,wrap");
      mainDialog.getContentPane().add(buttonsPanel, "center, growx");
      mainDialog.getRootPane().setDefaultButton(saveButton);
   }
   
   public static void show(MachineOut mOut) {
      instance = new SnapshotCreateDialog();
      instance.init(mOut);
      
      instance.mainDialog.pack();
      instance.mainDialog.setLocationRelativeTo(instance.mainDialog.getParent());
      instance.mainDialog.setVisible(true);
   }
   
   private void hide() {
      mainDialog.setVisible(false);
      mainDialog.dispose();
      instance = null;
   }
   
   private void save() {
      snapIn = new SnapshotIn();
      snapIn.setName(nameField.getText());
      snapIn.setDescription(descArea.getText());
      
      Gui.getServer(mOut.getServerId()).getMachineReader(mOut.getUuid()).takeSnapshot(snapIn);
      
      hide();
   }
   
   private void cancel() {
      hide();
   }
   
   @SuppressWarnings("serial")
   private class SaveAction extends AbstractAction {
      
      public SaveAction() {
         super("Save");
         setEnabled(true);
      }
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
         SnapshotCreateDialog.this.save();
      }
      
   }
   
   @SuppressWarnings("serial")
   private class CancelAction extends AbstractAction {
      
      public CancelAction() {
         super("Cancel");
         setEnabled(true);
      }
      
      @Override
      public void actionPerformed(ActionEvent e) {
         SnapshotCreateDialog.this.cancel();
      }
      
   }
   
}
