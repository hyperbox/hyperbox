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

package org.altherian.hboxc.front.gui.store;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.input.StoreInput;
import org.altherian.hbox.comm.output.StoreOutput;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import org.altherian.hboxc.front.gui.utils.CancelableUtils;
import org.altherian.tool.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StoreEditor implements _Saveable, _Cancelable {
   
   private JLabel storeLabel;
   private JTextField storeLabelValue;
   private JLabel storeLocLabel;
   private JTextField storeLocValue;
   private JLabel storeTypeLabel;
   private JComboBox storeTypeBox;
   
   private JButton saveButton;
   private JButton cancelButton;
   
   private JDialog dialog;
   
   private StoreInput stoIn;
   private StoreOutput stoOut;
   
   public StoreEditor() {
      Logger.track();
      
      storeLabel = new JLabel("Label");
      storeLabelValue = new JTextField();
      storeLocLabel = new JLabel("Location");
      storeLocValue = new JTextField();
      storeTypeLabel = new JLabel("Type");
      storeTypeBox = new JComboBox();
      // TODO retrieve full list of supported store types.
      storeTypeBox.addItem("Local Folder");
      
      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      JPanel buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      
      dialog = JDialogBuilder.get(saveButton);
      dialog.add(storeLabel);
      dialog.add(storeLabelValue, "growx, pushx, wrap");
      dialog.add(storeLocLabel);
      dialog.add(storeLocValue, "growx, pushx, wrap");
      dialog.add(storeTypeLabel);
      dialog.add(storeTypeBox, "growx, pushx, wrap");
      dialog.add(buttonPanel, "center, span 2");
      CancelableUtils.set(this, dialog.getRootPane());
   }
   
   public StoreInput create() {
      Logger.track();
      
      dialog.setTitle("Create new Store");
      show();
      return stoIn;
   }
   
   public StoreInput register() {
      Logger.track();
      
      dialog.setTitle("Registering new Store");
      show();
      return stoIn;
   }
   
   public StoreInput edit(StoreOutput stoOut) {
      Logger.track();
      
      this.stoOut = stoOut;
      dialog.setTitle("Edit store " + stoOut.getLabel());
      
      storeLabelValue.setText(stoOut.getLabel());
      storeLocValue.setText(stoOut.getLocation());
      
      show();
      return stoIn;
   }
   
   public static StoreInput getInputCreate() {
      return new StoreEditor().create();
   }
   
   public static StoreInput getInputRegister() {
      return new StoreEditor().register();
   }
   
   public static StoreInput getInputEdit(StoreOutput stoOut) {
      return new StoreEditor().edit(stoOut);
      
   }
   
   private void show() {
      Logger.track();
      
      dialog.pack();
      dialog.setSize(323, dialog.getHeight());
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
   public void save() {
      Logger.track();
      
      if (stoOut != null) {
         stoIn = new StoreInput(stoOut.getId());
      } else {
         stoIn = new StoreInput();
      }
      stoIn.setLabel(storeLabelValue.getText());
      stoIn.setLocation(storeLocValue.getText());
      hide();
   }
   
}
