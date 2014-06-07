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

package org.altherian.hboxc.front.gui.vm.view;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.SnapshotInput;
import org.altherian.hbox.comm.output.event.machine.MachineSnapshotDataChangedEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotDeletedEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotModifiedEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotRestoredEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotTakenEventOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.SnapshotOutput;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.event.machine.MachineStateChangedEvent;
import org.altherian.hboxc.event.machine.MachineUpdatedEvent;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.action.snapshot.SnapshotDeleteAction;
import org.altherian.hboxc.front.gui.action.snapshot.SnapshotModifyAction;
import org.altherian.hboxc.front.gui.action.snapshot.SnapshotRestoreAction;
import org.altherian.hboxc.front.gui.action.snapshot.SnapshotTakeAction;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.snapshot.CurrentState;
import org.altherian.hboxc.front.gui.snapshot.SnapshotModifyDialog;
import org.altherian.hboxc.front.gui.snapshot._SnapshotSelector;
import org.altherian.tool.logging.Logger;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

public class VmSnapshotView implements _SnapshotSelector {
   
   private JPanel mainPanel;
   private JButton takeSnapButton;
   private JButton restoreSnapButton;
   private JButton delSnapButton;
   private JButton infoSnapButton;
   
   private DefaultMutableTreeNode topNode;
   private DefaultTreeModel treeModel;
   private JTree tree;
   private JScrollPane treeView;
   private Map<String, DefaultMutableTreeNode> snapNodes;
   private DefaultMutableTreeNode currentStateNode;
   
   private MachineOutput mOut;
   
   public VmSnapshotView() {
      currentStateNode = new DefaultMutableTreeNode(new CurrentState());
      takeSnapButton = new JButton(new SnapshotTakeAction(this));
      takeSnapButton.setIcon(IconBuilder.getTask(HypervisorTasks.SnapshotTake));
      restoreSnapButton = new JButton(new SnapshotRestoreAction(this));
      restoreSnapButton.setIcon(IconBuilder.getTask(HypervisorTasks.SnapshotRestore));
      delSnapButton = new JButton(new SnapshotDeleteAction(this));
      delSnapButton.setIcon(IconBuilder.getTask(HypervisorTasks.SnapshotDelete));
      infoSnapButton = new JButton(new SnapshotModifyAction(this));
      infoSnapButton.setIcon(IconBuilder.getTask(HypervisorTasks.SnapshotGet));
      
      snapNodes = new HashMap<String, DefaultMutableTreeNode>();
      topNode = new DefaultMutableTreeNode("Snapshots");
      treeModel = new DefaultTreeModel(topNode);
      tree = new JTree(treeModel);
      tree.setRootVisible(false);
      tree.setCellRenderer(new TreeCellRenderer());
      tree.addTreeSelectionListener(new TreeListener());
      tree.addMouseListener(new TreeMouseListener());
      treeView = new JScrollPane(tree);
      
      mainPanel = new JPanel(new MigLayout());
      mainPanel.add(takeSnapButton);
      mainPanel.add(restoreSnapButton);
      mainPanel.add(delSnapButton);
      mainPanel.add(infoSnapButton, "wrap");
      mainPanel.add(treeView, "grow, push, span");
      
      FrontEventManager.register(this);
   }
   
   private void add(SnapshotOutput snapOut) {
      if (!snapNodes.containsKey(snapOut.getUuid())) {
         DefaultMutableTreeNode node = new DefaultMutableTreeNode(snapOut);
         DefaultMutableTreeNode parentNode = null;
         if (snapOut.hasParent()) {
            parentNode = snapNodes.get(snapOut.getParentUuid());
         } else {
            parentNode = topNode;
         }
         
         treeModel.insertNodeInto(node, parentNode, parentNode.getChildCount());
         snapNodes.put(snapOut.getUuid(), node);
         tree.scrollPathToVisible(new TreePath(node.getPath()));
         expandTree();
      }
   }
   
