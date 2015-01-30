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

package org.altherian.hboxc.front.gui.tasks;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hboxc.event.task.TaskAddedEvent;
import org.altherian.hboxc.event.task.TaskRemovedEvent;
import org.altherian.hboxc.event.task.TaskStateChangedEvent;
import org.altherian.hboxc.front.gui.ViewEventManager;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.task.TaskCancelAction;
import org.altherian.hboxc.front.gui.worker.receiver._TaskListReceiver;
import org.altherian.hboxc.front.gui.workers.TaskListWorker;
import org.altherian.helper.swing.MouseWheelController;
import org.altherian.tool.logging.Logger;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

public class ServerTaskListView implements _TaskSelector, _Refreshable {
   
   private ServerOut srvOut;
   
   private JLabel loadingLabel = new JLabel("Loading...");
   private ServerTaskListTableModel itemListModel;
   private JTable itemList;
   private JScrollPane scrollPane;
   private JPanel panel;
   private JScrollPane pane;
   
   private JPopupMenu actions;
   
   public ServerTaskListView() {
      Logger.track();
      
      itemListModel = new ServerTaskListTableModel();
      itemList = new JTable(itemListModel);
      itemList.setFillsViewportHeight(true);
      itemList.setAutoCreateRowSorter(true);
      // Sort by ID descending, so the newest queued task is always on top
      itemList.getRowSorter().setSortKeys(Arrays.asList(new RowSorter.SortKey(4, SortOrder.DESCENDING)));
      itemList.addMouseListener(new ItemListMouseListener());
      
      loadingLabel.setVisible(false);
      
      scrollPane = new JScrollPane(itemList);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(loadingLabel, "hidemode 3, growx, pushx, wrap");
      panel.add(scrollPane, "hidemode 3, grow, push, wrap");
      
      pane = new JScrollPane(panel);
      pane.setBorder(BorderFactory.createEmptyBorder());
      MouseWheelController.install(pane);
      
      actions = new JPopupMenu();
      actions.add(new JMenuItem(new TaskCancelAction(this)));
      
      ViewEventManager.register(this);
   }
   
   public JComponent getComponent() {
      return pane;
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
               TaskOut tOut = itemListModel.getObjectAtRow(itemList.convertRowIndexToModel(itemList.getSelectedRow()));
               TaskView.show(tOut);
            }
         } else {
            popupHandle(ev);
         }
      }
      
   }
   
   @Override
   public List<TaskOut> getSelection() {
      Logger.track();
      
      List<TaskOut> listSelectedItems = new ArrayList<TaskOut>();
      for (int row : itemList.getSelectedRows()) {
         listSelectedItems.add(itemListModel.getObjectAtRow(itemList.convertRowIndexToModel(row)));
      }
      return listSelectedItems;
   }
   
   @Override
   public void refresh() {
      TaskListWorker.execute(new TaskListReceiver(), srvOut);
   }
   
   public void show(ServerOut srvOut) {
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
         loadingLabel.setVisible(true);
         panel.setEnabled(false);
         panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         itemListModel.clear();
      }
      
      private void finish() {
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
      public void add(List<TaskOut> tOutList) {
         Logger.track();
         
         itemListModel.add(tOutList);
      }
   }
   
   @Handler
   private void putTaskAddedEvent(TaskAddedEvent ev) {
      if (ev.getServer().getId().equals(srvOut.getId())) {
         add(ev.getTask());
      }
   }
   
   @Handler
   private void putTaskRemovedEvent(TaskRemovedEvent ev) {
      if (ev.getServer().getId().equals(srvOut.getId())) {
         remove(ev.getTask());
      }
   }
   
   @Handler
   private void putTaskStateEvent(TaskStateChangedEvent ev) {
      if (ev.getServer().getId().equals(srvOut.getId())) {
         update(ev.getTask());
      }
   }
   
   private void add(TaskOut tOut) {
      itemListModel.add(tOut);
   }
   
   private void update(TaskOut tOut) {
      itemListModel.update(tOut);
   }
   
   private void remove(TaskOut tOut) {
      itemListModel.remove(tOut);
   }
   
}
