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

package org.altherian.hboxc.front.gui;

import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.SnapshotInput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorConnectedEventOutput;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorDisconnectedEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineRegistrationEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineStateEventOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.SnapshotOutput;
import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxc.PreferencesManager;
import org.altherian.hboxc.comm.input.ConnectorInput;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.event.connector.ConnectorAddedEvent;
import org.altherian.hboxc.event.connector.ConnectorConnectedEvent;
import org.altherian.hboxc.event.connector.ConnectorDisconnectedEvent;
import org.altherian.hboxc.event.connector.ConnectorModifiedEvent;
import org.altherian.hboxc.event.connector.ConnectorRemovedEvent;
import org.altherian.hboxc.event.connector.ConnectorStateChangedEvent;
import org.altherian.hboxc.event.machine.MachineAddedEvent;
import org.altherian.hboxc.event.machine.MachineRemovedEvent;
import org.altherian.hboxc.event.machine.MachineUpdatedEvent;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.builder.PopupMenuBuilder;
import org.altherian.hboxc.front.gui.connector.ConnectorDetailedView;
import org.altherian.hboxc.front.gui.connector._ConnectorSelector;
import org.altherian.hboxc.front.gui.server.ServerViewer;
import org.altherian.hboxc.front.gui.server._ServerSelector;
import org.altherian.hboxc.front.gui.utils.MachineOutputComparator;
import org.altherian.hboxc.front.gui.utils.RefreshUtil;
import org.altherian.hboxc.front.gui.vm._VmSelector;
import org.altherian.hboxc.front.gui.vm.view.VmDetailedView;
import org.altherian.tool.logging.Logger;

