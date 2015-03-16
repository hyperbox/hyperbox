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

package org.altherian.hboxc.front.gui.connector;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hboxc.comm.input.ConnectorInput;
import org.altherian.hboxc.comm.output.BackendOutput;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public final class ConnectorEditorDialog implements _Saveable, _Cancelable {

   private JDialog dialog;

   private JPanel inputPanel;
   private JLabel hostnameLabel;
   private JTextField hostnameField;
   private JLabel labelLabel;
   private JTextField labelField;
   private JLabel userLabel;
   private JTextField userField;
   private JLabel passLabel;
   private JPasswordField passField;
   private JLabel connectorLabel;
   private JComboBox connectorValue;

   private JPanel buttonPanel;
   private JButton loginButton;
   private JButton cancelButton;

   private String conId = "-1";
   private ClientTasks task;

   private ConnectorEditorDialog() {

      hostnameLabel = new JLabel("Hostname");
      hostnameField = new JTextField(15);
      hostnameField.setText("127.0.0.1");

      labelLabel = new JLabel("Label");
      labelField = new JTextField(15);

      userLabel = new JLabel("User");
      userField = new JTextField(15);

      passLabel = new JLabel("Password");
      passField = new JPasswordField(15);

      connectorLabel = new JLabel("Connector");
      connectorValue = new JComboBox();
      for (BackendOutput bckOut : Gui.getReader().listBackends()) {
         connectorValue.addItem(bckOut);
      }

      loginButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));

      inputPanel = new JPanel(new MigLayout());
      inputPanel.add(labelLabel);
      inputPanel.add(labelField, "growx,pushx,wrap");
      inputPanel.add(hostnameLabel);
      inputPanel.add(hostnameField, "growx, pushx, wrap");
      inputPanel.add(userLabel);
      inputPanel.add(userField, "growx, pushx, wrap");
      inputPanel.add(passLabel);
      inputPanel.add(passField, "growx, pushx, wrap");
      inputPanel.add(connectorLabel);
      inputPanel.add(connectorValue, "growx,pushx,wrap");

      buttonPanel = new JPanel(new MigLayout());
      buttonPanel.add(loginButton);
      buttonPanel.add(cancelButton);

      dialog = JDialogBuilder.get(loginButton);
      dialog.add(inputPanel, "growx, pushx, wrap");
      dialog.add(buttonPanel, "center, bottom");
   }

   public void show() {

      dialog.pack();
      dialog.setLocationRelativeTo(dialog.getParent());
      dialog.setVisible(true);
   }

   public void hide() {

      dialog.setVisible(false);
   }

   public void create() {

      task = ClientTasks.ConnectorAdd;
      dialog.setTitle("Add Server Connection");

      show();
   }

   public void modify(ConnectorOutput conOut) {

      conId = conOut.getId();
      task = ClientTasks.ConnectorModify;
      dialog.setTitle("Edit Server Connection");

      labelField.setText(conOut.getLabel());
      hostnameField.setText(conOut.getAddress());
      userField.setText(conOut.getUsername());

      show();
   }

   @Override
   public void save() {
      ConnectorInput conIn = new ConnectorInput(conId);
      conIn.setAddress(hostnameField.getText());
      conIn.setLabel(labelField.getText());
      conIn.setBackendId(((BackendOutput) connectorValue.getSelectedItem()).getId());

      if (task.equals(ClientTasks.ConnectorAdd)) {
         UserIn uIn = new UserIn(userField.getText(), passField.getPassword());
         Gui.post(new Request(task, uIn, conIn));
      } else {
         if (!userField.getText().isEmpty()) {
            char[] pass = passField.getPassword();
            if (pass.length > 0) {
               UserIn uIn = new UserIn(userField.getText(), passField.getPassword());
               Gui.post(new Request(task, uIn, conIn));
            } else {
               Gui.post(new Request(task, conIn));
            }
         } else {
            Gui.post(new Request(task, conIn));
         }
      }

      passField.setText(null);
      hide();
   }

   @Override
   public void cancel() {
      hide();
   }

   public static void add() {
      new ConnectorEditorDialog().create();
   }

   public static void edit(ConnectorOutput srvOut) {
      new ConnectorEditorDialog().modify(srvOut);
   }

}
