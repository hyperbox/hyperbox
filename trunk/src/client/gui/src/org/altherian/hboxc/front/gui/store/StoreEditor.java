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

import org.altherian.hbox.comm.in.StoreIn;
import org.altherian.hbox.comm.out.StoreItemOut;
import org.altherian.hbox.comm.out.StoreOut;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import org.altherian.hboxc.front.gui.store.utils.StoreItemChooser;
import org.altherian.hboxc.front.gui.utils.CancelableUtils;
import org.altherian.tool.AxStrings;
import org.altherian.tool.logging.Logger;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StoreEditor implements _Saveable, _Cancelable {
   
   private JLabel storeLabel;
   private JTextField storeLabelValue;
   private JLabel storeLocLabel;
   private JTextField storeLocValue;
   private JButton browseButton;
   private JLabel storeTypeLabel;
   private JComboBox storeTypeBox;
   
   private JButton saveButton;
   private JButton cancelButton;
   
   private JDialog dialog;
   
   private String srvId;
   private StoreIn stoIn;
   private StoreOut stoOut;
   
   @SuppressWarnings("serial")
   private class BrowseAction extends AbstractAction {
      
      public BrowseAction() {
         super("Browse");
      }
      
      @Override
      public void actionPerformed(ActionEvent e) {
         StoreItemOut stiOut = StoreItemChooser.getExisitingFolder(srvId);
         if (stiOut != null) {
            storeLocValue.setText(stiOut.getPath());
            storeLocValue.requestFocus();
         }
      }
      
   }
   
   private class EmptyValueListener implements DocumentListener {
      
      private void validate() {
         saveButton.setEnabled(!AxStrings.isEmpty(storeLabelValue.getText()) && !AxStrings.isEmpty(storeLocValue.getText()));
      }
      
      @Override
      public void insertUpdate(DocumentEvent e) {
         validate();
      }
      
      @Override
      public void removeUpdate(DocumentEvent e) {
         validate();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e) {
         validate();
      }
      
   }
   
   public StoreEditor(String srvId) {
      Logger.track();
      
      this.srvId = srvId;
      
      saveButton = new JButton(new SaveAction(this));
      saveButton.setEnabled(false);
      cancelButton = new JButton(new CancelAction(this));
      JPanel buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      
      storeLabel = new JLabel("Label");
      storeLocLabel = new JLabel("Location");
      
      storeLabelValue = new JTextField();
      storeLabelValue.getDocument().addDocumentListener(new EmptyValueListener());
      storeLocValue = new JTextField();
      storeLocValue.getDocument().addDocumentListener(new EmptyValueListener());
      browseButton = new JButton(new BrowseAction());
      storeTypeLabel = new JLabel("Type");
      storeTypeBox = new JComboBox();
      // TODO retrieve full list of supported store types.
      storeTypeBox.addItem("Native Folder");
      
      dialog = JDialogBuilder.get(saveButton);
      dialog.add(storeLabel);
      dialog.add(storeLabelValue, "growx, pushx, span 2, wrap");
      dialog.add(storeTypeLabel);
      dialog.add(storeTypeBox, "growx, pushx, span 2, wrap");
      dialog.add(storeLocLabel);
      dialog.add(storeLocValue, "growx, pushx");
      dialog.add(browseButton, "wrap");
      dialog.add(buttonPanel, "center, span 3");
      CancelableUtils.set(this, dialog.getRootPane());
   }
   
   public StoreIn create() {
      Logger.track();
      
      dialog.setTitle("Create new Store");
      show();
      return stoIn;
   }
   
   public StoreIn register() {
      Logger.track();
      
      dialog.setTitle("Registering new Store");
      show();
      return stoIn;
   }
   
   public StoreIn edit(StoreOut stoOut) {
      Logger.track();
      
      this.stoOut = stoOut;
      dialog.setTitle("Edit store " + stoOut.getLabel());
      
      storeLabelValue.setText(stoOut.getLabel());
      storeLocValue.setText(stoOut.getLocation());
      
      show();
      return stoIn;
   }
   
   public static StoreIn getInputCreate(String srvId) {
      return new StoreEditor(srvId).create();
   }
   
   public static StoreIn getInputRegister(String srvId) {
      return new StoreEditor(srvId).register();
   }
   
   public static StoreIn getInputEdit(String srvId, StoreOut stoOut) {
      return new StoreEditor(srvId).edit(stoOut);
      
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
         stoIn = new StoreIn(stoOut.getId());
      } else {
         stoIn = new StoreIn();
      }
      stoIn.setLabel(storeLabelValue.getText());
      stoIn.setLocation(storeLocValue.getText());
      hide();
   }
   
}
