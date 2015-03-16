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

package org.altherian.hboxc.front.gui.security.perm;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.SecurityAccess;
import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;
import org.altherian.hbox.comm.in.PermissionIn;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import org.altherian.hboxc.front.gui.worker.receiver._MachineListReceiver;
import org.altherian.hboxc.front.gui.workers.MachineListWorker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PermissionAddDialog implements _Saveable, _Cancelable, _MachineListReceiver {

   private String serverId;

   private PermissionIn permIn;

   private JDialog dialog;
   private JLabel itemTypeLabel;
   private JLabel itemLabel;
   private JLabel actionLabel;
   private JLabel rightLabel;

   private JComboBox itemTypeValue;
   private JComboBox itemValue;
   private JComboBox actionValue;
   private JComboBox rightValue;

   private JButton saveButton;
   private JButton cancelButton;
   private JPanel buttonPanel;

   private PermissionAddDialog(String serverId) {
      this.serverId = serverId;

      itemTypeLabel = new JLabel("Item Type");
      itemLabel = new JLabel("Item");
      actionLabel = new JLabel("Action");
      rightLabel = new JLabel("Access");

      itemValue = new JComboBox();

      itemTypeValue = new JComboBox();
      itemTypeValue.addActionListener(new ItemTypeListener());
      itemTypeValue.addItem(SecurityItem.Any);
      itemTypeValue.addItem(SecurityItem.Server);
      itemTypeValue.addItem(SecurityItem.Machine);

      actionValue = new JComboBox();
      actionValue.addItem(SecurityAction.Any);

      rightValue = new JComboBox();
      rightValue.addItem(SecurityAccess.Grant);
      rightValue.addItem(SecurityAccess.Deny);

      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);

      dialog = JDialogBuilder.get(saveButton);
      dialog.getContentPane().setLayout(new MigLayout());

      dialog.getContentPane().add(itemTypeLabel);
      dialog.getContentPane().add(itemTypeValue, "growx,pushx,wrap");

      dialog.getContentPane().add(itemLabel, "hidemode 3");
      dialog.getContentPane().add(itemValue, "hidemode 3,growx,pushx,wrap");

      dialog.getContentPane().add(actionLabel);
      dialog.getContentPane().add(actionValue, "growx,pushx,wrap");

      dialog.getContentPane().add(rightLabel);
      dialog.getContentPane().add(rightValue, "growx,pushx,wrap");

      dialog.getContentPane().add(buttonPanel, "growx,pushx,span 2,wrap");
   }

   public PermissionIn show() {
      dialog.pack();
      dialog.setLocationRelativeTo(dialog.getParent());
      dialog.setVisible(true);
      return permIn;
   }

   private void hide() {
      dialog.setVisible(false);
   }

   public static PermissionIn get(String serverId) {
      PermissionAddDialog permDiag = new PermissionAddDialog(serverId);
      return permDiag.show();
   }

   @Override
   public void cancel() {
      hide();
   }

   @Override
   public void save() {
      permIn = new PermissionIn();
      permIn.setItemTypeId(itemTypeValue.getSelectedItem().toString());
      permIn.setActionId(actionValue.getSelectedItem().toString());
      permIn.setAllowed(rightValue.getSelectedItem().equals(SecurityAccess.Grant));

      Object item = itemValue.getSelectedItem();
      if (item instanceof MachineOut) {
         permIn.setItemId(((MachineOut) item).getUuid());
      }

      hide();
   }

   private class ItemTypeListener implements ActionListener {

      @Override
      public void actionPerformed(ActionEvent e) {
         itemValue.setEnabled(!SecurityItem.Any.equals(itemTypeValue.getSelectedItem()));
         itemValue.removeAllItems();

         if (!SecurityItem.Any.equals(itemTypeValue.getSelectedItem())) {
            itemValue.addItem("All");
         }

         if (SecurityItem.Machine.equals(itemTypeValue.getSelectedItem())) {
            MachineListWorker.execute(PermissionAddDialog.this, serverId);
         }
      }

   }

   @Override
   public void loadingStarted() {
      saveButton.setEnabled(false);
      itemValue.removeAllItems();
   }

   @Override
   public void loadingFinished(boolean isSuccessful, String message) {
      saveButton.setEnabled(isSuccessful);
      itemValue.setEnabled(isSuccessful);
   }

   @Override
   public void add(List<MachineOut> mOutList) {
      for (MachineOut mOut : mOutList) {
         itemValue.addItem(mOut);
      }
   }

}
