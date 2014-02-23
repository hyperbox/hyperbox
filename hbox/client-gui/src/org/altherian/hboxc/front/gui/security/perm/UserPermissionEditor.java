package org.altherian.hboxc.front.gui.security.perm;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.PermissionInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.security.PermissionOutput;
import org.altherian.hbox.comm.output.security.UserOutput;
import org.altherian.hboxc.event.FrontEventManager;
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
   private ServerOutput srvOut;
   private UserOutput usrOut;
   private Set<PermissionInput> permInList = new HashSet<PermissionInput>();
   private Set<PermissionInput> addPermInList = new HashSet<PermissionInput>();
   private Set<PermissionInput> delPermInList = new HashSet<PermissionInput>();
   
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
            PermissionInput permIn = PermissionAddDialog.get(serverId);
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
            PermissionInput permIn = tableModel.getObjectAtRow(table.convertRowIndexToModel(table.getSelectedRow()));
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
      
      FrontEventManager.register(this);
   }
   
   public void show(String serverId, UserOutput usrOut) {
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
         
         new SwingWorker<Void, Void>() {
            
            @Override
            protected Void doInBackground() throws Exception {
               srvOut = Gui.getServerInfo(serverId);
               permInList.clear();
               for (PermissionOutput permOut : Gui.getServer(serverId).listPermissions(new UserInput(usrOut))) {
                  permInList.add(new PermissionInput(permOut));
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
      ServerInput srvIn = new ServerInput(srvOut);
      UserInput usrIn = new UserInput(usrOut);
      for (PermissionInput permIn : addPermInList) {
         Gui.post(new Request(Command.HBOX, HyperboxTasks.PermissionSet, srvIn, usrIn, permIn));
      }
      Logger.debug("Permissions to remove: " + delPermInList.size());
      for (PermissionInput permIn : delPermInList) {
         Gui.post(new Request(Command.HBOX, HyperboxTasks.PermissionDelete, srvIn, usrIn, permIn));
      }
   }
   
}
