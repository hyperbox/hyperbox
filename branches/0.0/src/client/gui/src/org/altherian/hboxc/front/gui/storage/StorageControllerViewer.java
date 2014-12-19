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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.storage;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.in.StorageControllerIn;
import org.altherian.hbox.comm.in.StorageControllerTypeIn;
import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.out.storage.StorageControllerSubTypeOut;
import org.altherian.hbox.constant.StorageControllerAttribute;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class StorageControllerViewer {
   
   private StorageControllerIn scIn;
   
   private JLabel nameLabel;
   private JTextField nameField;
   private JLabel typeLabel;
   private JLabel typeValue;
   private JLabel subTypeLabel;
   private JComboBox subTypeBox;
   
   private JLabel ioCacheLabel;
   private JCheckBox ioCacheBox;
   private JPanel panel;
   
   public StorageControllerViewer() {
      init();
   }
   
   private void init() {
      nameLabel = new JLabel("Name");
      nameField = new JTextField();
      
      typeLabel = new JLabel("Type");
      typeValue = new JLabel();
      
      subTypeLabel = new JLabel("Sub Type");
      subTypeBox = new JComboBox();
      
      ioCacheLabel = new JLabel("Host I/O Cache");
      ioCacheBox = new JCheckBox();
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(nameLabel);
      panel.add(nameField, "growx,pushx,wrap");
      panel.add(typeLabel);
      panel.add(typeValue, "growx,pushx,wrap");
      panel.add(subTypeLabel);
      panel.add(subTypeBox, "growx,pushx,wrap");
      panel.add(ioCacheLabel);
      panel.add(ioCacheBox, "growx,pushx,wrap");
   }
   
   public JPanel getPanel() {
      return panel;
   }
   
   public void display(String srvId, StorageControllerIn scIn) {
      this.scIn = scIn;
      
      nameField.setEditable(true);
      
      String name = "";
      String scTypeId = "";
      String scSubTypeId = "";
      boolean ioCache = false;
      
      name = scIn.getName();
      scTypeId = scIn.getSetting(StorageControllerAttribute.Type).getString();
      
      if (scIn.hasSetting(StorageControllerAttribute.SubType)) {
         scSubTypeId = scIn.getSetting(StorageControllerAttribute.SubType).getString();
         Logger.debug("Selecting " + scSubTypeId + " subType");
      } else {
         Logger.debug("No SubType in the object");
      }
      
      if (scIn.hasSetting(StorageControllerAttribute.IoCache)) {
         ioCache = scIn.getSetting(StorageControllerAttribute.IoCache).getBoolean();
      } else {
         Logger.debug("No IoCache in the object");
      }
      
      subTypeBox.removeAllItems();
      for (StorageControllerSubTypeOut scstOut : Gui.getServer(srvId).listStorageControllerSubType(
            new StorageControllerTypeIn(scTypeId))) {
         subTypeBox.addItem(scstOut.getId());
      }
      
      nameField.setText(name);
      typeValue.setText(scTypeId);
      subTypeBox.setSelectedItem(scSubTypeId);
      ioCacheBox.setSelected(ioCache);
   }
   
   public void save() {
      scIn.setSubType(subTypeBox.getSelectedItem().toString());
      scIn.setSetting(new BooleanSettingIO(StorageControllerAttribute.IoCache, ioCacheBox.isSelected()));
   }
   
}
