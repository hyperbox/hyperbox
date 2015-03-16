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

package org.altherian.hboxc.front.gui.vm.edit;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.constant.AudioController;
import org.altherian.hbox.constant.AudioDriver;
import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.tool.logging.Logger;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class AudioVmEdit {

   private MachineIn mIn;
   private MachineOut mOut;

   private JPanel panel;
   private JLabel audioEnableLabel;
   private JLabel driverLabel;
   private JLabel controllerLabel;
   private JCheckBox audioEnableBox;
   private JComboBox driverBox;
   private JComboBox controllerBox;

   public AudioVmEdit() {
      audioEnableLabel = new JLabel("Audio Enabled");
      audioEnableBox = new JCheckBox();

      driverLabel = new JLabel("Host Driver");
      driverBox = new JComboBox();

      controllerLabel = new JLabel("Guest Controller");
      controllerBox = new JComboBox();

      panel = new JPanel(new MigLayout());
      panel.add(audioEnableLabel);
      panel.add(audioEnableBox, "growx, pushx, wrap");
      panel.add(driverLabel);
      panel.add(driverBox, "growx, pushx, wrap");
      panel.add(controllerLabel);
      panel.add(controllerBox, "growx, pushx, wrap");
   }

   public Component getComp() {
      return panel;
   }

   public void update(MachineOut mOut, MachineIn mIn) {
      this.mIn = mIn;
      this.mOut = mOut;

      if (mOut.hasSetting(MachineAttribute.AudioEnable)) {
         audioEnableBox.setSelected(mOut.getSetting(MachineAttribute.AudioEnable).getBoolean());
      } else {
         Logger.debug("Setting " + MachineAttribute.AudioEnable + " was not found for " + mOut.toString());
      }

      driverBox.removeAllItems();
      // TODO request specific values for the machine
      for (AudioDriver driver : AudioDriver.values()) {
         driverBox.addItem(driver.toString());
      }
      if (mOut.hasSetting(MachineAttribute.AudioDriver)) {
         driverBox.setSelectedItem(mOut.getSetting(MachineAttribute.AudioDriver).getString());
      } else {
         Logger.debug("Setting " + MachineAttribute.AudioDriver + " was not found for " + mOut.toString());
      }

      controllerBox.removeAllItems();
      // TODO request specific values for the machine
      for (AudioController controller : AudioController.values()) {
         controllerBox.addItem(controller.toString());
      }
      if (mOut.hasSetting(MachineAttribute.AudioController)) {
         controllerBox.setSelectedItem(mOut.getSetting(MachineAttribute.AudioController).getString());
      } else {
         Logger.debug("Setting " + MachineAttribute.AudioController + " was not found for " + mOut.toString());
      }
   }

   public void save() {
      if (!mOut.getSetting(MachineAttribute.AudioEnable).getBoolean().equals(audioEnableBox.isSelected())) {
         mIn.setSetting(new BooleanSettingIO(MachineAttribute.AudioEnable, audioEnableBox.isSelected()));
      }
      if (!mOut.getSetting(MachineAttribute.AudioDriver).getString().contentEquals(driverBox.getSelectedItem().toString())) {
         mIn.setSetting(new StringSettingIO(MachineAttribute.AudioDriver, driverBox.getSelectedItem().toString()));
      }
      if (!mOut.getSetting(MachineAttribute.AudioController).getString().contentEquals(controllerBox.getSelectedItem().toString())) {
         mIn.setSetting(new StringSettingIO(MachineAttribute.AudioController, controllerBox.getSelectedItem().toString()));
      }
   }

}
