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

package org.altherian.hboxc.front.gui.store.utils;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.input.StoreInput;
import org.altherian.hbox.comm.input.StoreItemInput;
import org.altherian.hbox.comm.output.StoreItemOutput;
import org.altherian.hbox.comm.output.StoreOutput;
import org.altherian.hbox.constant.StoreItemAttributes;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.MainView;
import org.altherian.hboxc.front.gui._Cancelable;
import org.altherian.hboxc.front.gui._Saveable;
import org.altherian.hboxc.front.gui.action.CancelAction;
import org.altherian.hboxc.front.gui.action.SaveAction;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.utils.CancelableUtils;
import org.altherian.hboxc.front.gui.utils.StoreItemOutputComparator;
import org.altherian.tool.logging.Logger;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

public class StoreItemChooser implements _Saveable, _Cancelable {
   
   private static final int FILE_NEW = 1;
   private static final int FILE_EXIST = 2;
   private static final int FILE_NAME = 4;
   private static final int FOLDER_NAME = 8;
   private static final int BROWSE = 16;
   
   private String srvId;
   
   private int workingMode;
   private JDialog dialog;
   
   private JLabel addressBarName;
   private JTextField addressBarValue;
   private JPanel addressPanel;
   
   private JLabel storeItemFilterName;
   private JComboBox storeItemFilterValue;
   
   private JLabel storeItemName;
   private JTextField storeItemValue;
   
   private DefaultMutableTreeNode topNode;
   private DefaultTreeModel treeModel;
   private JTree tree;
   private JScrollPane treePane;
   
   private StoreItemBrowserTableModel storeItemTableModel;
   private JTable storeItemTable;
   private JScrollPane storeItemTablePane;
   
   private JButton acceptButton;
   private JButton cancelButton;
   
   private static StoreItemInput choosenStiIn;
   private StoreItemOutput choosenStiOut;
   
   protected StoreItemChooser(String srvId, int workingMode) {
      this.srvId = srvId;
      this.workingMode = workingMode;
      initDialog();
      initAddressPanel();
      initTree();
      
      if (this.workingMode == FOLDER_NAME) {
         dialog.add(treePane, "grow,push,wrap");
      } else {
         initTable();
         JSplitPane vSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, storeItemTablePane);
         vSplit.setResizeWeight(0.2d);
         
         storeItemFilterName = new JLabel("Type");
         storeItemFilterValue = new JComboBox();
         storeItemName = new JLabel("Name");
         storeItemValue = new JTextField();
         
         dialog.add(addressPanel, "growx, pushx, wrap");
         dialog.add(vSplit, "grow, push, wrap");
      }
      if ((this.workingMode != BROWSE) && (this.workingMode != FOLDER_NAME)) {
         JPanel selectorPanel = new JPanel(new MigLayout("ins 0"));
         selectorPanel.add(storeItemFilterName);
         selectorPanel.add(storeItemFilterValue, "growx, pushx, wrap");
         selectorPanel.add(storeItemName);
         selectorPanel.add(storeItemValue, "growx, pushx, wrap");
         dialog.add(selectorPanel, "growx, pushx, wrap");
      } else {
         dialog.setTitle("Store Browser");
      }
      
