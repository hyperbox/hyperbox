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

package org.altherian.hboxc.front.gui.hypervisor;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.input.HypervisorInput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.hypervisor.HypervisorLoaderOutput;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HypervisorConnectView implements _Saveable, _Cancelable {
   
   private ServerOutput srvOut;
   
   private JDialog dialog;
   
   private JLabel loaderLabel;
   private JComboBox loaderData;
   
   private JLabel optionsLabel;
   private JTextField optionsData;
   
   private JButton connectButton;
   private JButton cancelButton;
   
   private HypervisorInput hypIn;
   
   public HypervisorConnectView(ServerOutput srvOut) {
      this.srvOut = srvOut;
      
      loaderLabel = new JLabel("Connector ID");
      loaderData = new JComboBox();
      
      optionsLabel = new JLabel("Connector Options");
      optionsData = new JTextField();
      
      connectButton = new JButton(new SaveAction(this, "Connect"));
      cancelButton = new JButton(new CancelAction(this));
      
      
      JPanel centerPanel = new JPanel(new MigLayout("ins 0"));
      centerPanel.add(loaderLabel);
      centerPanel.add(loaderData, "growx,pushx,wrap");
      centerPanel.add(optionsLabel);
      centerPanel.add(optionsData, "growx,pushx,wrap");
      
      JPanel buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(connectButton);
      buttonPanel.add(cancelButton);
      
      dialog = JDialogBuilder.get("Connect to Hypervisor", connectButton);
      dialog.add(centerPanel, "growx,pushx,wrap");
      dialog.add(buttonPanel, "growx,pushx,wrap");
   }
   
   public void show() {
      for (HypervisorLoaderOutput hypOut : Gui.getServer(srvOut).listHypervisors()) {
         loaderData.addItem(hypOut);
      }
      dialog.pack();
      dialog.setLocationRelativeTo(dialog.getParent());
      dialog.setVisible(true);
   }
   
   public void hide() {
      dialog.setVisible(false);
   }
   
   @Override
   public void cancel() {
      hide();
   }
   
   @Override
   public void save() throws HyperboxException {
      hypIn = new HypervisorInput(((HypervisorLoaderOutput) loaderData.getSelectedItem()).getHypervisorId());
      hypIn.setConnectionOptions(optionsData.getText());
      hypIn.setAutoConnect(true);
      hide();
   }
   
   public HypervisorInput getUserInput() {
      show();
      return hypIn;
   }
   
   public static HypervisorInput getInput(ServerOutput srvOut) {
      return new HypervisorConnectView(srvOut).getUserInput();
   }
   
}
