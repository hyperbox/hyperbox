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

package org.altherian.hboxc.front.gui.security.user;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.UserOutput;
import org.altherian.hbox.comm.output.event.security.UserEventOutput;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.security.UserCreateAction;
import org.altherian.hboxc.front.gui.action.security.UserModifyAction;
import org.altherian.hboxc.front.gui.action.security.UserRemoveAction;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.server._SingleServerSelector;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.tool.logging.Logger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class UserListView implements _UserSelector, _Refreshable, _SingleServerSelector {
   
   private ServerOutput srvOut;
   
   private JPanel panel;
   private UserTableModel itemListModel;
   private JTable itemList;
   
   private JButton addButton;
   private JPanel buttonPanel;
   
   private JPopupMenu actions;
   
   public UserListView() {
      Logger.track();
      
      itemListModel = new UserTableModel();
      itemList = new JTable(itemListModel);
      itemList.setAutoCreateRowSorter(true);
      itemList.setFillsViewportHeight(true);
      itemList.addMouseListener(new ItemListMouseListener());
      
      JScrollPane scrollPane = new JScrollPane(itemList);
      
      addButton = new JButton(new UserCreateAction(this));
      addButton.setIcon(IconBuilder.getTask(HyperboxTasks.UserCreate));
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(addButton);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(scrollPane, "grow, push, wrap");
      panel.add(buttonPanel, "bottom");
      
      actions = new JPopupMenu();
      actions.add(new JMenuItem(new UserModifyAction(this)));
      actions.add(new JMenuItem(new UserRemoveAction(this)));
      
      FrontEventManager.register(this);
      RefreshUtil.set(panel, this);
   }
   
   @Override
   public String getId() {
      return "Users";
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   private void update(List<UserOutput> users) {
      itemListModel.put(users);
   }
   
   // TODO add specific handlers
   @Handler
   public void putUserEvent(UserEventOutput evOut) {
      refresh();
   }
   
   private class ItemListMouseListener extends MouseAdapter {
      
      private void showPopup(MouseEvent ev) {
         if (ev.isPopupTrigger()) {
            actions.show(ev.getComponent(), ev.getX(), ev.getY());
         }
      }
      
      @Override
      public void mouseReleased(MouseEvent ev) {
         showPopup(ev);
      }
      
      @Override
      public void mousePressed(MouseEvent ev) {
         showPopup(ev);
      }
      
      @Override
      public void mouseClicked(MouseEvent ev) {
         if ((ev.getButton() == MouseEvent.BUTTON1) && (itemList.rowAtPoint(ev.getPoint()) == -1)) {
            itemList.clearSelection();
         } else {
            showPopup(ev);
         }
      }
      
   }
   
   @Override
   public List<String> getSelection() {
      List<String> listSelectedItems = new ArrayList<String>();
      for (int row : itemList.getSelectedRows()) {
         listSelectedItems.add(itemListModel.getObjectAtRowId(itemList.convertRowIndexToModel(row)).getId());
      }
      return listSelectedItems;
   }
   
   @Override
   public void refresh() {
      update(Gui.getReader().getServerReader(srvOut.getId()).listUsers());
   }
   
   @Override
   public String getServerId() {
      return srvOut.getId();
   }
   
   public void show(ServerOutput srvOut) {
      if (srvOut == null) {
         itemListModel.clear();
      } else {
         this.srvOut = srvOut;
         refresh();
      }
   }
   
   @Override
   public ServerOutput getServer() {
      return srvOut;
   }
   
}
