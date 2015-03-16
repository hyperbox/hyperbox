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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.front.gui.vm.console.viewer;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.Request;
import org.altherian.hboxc.comm.input.ConsoleViewerInput;
import org.altherian.hboxc.comm.output.ConsoleViewerOutput;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConsoleViewerEditor implements _Saveable, _Cancelable {

   private ClientTasks saveTask;
   private ConsoleViewerInput cvIn;

   private JLabel hypervisorLabel;
   private JLabel moduleLabel;
   private JLabel pathLabel;
   private JLabel argsLabel;

   private JTextField hypervisorData;
   private JTextField moduleData;
   private JTextField pathData;
   private JTextField argsData;

   private JPanel buttonPanel;
   private JButton saveButton;
   private JButton cancelButton;

   private JDialog dialog;

   private ConsoleViewerEditor() {

      hypervisorLabel = new JLabel("Hypervisor Type Pattern");
      moduleLabel = new JLabel("Module Pattern");
      pathLabel = new JLabel("Viewer Path");
      argsLabel = new JLabel("Arguments");

      hypervisorData = new JTextField();
      moduleData = new JTextField();
      pathData = new JTextField();
      argsData = new JTextField();

      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);

      dialog = JDialogBuilder.get("Console Viewer - Edition", saveButton);
      dialog.add(hypervisorLabel);
      dialog.add(hypervisorData, "growx, pushx, wrap");
      dialog.add(moduleLabel);
      dialog.add(moduleData, "growx, pushx, wrap");
      dialog.add(pathLabel);
      dialog.add(pathData, "growx, pushx, wrap");
      dialog.add(argsLabel);
      dialog.add(argsData, "growx, pushx, wrap");
      dialog.add(buttonPanel, "center, bottom, span 2");
   }

   public static void create() {

      new ConsoleViewerEditor().add();
   }

   public static void edit(ConsoleViewerOutput conViewOut) {

      new ConsoleViewerEditor().modify(conViewOut);
   }

   private void add() {

      saveTask = ClientTasks.ConsoleViewerAdd;
      cvIn = new ConsoleViewerInput();

      show();
   }

   private void modify(ConsoleViewerOutput conViewOut) {

      saveTask = ClientTasks.ConsoleViewerModify;
      cvIn = new ConsoleViewerInput(conViewOut.getId());

      hypervisorData.setText(conViewOut.getHypervisorId());
      hypervisorData.setEditable(false);
      moduleData.setText(conViewOut.getModuleId());
      moduleData.setEditable(false);
      pathData.setText(conViewOut.getViewerPath());
      argsData.setText(conViewOut.getArgs().toString());

      show();
   }

   private void show() {

      dialog.setSize(410, 170);
      dialog.setLocationRelativeTo(dialog.getParent());
      dialog.setVisible(true);
   }

   private void hide() {

      dialog.setVisible(false);
   }

   @Override
   public void cancel() {

      hide();
   }

   @Override
   public void save() {

      cvIn.setHypervisorId(hypervisorData.getText());
      cvIn.setModuleId(moduleData.getText());
      cvIn.setViewer(pathData.getText());
      cvIn.setArgs(argsData.getText());

      Gui.post(new Request(saveTask, cvIn));

      hide();
   }

}
