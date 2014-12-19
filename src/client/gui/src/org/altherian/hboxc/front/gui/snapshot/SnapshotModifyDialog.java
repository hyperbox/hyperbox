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

package org.altherian.hboxc.front.gui.snapshot;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.SnapshotIn;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.comm.out.hypervisor.SnapshotOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SnapshotModifyDialog implements _Saveable, _Cancelable {
   
   private static SnapshotModifyDialog instance;
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
   private SnapshotOut snapOut;
   
   private void init(MachineOut mOut, SnapshotOut snapOut) {
      this.mOut = mOut;
      this.snapOut = snapOut;
      
      nameLabel = new JLabel("Name");
      nameField = new JTextField(40);
      nameField.setText(snapOut.getName());
      descLabel = new JLabel("Description");
      descArea = new JTextArea();
      descArea.setLineWrap(true);
      descArea.setRows(10);
      descArea.setText(snapOut.getDescription());
      
      mainPanel = new JPanel(new MigLayout());
      mainPanel.add(nameLabel);
      mainPanel.add(nameField, "growx,pushx,wrap");
      mainPanel.add(descLabel);
      mainPanel.add(descArea, "growx,pushx,wrap");
      
      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      
      buttonsPanel = new JPanel(new MigLayout());
      buttonsPanel.add(saveButton);
      buttonsPanel.add(cancelButton);
      
      mainDialog = JDialogBuilder.get("Edit Snapshot", saveButton);
      mainDialog.getContentPane().setLayout(new MigLayout());
      mainDialog.getContentPane().add(mainPanel, "grow,push,wrap");
      mainDialog.getContentPane().add(buttonsPanel, "center, growx");
      mainDialog.getRootPane().setDefaultButton(saveButton);
   }
   
   public static void show(MachineOut mOut, SnapshotOut snapOut) {
      instance = new SnapshotModifyDialog();
      // FIXME use SwingWorker
      instance.init(mOut, Gui.getServer(mOut.getServerId()).getSnapshot(mOut.getUuid(), snapOut.getUuid()));
      
      instance.mainDialog.pack();
      instance.mainDialog.setLocationRelativeTo(instance.mainDialog.getParent());
      instance.mainDialog.setVisible(true);
   }
   
   private void hide() {
      mainDialog.setVisible(false);
      mainDialog.dispose();
      instance = null;
   }
   
   @Override
   public void save() {
      snapIn = new SnapshotIn(snapOut.getUuid());
      if (!nameField.getText().contentEquals(snapOut.getName())) {
         snapIn.setName(nameField.getText());
      }
      if (!descArea.getText().contentEquals(snapOut.getDescription())) {
         snapIn.setDescription(descArea.getText());
      }
      
      if (snapIn.hasNewData()) {
         Gui.post(new Request(Command.VBOX, HypervisorTasks.SnapshotModify, new MachineIn(mOut), snapIn));
      }
      
      hide();
   }
   
   @Override
   public void cancel() {
      hide();
   }
   
}