   private void expandTree() {
      for (int i = 0; i < tree.getRowCount(); i++) {
         tree.expandRow(i);
      }
   }
   
   private void add(String snapUuid) {
      if (!snapNodes.containsKey(snapUuid)) {
         SnapshotOutput snapOut = Gui.getServer(mOut.getServerId()).getSnapshot(new MachineInput(mOut), new SnapshotInput(snapUuid));
         add(snapOut);
      }
   }
   
   private void modify(String snapUuid) {
      if (snapNodes.containsKey(snapUuid)) {
         SnapshotOutput snapOut = Gui.getServer(mOut.getServerId()).getSnapshot(new MachineInput(mOut), new SnapshotInput(snapUuid));
         TreePath[] selections = tree.getSelectionPaths();
         snapNodes.get(snapOut.getUuid()).setUserObject(snapOut);
         treeModel.reload();
         tree.setSelectionPaths(selections);
         expandTree();
      }
   }
   
   private void remove(String snapUuid) {
      if (snapNodes.containsKey(snapUuid)) {
         DefaultMutableTreeNode node = snapNodes.get(snapUuid);
         MutableTreeNode parentNode = (MutableTreeNode) node.getParent();
         for (int i = 0; i < node.getChildCount(); i++) {
            MutableTreeNode childNode = (MutableTreeNode) node.getChildAt(i);
            treeModel.removeNodeFromParent(childNode);
            treeModel.insertNodeInto(childNode, parentNode, parentNode.getIndex(node));
         }
         treeModel.removeNodeFromParent(node);
         snapNodes.remove(snapUuid);
         expandTree();
      }
   }
   
   private void process() {
      SnapshotOutput snapOut = Gui.getServer(mOut.getServerId()).getRootSnapshot(new MachineInput(mOut));
      add(snapOut);
      for (String childUuid : snapOut.getChildrenUuid()) {
         process(childUuid);
      }
   }
   
   private void process(String snapUuid) {
      SnapshotOutput snapOut = Gui.getServer(mOut.getServerId()).getSnapshot(new MachineInput(mOut), new SnapshotInput(snapUuid));
      add(snapOut);
      for (String childUuid : snapOut.getChildrenUuid()) {
         process(childUuid);
      }
   }
   
   private void setCurrentState(String snapUuid) {
      if (currentStateNode.getParent() != null) {
         treeModel.removeNodeFromParent(currentStateNode);
      }
      DefaultMutableTreeNode parentNode = null;
      if ((snapUuid != null) && snapNodes.containsKey(snapUuid)) {
         parentNode = snapNodes.get(snapUuid);
      } else {
         parentNode = topNode;
      }
      treeModel.insertNodeInto(currentStateNode, parentNode, parentNode.getChildCount());
      tree.scrollPathToVisible(new TreePath(currentStateNode.getPath()));
   }
   
   public void show(MachineOutput newVmOut) {
      if ((mOut == null) || !mOut.getUuid().contentEquals(newVmOut.getUuid())) {
         refresh(newVmOut);
      }
   }
   
   private void refresh(MachineOutput newVmOut) {
      clear();
      String currentSnapUuid = null;
      mOut = newVmOut;
      if (mOut.hasSnapshots()) {
         process();
         currentSnapUuid = mOut.getCurrentSnapshot();
      }
      setCurrentState(currentSnapUuid);
   }
   
   public JComponent getComponent() {
      return mainPanel;
   }
   
   public void clear() {
      takeSnapButton.setEnabled(false);
      restoreSnapButton.setEnabled(false);
      delSnapButton.setEnabled(false);
      infoSnapButton.setEnabled(false);
      currentStateNode.removeFromParent();
      snapNodes.clear();
      topNode.removeAllChildren();
      treeModel.reload();
      mOut = null;
   }
   
   private class TreeListener implements TreeSelectionListener {
      
