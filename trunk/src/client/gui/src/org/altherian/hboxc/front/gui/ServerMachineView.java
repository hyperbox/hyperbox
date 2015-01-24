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
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.SnapshotIn;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorConnectedEventOut;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorDisconnectedEventOut;
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
import org.altherian.hboxc.front.gui.utils.MachineOutputComparator;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.hboxc.front.gui.vm._MachineSelector;
import org.altherian.hboxc.front.gui.vm.view.VmDetailedView;
import org.altherian.tool.logging.Logger;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public final class ServerMachineView implements _MachineSelector, _ServerSelector, _ConnectorSelector, _Refreshable {

   private static final JPanel EmptyPanel = new JPanel();

   private DefaultMutableTreeNode topNode;
   private DefaultTreeModel treeModel;
   private JTree tree;
   private JScrollPane treeView;

   private Map<String, Map<String, DefaultMutableTreeNode>> entities;

   private Map<String, SnapshotOut> vmCurrentSnaps = new HashMap<String, SnapshotOut>();
   private Map<String, Map<String, DefaultMutableTreeNode>> vmNodes = new HashMap<String, Map<String, DefaultMutableTreeNode>>();
   private Map<String, DefaultMutableTreeNode> srvNodes = new HashMap<String, DefaultMutableTreeNode>();
   private Map<String, DefaultMutableTreeNode> conNodes = new HashMap<String, DefaultMutableTreeNode>();

   private JSplitPane vSplit;

   public ServerMachineView() {
      Logger.track();

      entities = new HashMap<String, Map<String, DefaultMutableTreeNode>>();
      initEntities();
      vmCurrentSnaps = new HashMap<String, SnapshotOut>();

      topNode = new DefaultMutableTreeNode("Hyperbox");
      treeModel = new DefaultTreeModel(topNode);
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
      vSplit.setDividerLocation(Integer.parseInt(PreferencesManager.get(ServerMachineView.class.getName()).getProperty(
            JSplitPane.DIVIDER_LOCATION_PROPERTY, "168")));
      vSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {

         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            PreferencesManager.get(this.getClass().getName()).setProperty(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt.getNewValue().toString());
         }
      });

      RefreshUtil.set(vSplit, this);
      FrontEventManager.register(this);
   }

   private void initEntities() {
      entities.put(EntityType.Server.getId(), new HashMap<String, DefaultMutableTreeNode>());
      entities.put(EntityType.Machine.getId(), new HashMap<String, DefaultMutableTreeNode>());
      entities.put(EntityType.Snapshot.getId(), new HashMap<String, DefaultMutableTreeNode>());
      entities.put(EntityType.Store.getId(), new HashMap<String, DefaultMutableTreeNode>());
   }

   public JComponent getComponent() {
      return vSplit;
   }

   private void clear() {
      Logger.track();
      
      entities.clear();
      topNode.removeAllChildren();
      treeModel.reload();
   }

   @Override
   public void refresh() {
      Logger.track();

      List<ConnectorOutput> conOutList = Gui.getReader().listConnectors();
      refresh(conOutList);
   }

   private void refresh(List<ConnectorOutput> conOutList) {
      Logger.track();

      clear();
      for (ConnectorOutput conOut : conOutList) {
         addConnector(conOut);
      }
   }

   private void refresh(ConnectorOutput conOut) {
      Logger.track();

      DefaultMutableTreeNode conNode = conNodes.get(conOut.getId());
      conNode.removeAllChildren();
      treeModel.reload(conNode);
      if (conOut.isConnected()) {
         srvNodes.put(conOut.getServer().getId(), conNode);
         vmNodes.put(conOut.getServer().getId(), new HashMap<String, DefaultMutableTreeNode>());
         refresh(conOut.getServer());
      }
   }

   private void refresh(ServerOut srvOut) {
      Logger.track();

      DefaultMutableTreeNode srvNode = srvNodes.get(srvOut.getId());
      srvNode.removeAllChildren();
      vmNodes.get(srvOut.getId()).clear();
      treeModel.reload(srvNode);
      if (srvOut.isHypervisorConnected()) {
         Logger.debug(srvOut.getName() + " is connected, listing VMs...");
         List<MachineOut> mOutList = Gui.getServer(srvOut).listMachines();
         Logger.debug("Got " + mOutList.size() + " machines to display");
         Collections.sort(mOutList, new MachineOutputComparator());
         for (MachineOut mOut : mOutList) {
            addMachine(srvOut.getId(), mOut);
         }
      } else {
         Logger.debug(srvOut.getName() + " is not connected, skipping VM listing");
      }
   }

   private void addConnector(ConnectorOutput conOut) {
      Logger.track();

      DefaultMutableTreeNode conNode = new DefaultMutableTreeNode(conOut);
      treeModel.insertNodeInto(conNode, topNode, topNode.getChildCount());
      tree.scrollPathToVisible(new TreePath(conNode.getPath()));
      conNodes.put(conOut.getId(), conNode);
      refresh(conOut);
   }

   private void updateConnector(final ConnectorOutput conOut) {
      Logger.track();
      
      DefaultMutableTreeNode conNode = conNodes.get(conOut.getId());
      conNode.setUserObject(conOut);
      treeModel.reload(conNode);
   }

   private void updateServer(ServerOut srvOut) {
      Logger.track();

      DefaultMutableTreeNode conNode = srvNodes.get(srvOut.getId());
      updateConnector(Gui.getReader().getConnector(((ConnectorOutput) conNode.getUserObject()).getId()));
   }

   private void removeConnector(final ConnectorOutput conOut) {
      Logger.track();
      
      topNode.remove(conNodes.remove(conOut.getId()));
      treeModel.reload(topNode);
   }

   private void addMachine(final String serverId, final MachineOut mOut) {
      Logger.track();
      
      if (!vmNodes.get(serverId).containsKey(mOut.getId())) {
         DefaultMutableTreeNode srvNode = srvNodes.get(serverId);
         DefaultMutableTreeNode node = new DefaultMutableTreeNode(mOut);
         treeModel.insertNodeInto(node, srvNode, srvNode.getChildCount());
         tree.scrollPathToVisible(new TreePath(node.getPath()));
         vmNodes.get(serverId).put(mOut.getId(), node);
         if (mOut.isAvailable() && mOut.hasSnapshots()) {
            SnapshotOut snapOut = Gui.getServer(serverId).getSnapshot(new MachineIn(mOut), new SnapshotIn(mOut.getCurrentSnapshot()));
            vmCurrentSnaps.put(mOut.getId(), snapOut);
         }
         treeModel.reload(node);
      }
   }

   private void updateMachine(final String serverId, final MachineOut mOut) {
      Logger.track();
      
      if (vmNodes.get(serverId).containsKey(mOut.getId())) {
         if (mOut.hasSnapshots()) {
            SnapshotOut snapOut = Gui.getServer(serverId).getSnapshot(new MachineIn(mOut), new SnapshotIn(mOut.getCurrentSnapshot()));
            vmCurrentSnaps.put(mOut.getId(), snapOut);
         } else {
            vmCurrentSnaps.remove(mOut.getId());
         }
         DefaultMutableTreeNode node = vmNodes.get(serverId).get(mOut.getId());
         node.setUserObject(mOut);
         treeModel.reload(node);
      }
   }

   private void removeMachine(final String serverId, final String id) {
      Logger.track();
      
      if (vmNodes.get(serverId).containsKey(id)) {
         vmCurrentSnaps.remove(id);
         treeModel.removeNodeFromParent(vmNodes.get(serverId).remove(id));
      } else {
         Logger.warning("Trying to remove machine not in the view: " + serverId + " - " + id);
      }
   }

   @Handler
   public void putHypervisorConnected(HypervisorConnectedEventOut ev) {
      Logger.track();

      updateServer(ev.getServer());
      refresh(ev.getServer());
   }

   @Handler
   public void putHypervisorDisconnected(HypervisorDisconnectedEventOut ev) {
      Logger.track();

      updateServer(ev.getServer());
      refresh(ev.getServer());
   }

   @Handler
   public void putServerConnectionStateChange(ServerConnectionStateEventOut ev) {
      Logger.track();

      updateServer(ev.getServer());
      refresh(ev.getServer());
   }

   @Handler
   public void putMachineUpdate(MachineUpdatedEvent ev) {
      Logger.track();

      updateMachine(ev.getServerId(), ev.getMachine());
   }

   @Handler
   public void putMachineUpdate(final MachineStateChangedEvent ev) {
      Logger.track();

      updateMachine(ev.getServerId(), ev.getMachine());
   }

   @Handler
   public void putMachineRegistration(final MachineRegistrationEventOut ev) {
      Logger.track();

      if (ev.isRegistered()) {
         addMachine(ev.getServerId(), ev.getMachine());
      } else {
         removeMachine(ev.getServerId(), ev.getMachine().getId());
      }
   }

   @Handler
   public void putMachineAdd(final MachineAddedEvent ev) {
      Logger.track();

      addMachine(ev.getServerId(), ev.getMachine());
   }

   @Handler
   public void putMachineRemove(final MachineRemovedEvent ev) {
      Logger.track();

      removeMachine(ev.getServerId(), ev.getMachine().getUuid());
   }

   @Handler
   public void putConnectorAdded(final ConnectorAddedEvent ev) {
      Logger.track();

      addConnector(ev.getConnector());
   }

   @Handler
   public void putConnectorModified(final ConnectorModifiedEvent ev) {
      Logger.track();

      updateConnector(ev.getConnector());
   }

   @Handler
   public void putConnectorRemoved(ConnectorRemovedEvent ev) {
      Logger.track();

      removeConnector(ev.getConnector());
   }

   @Handler
   public void putConnectorConnected(ConnectorConnectedEvent ev) {
      Logger.track();

      updateConnector(ev.getConnector());
      refresh(ev.getConnector());
   }

   @Handler
   public void putConnectorDisconnected(ConnectorDisconnectedEvent ev) {
      Logger.track();

      updateConnector(ev.getConnector());
      refresh(ev.getConnector());
   }

   @Handler
   public void putConnectorStateChanged(ConnectorStateChangedEvent ev) {
      Logger.track();

      updateConnector(ev.getConnector());
   }

   @Override
   public List<MachineOut> getMachines() {
      Logger.track();

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

   private class TreeMouseListener extends MouseAdapter {

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
                     setText("<Unavailable>");
                     setIcon(IconBuilder.getMachineState(MachineStates.Inaccessible));
                  } else {
                     try {
                        setIcon(IconBuilder.getMachineState(MachineStates.valueOf(simpleVmOut.getState())));
                     } catch (Throwable t) {
                        setIcon(IconBuilder.getMachineState(MachineStates.Unknown));
                     }
                     try {
                        if (simpleVmOut.hasSnapshots()) {
                           setText(simpleVmOut.getName() + " (" + vmCurrentSnaps.get(simpleVmOut.getId()).getName() + ")");
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

}
