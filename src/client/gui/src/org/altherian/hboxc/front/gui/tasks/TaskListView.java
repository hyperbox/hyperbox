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

import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.event.connector.ConnectorStateChangedEvent;
import org.altherian.hboxc.event.task.TaskAddedEvent;
import org.altherian.hboxc.event.task.TaskRemovedEvent;
import org.altherian.hboxc.event.task.TaskStateChangedEvent;
import org.altherian.hboxc.front.gui.FrontEventManager;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.task.TaskCancelAction;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.tool.logging.Logger;

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
import javax.swing.SwingUtilities;

public class TaskListView implements _TaskSelector, _Refreshable {
   
   private JPanel panel;
   private TaskListTableModel itemListModel;
   private JTable itemList;
   private JPopupMenu actions;
   
   public TaskListView() {
      Logger.track();
      
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
      
      FrontEventManager.register(this);
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
   
   public void refresh(final String serverId) {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refresh(serverId);
            }
         });
      } else {
         try {
            itemListModel.merge(Gui.getReader().getServerReader(serverId).listTasks());
         } catch (HyperboxRuntimeException e) {
            itemListModel.removeServer(serverId);
         }
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
         itemListModel.clear();
         for (ConnectorOutput conOut : Gui.getReader().listConnectors()) {
            if (conOut.isConnected()) {
               refresh(conOut.getServer().getId());
            }
         }
      }
      
   }
   
   private void add(final TaskOut tOut) {
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
   
   private void update(final TaskOut tOut) {
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
   
   private void remove(final TaskOut tOut) {
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
      Logger.track();
      
      update(ev.getTask());
   }
   
   @Handler
   private void putConnectorStateEvet(ConnectorStateChangedEvent ev) {
      Logger.track();
      
      refresh(ev.getConnector().getServerId());
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
   
}
