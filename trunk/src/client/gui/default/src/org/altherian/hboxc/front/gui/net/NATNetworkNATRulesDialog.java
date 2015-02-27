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
import org.altherian.hbox.comm.io.NetService_NAT_IP6_IO;
import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.hypervisor.net._NATRule;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.JDialogBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class NATNetworkNATRulesDialog implements _Saveable, _Cancelable {
   
   private JDialog dialog;
   private NATRulesView ip4;
   private NATRulesView ip6;
   
   private JTabbedPane tabs;
   
   private JPanel buttonPanel;
   private JButton saveButton;
   private JButton cancelButton;

   private List<NetService_NAT_IO> rules = new ArrayList<NetService_NAT_IO>();

   public static List<NetService_NAT_IO> getInput(String srvId, String modeId, String adaptId) {
      return (new NATNetworkNATRulesDialog(srvId, modeId, adaptId)).getInput();
   }

   public NATNetworkNATRulesDialog(String srvId, String modeId, String adaptId) {
      ip4 = new NATRulesView(srvId, modeId, adaptId, NetServiceType.NAT_IPv4.getId());
      ip6 = new NATRulesView(srvId, modeId, adaptId, NetServiceType.NAT_IPv6.getId());
      
      tabs = new JTabbedPane();
      tabs.addTab("IPv4", ip4.getComponent());
      tabs.addTab("IPv6", ip6.getComponent());
      
      saveButton = new JButton(new SaveAction(this));
      cancelButton = new JButton(new CancelAction(this));
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);

      dialog = JDialogBuilder.get("NAT Rules", saveButton);
      dialog.add(tabs, "grow,push,wrap");
      dialog.add(buttonPanel, "growx,pushx,center");
   }
   
   public List<NetService_NAT_IO> getInput() {
      ip4.refresh();
      ip6.refresh();
      
      dialog.pack();
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
      NetService_NAT_IP4_IO ip4svc = new NetService_NAT_IP4_IO(true);
      for (_NATRule rule : ip4.getRules()) {
         ip4svc.addRule(rule);
      }
      NetService_NAT_IP6_IO ip6svc = new NetService_NAT_IP6_IO(true);
      for (_NATRule rule : ip6.getRules()) {
         ip6svc.addRule(rule);
      }
      rules.add(ip4svc);
      rules.add(ip6svc);
      hide();
   }

}
