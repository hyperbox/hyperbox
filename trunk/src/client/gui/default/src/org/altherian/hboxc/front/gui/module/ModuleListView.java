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

package org.altherian.hboxc.front.gui.module;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.out.ModuleOut;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hboxc.event.module.ModuleEvent;
import org.altherian.hboxc.front.gui.ViewEventManager;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.module.ModuleRefreshAction;
import org.altherian.hboxc.front.gui.action.module.ModuleRegisterAction;
import org.altherian.hboxc.front.gui.builder.PopupMenuBuilder;
import org.altherian.hboxc.front.gui.worker.receiver._ModuleListReceiver;
import org.altherian.hboxc.front.gui.workers.ModuleListWorker;
import org.altherian.helper.swing.MouseWheelController;
import org.altherian.tool.logging.Logger;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

public class ModuleListView implements _ModuleSelector, _Refreshable, _ModuleListReceiver {

   private String srvId;

   private JProgressBar refreshProgress;
   private ModuleListTableModel itemListModel;
   private JTable itemList;
   private JScrollPane scrollPane;

   private JButton refreshButton;
   private JButton registerButton;
   private JPanel buttonPanel;

   private JPanel panel;

   public ModuleListView() {
      

      refreshProgress = new JProgressBar();
      refreshProgress.setVisible(false);

      itemListModel = new ModuleListTableModel();
      itemList = new JTable(itemListModel);
      itemList.setAutoCreateRowSorter(true);
      itemList.setFillsViewportHeight(true);
      itemList.getRowSorter().setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
      itemList.addMouseListener(new ItemListMouseListener());
      scrollPane = new JScrollPane(itemList);
      MouseWheelController.install(scrollPane);

      refreshButton = new JButton(new ModuleRefreshAction(this));
      registerButton = new JButton(new ModuleRegisterAction(this));
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(refreshButton);
      buttonPanel.add(registerButton);

      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(refreshProgress, "hidemode 3, growx, pushx, wrap");
      panel.add(buttonPanel, "hidemode 3, growx, pushx, wrap");
      panel.add(scrollPane, "hidemode 3, grow, push, wrap");

      ViewEventManager.register(this);
   }

   public void show(ServerOut srvOut) {
      if (srvOut == null) {
         itemListModel.clear();
      } else {
         srvId = srvOut.getId();
         refresh();
      }
   }

   @Override
   public void refresh() {
      ModuleListWorker.execute(this, srvId);
   }

   @Override
   public String getServerId() {
      return srvId;
   }

   @Override
   public List<ModuleOut> getModuleSelection() {
      List<ModuleOut> listSelectedItems = new ArrayList<ModuleOut>();
      for (int row : itemList.getSelectedRows()) {
         listSelectedItems.add(itemListModel.getObjectAtRow(itemList.convertRowIndexToModel(row)));
      }
      return listSelectedItems;
   }

   public JComponent getComponent() {
      return panel;
   }

   @Override
   public void loadingStarted() {
      itemListModel.clear();
      itemList.setEnabled(false);
      refreshProgress.setIndeterminate(true);
      refreshProgress.setVisible(true);
   }

   @Override
   public void loadingFinished(boolean isSuccessful, String message) {
      refreshProgress.setIndeterminate(false);
      refreshProgress.setVisible(false);
      itemList.setEnabled(true);
   }

   @Override
   public void add(List<ModuleOut> objOutList) {
      itemListModel.add(objOutList);
   }

   @Handler
   public void putModuleEvent(ModuleEvent ev) {
      if (srvId.equals(ev.getServer().getId())) {
         refresh();
      }
   }

   private class ItemListMouseListener extends MouseAdapter {

      private void showPopup(MouseEvent ev) {
         if (ev.isPopupTrigger() && (itemList.getSelectedRow() > -1)) {
            JPopupMenu actions = PopupMenuBuilder.get(ModuleListView.this,
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
            // TODO show details
         } else {
            showPopup(ev);
         }
      }

   }

}