      @Override
      public void valueChanged(TreeSelectionEvent ev) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
         takeSnapButton.setEnabled(false);
         restoreSnapButton.setEnabled(false);
         delSnapButton.setEnabled(false);
         infoSnapButton.setEnabled(false);
         
         if (node != null) {
            if (node.getUserObject() instanceof CurrentState) {
               takeSnapButton.setEnabled(true);
               restoreSnapButton.setEnabled(false);
               delSnapButton.setEnabled(false);
               infoSnapButton.setEnabled(false);
            }
            if (node.getUserObject() instanceof SnapshotOutput) {
               takeSnapButton.setEnabled(false);
               restoreSnapButton.setEnabled(true);
               delSnapButton.setEnabled(true);
               infoSnapButton.setEnabled(true);
            }
         }
      }
      
   }
   
   @SuppressWarnings("serial")
   private class TreeCellRenderer extends DefaultTreeCellRenderer {
      
      @Override
      public Component getTreeCellRendererComponent(JTree rawTree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
         super.getTreeCellRendererComponent(rawTree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
         
         if (node.getUserObject() instanceof SnapshotOutput) {
            SnapshotOutput snapOut = (SnapshotOutput) node.getUserObject();
            setText(snapOut.getName());
            setIcon(IconBuilder.getSnapshot(snapOut));
         }
         if (node.getUserObject() instanceof CurrentState) {
            setIcon(IconBuilder.getMachineState(MachineStates.valueOf(mOut.getState())));
         }
         
         return this;
      }
   }
   
   private class TreeMouseListener extends MouseAdapter {
      
      @Override
      public void mouseClicked(MouseEvent e) {
         DefaultMutableTreeNode node = ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent());
         if ((e.getClickCount() == 2) && (node.getUserObject() instanceof SnapshotOutput)) {
            SnapshotModifyDialog.show(mOut, ((SnapshotOutput) node.getUserObject()));
         }
      }
      
   }
   
   @Override
   public List<SnapshotOutput> getSelection() {
      List<SnapshotOutput> snapOutList = new ArrayList<SnapshotOutput>();
      for (TreePath path : tree.getSelectionPaths()) {
         snapOutList.add(((SnapshotOutput) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject()));
      }
      return snapOutList;
   }
   
   @Override
   public MachineOutput getMachine() {
      return mOut;
   }
   
   private void refreshEvent(String uuid) {
      if ((mOut != null) && mOut.getUuid().contentEquals(uuid)) {
         refresh(mOut);
      }
   }
   
   @Handler
   public void putMachineStateChanged(MachineStateChangedEvent ev) {
      if (ev.getUuid().contentEquals(mOut.getUuid())) {
         mOut = ev.getMachine();
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               treeModel.reload(currentStateNode);
            }
         });
      }
   }
   
   @Handler
   public void putMachineDataChangeEvent(final MachineSnapshotDataChangedEventOutput ev) {
      Logger.track();
      
      refreshEvent(ev.getUuid());
   }
   
   @Handler
   public void getMachineUpdateEvent(final MachineUpdatedEvent ev) {
      Logger.track();
      
      // refreshEvent(ev.getUuid());
   }
   
   @Handler
   public void putSnapTakenEv(final SnapshotTakenEventOutput ev) {
      Logger.track();
      
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            add(ev.getSnapshotUuid());
            setCurrentState(ev.getSnapshotUuid());
         }
      });
   }
   
   @Handler
   public void putSnapDelEv(final SnapshotDeletedEventOutput ev) {
      Logger.track();
      
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            remove(ev.getSnapshotUuid());
         }
      });
   }
   
   @Handler
   public void putSnapResEv(final SnapshotRestoredEventOutput ev) {
      Logger.track();
      
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            modify(ev.getSnapshotUuid());
            setCurrentState(ev.getSnapshotUuid());
         }
      });
   }
   
   @Handler
   public void putSnapModEv(final SnapshotModifiedEventOutput ev) {
      Logger.track();
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            modify(ev.getSnapshotUuid());
         }
      });
   }
   
}