import java.awt.CardLayout;
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

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public final class InfrastructureView implements _VmSelector, _ServerSelector, _ConnectorSelector, _Refreshable {
   
   private static final String EmptyPanelId = "empty";
   private static final JPanel EmptyPanel = new JPanel();
   
   private DefaultMutableTreeNode topNode;
   private DefaultTreeModel treeModel;
   private JTree tree;
   private JScrollPane treeView;
   
   private Map<String, SnapshotOutput> vmCurrentSnaps;
   private Map<String, Map<String, DefaultMutableTreeNode>> vmNodes;
   private Map<String, DefaultMutableTreeNode> srvNodes;
   private Map<String, DefaultMutableTreeNode> conNodes;
   
   private VmDetailedView vmView;
   private ServerViewer srvView;
   private ConnectorDetailedView conView;
   private CardLayout detailLayout;
   private JPanel detailViews;
   
   private JSplitPane vSplit;
   
   public InfrastructureView() {
      Logger.track();
      
      vmCurrentSnaps = new HashMap<String, SnapshotOutput>();
      vmNodes = new HashMap<String, Map<String, DefaultMutableTreeNode>>();
      srvNodes = new HashMap<String, DefaultMutableTreeNode>();
      conNodes = new HashMap<String, DefaultMutableTreeNode>();
      
      topNode = new DefaultMutableTreeNode("Hyperbox");
      treeModel = new DefaultTreeModel(topNode);
      tree = new JTree(treeModel);
      tree.setCellRenderer(new TreeCellRenderer());
      tree.addMouseListener(new TreeMouseListener());
      tree.addTreeSelectionListener(new TreeSelectListener());
      tree.setRootVisible(false);
      tree.setShowsRootHandles(true);
      treeView = new JScrollPane(tree);
      
      vmView = new VmDetailedView();
      srvView = new ServerViewer();
      conView = new ConnectorDetailedView();
      
      detailLayout = new CardLayout();
      detailViews = new JPanel(detailLayout);
      detailViews.add(EmptyPanel, EmptyPanelId);
      detailViews.add(conView.getComponent(), EntityTypes.Connector.getId());
      detailViews.add(vmView.getComponent(), EntityTypes.Machine.getId());
      detailViews.add(srvView.getComponent(), EntityTypes.Server.getId());
      
      vSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, detailViews);
      vSplit.setResizeWeight(0);
      vSplit.setDividerLocation(Integer.parseInt(PreferencesManager.get(InfrastructureView.class.getName()).getProperty(
            JSplitPane.DIVIDER_LOCATION_PROPERTY, "168")));
      vSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
         
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            PreferencesManager.get(InfrastructureView.class.getName()).setProperty(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt.getNewValue().toString());
         }
      });
      
      RefreshUtil.set(vSplit, this);
      FrontEventManager.register(this);
   }
   
   @Override
   public String getId() {
      return "VM and Servers";
   }
   
   public JComponent getComponent() {
      return vSplit;
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
         vmNodes.clear();
         srvNodes.clear();
         conNodes.clear();
         
         topNode.removeAllChildren();
         treeModel.reload();
      }
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
         add(conOut);
      }
   }
   
   private void refresh(final ConnectorOutput conOut) {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refresh(conOut);
            }
         });
      } else {
         DefaultMutableTreeNode conNode = conNodes.get(conOut.getId());
         conNode.removeAllChildren();
         treeModel.reload(conNode);
         if (conOut.isConnected()) {
            srvNodes.put(conOut.getServer().getId(), conNode);
            vmNodes.put(conOut.getServer().getId(), new HashMap<String, DefaultMutableTreeNode>());
            refresh(conOut.getServer());
         }
      }
      
   }
   
   private void refresh(final ServerOutput srvOut) {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               refresh(srvOut);
            }
         });
      } else {
         DefaultMutableTreeNode srvNode = srvNodes.get(srvOut.getId());
         srvNode.removeAllChildren();
         vmNodes.get(srvOut.getId()).clear();
         treeModel.reload(srvNode);
         if (srvOut.isHypervisorConnected()) {
            Logger.debug(srvOut.getName() + " is connected, listing VMs...");
            List<MachineOutput> mOutList = Gui.getServer(srvOut).listMachines();
            Logger.debug("Got " + mOutList.size() + " machines to display");
            Collections.sort(mOutList, new MachineOutputComparator());
            for (MachineOutput mOut : mOutList) {
               add(srvOut.getId(), mOut);
            }
         } else {
            Logger.debug(srvOut.getName() + " is not connected, skipping VM listing");
         }
      }
      
   }
   
   private void add(final ConnectorOutput conOut) {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               add(conOut);
            }
         });
      } else {
         DefaultMutableTreeNode conNode = new DefaultMutableTreeNode(conOut);
         treeModel.insertNodeInto(conNode, topNode, topNode.getChildCount());
         tree.scrollPathToVisible(new TreePath(conNode.getPath()));
         conNodes.put(conOut.getId(), conNode);
         refresh(conOut);
      }
      
   }
   
   private void update(final ConnectorOutput conOut) {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               update(conOut);
            }
         });
      } else {
         DefaultMutableTreeNode conNode = conNodes.get(conOut.getId());
         conNode.setUserObject(conOut);
         treeModel.reload(conNode);
      }
      
   }
   
   private void update(ServerOutput srvOut) {
      Logger.track();
      
      DefaultMutableTreeNode conNode = srvNodes.get(srvOut.getId());
      update(Gui.getReader().getConnector(((ConnectorOutput) conNode.getUserObject()).getId()));
   }
   
   private void remove(final ConnectorOutput conOut) {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               remove(conOut);
            }
         });
      } else {
         topNode.remove(conNodes.remove(conOut.getId()));
         treeModel.reload(topNode);
      }
      
   }
   
   private void add(final String serverId, final MachineOutput mOut) {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               add(serverId, mOut);
            }
         });
      } else {
         if (!vmNodes.get(serverId).containsKey(mOut.getId())) {
            if (!SwingUtilities.isEventDispatchThread()) {
               Thread.dumpStack();
            }
            DefaultMutableTreeNode srvNode = srvNodes.get(serverId);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(mOut);
            treeModel.insertNodeInto(node, srvNode, srvNode.getChildCount());
            tree.scrollPathToVisible(new TreePath(node.getPath()));
            vmNodes.get(serverId).put(mOut.getId(), node);
            if (mOut.hasSnapshots()) {
               SnapshotOutput snapOut = Gui.getServer(serverId).getSnapshot(new MachineInput(mOut), new SnapshotInput(mOut.getCurrentSnapshot()));
               vmCurrentSnaps.put(mOut.getId(), snapOut);
            }
            treeModel.reload(node);
         }
      }
      
   }
   
   private void update(final String serverId, final MachineOutput mOut) {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               update(serverId, mOut);
            }
         });
      } else {
         if (vmNodes.get(serverId).containsKey(mOut.getId())) {
            if (mOut.hasSnapshots()) {
               SnapshotOutput snapOut = Gui.getServer(serverId).getSnapshot(new MachineInput(mOut), new SnapshotInput(mOut.getCurrentSnapshot()));
               vmCurrentSnaps.put(mOut.getId(), snapOut);
            } else {
               vmCurrentSnaps.remove(mOut.getId());
            }
            DefaultMutableTreeNode node = vmNodes.get(serverId).get(mOut.getId());
            node.setUserObject(mOut);
            treeModel.reload(node);
         }
      }
      
   }
   
   private void removeMachine(final String serverId, final String id) {
      Logger.track();
      
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               removeMachine(serverId, id);
            }
         });
      } else {
         if (vmNodes.get(serverId).containsKey(id)) {
            try {
               vmCurrentSnaps.remove(id);
               treeModel.removeNodeFromParent(vmNodes.get(serverId).remove(id));
            } catch (IllegalArgumentException e) {
               // It is possible that the node is already removed if we get two events in a short period of time (VM unregistered and VM deleted).
               // Ignoring this exception until a better implementation is found
               // TODO implement better Swing by using dispatcher thread everywhere
            }
            
         }
      }
      
   }
   
   @Handler
   public void putHypervisorConnected(final HypervisorConnectedEventOutput ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            update(ev.getServer());
            refresh(ev.getServer());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putHypervisorDisconnected(final HypervisorDisconnectedEventOutput ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            update(ev.getServer());
            refresh(ev.getServer());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putMachineUpdate(final MachineUpdatedEvent ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            update(ev.getServerId(), ev.getMachine());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putMachineUpdate(final MachineStateEventOutput ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            update(ev.getServerId(), ev.getMachine());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putMachineRegistration(final MachineRegistrationEventOutput ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            if (ev.isRegistered()) {
               add(ev.getServerId(), ev.getMachine());
            } else {
               removeMachine(ev.getServerId(), ev.getMachine().getId());
            }
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putMachineAdd(final MachineAddedEvent ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            add(ev.getServerId(), ev.getMachine());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putMachineRemove(final MachineRemovedEvent ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            removeMachine(ev.getServerId(), ev.getMachine().getId());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putConnectorAdded(final ConnectorAddedEvent ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            add(ev.getConnector());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putConnectorModified(final ConnectorModifiedEvent ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            update(ev.getConnector());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putConnectorRemoved(final ConnectorRemovedEvent ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            remove(ev.getConnector());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putConnectorConnected(final ConnectorConnectedEvent ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            update(ev.getConnector());
            refresh(ev.getConnector());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putConnectorDisconnected(final ConnectorDisconnectedEvent ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            update(ev.getConnector());
            refresh(ev.getConnector());
            return null;
         }
         
      }.execute();
   }
   
   @Handler
   public void putConnectorStateChanged(final ConnectorStateChangedEvent ev) {
      Logger.track();
      
      new SwingWorker<Void, Void>() {
         
         @Override
         protected Void doInBackground() throws Exception {
            update(ev.getConnector());
            return null;
         }
         
      }.execute();
   }
   
   @Override
   public List<MachineOutput> getMachines() {
      Logger.track();
      
      List<MachineOutput> selectedVms = new ArrayList<MachineOutput>();
      for (TreePath path : tree.getSelectionPaths()) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
         if (node.getUserObject() instanceof MachineOutput) {
            selectedVms.add((MachineOutput) node.getUserObject());
         }
      }
      return selectedVms;
   }
   
   @Override
   public List<ServerOutput> getServers() {
      List<ServerOutput> selectedSrv = new ArrayList<ServerOutput>();
      for (TreePath path : tree.getSelectionPaths()) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
         if (node.getUserObject() instanceof ServerOutput) {
            selectedSrv.add((ServerOutput) node.getUserObject());
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
               if (node.getUserObject() instanceof MachineOutput) {
                  JPopupMenu vmPopupMenu = PopupMenuBuilder.get(InfrastructureView.this, (MachineOutput) node.getUserObject());
                  vmPopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
               }
               if (node.getUserObject() instanceof ConnectorOutput) {
                  ConnectorOutput conOut = (ConnectorOutput) node.getUserObject();
                  JPopupMenu conPopupMenu = PopupMenuBuilder.get(InfrastructureView.this, InfrastructureView.this, conOut);
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
               if (node.getUserObject() instanceof ConnectorOutput) {
                  final ConnectorOutput conOut = (ConnectorOutput) node.getUserObject();
                  if (!conOut.isConnected()) {
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
               if (node.getUserObject() instanceof MachineOutput) {
                  detailLayout.show(detailViews, EntityTypes.Machine.getId());
                  vmView.setUserSelection((MachineOutput) node.getUserObject());
               }
               if (node.getUserObject() instanceof ServerOutput) {
                  detailLayout.show(detailViews, EntityTypes.Server.getId());
                  srvView.show((ServerOutput) node.getUserObject());
               }
               if (node.getUserObject() instanceof ConnectorOutput) {
                  ConnectorOutput conOut = (ConnectorOutput) node.getUserObject();
                  Logger.debug("Connector ID " + conOut.getId() + " under label " + conOut.getLabel() + " is selected");
                  detailLayout.show(detailViews, EntityTypes.Connector.getId());
                  conView.show(conOut);
               }
            } else {
               detailLayout.show(detailViews, EmptyPanelId);
            }
         } else {
            detailLayout.show(detailViews, EmptyPanelId);
         }
      }
      
   }
   
   @SuppressWarnings("serial")
   private class TreeCellRenderer extends DefaultTreeCellRenderer {
      
      @Override
      public Component getTreeCellRendererComponent(JTree rawTree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
         super.getTreeCellRendererComponent(rawTree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
         if ((node != topNode) && (node.getUserObject() != null)) {
            if (node.getUserObject() instanceof MachineOutput) {
               try {
                  MachineOutput simpleVmOut = (MachineOutput) node.getUserObject();
                  setIcon(IconBuilder.getMachineState(MachineStates.valueOf(simpleVmOut.getState())));
                  try {
                     if (simpleVmOut.hasSnapshots()) {
                        setText(simpleVmOut.getName() + " (" + vmCurrentSnaps.get(simpleVmOut.getId()).getName() + ")");
                     } else {
                        setText(simpleVmOut.getName());
                     }
                  } catch (Throwable t) {
                     setText(simpleVmOut.getName());
                  }
               } catch (Throwable t) {
                  Logger.exception(t);
               }
            }
            else if (node.getUserObject() instanceof ConnectorOutput) {
               ConnectorOutput conOut = (ConnectorOutput) node.getUserObject();
               setIcon(IconBuilder.getConnector(conOut));
               if (conOut.isConnected()) {
                  setText(conOut.getServer().getName());
               } else {
                  setText(conOut.getLabel());
               }
            }
            else {
               Logger.warning("Unknown object: " + node.getUserObject().getClass().getName());
            }
         }
         
         return this;
      }
      
   }
   
   @Override
   public ServerOutput getServer() {
      try {
         Object o = ((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent()).getUserObject();
         if (o instanceof ConnectorOutput) {
            return ((ConnectorOutput) o).getServer();
         } else if (o instanceof ServerOutput) {
            return (ServerOutput) o;
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
