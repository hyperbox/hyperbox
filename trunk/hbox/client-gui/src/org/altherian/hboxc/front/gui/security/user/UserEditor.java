/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxc.front.gui.security.user;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hbox.comm.output.security.UserOutput;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import org.altherian.hboxc.front.gui.security.perm.UserPermissionEditor;
import org.altherian.tool.logging.Logger;

import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UserEditor implements _Saveable, _Cancelable {
   
   private UserPermissionEditor permEditor;
   
   private JLabel domainLabel;
   private JTextField domainValue;
   private JLabel usernameLabel;
   private JTextField usernameValue;
   private JLabel firstPassLabel;
   private JPasswordField firstPassValue;
   private JLabel secondPassLabel;
   private JPasswordField secondPassValue;
   
   private JPanel buttonPanel;
   private JButton saveButton;
   private JButton cancelButton;
   
   private JDialog dialog;
   
   private UserInput usrIn;
   private UserOutput usrOut;
   
   public UserEditor() {
      permEditor = new UserPermissionEditor();
      
      domainValue = new JTextField();
      domainLabel = new JLabel("Domain");
      domainLabel.setLabelFor(domainValue);
      
      usernameValue = new JTextField();
      usernameLabel = new JLabel("Username");
      usernameLabel.setLabelFor(usernameValue);
      
      firstPassValue = new JPasswordField();
      firstPassLabel = new JLabel("Enter Password");
      firstPassLabel.setLabelFor(firstPassValue);
      
      secondPassValue = new JPasswordField();
      secondPassLabel = new JLabel("Password again");
      secondPassLabel.setLabelFor(secondPassValue);
      
      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      
      dialog = JDialogBuilder.get(saveButton);
      dialog.add(usernameLabel);
      dialog.add(usernameValue, "growx, pushx, wrap");
      dialog.add(firstPassLabel);
      dialog.add(firstPassValue, "growx, pushx, wrap");
      dialog.add(secondPassLabel);
      dialog.add(secondPassValue, "growx, pushx, wrap");
      dialog.add(permEditor.getComponent(), "hidemode 3,span 2, growx, pushx, wrap");
      dialog.add(buttonPanel, "span 2, center, bottom");
   }
   
   public UserInput create() {
      Logger.track();
      
      dialog.setTitle("Create new User");
      permEditor.getComponent().setVisible(false);
      show();
      return usrIn;
   }
   
   public UserInput edit(String serverId, UserOutput usrOut) {
      Logger.track();
      
      dialog.setTitle("Editing user " + usrOut.getDomainLogonName());
      this.usrOut = usrOut;
      
      domainValue.setText(usrOut.getDomain());
      usernameValue.setText(usrOut.getUsername());
      
      permEditor.show(serverId, usrOut);
      
      show();
      return usrIn;
   }
   
   public static UserInput getInput() {
      Logger.track();
      
      return new UserEditor().create();
   }
   
   public static UserInput getInput(String serverId, UserOutput usrOut) {
      Logger.track();
      
      return new UserEditor().edit(serverId, usrOut);
   }
   
   private void show() {
      Logger.track();
      
      dialog.pack();
      dialog.setSize(375, dialog.getHeight());
      dialog.setLocationRelativeTo(dialog.getParent());
      dialog.setVisible(true);
   }
   
   private void hide() {
      Logger.track();
      
      dialog.setVisible(false);
   }
   
   @Override
   public void cancel() {
      Logger.track();
      
      hide();
   }
   
   @Override
   public void save() throws HyperboxException {
      Logger.track();
      
      if (usrOut != null) {
         usrIn = new UserInput(usrOut.getId());
         permEditor.save();
      } else {
         usrIn = new UserInput();
         
         if (!domainValue.getText().isEmpty()) {
            usrIn.setDomain(domainValue.getText());
         }
         
         if ((firstPassValue.getPassword().length == 0) || (secondPassValue.getPassword().length == 0)) {
            throw new HyperboxException("Password cannot be empty");
         }
      }
      
      if (usernameValue.getText().isEmpty()) {
         throw new HyperboxException("Username cannot be empty");
      }
      usrIn.setUsername(usernameValue.getText());
      
      if ((firstPassValue.getPassword().length > 0) || (secondPassValue.getPassword().length > 0)) {
         if (!Arrays.equals(firstPassValue.getPassword(), secondPassValue.getPassword())) {
            throw new HyperboxException("Password do not match");
         }
         usrIn.setPassword(firstPassValue.getPassword());
         
      }
      
      hide();
   }
   
}
