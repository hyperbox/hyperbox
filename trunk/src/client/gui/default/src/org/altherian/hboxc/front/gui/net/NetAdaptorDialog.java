/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.hboxc.front.gui.net;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.in.NetAdaptorIn;
import org.altherian.hbox.comm.out.network.NetAdaptorOut;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import org.altherian.hboxc.front.gui.hypervisor._NetAdaptorConfigureView;
import org.altherian.tool.AxStrings;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class NetAdaptorDialog implements _Saveable, _Cancelable {
   
   private NetAdaptorIn adaptIn;
   private _NetAdaptorConfigureView configView;
   
   private JPanel buttonsPanel;
   private JButton saveButton;
   private JButton cancelButton;
   
   private JDialog dialog;
   
   private NetAdaptorDialog(final String srvId, final String modeId, final String adaptId) {
      configView = Gui.getHypervisorModel(Gui.getServer(srvId).getHypervisor().getInfo().getId()).getNetAdaptorConfig(srvId, modeId, adaptId);
      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      
      buttonsPanel = new JPanel(new MigLayout("ins 0"));
      buttonsPanel.add(saveButton);
      buttonsPanel.add(cancelButton);
      
      dialog = JDialogBuilder.get("Add Network Adaptor", saveButton);
      dialog.getContentPane().add(configView.getComponent(), "grow, push, wrap");
      dialog.getContentPane().add(buttonsPanel, "center");
      
      if (!AxStrings.isEmpty(adaptId)) {
         dialog.setTitle("Modifying Network Adator - Loading...");
         new SwingWorker<NetAdaptorOut, Void>() {
            
            @Override
            protected NetAdaptorOut doInBackground() throws Exception {
               return Gui.getServer(srvId).getHypervisor().getNetAdaptor(modeId, adaptId);
            }
            
            @Override
            protected void done() {
               try {
                  NetAdaptorOut naOut = get();
                  configView.update(naOut);
                  dialog.setTitle("Modifying Network Adaptor - " + naOut.getLabel());
               } catch (InterruptedException e) {
                  Gui.showError(e);
               } catch (ExecutionException e) {
                  Gui.showError(e.getCause());
               }
            }
         }.execute();
      }
   }
   
   private NetAdaptorIn getInput() {
      show();
      return adaptIn;
   }
   
   public static NetAdaptorIn getInput(String srvId, String modeId, String adaptId) {
      try {
         return new NetAdaptorDialog(srvId, modeId, adaptId).getInput();
      } catch (HyperboxRuntimeException e) {
         Gui.showError(e.getMessage());
         return null;
      }
   }
   
   private void show() {
      dialog.pack();
      dialog.setLocationRelativeTo(dialog.getParent());
      dialog.setVisible(true);
   }
   
   private void hide() {
      dialog.setVisible(false);
   }
   
   @Override
   public void cancel() {
      adaptIn = null;
      hide();
   }
   
   @Override
   public void save() {
      adaptIn = configView.getInput();
      hide();
   }
   
}
