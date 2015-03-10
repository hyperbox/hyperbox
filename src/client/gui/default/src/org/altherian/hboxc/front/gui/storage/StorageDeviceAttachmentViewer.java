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
import org.altherian.hbox.comm.in.StorageControllerTypeIn;
import org.altherian.hbox.comm.in.StorageDeviceAttachmentIn;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hbox.comm.out.storage.StorageControllerTypeOut;
import org.altherian.hbox.constant.MediumAttribute;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.FileSizeNumber;
import org.altherian.tool.logging.Logger;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StorageDeviceAttachmentViewer {
   
   private String srvId;
   private StorageDeviceAttachmentIn sdaIn;
   private String scTypeId;
   
   private JPanel mainPanel;
   
   private JPanel attachPanel;
   private JLabel portCountLabel;
   private JComboBox portCountBox;
   private JLabel attachTypeLabel;
   private JLabel attachTypeValue;
   
   private JPanel mediumPanel;
   private JLabel mediumTypeLabel;
   private JLabel mediumTypeValue;
   private JLabel formatLabel;
   private JLabel formatValue;
   private JLabel sizeLabel;
   private JLabel sizeValue;
   private JLabel diskSizeLabel;
   private JLabel diskSizeValue;
   private JLabel locationLabel;
   private JLabel locationValue;
   private JLabel baseLocationLabel;
   private JLabel baseLocationValue;
   
   public StorageDeviceAttachmentViewer() {
      portCountLabel = new JLabel("Port");
      portCountBox = new JComboBox();
      attachTypeLabel = new JLabel("Type");
      attachTypeValue = new JLabel();
      
      attachPanel = new JPanel(new MigLayout());
      attachPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Attachment"));
      attachPanel.add(portCountLabel);
      attachPanel.add(portCountBox, "growx, pushx, wrap");
      attachPanel.add(attachTypeLabel);
      attachPanel.add(attachTypeValue, "growx, pushx, wrap");
      
      mediumTypeLabel = new JLabel("Type");
      mediumTypeValue = new JLabel();
      formatLabel = new JLabel("Format");
      formatValue = new JLabel();
      sizeLabel = new JLabel("Size");
      sizeValue = new JLabel();
      diskSizeLabel = new JLabel("Size on Disk");
      diskSizeValue = new JLabel();
      locationLabel = new JLabel("Location");
      locationValue = new JLabel();
      baseLocationLabel = new JLabel("Base Location");
      baseLocationLabel.setVisible(false);
      baseLocationValue = new JLabel();
      baseLocationValue.setVisible(false);
      
      mediumPanel = new JPanel(new MigLayout());
      mediumPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Medium"));
      
      mediumPanel.add(mediumTypeLabel);
      mediumPanel.add(mediumTypeValue, "growx, pushx, wrap");
      mediumPanel.add(formatLabel);
      mediumPanel.add(formatValue, "growx, pushx, wrap");
      mediumPanel.add(sizeLabel);
      mediumPanel.add(sizeValue, "growx, pushx, wrap");
      mediumPanel.add(diskSizeLabel);
      mediumPanel.add(diskSizeValue, "growx, pushx, wrap");
      mediumPanel.add(baseLocationLabel, "hidemode 3");
      mediumPanel.add(baseLocationValue, "growx, wrap, hidemode 3");
      mediumPanel.add(locationLabel);
      mediumPanel.add(locationValue, "growx, pushx, wrap");
      
      mainPanel = new JPanel(new MigLayout("ins 0"));
      mainPanel.add(attachPanel, "wrap, growx, pushx");
      mainPanel.add(mediumPanel, "wrap, growx, pushx");
   }
   
   public JPanel show(String srvId, String scTypeId, StorageDeviceAttachmentIn sdaIn) {
      
      
      this.srvId = srvId;
      this.sdaIn = sdaIn;
      this.scTypeId = scTypeId;
      
      refresh();
      
      return getPanel();
   }
   
   public void refresh() {
      Logger.debug(sdaIn);
      attachTypeValue.setText(sdaIn.getDeviceType());
      portCountBox.removeAllItems();
      StorageControllerTypeOut sctOut = Gui.getServer(srvId).getStorageControllerType(new StorageControllerTypeIn(scTypeId));
      for (long i = sctOut.getMinPort() - 1; i < sctOut.getMaxPort(); i++) {
         if (sctOut.getMaxPort() >= sctOut.getMinPort()) {
            String attachLocation = Long.toString(i);
            for (long j = 0; j < sctOut.getMaxDevicePerPort(); j++) {
               portCountBox.addItem(attachLocation + ":" + Long.toString(j));
            }
         }
      }
      
      portCountBox.setSelectedItem(sdaIn.getPortId() + ":" + sdaIn.getDeviceId());
      
      mediumPanel.setVisible(sdaIn.hasMedium());
      if (sdaIn.hasMedium()) {
         show(sdaIn.getMedium());
      }
   }
   
   public JPanel show(MediumIn medIn) {
      
      
      if (medIn.hasSetting(MediumAttribute.Type)) {
         mediumTypeValue.setText(medIn.getSetting(MediumAttribute.Type).getString());
      }
      if (medIn.hasSetting(MediumAttribute.Format)) {
         formatValue.setText(medIn.getSetting(MediumAttribute.Format).getString());
      }
      if (medIn.hasSetting(MediumAttribute.LogicalSize)) {
         try {
            FileSizeNumber size = new FileSizeNumber(medIn.getSetting(MediumAttribute.LogicalSize).getString());
            sizeValue.setText(size.getHumanSize() + " " + size.getHumanUnit());
         } catch (NumberFormatException e) {
            sizeValue.setText("NaN - " + medIn.getSetting(MediumAttribute.LogicalSize).getString());
         }
      }
      if (medIn.hasSetting(MediumAttribute.Size)) {
         try {
            FileSizeNumber size = new FileSizeNumber(medIn.getSetting(MediumAttribute.Size).getString());
            diskSizeValue.setText(size.getHumanSize() + " " + size.getHumanUnit());
         } catch (NumberFormatException e) {
            diskSizeValue.setText("NaN - " + medIn.getSetting(MediumAttribute.Size).getString());
         }
      }
      if (medIn.hasSetting(MediumAttribute.Location)) {
         locationValue.setText(medIn.getSetting(MediumAttribute.Location).getString());
         baseLocationLabel.setVisible(medIn.hasParent());
         baseLocationValue.setVisible(medIn.hasParent());
         if (medIn.hasParent()) {
            MediumOut medOut = Gui.getServer(srvId).getMedium(medIn);
            Logger.debug("Requesting medium with UUID " + medOut.getBaseUuid());
            MediumOut baseMedOut = Gui.getServer(srvId).getMedium(new MediumIn(medOut.getBaseUuid()));
            baseLocationValue.setText(baseMedOut.getLocation());
         }
         
      }
      return getPanel();
   }
   
   public JPanel getPanel() {
      return mainPanel;
   }
   
}
