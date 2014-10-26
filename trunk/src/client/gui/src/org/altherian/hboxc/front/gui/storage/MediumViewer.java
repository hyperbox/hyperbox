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

import org.altherian.hbox.comm.in.MediumIn;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hbox.constant.MediumAttribute;
import org.altherian.hboxc.front.gui.Gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public final class MediumViewer {
   
   private JPanel panel;
   
   private JLabel typeLabel;
   private JLabel typeValue;
   private JLabel formatLabel;
   private JLabel formatValue;
   private JLabel sizeLabel;
   private JLabel sizeValue;
   private JLabel diskSizeLabel;
   private JLabel diskSizeValue;
   private JLabel locationLabel;
   private JLabel locationValue;
   
   public MediumViewer(ServerOut srvOut) {
      typeLabel = new JLabel("Type");
      typeValue = new JLabel();
      formatLabel = new JLabel("Format");
      formatValue = new JLabel();
      sizeLabel = new JLabel("Size");
      sizeValue = new JLabel();
      diskSizeLabel = new JLabel("Size on Disk");
      diskSizeValue = new JLabel();
      locationLabel = new JLabel("Location");
      locationValue = new JLabel();
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(typeLabel);
      panel.add(typeValue, "growx, wrap");
      panel.add(formatLabel);
      panel.add(formatValue, "growx, wrap");
      panel.add(sizeLabel);
      panel.add(sizeValue, "growx, wrap");
      panel.add(diskSizeLabel);
      panel.add(diskSizeValue, "growx, wrap");
      panel.add(locationLabel);
      panel.add(locationValue, "growx, wrap");
   }
   
   public JPanel getPanel() {
      return panel;
   }
   
   public JPanel show(ServerOut srvOut, String mediumId) {
      MediumOut medOut = Gui.getServer(srvOut).getMedium(new MediumIn(mediumId));
      return show(srvOut, medOut);
   }
   
   public JPanel show(ServerOut srvOut, MediumOut medOut) {
      if (medOut.hasSetting(MediumAttribute.Type)) {
         typeValue.setText(medOut.getSetting(MediumAttribute.Type).getString());
      }
      if (medOut.hasSetting(MediumAttribute.Format)) {
         formatValue.setText(medOut.getSetting(MediumAttribute.Format).getString());
      }
      if (medOut.hasSetting(MediumAttribute.LogicalSize)) {
         sizeValue.setText(medOut.getSetting(MediumAttribute.LogicalSize).getString());
      }
      if (medOut.hasSetting(MediumAttribute.Size)) {
         diskSizeValue.setText(medOut.getSetting(MediumAttribute.Size).getString());
      }
      if (medOut.hasSetting(MediumAttribute.Location)) {
         locationValue.setText(medOut.getSetting(MediumAttribute.Location).getString());
      }
      return getPanel();
   }
   
   public JPanel show(ServerOut srvOut, MediumIn medIn) {
      if (medIn.hasSetting(MediumAttribute.Type)) {
         typeValue.setText(medIn.getSetting(MediumAttribute.Type).getString());
      }
      if (medIn.hasSetting(MediumAttribute.Format)) {
         formatValue.setText(medIn.getSetting(MediumAttribute.Format).getString());
      }
      if (medIn.hasSetting(MediumAttribute.LogicalSize)) {
         sizeValue.setText(medIn.getSetting(MediumAttribute.LogicalSize).getString());
      }
      if (medIn.hasSetting(MediumAttribute.Size)) {
         diskSizeValue.setText(medIn.getSetting(MediumAttribute.Size).getString());
      }
      if (medIn.hasSetting(MediumAttribute.Location)) {
         locationValue.setText(medIn.getSetting(MediumAttribute.Location).getString());
      }
      return getPanel();
   }
   
}
