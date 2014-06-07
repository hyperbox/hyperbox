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

import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.PositiveNumberSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.constant.Chipsets;
import org.altherian.hbox.constant.Firmwares;
import org.altherian.hbox.constant.MachineAttributes;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SystemVmEdit {
   
   private JPanel panel;
   private JLabel cpuNbLabel;
   private JTextField cpuNbField;
   private JLabel cpuExecCapLabel;
   private JTextField cpuExecCapField;
   private JLabel ramLabel;
   private JTextField ramField;
   private JLabel mbChipsetLabel;
   private JComboBox mbChipsetBox;
   private JLabel firmwareLabel;
   private JComboBox firmwareBox;
   private JLabel utcClockLabel;
   private JCheckBox utcClockBox;
   private JLabel hosttimeSyncLabel;
   private JCheckBox hosttimeSynckBox;
   private JLabel hpetLabel;
   private JCheckBox hpetBox;
   private JLabel virtLabel;
   private JCheckBox virtBox;
   private JLabel excVirtLabel;
   private JCheckBox excVirtBox;
   private JLabel largePageLabel;
   private JCheckBox largePageBox;
   private JLabel nestedPageLabel;
   private JCheckBox nestedPageBox;
   private JLabel vpidLabel;
   private JCheckBox vpidBox;
   
   private MachineInput mIn;
   private MachineOutput mOut;
   
   public SystemVmEdit() {
      cpuNbField = new JTextField(5);
      cpuExecCapField = new JTextField(10);
      ramField = new JTextField(15);
      mbChipsetBox = new JComboBox();
      for (Chipsets chip : Chipsets.values()) {
         mbChipsetBox.addItem(chip.getId());
      }
      firmwareBox = new JComboBox();
      for (Firmwares firm : Firmwares.values()) {
         firmwareBox.addItem(firm.getId());
      }
      utcClockBox = new JCheckBox();
      hosttimeSynckBox = new JCheckBox();
      hpetBox = new JCheckBox();
      virtBox = new JCheckBox();
      excVirtBox = new JCheckBox();
      largePageBox = new JCheckBox();
      nestedPageBox = new JCheckBox();
      vpidBox = new JCheckBox();
      
      cpuNbLabel = new JLabel("CPU");
      cpuNbLabel.setLabelFor(cpuNbField);
      cpuExecCapLabel = new JLabel("CPU Exec Cap");
      ramLabel = new JLabel("RAM");
      mbChipsetLabel = new JLabel("Motherboard Chipset");
      firmwareLabel = new JLabel("Firmware");
      utcClockLabel = new JLabel("Clock in UTC mode");
      hosttimeSyncLabel = new JLabel("Sync time with host");
      hpetLabel = new JLabel("HPET");
      virtLabel = new JLabel("VT-x/AMD-V");
      excVirtLabel = new JLabel("Virtualization Exclusive");
      largePageLabel = new JLabel("Large Pages");
      nestedPageLabel = new JLabel("Nested Pages");
      vpidLabel = new JLabel("VPID (VT-x Only)");
      
      JPanel mbPanel = new JPanel(new MigLayout());
      mbPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Motherboard"));
      JPanel cpuPanel = new JPanel(new MigLayout());
      cpuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Processor"));
      JPanel accelPanel = new JPanel(new MigLayout());
      accelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Acceleration"));
      
      cpuPanel.add(cpuNbLabel);
      cpuPanel.add(cpuNbField, "growx,pushx,wrap");
      cpuPanel.add(cpuExecCapLabel);
      cpuPanel.add(cpuExecCapField, "growx,pushx,wrap");
      mbPanel.add(ramLabel);
      mbPanel.add(ramField, "growx,pushx,wrap");
      mbPanel.add(mbChipsetLabel);
      mbPanel.add(mbChipsetBox, "growx,pushx,wrap");
      mbPanel.add(firmwareLabel);
      mbPanel.add(firmwareBox, "growx,pushx,wrap");
      mbPanel.add(utcClockLabel);
      mbPanel.add(utcClockBox, "growx,pushx,wrap");
      mbPanel.add(hosttimeSyncLabel);
      mbPanel.add(hosttimeSynckBox, "growx,pushx,wrap");
      mbPanel.add(hpetLabel);
      mbPanel.add(hpetBox, "growx,pushx,wrap");
      accelPanel.add(virtLabel);
      accelPanel.add(virtBox, "growx,pushx,wrap");
      accelPanel.add(excVirtLabel);
      accelPanel.add(excVirtBox, "growx,pushx,wrap");
      accelPanel.add(largePageLabel);
      accelPanel.add(largePageBox, "growx,pushx,wrap");
      accelPanel.add(nestedPageLabel);
      accelPanel.add(nestedPageBox, "growx,pushx,wrap");
      accelPanel.add(vpidLabel);
      accelPanel.add(vpidBox, "growx,pushx,wrap");
      
      panel = new JPanel(new MigLayout());
      panel.add(mbPanel, "growx,pushx,wrap");
      panel.add(cpuPanel, "growx,pushx,wrap");
      panel.add(accelPanel, "growx,pushx,wrap");
   }
   
   public Component getComp() {
      return panel;
   }
   
   public void update(MachineOutput mOut, MachineInput mIn) {
      this.mIn = mIn;
      this.mOut = mOut;
      
      cpuNbField.setText(mOut.getSetting(MachineAttributes.CpuCount).getString());
      cpuExecCapField.setText(mOut.getSetting(MachineAttributes.CpuExecCap).getString());
      ramField.setText(mOut.getSetting(MachineAttributes.Memory).getString());
      mbChipsetBox.setSelectedItem(mOut.getSetting(MachineAttributes.Chipset).getString());
      firmwareBox.setSelectedItem(mOut.getSetting(MachineAttributes.Firmware).getString());
      // TODO not supported
      utcClockBox.setEnabled(false);
      // TODO not supported
      hosttimeSynckBox.setEnabled(false);
      hpetBox.setSelected(mOut.getSetting(MachineAttributes.HPET).getBoolean());
      virtBox.setSelected(mOut.getSetting(MachineAttributes.HwVirtEx).getBoolean());
      excVirtBox.setSelected(mOut.getSetting(MachineAttributes.HwVirtExExcl).getBoolean());
      largePageBox.setSelected(mOut.getSetting(MachineAttributes.LargePages).getBoolean());
      nestedPageBox.setSelected(mOut.getSetting(MachineAttributes.NestedPaging).getBoolean());
      vpidBox.setSelected(mOut.getSetting(MachineAttributes.Vtxvpid).getBoolean());
   }
   
   public void save() {
      if (!mOut.getSetting(MachineAttributes.CpuCount).getString().contentEquals(cpuNbField.getText())) {
         mIn.setSetting(new PositiveNumberSettingIO(MachineAttributes.CpuCount, Long.parseLong(cpuNbField.getText())));
      }
      if (!mOut.getSetting(MachineAttributes.CpuExecCap).getString().contentEquals(cpuExecCapField.getText())) {
         mIn.setSetting(new PositiveNumberSettingIO(MachineAttributes.CpuExecCap, Long.parseLong(cpuExecCapField.getText())));
      }
      if (!mOut.getSetting(MachineAttributes.Memory).getString().contentEquals(ramField.getText())) {
         mIn.setSetting(new PositiveNumberSettingIO(MachineAttributes.Memory, Long.parseLong(ramField.getText())));
      }
      if (!mOut.getSetting(MachineAttributes.Chipset).getString().contentEquals(mbChipsetBox.getSelectedItem().toString())) {
         mIn.setSetting(new StringSettingIO(MachineAttributes.Chipset, mbChipsetBox.getSelectedItem().toString()));
      }
      if (!mOut.getSetting(MachineAttributes.Firmware).getString().contentEquals(firmwareBox.getSelectedItem().toString())) {
         mIn.setSetting(new StringSettingIO(MachineAttributes.Firmware, firmwareBox.getSelectedItem().toString()));
      }
      // TODO utcoClock
      // TODO hostTimeSync
      if (!mOut.getSetting(MachineAttributes.HPET).getBoolean().equals(hpetBox.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttributes.HPET, hpetBox.isSelected()));
      }
      if (!mOut.getSetting(MachineAttributes.HwVirtEx).getBoolean().equals(virtBox.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttributes.HwVirtEx, virtBox.isSelected()));
      }
      if (!mOut.getSetting(MachineAttributes.HwVirtExExcl).getBoolean().equals(excVirtBox.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttributes.HwVirtExExcl, excVirtBox.isSelected()));
      }
      if (!mOut.getSetting(MachineAttributes.LargePages).getBoolean().equals(largePageBox.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttributes.LargePages, largePageBox.isSelected()));
      }
      if (!mOut.getSetting(MachineAttributes.NestedPaging).getBoolean().equals(nestedPageBox.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttributes.NestedPaging, nestedPageBox.isSelected()));
      }
      if (!mOut.getSetting(MachineAttributes.Vtxvpid).getBoolean().equals(vpidBox.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttributes.Vtxvpid, vpidBox.isSelected()));
      }
   }
   
}
