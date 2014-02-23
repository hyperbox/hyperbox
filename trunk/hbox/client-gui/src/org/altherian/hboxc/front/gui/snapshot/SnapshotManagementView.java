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

package org.altherian.hboxc.front.gui.snapshot;

import net.engio.mbassy.listener.Handler;
import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.SnapshotInput;
import org.altherian.hbox.comm.output.event.machine.MachineSnapshotDataChangedEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotDeletedEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotTakenEventOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.SnapshotOutput;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.event.machine.MachineStateChangedEvent;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.snapshot.SnapshotDeleteAction;
import org.altherian.hboxc.front.gui.action.snapshot.SnapshotModifyAction;
import org.altherian.hboxc.front.gui.action.snapshot.SnapshotRestoreAction;
import org.altherian.hboxc.front.gui.action.snapshot.SnapshotTakeAction;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

public class SnapshotManagementView implements _SnapshotSelector, _Refreshable {
   
   private JPanel mainPanel;
   private JProgressBar refreshProgress;
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
   
   public SnapshotManagementView() {
      Logger.track();
      
      currentStateNode = new DefaultMutableTreeNode(new CurrentState());
      
      refreshProgress = new JProgressBar();
      refreshProgress.setVisible(false);
      
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
      tree.setShowsRootHandles(true);
      tree.setCellRenderer(new TreeCellRenderer());
      tree.addTreeSelectionListener(new TreeListener());
      tree.addMouseListener(new TreeMouseListener());
      treeView = new JScrollPane(tree);
      
      mainPanel = new JPanel(new MigLayout());
      mainPanel.add(refreshProgress, "hidemode 3, growx,pushx,wrap,span");
      mainPanel.add(takeSnapButton);
      mainPanel.add(restoreSnapButton);
      mainPanel.add(delSnapButton);
      mainPanel.add(infoSnapButton, "wrap");
      mainPanel.add(treeView, "grow, push, span");
      
      FrontEventManager.register(this);
   }
   
