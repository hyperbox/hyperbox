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
import org.altherian.hbox.comm.io.NetService_NAT_IO;
import org.altherian.hbox.comm.io.NetService_NAT_IP4_IO;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.hypervisor.net._NATRule;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class MachineNATRulesDialog implements _Saveable, _Cancelable, _Refreshable {

   private String srvId;
   private String vmId;
   private String adaptId;

   private JDialog dialog;
   private NATRulesView ip4;

   private JPanel buttonPanel;
   private JButton saveButton;
   private JButton cancelButton;
   
   private NetService_NAT_IO rules;
   
   public static List<NetService_NAT_IO> getInput(String srvId, String vmId, String adaptId) {
      return (new NATNetworkNATRulesDialog(srvId, vmId, adaptId)).getInput();
   }
   
   public MachineNATRulesDialog(String srvId, String vmId, String adaptId) {
      this.srvId = srvId;
      this.vmId = vmId;
      this.adaptId = adaptId;
      
      ip4 = new NATRulesView();
      RefreshUtil.set(ip4.getComponent(), new _Refreshable() {

         @Override
         public void refresh() {
            refresh();
         }
         
      });

      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      
      dialog = JDialogBuilder.get("NAT Rules", saveButton);
      dialog.add(ip4.getComponent(), "grow,push,wrap");
      dialog.add(buttonPanel, "growx,pushx,center");
   }

   public NetService_NAT_IO getInput() {
      refresh();

      dialog.setSize(538, 278);
      dialog.setLocationRelativeTo(dialog.getParent());
      dialog.setVisible(true);

      return rules;
   }
   
   public void hide() {
      dialog.setVisible(false);
   }
   
   @Override
   public void cancel() {
      rules = null;
      hide();
   }
   
   @Override
   public void save() throws HyperboxRuntimeException {
      rules = new NetService_NAT_IP4_IO(true);
      for (_NATRule rule : ip4.getRules()) {
         rules.addRule(rule);
      }
      
      hide();
   }

   @Override
   public void refresh() {
      // TODO
   }

}
