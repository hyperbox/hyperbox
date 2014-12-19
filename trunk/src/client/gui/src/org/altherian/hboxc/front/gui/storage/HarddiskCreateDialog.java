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

package org.altherian.hboxc.front.gui.storage;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.in.MediumIn;
import org.altherian.hbox.comm.in.StoreItemIn;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.HardDiskFormat;
import org.altherian.hboxc.front.gui.MainView;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.store.utils.StoreItemChooser;
import org.altherian.tool.logging.Logger;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HarddiskCreateDialog implements _Saveable, _Cancelable {
   
   private static final String MB = "MB";
   private static final String GB = "GB";
   
   private ServerOut srvOut;
   
   private JDialog dialog;
   
   private JLabel locationLabel;
   private JTextField locationField;
   private JButton locationButton;
   
   private JLabel sizeLabel;
   private JTextField sizeField;
   private JComboBox sizeUnit;
   
   private JLabel formatLabel;
   private JComboBox formatBox;
   
   private JButton saveButton;
   private JButton cancelButton;
   
   private MediumIn medIn;
   
   private class BrowseAction implements ActionListener {
      
      @Override
      public void actionPerformed(ActionEvent ae) {
         StoreItemIn stiIn = StoreItemChooser.getFilename(srvOut.getId());
         if (stiIn != null) {
            locationField.setText(stiIn.getPath());
            sizeField.requestFocus();
         }
      }
      
   }
   
   private HarddiskCreateDialog(ServerOut srvOut) {
      this.srvOut = srvOut;
      locationLabel = new JLabel("Location");
      locationField = new JTextField(50);
      locationButton = new JButton("Browse...");
      locationButton.addActionListener(new BrowseAction());
      
      sizeLabel = new JLabel("Size");
      sizeField = new JTextField();
      sizeUnit = new JComboBox();
      sizeUnit.addItem(GB);
      sizeUnit.addItem(MB);
      
      formatLabel = new JLabel("Format");
      formatBox = new JComboBox();
      for (HardDiskFormat format : HardDiskFormat.values()) {
         formatBox.addItem(format);
      }
      
      // TODO add provisionning type
      
      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      
      dialog = new JDialog(MainView.getMainFrame());
      dialog.setTitle("Create Harddisk");
      dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
      
      JPanel content = new JPanel(new MigLayout());
      content.add(locationLabel);
      content.add(locationField, "growx,pushx");
      content.add(locationButton, "wrap");
      
      content.add(sizeLabel);
      content.add(sizeField, "growx,pushx");
      content.add(sizeUnit, "wrap");
      
      content.add(formatLabel);
      content.add(formatBox, "growx,pushx,span 2,wrap");
      
      content.add(saveButton);
      content.add(cancelButton);
      
      dialog.add(content);
   }
   
   /**
    * Get a new Medium info from the user
    * 
    * @param srvOut the server
    * @return MediumInput object if user has entered valid data, or <code>null</code> if the user cancelled.
    */
   public static MediumIn show(ServerOut srvOut) {
      Logger.track();
      
      HarddiskCreateDialog diskCreateDialog = new HarddiskCreateDialog(srvOut);
      MediumIn medIn = diskCreateDialog.getUserInput();
      return medIn;
   }
   
   private MediumIn getUserInput() {
      Logger.track();
      
      dialog.pack();
      dialog.setLocationRelativeTo(MainView.getMainFrame());
      dialog.setVisible(true);
      Logger.debug("User input : " + medIn);
      return medIn;
   }
   
   @Override
   public void cancel() {
      Logger.track();
      
      medIn = null;
      dialog.setVisible(false);
      dialog.dispose();
   }
   
   @Override
   public void save() {
      Logger.track();
      
      String path = locationField.getText();
      Long size = Long.parseLong(sizeField.getText());
      if (sizeUnit.getSelectedItem().toString().equalsIgnoreCase(MB)) {
         size = size * 1048576;
      }
      if (sizeUnit.getSelectedItem().toString().equalsIgnoreCase(GB)) {
         size = size * 1073741824;
      }
      String format = formatBox.getSelectedItem().toString();
      
      medIn = new MediumIn();
      medIn.setDeviceType(EntityType.HardDisk.getId());
      medIn.setLocation(path);
      medIn.setFormat(format);
      medIn.setLogicalSize(size);
      
      dialog.setVisible(false);
   }
   
}