   private void add(SnapshotOutput snapOut) {
      Logger.track();
      
      if (!snapNodes.containsKey(snapOut.getUuid())) {
         final DefaultMutableTreeNode node = new DefaultMutableTreeNode(snapOut);
         DefaultMutableTreeNode parentNode = null;
         if (snapOut.hasParent()) {
            parentNode = snapNodes.get(snapOut.getParentUuid());
         } else {
            parentNode = topNode;
         }
         
         treeModel.insertNodeInto(node, parentNode, parentNode.getChildCount());
         Logger.debug("Adding snapshot node for UUID " + snapOut.getUuid());
         snapNodes.put(snapOut.getUuid(), node);
         
         SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
               tree.scrollPathToVisible(new TreePath(node.getPath()));
               expandTree();
            }
         });
         
      }
   }
   
   private void remove(String snapUuid) {
      if (snapNodes.containsKey(snapUuid)) {
         DefaultMutableTreeNode node = snapNodes.remove(snapUuid);
         MutableTreeNode parentNode = (MutableTreeNode) node.getParent();
         for (int i = 0; i < node.getChildCount(); i++) {
            MutableTreeNode childNode = (MutableTreeNode) node.getChildAt(i);
            treeModel.removeNodeFromParent(childNode);
            treeModel.insertNodeInto(childNode, parentNode, parentNode.getIndex(node));
         }
         treeModel.removeNodeFromParent(node);
      }
   }
   
   private void expandTree() {
      for (int i = 0; i < tree.getRowCount(); i++) {
         tree.expandRow(i);
      }
   }
   
   private void process(final String snapUuid) {
      Logger.track();
      
      SnapshotOutput snapOut = Gui.getServer(mOut.getServerId()).getSnapshot(new MachineInput(mOut), new SnapshotInput(snapUuid));
      add(snapOut);
      for (String childUuid : snapOut.getChildrenUuid()) {
         SnapshotManagementView.this.process(childUuid);
      }
   }
   
   private void setCurrentState() {
      Logger.track();
      
      Logger.debug("Setting currentState to Snapshot UUID: " + mOut.getCurrentSnapshot());
      if (currentStateNode.getParent() != null) {
         treeModel.removeNodeFromParent(currentStateNode);
      }
      DefaultMutableTreeNode parentNode = null;
      if ((mOut.getCurrentSnapshot() != null) && snapNodes.containsKey(mOut.getCurrentSnapshot())) {
         parentNode = snapNodes.get(mOut.getCurrentSnapshot());
      } else {
         Logger.debug("Found no snapNode for UUID " + mOut.getCurrentSnapshot());
         parentNode = topNode;
      }
      treeModel.insertNodeInto(currentStateNode, parentNode, parentNode.getChildCount());
      tree.scrollPathToVisible(new TreePath(currentStateNode.getPath()));
   }
   
   public void show(MachineOutput newVmOut) {
      Logger.track();
      
      if ((mOut == null) || !mOut.getId().equals(newVmOut.getId())) {
         mOut = newVmOut;
         refresh();
      } else {
         mOut = newVmOut;
         setCurrentState();
      }
   }
   
   @Override
   public void refresh() {
      Logger.track();
      
      clear();
      Logger.debug("Refreshing Snapshot Data for " + mOut.getName() + " (" + mOut.getUuid() + ")");
      if (mOut.hasSnapshots()) {
         Logger.debug("Machine has snapshot, processing");
         refreshProgress.setIndeterminate(true);
         refreshProgress.setVisible(true);
         tree.setEnabled(false);
         new SwingWorker<Void, Void>() {
            
            @Override
            protected Void doInBackground() throws Exception {
               SnapshotOutput snapOut = Gui.getServer(mOut.getServerId()).getRootSnapshot(new MachineInput(mOut));
               add(snapOut);
               for (String childUuid : snapOut.getChildrenUuid()) {
                  SnapshotManagementView.this.process(childUuid);
               }
               
               return null;
            }
            
            @Override
            protected void done() {
               refreshProgress.setVisible(false);
               refreshProgress.setIndeterminate(false);
               tree.setEnabled(true);
               setCurrentState();
            }
            
         }.execute();
      } else {
         Logger.debug("Machine has no snapshot, setting current state only");
         setCurrentState();
      }
      
   }
   
   public JComponent getComponent() {
      return mainPanel;
   }
   
   private void clear() {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               clear();
            }
         });
      } else {
         takeSnapButton.setEnabled(false);
         restoreSnapButton.setEnabled(false);
         delSnapButton.setEnabled(false);
         infoSnapButton.setEnabled(false);
         currentStateNode.removeFromParent();
         snapNodes.clear();
         topNode.removeAllChildren();
         treeModel.reload();
      }
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
         if (node != null) {
            if ((e.getClickCount() == 2) && (node.getUserObject() instanceof SnapshotOutput)) {
               SnapshotModifyDialog.show(mOut, ((SnapshotOutput) node.getUserObject()));
            }
         } else {
            // TODO clear selection
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
   
   
   
   @Handler
   public void putMachineStateChanged(MachineStateChangedEvent ev) {
      Logger.track();
      
      // FIXME improve
      if ((mOut.getUuid().contentEquals(ev.getUuid()))) {
         mOut = ev.getMachine();
         treeModel.reload(currentStateNode);
      }
   }
   
   @Handler
   public void putMachineSnapshotDataChangeEvent(final MachineSnapshotDataChangedEventOutput ev) {
      Logger.track();
      
      if (mOut.getId().equals(ev.getMachine().getId())) {
         refresh();
      }
   }
   
   @Handler
   public void getSnapTakenEv(final SnapshotTakenEventOutput ev) {
      Logger.track();
      
      if (mOut.getId().equals(ev.getMachine().getId())) {
         add(ev.getSnapshot());
         setCurrentState();
      }
   }
   
   @Handler
   public void getSnapDelEv(SnapshotDeletedEventOutput ev) {
      Logger.track();
      
      if (mOut.getId().equals(ev.getMachine().getId())) {
         remove(ev.getSnapshotUuid());
         setCurrentState();
      }
   }
   
}
