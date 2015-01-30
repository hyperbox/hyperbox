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

package org.altherian.hboxc.front.gui.security.perm;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.PermissionIn;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.security.PermissionOut;
import org.altherian.hbox.comm.out.security.UserOut;
import org.altherian.hboxc.front.gui.ViewEventManager;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.tool.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class UserPermissionEditor implements _Refreshable {
   
   private String serverId;
   private ServerOut srvOut;
   private UserOut usrOut;
   private Set<PermissionIn> permInList = new HashSet<PermissionIn>();
   private Set<PermissionIn> addPermInList = new HashSet<PermissionIn>();
   private Set<PermissionIn> delPermInList = new HashSet<PermissionIn>();
   
   private JPanel panel;
   private JProgressBar refreshProgress;
   
   private PermissionTableModel tableModel;
   private JTable table;
   private JScrollPane scrollPane;
   
   private JButton addButton;
   private JButton delButton;
   private JPanel buttonPanel;
   
   public UserPermissionEditor() {
      refreshProgress = new JProgressBar();
      refreshProgress.setVisible(false);
      
      addButton = new JButton("+");
      addButton.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            PermissionIn permIn = PermissionAddDialog.get(serverId);
            if (permIn != null) {
               if (!permInList.contains(permIn)) {
                  if (!delPermInList.contains(permIn)) {
                     addPermInList.add(permIn);
                  } else {
                     delPermInList.remove(permIn);
                  }
                  tableModel.add(permIn);
               }
            }
         }
      });
      
      delButton = new JButton("-");
      delButton.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            PermissionIn permIn = tableModel.getObjectAtRow(table.convertRowIndexToModel(table.getSelectedRow()));
            if (addPermInList.contains(permIn)) {
               addPermInList.remove(permIn);
            }
            if (permInList.contains(permIn)) {
               delPermInList.add(permIn);
            }
            tableModel.remove(permIn);
         }
      });
      
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(addButton);
      buttonPanel.add(delButton, "wrap");
      
      tableModel = new PermissionTableModel();
      table = new JTable(tableModel);
      table.setFillsViewportHeight(true);
      table.setAutoCreateRowSorter(true);
      table.getRowSorter().setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
      scrollPane = new JScrollPane(table);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(refreshProgress, "hidemode 3, growx, pushx, wrap");
      panel.add(scrollPane, "grow, push, wrap");
      panel.add(buttonPanel, "growx, pushx, wrap");
      
      ViewEventManager.register(this);
   }
   
   public void show(String serverId, UserOut usrOut) {
      this.serverId = serverId;
      this.usrOut = usrOut;
      
      refresh();
   }
   
   private void clear() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
               clear();
            }
         });
      } else {
         tableModel.clear();
      }
   }
   
   @Override
   public void refresh() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
               refresh();
            }
         });
      } else {
         clear();
         refreshProgress.setIndeterminate(true);
         refreshProgress.setVisible(true);
         
         // TODO decouple in another class
         new SwingWorker<Void, Void>() {
            
            @Override
            protected Void doInBackground() throws Exception {
               srvOut = Gui.getServerInfo(serverId);
               permInList.clear();
               for (PermissionOut permOut : Gui.getServer(serverId).listPermissions(new UserIn(usrOut))) {
                  permInList.add(new PermissionIn(permOut));
               }
               
               return null;
            }
            
            @Override
            protected void done() {
               tableModel.put(permInList);
               refreshProgress.setVisible(false);
               refreshProgress.setIndeterminate(false);
            }
            
         }.execute();
      }
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   public void save() {
      Logger.debug("Permissions to add: " + addPermInList.size());
      ServerIn srvIn = new ServerIn(srvOut);
      UserIn usrIn = new UserIn(usrOut);
      for (PermissionIn permIn : addPermInList) {
         Gui.post(new Request(Command.HBOX, HyperboxTasks.PermissionSet, srvIn, usrIn, permIn));
      }
      Logger.debug("Permissions to remove: " + delPermInList.size());
      for (PermissionIn permIn : delPermInList) {
         Gui.post(new Request(Command.HBOX, HyperboxTasks.PermissionDelete, srvIn, usrIn, permIn));
      }
   }
   
}
