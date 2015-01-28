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

package org.altherian.hboxc.front.gui.vm;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.hypervisor.OsTypeOut;
import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.MainView;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.utils.JDialogUtils;
import java.awt.Dialog.ModalityType;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class VmCreateDialog implements _Saveable, _Cancelable {

   private static VmCreateDialog instance;
   private ServerOut srvOut;
   private JDialog mainDialog;

   private JPanel mainPanel;
   private JLabel nameLabel;
   private JTextField nameField;
   private JLabel osLabel;
   private JComboBox osBox;

   private JPanel buttonsPanel;
   private JButton saveButton;
   private JButton cancelButton;

   private MachineIn mIn;

   public VmCreateDialog(ServerOut srvOut) {
      this.srvOut = srvOut;
   }

   private void init() {
      mainDialog = new JDialog(MainView.getMainFrame());
      mainDialog.setModalityType(ModalityType.APPLICATION_MODAL);
      mainDialog.setTitle("Create Machine");
      JDialogUtils.setCloseOnEscapeKey(mainDialog, true);

      nameLabel = new JLabel("Name");
      nameField = new JTextField(40);
      osLabel = new JLabel("Settings Template");
      osBox = new JComboBox();

      mainPanel = new JPanel(new MigLayout());
      mainPanel.add(nameLabel);
      mainPanel.add(nameField, "growx,pushx,wrap");
      mainPanel.add(osLabel);
      mainPanel.add(osBox, "growx,pushx,wrap");

      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));

      buttonsPanel = new JPanel(new MigLayout());
      buttonsPanel.add(saveButton);
      buttonsPanel.add(cancelButton);

      mainDialog.getContentPane().setLayout(new MigLayout());
      mainDialog.getContentPane().add(mainPanel, "grow,push,wrap");
      mainDialog.getContentPane().add(buttonsPanel, "center, growx");
      mainDialog.getRootPane().setDefaultButton(saveButton);
   }

   public static void show(final ServerOut srvOut) {
      if (instance == null) {
         instance = new VmCreateDialog(srvOut);
         instance.init();
      }

      instance.osBox.removeAllItems();
      instance.osBox.setEnabled(false);
      instance.osBox.addItem("Loading...");
      new SwingWorker<List<OsTypeOut>, Void>() {

         @Override
         protected List<OsTypeOut> doInBackground() throws Exception {
            return Gui.getReader().getServerReader(srvOut.getId()).listOsType();
         }

         @Override
         protected void done() {
            List<OsTypeOut> osTypes;
            try {
               osTypes = get();
               for (OsTypeOut osOut : osTypes) {
                  instance.osBox.addItem(osOut.getId());
               }
               instance.osBox.removeItem("Loading...");
               instance.osBox.setEnabled(true);
            } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch (ExecutionException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }

      }.execute();

      instance.mainDialog.pack();
      instance.mainDialog.setSize(475, instance.mainDialog.getHeight());
      instance.mainDialog.setLocationRelativeTo(instance.mainDialog.getParent());
      instance.mainDialog.setVisible(true);
   }

   private void hide() {
      mainDialog.setVisible(false);
      mainDialog.dispose();
      mIn = null;
      instance = null;
   }

   @Override
   public void save() {
      mIn = new MachineIn();
      mIn.setName(nameField.getText());
      mIn.setSetting(new StringSettingIO(MachineAttribute.OsType, osBox.getSelectedItem().toString()));

      Gui.post(new Request(Command.VBOX, HypervisorTasks.MachineCreate, new ServerIn(srvOut.getId()), mIn));

      hide();
   }

   @Override
   public void cancel() {
      hide();
   }

}
