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

package org.altherian.hboxc.front.gui.vm.edit;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.PositiveNumberSettingIO;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.constant.MachineAttribute;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class DisplayVmEdit {
   
   private MachineIn mIn;
   private MachineOut mOut;
   
   private JPanel panel;
   private JLabel vramLabel;
   private JLabel monitorCountLabel;
   private JLabel accel2dLabel;
   private JLabel accel3dLabel;
   private JTextField vramField;
   private JTextField monitorCountField;
   private JCheckBox accel2dBox;
   private JCheckBox accel3dBox;
   
   public DisplayVmEdit() {
      vramLabel = new JLabel("VRAM");
      vramField = new JTextField();
      monitorCountLabel = new JLabel("Monitors");
      monitorCountField = new JTextField();
      accel2dLabel = new JLabel("2D Acceleration");
      accel2dBox = new JCheckBox();
      accel3dLabel = new JLabel("3D Acceleration");
      accel3dBox = new JCheckBox();
      
      JPanel gfxPanel = new JPanel(new MigLayout());
      gfxPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Graphics"));
      gfxPanel.add(vramLabel);
      gfxPanel.add(vramField, "growx,pushx,wrap");
      gfxPanel.add(monitorCountLabel);
      gfxPanel.add(monitorCountField, "growx,pushx,wrap");
      gfxPanel.add(accel2dLabel);
      gfxPanel.add(accel2dBox, "growx,pushx,wrap");
      gfxPanel.add(accel3dLabel);
      gfxPanel.add(accel3dBox, "growx,pushx,wrap");
      
      panel = new JPanel(new MigLayout());
      panel.add(gfxPanel, "growx,pushx,wrap");
   }
   
   public Component getComp() {
      return panel;
   }
   
   public void update(MachineOut mOut, MachineIn mIn) {
      this.mIn = mIn;
      this.mOut = mOut;
      
      vramField.setText(mOut.getSetting(MachineAttribute.VRAM).getString());
      monitorCountField.setText(mOut.getSetting(MachineAttribute.MonitorCount).getString());
      accel2dBox.setSelected(mOut.getSetting(MachineAttribute.Accelerate2dVideo).getBoolean());
      accel3dBox.setSelected(mOut.getSetting(MachineAttribute.Accelerate3d).getBoolean());
   }
   
   public void save() {
      if (!mOut.getSetting(MachineAttribute.VRAM).getString().contentEquals(vramField.getText())) {
         mIn.setSetting(new PositiveNumberSettingIO(MachineAttribute.VRAM, Long.parseLong(vramField.getText())));
      }
      if (!mOut.getSetting(MachineAttribute.MonitorCount).getString().contentEquals(monitorCountField.getText())) {
         mIn.setSetting(new PositiveNumberSettingIO(MachineAttribute.MonitorCount, Long.parseLong(monitorCountField.getText())));
      }
      if (!mOut.getSetting(MachineAttribute.Accelerate2dVideo).getBoolean().equals(accel2dBox.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttribute.Accelerate2dVideo, accel2dBox.isSelected()));
      }
      if (!mOut.getSetting(MachineAttribute.Accelerate3d).getBoolean().equals(accel3dBox.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttribute.Accelerate3d, accel3dBox.isSelected()));
      }
   }
   
}