      initButtons();
      JPanel buttonPanel = new JPanel(new MigLayout("ins 0"));
      buttonPanel.add(acceptButton);
      buttonPanel.add(cancelButton);
      dialog.add(buttonPanel, "growx, pushx, wrap");
   }
   
   private void initDialog() {
      dialog = new JDialog(MainView.getMainFrame());
      dialog.setIconImage(IconBuilder.getHyperbox().getImage());
      dialog.setTitle("Choose an entry");
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
      dialog.setLayout(new MigLayout());
      CancelableUtils.set(this, dialog.getRootPane());
   }
   
   private void initAddressPanel() {
      addressBarName = new JLabel("Location");
      addressBarValue = new JTextField();
      addressPanel = new JPanel(new MigLayout("ins 0"));
      addressPanel.add(addressBarName);
      addressPanel.add(addressBarValue, "growx, pushx, wrap");
   }
   
   private void initTree() {
      topNode = new DefaultMutableTreeNode("Stores");
      treeModel = new DefaultTreeModel(topNode);
      tree = new JTree(treeModel);
      tree.setCellRenderer(new TreeCellRenderer());
      tree.addTreeSelectionListener(new TreeSelectListener());
      tree.addTreeWillExpandListener(new StorageTreeWillExpandListener());
      tree.setRootVisible(false);
      tree.setShowsRootHandles(true);
      treePane = new JScrollPane(tree);
   }
   
   private void initTable() {
      storeItemTableModel = new StoreItemBrowserTableModel();
      storeItemTable = new JTable(storeItemTableModel);
      storeItemTable.addMouseListener(new BrowseMouseListener());
      storeItemTable.setShowGrid(false);
      storeItemTable.setFillsViewportHeight(true);
      storeItemTable.setAutoCreateRowSorter(true);
      storeItemTablePane = new JScrollPane(storeItemTable);
   }
   
   private void initButtons() {
      acceptButton = new JButton(new SaveAction(this));
      if (workingMode == FOLDER_NAME) {
         acceptButton.setEnabled(false); // We will only enable the button if a valid selection is choosen
      }
      cancelButton = new JButton(new CancelAction(this));
   }
   
   private void insertInTree(StoreOutput stoOut) {
      DefaultMutableTreeNode storeNode = new StoreOutputTreeNode(stoOut);
      topNode.insert(storeNode, topNode.getChildCount());
      tree.scrollPathToVisible(new TreePath(storeNode.getPath()));
      treeModel.reload(topNode);
   }
   
   private void insertInTree(List<StoreItemOutput> stiOutList, DefaultMutableTreeNode parentNode) {
      Collections.sort(stiOutList, new StoreItemOutputComparator());
      for (StoreItemOutput stiOut : stiOutList) {
         if (stiOut.isContainer()) {
            insertInTree(stiOut, parentNode);
         }
      }
   }
   
   private void insertInTree(StoreItemOutput stiOut, DefaultMutableTreeNode parentNode) {
      DefaultMutableTreeNode storeItemNode = new StoreItemOutputTreeNode(stiOut);
      parentNode.insert(storeItemNode, parentNode.getChildCount());
   }
   
   private void showInTable(StoreOutput stoOut) {
      if (workingMode != FOLDER_NAME) {
         List<StoreItemOutput> stiOutList = Gui.getServer(srvId).listStoreItems(new StoreInput(stoOut.getId()));
         showInTable(stiOutList);
         addressBarValue.setText(stoOut.getLabel());
      } else {
         acceptButton.setEnabled(false);
      }
   }
   
   private void showInTable(StoreItemOutput stiOut) {
      choosenStiOut = stiOut;
      if (workingMode != FOLDER_NAME) {
         List<StoreItemOutput> stiOutList = Gui.getServer(srvId).listStoreItems(new StoreInput(stiOut.getStoreId()),
               new StoreItemInput(stiOut.getPath()));
         showInTable(stiOutList);
         addressBarValue.setText(stiOut.getPath());
      } else {
         acceptButton.setEnabled(true);
      }
   }
   
   private void showInTable(List<StoreItemOutput> stiOutList) {
      // We are not in a folder-only mode
      if (workingMode != FOLDER_NAME) {
         Collections.sort(stiOutList, new StoreItemOutputComparator());
         storeItemTableModel.put(stiOutList);
      }
   }
   
   private void select(StoreItemOutput stiOut) {
      Logger.track();
      
      storeItemValue.setText(stiOut.getName());
   }
   
   private void choose(StoreItemOutput stiOut) {
      Logger.track();
      
      
      select(stiOut);
      choosenStiOut = stiOut;
      save();
   }
   
   private void loadStores() {
      topNode.removeAllChildren();
      treeModel.reload();
      
      for (StoreOutput stoOut : Gui.getServer(srvId).listStores()) {
         insertInTree(stoOut);
      }
   }
   
   private StoreItemOutput getUserInput() {
      choosenStiIn = null;
      loadStores();
      if (workingMode == FOLDER_NAME) {
         dialog.setSize(430, 540);
      } else {
         dialog.setSize(860, 540);
      }
      dialog.setLocationRelativeTo(MainView.getMainFrame());
      dialog.setVisible(true);
      return choosenStiOut;
   }
   
   private void hide() {
      dialog.setVisible(false);
   }
   
   @Override
   public void cancel() {
      hide();
   }
   
   @Override
   public void save() {
      Logger.track();
      
      if (workingMode != FOLDER_NAME) {
         choosenStiIn = new StoreItemInput(addressBarValue.getText() + "/" + storeItemValue.getText());
      }
      if (workingMode == FILE_EXIST) {
         // TODO implement check for valid file
      }
      hide();
   }
   
   // TODO implement selective store browsing, selected when dialog is showing
   public static void browse(String srvId) {
      new StoreItemChooser(srvId, BROWSE).getUserInput();
   }
   
   /**
    * Get a Store Item path from the user. Only guarantees that the Store Item object would be a file, but does not guarantee its (non)existence.
    * 
    * @param srvId the server
    * @return StoreItemInput object with only {@link StoreItemAttributes#Path} attribute or <code>null</code> if no item was chosen by the user.
    */
   public static StoreItemInput getFilename(String srvId) {
      new StoreItemChooser(srvId, FILE_NAME).getUserInput();
      return choosenStiIn;
   }
   
   /**
    * Not Fully Implement - Does not give any guarantee.
    * 
    * @param srvId the server
    * @return StoreItemInput object with only {@link StoreItemAttributes#Path} attribute or <code>null</code> if no item was chosen by the user.
    */
   // TODO implement verification
   public static StoreItemInput getNewFilename(String srvId) {
      new StoreItemChooser(srvId, FILE_NEW).getUserInput();
      return choosenStiIn;
   }
   
   /**
    * Get a Store Item path from the user and guarantees that the given file exist, or null if the user cancel or no existing file was chosen.
    * 
    * @param srvId the server
    * @return a StoreItemOutput object or <code>null</code> if no item was chosen by the user.
    */
   public static StoreItemOutput getExisitingFile(String srvId) {
      return new StoreItemChooser(srvId, FILE_EXIST).getUserInput();
   }
   
   public static StoreItemOutput getExisitingFolder(String srvId) {
      return new StoreItemChooser(srvId, FOLDER_NAME).getUserInput();
   }
   
   private class StoreOutputTreeNode extends DefaultMutableTreeNode {
      
      private static final long serialVersionUID = 1L;
      
      public StoreOutputTreeNode(StoreOutput node) {
         super(node);
      }
      
      @Override
      public boolean isLeaf() {
         return false;
      }
   }
   
   private class StoreItemOutputTreeNode extends DefaultMutableTreeNode {
      
      private static final long serialVersionUID = 1L;
      private StoreItemOutput siOut;
      
      public StoreItemOutputTreeNode(StoreItemOutput node) {
         super(node);
         siOut = node;
      }
      
      @Override
      public boolean isLeaf() {
         return !siOut.isContainer();
      }
   }
   
   private class TreeCellRenderer extends DefaultTreeCellRenderer {
      
      private static final long serialVersionUID = 1L;
      
      @Override
      public Component getTreeCellRendererComponent(JTree rawTree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
         super.getTreeCellRendererComponent(rawTree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
         
         return this;
      }
      
   }
   
   private class TreeSelectListener implements TreeSelectionListener {
      
      @Override
      public void valueChanged(TreeSelectionEvent ev) {
         if ((ev.getNewLeadSelectionPath() != null) && (ev.getNewLeadSelectionPath().getLastPathComponent() != null)) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) ev.getNewLeadSelectionPath().getLastPathComponent();
            
            if (node.getUserObject() instanceof StoreOutput) {
               showInTable((StoreOutput) node.getUserObject());
            }
            if (node.getUserObject() instanceof StoreItemOutput) {
               showInTable((StoreItemOutput) node.getUserObject());
            }
         }
      }
      
   }
   
   private class StorageTreeWillExpandListener implements TreeWillExpandListener {
      
      @Override
      public void treeWillCollapse(TreeExpansionEvent ev) throws ExpandVetoException {
         // nothing to do here
      }
      
      @Override
      public void treeWillExpand(TreeExpansionEvent ev) throws ExpandVetoException {
         DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) ev.getPath().getLastPathComponent();
         if (dmtn.getUserObject() instanceof StoreOutput) {
            StoreOutput stoOut = (StoreOutput) dmtn.getUserObject();
            List<StoreItemOutput> stiOutList = Gui.getServer(srvId).listStoreItems(new StoreInput(stoOut.getId()));
            insertInTree(stiOutList, dmtn);
         }
         if (dmtn.getUserObject() instanceof StoreItemOutput) {
            StoreItemOutput stiOutParent = (StoreItemOutput) dmtn.getUserObject();
            List<StoreItemOutput> stiOutList = Gui.getServer(srvId).listStoreItems(new StoreInput(stiOutParent.getStoreId()),
                  new StoreItemInput(stiOutParent.getPath()));
            insertInTree(stiOutList, dmtn);
         }
      }
      
   }
   
   private class BrowseMouseListener extends MouseAdapter {
      
      private StoreItemOutput getSelection() {
         int selectedRow = storeItemTable.getSelectedRow();
         Logger.debug("Selected row: " + selectedRow);
         
         if (selectedRow == -1) {
            return null;
         } else {
            return storeItemTableModel.getObjectAtRowId(storeItemTable.convertRowIndexToModel(selectedRow));
         }
      }
      
      @Override
      public void mouseClicked(MouseEvent ev) {
         Logger.track();
         
         StoreItemOutput stiOut = getSelection();
         if (stiOut != null) {
            if ((ev.getClickCount() == 1) && !stiOut.isContainer()) {
               System.out.println("Selecting");
               select(stiOut);
            } else {
               Logger.track();
            }
            if (ev.getClickCount() == 2) {
               if (stiOut.isContainer()) {
                  System.out.println("Showing");
                  showInTable(stiOut);
               } else {
                  System.out.println("Choosing");
                  choose(stiOut);
               }
            } else {
               Logger.track();
            }
         } else {
            Logger.track();
         }
      }
   }
}
