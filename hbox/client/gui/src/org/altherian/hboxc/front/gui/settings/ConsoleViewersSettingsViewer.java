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

package org.altherian.hboxc.front.gui.settings;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hboxc.comm.output.ConsoleViewerOutput;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.event.consoleviewer.ConsoleViewerEvent;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.action.ConsoleViewerCreateAction;
import org.altherian.hboxc.front.gui.action.ConsoleViewerRemoveAction;
import org.altherian.hboxc.front.gui.vm.console.viewer.ConsoleViewerEditor;
import org.altherian.hboxc.front.gui.vm.console.viewer.ConsoleViewerTableModel;
import org.altherian.hboxc.front.gui.vm.console.viewer._ConsoleViewerSelector;
import org.altherian.tool.logging.Logger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ConsoleViewersSettingsViewer implements _ConsoleViewerSelector {
   
   private JPanel panel;
   
   private ConsoleViewerTableModel consViewerTableModel;
   private JTable consViewerTable;
   private JScrollPane consViewerTablePane;
   
   private JButton addButton;
   private JButton remButton;
   private JPanel buttonPanel;
   
   public ConsoleViewersSettingsViewer() {
      Logger.track();
      
      FrontEventManager.register(this);
      
      consViewerTableModel = new ConsoleViewerTableModel();
      consViewerTable = new JTable(consViewerTableModel);
      consViewerTable.addMouseListener(new BrowseMouseListener());
      consViewerTable.setShowGrid(true);
      consViewerTable.setFillsViewportHeight(true);
      consViewerTable.setAutoCreateRowSorter(true);
      consViewerTablePane = new JScrollPane(consViewerTable);
      
      addButton = new JButton(new ConsoleViewerCreateAction());
      remButton = new JButton(new ConsoleViewerRemoveAction(this));
      buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(addButton);
      buttonPanel.add(remButton);
      
      panel = new JPanel(new MigLayout());
      panel.add(consViewerTablePane, "grow,push,wrap");
      panel.add(buttonPanel, "growx,pushx,wrap");
   }
   
   public JComponent getComponet() {
      return panel;
   }
   
   private class BrowseMouseListener extends MouseAdapter {
      
      private ConsoleViewerOutput getSelection() {
         if (consViewerTable.getSelectedRow() == -1) {
            return null;
         } else {
            return consViewerTableModel.getObjectAtRowId(consViewerTable.convertRowIndexToModel(consViewerTable.getSelectedRow()));
         }
      }
      
      @Override
      public void mouseClicked(MouseEvent ev) {
         Logger.track();
         
         if (ev.getClickCount() == 2) {
            ConsoleViewerOutput cvOut = getSelection();
            if (cvOut != null) {
               ConsoleViewerEditor.edit(cvOut);
            }
         }
      }
   }
   
   public void load() {
      Logger.track();
      
      consViewerTableModel.put(Gui.getReader().listConsoleViewers());
   }
   
   @Override
   public List<ConsoleViewerOutput> getConsoleViewers() {
      List<ConsoleViewerOutput> listOut = new ArrayList<ConsoleViewerOutput>();
      for (int rowId : consViewerTable.getSelectedRows()) {
         listOut.add(consViewerTableModel.getObjectAtRowId(consViewerTable.convertRowIndexToModel(rowId)));
      }
      return listOut;
   }
   
   @Handler
   public void putConsoleViewerEvent(ConsoleViewerEvent ev) {
      load();
   }
   
}
