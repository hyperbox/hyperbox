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

package org.altherian.hboxc.front.gui;

import net.engio.mbassy.listener.Handler;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorConnectionStateEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineRegistrationEventOut;
import org.altherian.hbox.comm.out.event.server.ServerConnectionStateEventOut;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.comm.out.hypervisor.SnapshotOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxc.PreferencesManager;
import org.altherian.hboxc.comm.input.ConnectorInput;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.event.connector.ConnectorAddedEvent;
import org.altherian.hboxc.event.connector.ConnectorConnectedEvent;
import org.altherian.hboxc.event.connector.ConnectorDisconnectedEvent;
import org.altherian.hboxc.event.connector.ConnectorModifiedEvent;
import org.altherian.hboxc.event.connector.ConnectorRemovedEvent;
import org.altherian.hboxc.event.connector.ConnectorStateChangedEvent;
import org.altherian.hboxc.event.machine.MachineAddedEvent;
import org.altherian.hboxc.event.machine.MachineRemovedEvent;
import org.altherian.hboxc.event.machine.MachineStateChangedEvent;
import org.altherian.hboxc.event.machine.MachineUpdatedEvent;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.builder.PopupMenuBuilder;
import org.altherian.hboxc.front.gui.connector.ConnectorDetailedView;
import org.altherian.hboxc.front.gui.connector._ConnectorSelector;
import org.altherian.hboxc.front.gui.server._ServerSelector;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.hboxc.front.gui.vm._MachineSelector;
import org.altherian.hboxc.front.gui.vm.view.VmDetailedView;
import org.altherian.hboxc.front.gui.worker.receiver._MachineListReceiver;
import org.altherian.hboxc.front.gui.worker.receiver._SnapshotGetReceiver;
import org.altherian.hboxc.front.gui.workers.MachineListWorker;
import org.altherian.hboxc.front.gui.workers.SnapshotGetWorker;
import org.altherian.hboxc.front.gui.workers.WorkerDataReceiver;
import org.altherian.helper.swing.SortedTreeModel;
import org.altherian.tool.logging.Logger;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

public final class ServerMachineView implements _MachineSelector, _ServerSelector, _ConnectorSelector, _Refreshable {
   
   private static final JPanel EmptyPanel = new JPanel();
   
   private Map<String, DefaultMutableTreeNode> conNodes = new HashMap<String, DefaultMutableTreeNode>();
   private Map<String, Map<String, DefaultMutableTreeNode>> entities;
   private Map<String, DefaultMutableTreeNode> srvNodes = new HashMap<String, DefaultMutableTreeNode>();
   private DefaultMutableTreeNode topNode;
   
   private JTree tree;
   
   private SortedTreeModel treeModel;
   private JScrollPane treeView;
   private Map<String, SnapshotOut> vmCurrentSnaps = new HashMap<String, SnapshotOut>();
   private Map<String, Map<String, DefaultMutableTreeNode>> vmNodes = new HashMap<String, Map<String, DefaultMutableTreeNode>>();
   
   private JSplitPane vSplit;
   
