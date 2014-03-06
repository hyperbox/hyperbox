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

package org.altherian.hboxc.front.gui.tasks;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hbox.comm.output.event.task.TaskQueueEventOutput;
import org.altherian.hbox.comm.output.event.task.TaskStateEventOutput;
import org.altherian.hbox.states.TaskQueueEvents;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.task.TaskCancelAction;
import org.altherian.hboxc.front.gui.workers.TaskListWorker;
import org.altherian.hboxc.front.gui.workers._TaskListReceiver;
import org.altherian.tool.logging.Logger;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;

public class ServerTaskListView implements _TaskSelector, _Refreshable {
   
   private ServerOutput srvOut;
   
   private JLabel loadingLabel = new JLabel("Loading...");
   private ServerTaskListTableModel itemListModel;
   private JTable itemList;
   private JScrollPane scrollPane;
   private JPanel panel;
   
   private JPopupMenu actions;
   private volatile boolean isLoading;
   
   public ServerTaskListView() {
      Logger.track();
      
      itemListModel = new ServerTaskListTableModel();
      itemList = new JTable(itemListModel);
      itemList.setFillsViewportHeight(true);
      itemList.setAutoCreateRowSorter(true);
      // Sort by ID descending, so the newest queued task is always on top
      itemList.getRowSorter().setSortKeys(Arrays.asList(new RowSorter.SortKey(4, SortOrder.DESCENDING)));
      itemList.addMouseListener(new ItemListMouseListener());
      
      scrollPane = new JScrollPane(itemList);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(loadingLabel, "growx,pushx,wrap,hidemode 3");
      panel.add(scrollPane, "grow,push,wrap");
      loadingLabel.setVisible(false);
      
      actions = new JPopupMenu();
      actions.add(new JMenuItem(new TaskCancelAction(this)));
      
      FrontEventManager.register(this);
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   private class ItemListMouseListener extends MouseAdapter {
      
      private void popupHandle(MouseEvent ev) {
         if (ev.isPopupTrigger()) {
            // TODO enable when cancel task is possible
            //actions.show(ev.getComponent(), ev.getX(), ev.getY());
         }
      }
      
      @Override
      public void mouseReleased(MouseEvent ev) {
         popupHandle(ev);
      }
      
      @Override
      public void mousePressed(MouseEvent ev) {
         popupHandle(ev);
      }
      
      @Override
      public void mouseClicked(MouseEvent ev) {
         if (ev.getButton() == MouseEvent.BUTTON1) {
            if (itemList.rowAtPoint(ev.getPoint()) == -1) {
               itemList.clearSelection();
            }
            if ((ev.getClickCount() == 2) && (itemList.getSelectedRow() > -1)) {
               TaskOutput tOut = itemListModel.getObjectAtRow(itemList.convertRowIndexToModel(itemList.getSelectedRow()));
               TaskView.show(tOut);
            }
         } else {
            popupHandle(ev);
         }
      }
      
   }
   
   @Override
   public List<TaskOutput> getSelection() {
      Logger.track();
      
      List<TaskOutput> listSelectedItems = new ArrayList<TaskOutput>();
      for (int row : itemList.getSelectedRows()) {
         listSelectedItems.add(itemListModel.getObjectAtRow(itemList.convertRowIndexToModel(row)));
      }
      return listSelectedItems;
   }
   
   @Override
   public void refresh() {
      if (!isLoading) {
         isLoading = true;
         TaskListWorker.run(new TaskListReceiver(), srvOut);
      }
   }
   
   public void show(ServerOutput srvOut) {
      Logger.track();
      
      if (srvOut == null) {
         itemListModel.clear();
      } else {
         this.srvOut = srvOut;
         refresh();
      }
   }
   
   private class TaskListReceiver implements _TaskListReceiver {
      @Override
      public void loadingStarted() {
         Logger.track();
         
         loadingLabel.setVisible(true);
         panel.setEnabled(false);
         panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         itemListModel.clear();
      }
      
      private void finish() {
         isLoading = false;
         panel.setEnabled(false);
         panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      }
      
      @Override
      public void loadingFinished(boolean isSuccessful, String message) {
         Logger.track();
         
         finish();
         if (isSuccessful) {
            loadingLabel.setVisible(false);
         } else {
            loadingLabel.setText("Error when loading tasks: " + message);
         }
      }
      
      @Override
      public void add(List<TaskOutput> tOutList) {
         Logger.track();
         
         itemListModel.add(tOutList);
      }
   }
   
   @Handler
   private void putTaskQueueEvent(TaskQueueEventOutput ev) {
      if (ev.getServerId().equals(srvOut.getId())) {
         if (ev.getQueueEvent().equals(TaskQueueEvents.TaskAdded)) {
            add(ev.getTask());
         }
         if (ev.getQueueEvent().equals(TaskQueueEvents.TaskRemoved)) {
            remove(ev.getTask());
         }
      }
   }
   
   @Handler
   private void putTaskStateEvent(TaskStateEventOutput ev) {
      Logger.track();
      
      if (ev.getServerId().equals(srvOut.getId())) {
         update(ev.getTask());
      }
   }
   
   private void add(final TaskOutput tOut) {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               add(tOut);
            }
         });
      } else {
         itemListModel.add(tOut);
      }
   }
   
   private void update(final TaskOutput tOut) {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               update(tOut);
            }
         });
      } else {
         itemListModel.update(tOut);
      }
   }
   
   private void remove(final TaskOutput tOut) {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               remove(tOut);
            }
         });
      } else {
         itemListModel.remove(tOut);
      }
   }
   
}
