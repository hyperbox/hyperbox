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
import org.altherian.hbox.comm.io.NATRuleIO;
import org.altherian.hbox.comm.io.NetService_NAT_IO;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;

public class NATRulesView implements _Refreshable {
   
   private String srvId;
   private String modeId;
   private String adaptId;
   private String svcTypeId;
   
   private NATRuleTableModel model;
   private JTable table;
   private JButton addButton;
   private JButton remButton;
   private JPanel panel;
   
   public static NATRulesView get(String srvId, String modeId, String adaptId, String svcTypeId) {
      return new NATRulesView(srvId, modeId, adaptId, svcTypeId);
   }
   
   public NATRulesView(String srvId, String modeId, String adaptId, String svcTypeId) {
      this.srvId = srvId;
      this.modeId = modeId;
      this.adaptId = adaptId;
      this.svcTypeId = svcTypeId;

      model = new NATRuleTableModel();
      table = new JTable(model);
      table.setFillsViewportHeight(true);
      table.setAutoCreateRowSorter(true);
      JScrollPane scrollPane = new JScrollPane(table);
      addButton = new JButton("Add");
      remButton = new JButton("Rem");
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(scrollPane, "grow, push, spany 3");
      panel.add(addButton, "wrap");
      panel.add(remButton, "wrap");
      panel.add(new JLabel());
      RefreshUtil.set(panel, this);
   }
   
   @Override
   public void refresh() {
      new SwingWorker<NetService_NAT_IO, Void>() {

         @Override
         protected NetService_NAT_IO doInBackground() throws Exception {
            return (NetService_NAT_IO) Gui.getServer(srvId).getHypervisor().getNetService(modeId, adaptId, svcTypeId);
         }
         
         @Override
         protected void done() {
            try {
               NetService_NAT_IO svc = get();
            } catch (InterruptedException e) {
               Gui.showError("Operation was canceled");
            } catch (ExecutionException e) {
               Gui.showError(e.getCause());
            }
         }

      }.execute();
   }

   public JComponent getComponent() {
      return panel;
   }
   
   public List<NATRuleIO> getRules() {
      return model.list();
   }
}
