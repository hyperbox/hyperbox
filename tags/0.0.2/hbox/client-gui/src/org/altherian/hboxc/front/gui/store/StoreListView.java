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

package org.altherian.hboxc.front.gui.store;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.StoreOutput;
import org.altherian.hbox.comm.output.event.store.StoreStateEventOutput;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.store.StoreCreateAction;
import org.altherian.hboxc.front.gui.action.store.StoreRegisterAction;
import org.altherian.hboxc.front.gui.builder.PopupMenuBuilder;
import org.altherian.hboxc.front.gui.server._SingleServerSelector;
import org.altherian.hboxc.front.gui.store.utils.StoreItemChooser;
import org.altherian.helper.swing.MouseWheelController;
import org.altherian.tool.logging.Logger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public final class StoreListView implements _StoreSelector, _Refreshable, _SingleServerSelector {
   
   private ServerOutput srvOut;
   
   private StoreListTableModel itemListModel;
   private JTable itemList;
   private JScrollPane scrollPane;
   
   private JButton addButton;
   private JButton registerButton;
   private JPanel buttonPanel;
   
   private JPanel panel;
   
   public StoreListView() {
      Logger.track();
      
      itemListModel = new StoreListTableModel();
      itemList = new JTable(itemListModel);
      itemList.setAutoCreateRowSorter(true);
      itemList.setFillsViewportHeight(true);
      itemList.addMouseListener(new ItemListMouseListener());
      scrollPane = new JScrollPane(itemList);
      MouseWheelController.install(scrollPane);
      
      addButton = new JButton(new StoreCreateAction(this));
      registerButton = new JButton(new StoreRegisterAction(this));
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(addButton);
      buttonPanel.add(registerButton);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(scrollPane, "grow, push, wrap");
      panel.add(buttonPanel, "growx, pushx, wrap");
      
      FrontEventManager.register(this);
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   @Override
   public List<String> getSelection() {
      List<String> listSelectedItems = new ArrayList<String>();
      for (int row : itemList.getSelectedRows()) {
         listSelectedItems.add(itemListModel.getObjectAtRow(itemList.convertRowIndexToModel(row)).getId());
      }
      return listSelectedItems;
   }
   
   private void update(final List<StoreOutput> stores) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (stores.isEmpty()) {
               itemListModel.clear();
            } else {
               itemListModel.put(stores);
            }
         }
      });
   }
   
   @Handler
   public void postStoreState(StoreStateEventOutput event) {
      Logger.track();
      
      refresh();
   }
   
   private class ItemListMouseListener extends MouseAdapter {
      
      private void showPopup(MouseEvent ev) {
         if (ev.isPopupTrigger() && (itemList.getSelectedRow() > -1)) {
            JPopupMenu actions = PopupMenuBuilder.get(StoreListView.this,
                  itemListModel.getObjectAtRow(itemList.convertRowIndexToModel(itemList.getSelectedRow())));
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
         } else if ((ev.getButton() == MouseEvent.BUTTON1) && (ev.getClickCount() == 2) && (itemList.rowAtPoint(ev.getPoint()) != -1)) {
            // StoreOutput stoOut = itemListModel.getObjectAtRowId(itemList.convertRowIndexToModel(itemList.rowAtPoint(ev.getPoint())));
            StoreItemChooser.browse(srvOut);
         } else {
            showPopup(ev);
         }
      }
      
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
   
   @Override
   public void refresh() {
      Logger.track();
      
      update(Gui.getServer(srvOut).listStores());
   }
   
   @Override
   public String getId() {
      return srvOut.getId();
   }
   
}
