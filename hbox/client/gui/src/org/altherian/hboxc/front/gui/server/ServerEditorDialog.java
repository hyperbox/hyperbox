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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.server;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.constant.Entity;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import org.altherian.tool.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ServerEditorDialog implements _Saveable, _Cancelable {
   
   private JLabel nameLabel;
   private JTextField nameValue;
   private JLabel logLevelLabel;
   private JComboBox logLevelValue;
   
   private JButton saveButton;
   private JButton cancelButton;
   
   private JDialog dialog;
   
   private ServerIn srvIn;
   private ServerOut srvOut;
   
   public static ServerIn getInput(String srvId) {
      Logger.track();
      
      return new ServerEditorDialog().getUserInput(srvId);
   }
   
   public ServerEditorDialog() {
      nameValue = new JTextField();
      nameLabel = new JLabel("Name");
      nameLabel.setLabelFor(nameValue);
      
      logLevelValue = new JComboBox();
      logLevelLabel = new JLabel("Log level");
      logLevelLabel.setLabelFor(logLevelValue);
      
      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      JPanel buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      
      dialog = JDialogBuilder.get("Server Configuration", IconBuilder.getEntityType(Entity.Server).getImage(), saveButton);
      dialog.add(nameLabel);
      dialog.add(nameValue, "growx, pushx, wrap");
      dialog.add(logLevelLabel);
      dialog.add(logLevelValue, "growx, pushx, wrap");
      dialog.add(buttonPanel, "center, span 2");
   }
   
   private ServerIn getUserInput(String srvId) {
      Logger.track();
      
      srvOut = Gui.getServerInfo(srvId);
      logLevelValue.addItem("");
      for (String level : Gui.getServer(srvId).listLogLevel()) {
         logLevelValue.addItem(level);
      }
      
      nameValue.setText(srvOut.getName());
      logLevelValue.setSelectedItem(srvOut.getLogLevel());
      
      show();
      return srvIn;
   }
   
   private void show() {
      dialog.pack();
      dialog.setSize(650, dialog.getHeight());
      dialog.setLocationRelativeTo(dialog.getParent());
      dialog.setVisible(true);
   }
   
   private void hide() {
      dialog.setVisible(false);
   }
   
   @Override
   public void cancel() {
      Logger.track();
      
      srvIn = null;
      hide();
   }
   
   @Override
   public void save() throws HyperboxException {
      Logger.track();
      
      srvIn = new ServerIn(srvOut.getId());
      srvIn.setName(nameValue.getText());
      srvIn.setLogLevel(logLevelValue.getSelectedItem().toString());
      hide();
   }
   
}