   public ServerMachineView() {
      
      
      entities = new HashMap<String, Map<String, DefaultMutableTreeNode>>();
      initEntities();
      vmCurrentSnaps = new HashMap<String, SnapshotOut>();
      
      topNode = new DefaultMutableTreeNode("Hyperbox");
      treeModel = new SortedTreeModel(topNode);
      tree = new JTree(treeModel);
      tree.setCellRenderer(new TreeCellRenderer());
      tree.addMouseListener(new TreeMouseListener());
      tree.addTreeSelectionListener(new TreeSelectListener());
      tree.setRootVisible(false);
      tree.setShowsRootHandles(true);
      treeView = new JScrollPane(tree);
      treeView.setBorder(BorderFactory.createEmptyBorder());
      
      vSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, EmptyPanel);
      vSplit.setResizeWeight(0);
      vSplit.setDividerLocation(Integer.parseInt(PreferencesManager.get().getProperty(JSplitPane.DIVIDER_LOCATION_PROPERTY, "168")));
      vSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
         
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            PreferencesManager.get().setProperty(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt.getNewValue().toString());
         }
      });
      
      RefreshUtil.set(vSplit, this);
      ViewEventManager.register(this);
   }
   
   private void addConnector(ConnectorOutput conOut) {
      
      
      DefaultMutableTreeNode conNode = new DefaultMutableTreeNode(conOut);
      treeModel.insertNode(conNode, topNode);
      tree.scrollPathToVisible(new TreePath(conNode.getPath()));
      conNodes.put(conOut.getId(), conNode);
      refresh(conOut);
   }
   
   private void addMachine(String srvId, MachineOut mOut) {
      
      
      if (!vmNodes.get(srvId).containsKey(mOut.getId())) {
         DefaultMutableTreeNode srvNode = srvNodes.get(srvId);
         DefaultMutableTreeNode node = new DefaultMutableTreeNode(mOut);
         treeModel.insertNode(node, srvNode);
         // TODO improve so the scrolling doesn't change, only the server/connector node gets expanded
         tree.scrollPathToVisible(new TreePath(node.getPath()));
         vmNodes.get(srvId).put(mOut.getId(), node);
         if (mOut.isAvailable() && mOut.hasSnapshots()) {
            SnapshotGetWorker.execute(new SnapshotGetReceiver(), srvId, mOut.getId(), mOut.getCurrentSnapshot());
         }
         treeModel.reload(node);
      }
   }
   
   private void clear() {
      entities.clear();
      topNode.removeAllChildren();
      treeModel.reload();
   }
   
   public JComponent getComponent() {
      return vSplit;
   }
   
   @Override
   public ConnectorOutput getConnector() {
      try {
         ConnectorOutput conOut = ((ConnectorOutput) ((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent()).getUserObject());
         Logger.debug("Connector ID " + conOut.getId() + " under label " + conOut.getLabel() + " is selected");
         return conOut;
      } catch (Throwable e) {
         Logger.debug("No connector is selected");
         return null;
      }
   }
   
   @Override
   public List<MachineOut> getMachines() {
      
      
      List<MachineOut> selectedVms = new ArrayList<MachineOut>();
      for (TreePath path : tree.getSelectionPaths()) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
         if (node.getUserObject() instanceof MachineOut) {
            selectedVms.add((MachineOut) node.getUserObject());
         }
      }
      return selectedVms;
   }
   
   @Override
   public ServerOut getServer() {
      try {
         Object o = ((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent()).getUserObject();
         if (o instanceof ConnectorOutput) {
            return ((ConnectorOutput) o).getServer();
         } else if (o instanceof ServerOut) {
            return (ServerOut) o;
         } else {
            return null;
         }
      } catch (Throwable e) {
         Logger.debug("No connector is selected");
         return null;
      }
   }
   
   @Override
   public List<ServerOut> getServers() {
      List<ServerOut> selectedSrv = new ArrayList<ServerOut>();
      for (TreePath path : tree.getSelectionPaths()) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
         if (node.getUserObject() instanceof ServerOut) {
            selectedSrv.add((ServerOut) node.getUserObject());
         }
         if (node.getUserObject() instanceof ConnectorOutput) {
            selectedSrv.add(((ConnectorOutput) node.getUserObject()).getServer());
         }
      }
      return selectedSrv;
   }
   
   private void initEntities() {
      entities.put(EntityType.Server.getId(), new HashMap<String, DefaultMutableTreeNode>());
      entities.put(EntityType.Machine.getId(), new HashMap<String, DefaultMutableTreeNode>());
      entities.put(EntityType.Snapshot.getId(), new HashMap<String, DefaultMutableTreeNode>());
      entities.put(EntityType.Store.getId(), new HashMap<String, DefaultMutableTreeNode>());
   }
   
   @Override
   public List<ConnectorOutput> listConnectors() {
      List<ConnectorOutput> conOutList = new ArrayList<ConnectorOutput>();
      try {
         for (TreePath path : tree.getSelectionPaths()) {
            ConnectorOutput conOut = ((ConnectorOutput) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject());
            Logger.debug("Connector ID " + conOut.getId() + " under label " + conOut.getLabel() + " is selected");
            conOutList.add(conOut);
         }
         return conOutList;
      } catch (Throwable e) {
         Logger.debug("Not everything selected is a connector, returning empty list");
         conOutList.clear();
         return conOutList;
      }
   }
   
   @Handler
   private void putConnectorAddedEvent(ConnectorAddedEvent ev) {
      
      
      addConnector(ev.getConnector());
   }
   
   @Handler
   private void putConnectorConnectedEvent(ConnectorConnectedEvent ev) {
      
      
      updateConnector(ev.getConnector());
      refresh(ev.getConnector());
   }
   
   @Handler
   private void putConnectorDisconnectedEvent(ConnectorDisconnectedEvent ev) {
      
      
      updateConnector(ev.getConnector());
      refresh(ev.getConnector());
   }
   
   @Handler
   private void putConnectorModifiedEvent(ConnectorModifiedEvent ev) {
      
      
      updateConnector(ev.getConnector());
   }
   
   @Handler
   private void putConnectorRemovedEvent(ConnectorRemovedEvent ev) {
      
      
      removeConnector(ev.getConnector());
   }
   
   @Handler
   private void putConnectorStateChangedEvent(ConnectorStateChangedEvent ev) {
      
      
      updateConnector(ev.getConnector());
   }
   
   @Handler
   public void putHypervisorConnectionStateEvent(HypervisorConnectionStateEventOut ev) {
      
      
      updateServer(ev.getServer());
      refresh(ev.getServer());
   }
   
   @Handler
   private void putMachineAddEvent(MachineAddedEvent ev) {
      
      
      addMachine(ev.getServerId(), ev.getMachine());
   }
   
   @Handler
   private void putMachineRegistrationEvent(MachineRegistrationEventOut ev) {
      
      
      if (ev.isRegistered()) {
         addMachine(ev.getServerId(), ev.getMachine());
      } else {
         removeMachine(ev.getServerId(), ev.getMachine().getId());
      }
   }
   
   @Handler
   private void putMachineRemoveEvent(MachineRemovedEvent ev) {
      
      
      removeMachine(ev.getServerId(), ev.getMachine().getUuid());
   }
   
   @Handler
   private void putMachineUpdateEvent(MachineStateChangedEvent ev) {
      
      
      updateMachine(ev.getServerId(), ev.getMachine());
   }
   
   @Handler
   private void putMachineUpdateEvent(MachineUpdatedEvent ev) {
      
      
      updateMachine(ev.getServerId(), ev.getMachine());
   }
   
   @Handler
   private void putServerConnectionStateChangeEvent(ServerConnectionStateEventOut ev) {
      
      
      updateServer(ev.getServer());
      refresh(ev.getServer());
   }
   
   @Override
   public void refresh() {
      List<ConnectorOutput> conOutList = Gui.getReader().listConnectors();
      refresh(conOutList);
   }
   
   private void refresh(ConnectorOutput conOut) {
      DefaultMutableTreeNode conNode = conNodes.get(conOut.getId());
      conNode.removeAllChildren();
      treeModel.reload(conNode);
      if (conOut.isConnected()) {
         srvNodes.put(conOut.getServer().getId(), conNode);
         vmNodes.put(conOut.getServer().getId(), new HashMap<String, DefaultMutableTreeNode>());
         refresh(conOut.getServer());
      }
   }
   
   private void refresh(List<ConnectorOutput> conOutList) {
      
      
      clear();
      Collections.sort(conOutList, new Comparator<ConnectorOutput>() {
         
         @Override
         public int compare(ConnectorOutput o1, ConnectorOutput o2) {
            
            return o1.getLabel().compareTo(o2.getLabel());
         }
         
      });
      for (ConnectorOutput conOut : conOutList) {
         addConnector(conOut);
      }
   }
   
   private void refresh(ServerOut srvOut) {
      
      
      DefaultMutableTreeNode srvNode = srvNodes.get(srvOut.getId());
      srvNode.removeAllChildren();
      vmNodes.get(srvOut.getId()).clear();
      treeModel.reload(srvNode);
      if (srvOut.isHypervisorConnected()) {
         MachineListWorker.execute(new MachineListReceiver(), srvOut.getId());
      }
   }
   
   private void removeConnector(ConnectorOutput conOut) {
      
      
      topNode.remove(conNodes.remove(conOut.getId()));
      treeModel.reload(topNode);
   }
   
   private void removeMachine(String serverId, String id) {
      
      
      if (vmNodes.get(serverId).containsKey(id)) {
         vmCurrentSnaps.remove(id);
         treeModel.removeNodeFromParent(vmNodes.get(serverId).remove(id));
      } else {
         Logger.warning("Trying to remove machine not in the view: " + serverId + " - " + id);
      }
   }
   
   private void updateConnector(ConnectorOutput conOut) {
      
      
      DefaultMutableTreeNode conNode = conNodes.get(conOut.getId());
      conNode.setUserObject(conOut);
      treeModel.reload(conNode);
   }
   
   private void updateMachine(String srvId, MachineOut mOut) {
      
      
      if (vmNodes.get(srvId).containsKey(mOut.getId())) {
         if (mOut.hasSnapshots()) {
            SnapshotGetWorker.execute(new SnapshotGetReceiver(), srvId, mOut.getId(), mOut.getCurrentSnapshot());
         } else {
            vmCurrentSnaps.remove(mOut.getId());
         }
         DefaultMutableTreeNode node = vmNodes.get(srvId).get(mOut.getId());
         node.setUserObject(mOut);
         treeModel.reload(node);
      }
   }
   
   private void updateServer(ServerOut srvOut) {
      
      
      DefaultMutableTreeNode conNode = srvNodes.get(srvOut.getId());
      updateConnector(Gui.getReader().getConnector(((ConnectorOutput) conNode.getUserObject()).getId()));
   }
   
   @SuppressWarnings("serial")
   private class TreeCellRenderer extends DefaultTreeCellRenderer {
      
      @Override
      public Component getTreeCellRendererComponent(JTree rawTree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row,
            boolean hasFocus) {
         super.getTreeCellRendererComponent(rawTree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
         if ((node != topNode) && (node.getUserObject() != null)) {
            if (node.getUserObject() instanceof MachineOut) {
               try {
                  MachineOut simpleVmOut = (MachineOut) node.getUserObject();
                  if (!simpleVmOut.isAvailable()) {
                     setText(simpleVmOut.getUuid() + " <Unavailable>");
                     setIcon(IconBuilder.getMachineState(MachineStates.Inaccessible));
                  } else {
                     try {
                        setIcon(IconBuilder.getMachineState(MachineStates.valueOf(simpleVmOut.getState())));
                     } catch (Throwable t) {
                        setIcon(IconBuilder.getMachineState(MachineStates.Unknown));
                     }
                     try {
                        if (simpleVmOut.hasSnapshots()) {
                           String snapName = "Loading...";
                           if (vmCurrentSnaps.containsKey(simpleVmOut.getId())) {
                              snapName = vmCurrentSnaps.get(simpleVmOut.getId()).getName();
                           }
                           setText(simpleVmOut.getName() + " (" + snapName + ")");
                        } else {
                           setText(simpleVmOut.getName());
                        }
                     } catch (Throwable t) {
                        setText(simpleVmOut.getName());
                     }
                  }
               } catch (Throwable t) {
                  Logger.exception(t);
               }
            }
            else if (node.getUserObject() instanceof ConnectorOutput) {
               ConnectorOutput conOut = (ConnectorOutput) node.getUserObject();
               setIcon(IconBuilder.getConnector(conOut));
               setText(conOut.getLabel());
            }
            else {
               Logger.warning("Unknown object: " + node.getUserObject().getClass().getName());
            }
         }
         
         return this;
      }
      
   }
   
   private class TreeMouseListener extends MouseAdapter {
      
      @Override
      public void mouseClicked(MouseEvent ev) {
         if ((ev.getButton() == MouseEvent.BUTTON1) && (ev.getClickCount() == 2)) {
            if (tree.getLastSelectedPathComponent() != null) {
               DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
               if ((node != null) && (node.getUserObject() instanceof ConnectorOutput)) {
                  final ConnectorOutput conOut = (ConnectorOutput) node.getUserObject();
                  if (!conOut.isConnected()) {
                     Logger.debug("User request: Connect to server using " + conOut);
                     new SwingWorker<Void, Void>() {
                        
                        @Override
                        protected Void doInBackground() throws Exception {
                           Gui.post(new Request(ClientTasks.ConnectorConnect, new ConnectorInput(conOut.getId())));
                           return null;
                        }
                     }.execute();
                  }
               }
            }
         } else {
            showPopup(ev);
         }
      }
      
      @Override
      public void mousePressed(MouseEvent ev) {
         showPopup(ev);
      }
      
      @Override
      public void mouseReleased(MouseEvent ev) {
         showPopup(ev);
      }
      
      private void showPopup(MouseEvent ev) {
         if (ev.isPopupTrigger()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node != null) {
               if (node.getUserObject() instanceof MachineOut) {
                  JPopupMenu vmPopupMenu = PopupMenuBuilder.get(ServerMachineView.this, (MachineOut) node.getUserObject());
                  vmPopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
               }
               if (node.getUserObject() instanceof ConnectorOutput) {
                  ConnectorOutput conOut = (ConnectorOutput) node.getUserObject();
                  JPopupMenu conPopupMenu = PopupMenuBuilder.get(ServerMachineView.this, ServerMachineView.this, conOut);
                  conPopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
               }
            }
         }
      }
   }
   
   private class TreeSelectListener implements TreeSelectionListener {
      
      @Override
      public void valueChanged(TreeSelectionEvent ev) {
         if (ev.getNewLeadSelectionPath() != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) ev.getNewLeadSelectionPath().getLastPathComponent();
            
            if (node != null) {
               if (node.getUserObject() instanceof MachineOut) {
                  vSplit.setRightComponent(new JScrollPane(new VmDetailedView((MachineOut) node.getUserObject()).getComponent()));
               }
               if (node.getUserObject() instanceof ConnectorOutput) {
                  vSplit.setRightComponent(new JScrollPane(new ConnectorDetailedView((ConnectorOutput) node.getUserObject()).getComponent()));
               }
            } else {
               vSplit.setRightComponent(EmptyPanel);
            }
         } else {
            vSplit.setRightComponent(EmptyPanel);
         }
      }
      
   }
   
   private class MachineListReceiver extends WorkerDataReceiver implements _MachineListReceiver {
      
      @Override
      public void add(List<MachineOut> objOutList) {
         for (MachineOut mOut : objOutList) {
            addMachine(mOut.getServerId(), mOut);
         }
      }
      
   }
   
   private class SnapshotGetReceiver extends WorkerDataReceiver implements _SnapshotGetReceiver {
      
      @Override
      public void put(String srvId, String vmId, SnapshotOut snapOut) {
         vmCurrentSnaps.put(vmId, snapOut);
         treeModel.reload(vmNodes.get(srvId).get(vmId));
      }
      
   }
   
}
