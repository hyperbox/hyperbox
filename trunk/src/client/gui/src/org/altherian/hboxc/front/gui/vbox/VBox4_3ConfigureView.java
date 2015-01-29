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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.front.gui.vbox;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.in.HypervisorIn;
import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.hypervisor.HypervisorOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VBox4_3ConfigureView implements _Saveable, _Cancelable {
   
   private JLabel machineFolderLabel;
   private JTextField machineFolderValue;
   private JLabel consoleModuleLabel;
   private JTextField consoleModuleValue;
   private JLabel virtExLabel;
   private JCheckBox virtExValue;
   private JButton saveButton;
   private JButton cancelButton;
   
   private JDialog dialog;
   
   private HypervisorOut hypOut;
   private HypervisorIn hypIn;
   
   public static HypervisorIn getInput(String srvId) {
      return new VBox4_3ConfigureView().getUserInput(srvId);
   }
   
   public VBox4_3ConfigureView() {
      machineFolderLabel = new JLabel("Default Machine Folder");
      consoleModuleLabel = new JLabel("Default Extention Pack");
      virtExLabel = new JLabel("Hardware Virtualization Exclusive");
      
      machineFolderValue = new JTextField();
      consoleModuleValue = new JTextField();
      virtExValue = new JCheckBox();
      
      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      JPanel buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      
      dialog = JDialogBuilder.get("Hypervisor Configuration Edit", IconBuilder.getEntityType(EntityType.Server).getImage(), saveButton);
      dialog.add(machineFolderLabel);
      dialog.add(machineFolderValue, "growx, pushx, wrap");
      dialog.add(consoleModuleLabel);
      dialog.add(consoleModuleValue, "growx, pushx, wrap");
      dialog.add(virtExLabel);
      dialog.add(virtExValue, "growx, pushx, wrap");
      dialog.add(buttonPanel, "center, span 2");
   }
   
   private HypervisorIn getUserInput(String srvId) {
      hypOut = Gui.getServer(srvId).getHypervisor().getInfo();
      machineFolderValue.setText(hypOut.getSetting("vbox.global.machineFolder").getString());
      consoleModuleValue.setText(hypOut.getSetting("vbox.global.consoleModule").getString());
      virtExValue.setSelected(hypOut.hasSetting("vbox.global.virtEx") ? hypOut.getSetting("vbox.global.virtEx").getBoolean() : false);
      
      show();
      return hypIn;
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
      hypIn = null;
      hide();
   }
   
   @Override
   public void save() {
      hypIn = new HypervisorIn();
      hypIn.setSetting(new StringSettingIO("vbox.global.machineFolder", machineFolderValue.getText()));
      hypIn.setSetting(new StringSettingIO("vbox.global.consoleModule", consoleModuleValue.getText()));
      hypIn.setSetting(new BooleanSettingIO("vbox.global.virtEx", virtExValue.isSelected()));
      hide();
   }
   
}
