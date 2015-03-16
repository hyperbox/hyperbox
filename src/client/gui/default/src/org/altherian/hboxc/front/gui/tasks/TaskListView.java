/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
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
import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.event.server.ServerConnectedEvent;
import org.altherian.hboxc.event.server.ServerDisconnectedEvent;
import org.altherian.hboxc.event.task.TaskAddedEvent;
import org.altherian.hboxc.event.task.TaskRemovedEvent;
import org.altherian.hboxc.event.task.TaskStateChangedEvent;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.ViewEventManager;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.task.TaskCancelAction;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.hboxc.front.gui.worker.receiver._TaskListReceiver;
import org.altherian.hboxc.front.gui.workers.TaskListWorker;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

public class TaskListView implements _TaskSelector, _Refreshable, _TaskListReceiver {

   private JPanel panel;
   private TaskListTableModel itemListModel;
   private JTable itemList;
   private JPopupMenu actions;

   public TaskListView() {
      itemListModel = new TaskListTableModel();
      itemList = new JTable(itemListModel);
      itemList.setFillsViewportHeight(true);
      itemList.setAutoCreateRowSorter(true);
      // Sort by ID descending, so the newest task is always on top
      itemList.getRowSorter().setSortKeys(Arrays.asList(new RowSorter.SortKey(6, SortOrder.DESCENDING)));
      itemList.addMouseListener(new ItemListMouseListener());

      JScrollPane scrollPane = new JScrollPane(itemList);

      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(scrollPane, "grow,push");
      RefreshUtil.set(panel, this);

      actions = new JPopupMenu();
      actions.add(new JMenuItem(new TaskCancelAction(this)));

      ViewEventManager.register(this);
   }

   public JComponent getComponent() {
      return panel;
   }

   @Override
   public List<TaskOut> getSelection() {
      List<TaskOut> listSelectedItems = new ArrayList<TaskOut>();
      for (int row : itemList.getSelectedRows()) {
         listSelectedItems.add(itemListModel.getObjectAtRow(itemList.convertRowIndexToModel(row)));
      }
      return listSelectedItems;
   }

   @Override
   public void refresh() {
      itemListModel.clear();
      for (ConnectorOutput conOut : Gui.getReader().listConnectors()) {
         if (conOut.isConnected()) {
            refresh(conOut.getServer().getId());
         }
      }

   }

   private void refresh(String srvId) {
      TaskListWorker.execute(this, srvId);
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

   @Handler
   private void putTaskAddedEvent(TaskAddedEvent ev) {
      add(ev.getTask());
   }

   @Handler
   private void putTaskRemovedEvent(TaskRemovedEvent ev) {
      remove(ev.getTask());
   }

   // TODO don't reload everything, be more specific
   @Handler
   private void putTaskStateEvent(TaskStateChangedEvent ev) {

      update(ev.getTask());
   }

   @Handler
   private void putConnectorStateEvent(ServerConnectedEvent ev) {
      refresh(ev.getServer().getId());
   }

   @Handler
   private void putServerDisconnectedEvent(ServerDisconnectedEvent ev) {
      itemListModel.removeServer(ev.getServer().getId());
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
         if (itemList.rowAtPoint(ev.getPoint()) == -1) {
            itemList.clearSelection();
         }
         if ((ev.getButton() == MouseEvent.BUTTON1) && (ev.getClickCount() == 2)) {
            if (itemList.getSelectedRow() > -1) {
               TaskOut tOut = itemListModel.getObjectAtRow(itemList.convertRowIndexToModel(itemList.getSelectedRow()));
               TaskView.show(tOut);
            }
         } else {
            popupHandle(ev);
         }
      }

   }

   @Override
   public void loadingStarted() {
      // stub
   }

   @Override
   public void loadingFinished(boolean isSuccessful, String message) {
      // stub
   }

   @Override
   public void add(List<TaskOut> objOutList) {
      for (TaskOut tOut : objOutList) {
         itemListModel.merge(tOut);
      }
   }

}
