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
import org.altherian.hbox.comm.in.NetServiceIn;
import org.altherian.hbox.comm.out.network.NetModeOut;
import org.altherian.hbox.comm.out.network.NetServiceOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import org.altherian.tool.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NetAdaptorDialog implements _Saveable, _Cancelable {
   
   private String modeId;
   private String adaptId;
   
   private NetAdaptorIn adaptIn;
   private List<_NetServiceEditor> svcEditors = new ArrayList<_NetServiceEditor>();
   
   private JPanel buttonsPanel;
   private JButton saveButton;
   private JButton cancelButton;
   
   private JDialog dialog;
   
   private NetAdaptorDialog(String srvId, String modeId, String adaptId) {
      this.modeId = modeId;
      this.adaptId = adaptId;
      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      
      buttonsPanel = new JPanel(new MigLayout("ins 0"));
      buttonsPanel.add(saveButton);
      buttonsPanel.add(cancelButton);
      
      dialog = JDialogBuilder.get("Add Network Adaptor", saveButton);
      dialog.getContentPane().add(new JLabel("Mode: "));
      dialog.getContentPane().add(new JLabel(modeId), "left, growx, pushx, span, wrap");
      
      NetModeOut modeOut = Gui.getServer(srvId).getHypervisor().getNetworkMode(modeId);
      for (String svcTypeId : modeOut.getNetServices()) {
         NetServiceOut svcOut = null;
         if (adaptId != null) {
            svcOut = Gui.getServer(srvId).getHypervisor().getNetService(modeId, adaptId, svcTypeId);
         }
         Logger.debug("Getting editor for " + svcTypeId + " on " + modeId);
         _NetServiceEditor editor = NetServiceEditFactory.get(svcTypeId, svcOut);
         if (editor != null) {
            svcEditors.add(editor);
            editor.getComponent().setBorder(BorderFactory.createTitledBorder(svcTypeId));
            dialog.getContentPane().add(editor.getComponent(), "span, growx, pushx, wrap");
         } else {
            Logger.warning("Service Type " + svcTypeId + " for Mode " + modeOut.getId() + " is not supported");
         }
      }
      
      dialog.getContentPane().add(buttonsPanel, "center");
   }
   
   private NetAdaptorIn getInputPrivate() {
      show();
      return adaptIn;
   }
   
   public static NetAdaptorIn getInput(String srvId, String modeId, String adaptId) {
      // TODO get precise class for different mode
      return new NetAdaptorDialog(srvId, modeId, adaptId).getInputPrivate();
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
      adaptIn = new NetAdaptorIn(modeId, adaptId);
      for (_NetServiceEditor svcEditor : svcEditors) {
         NetServiceIn svcIn = svcEditor.getInput();
         if (svcIn != null) {
            adaptIn.addService(svcIn);
         } else {
            Logger.info("No user data for Service of type " + svcEditor.getServiceId());
         }
      }
      Logger.track();
      hide();
   }
   
}
