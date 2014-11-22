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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui;

import org.altherian.hbox.constant.EntityType;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.data.EntityGui;
import org.altherian.hboxc.front.gui.data.generic.HyperboxGui;
import org.altherian.hboxc.front.gui.data.generic.MachinesGui;
import org.altherian.hboxc.front.gui.data.generic.ServersGui;
import org.altherian.hboxc.front.gui.data.generic.SnapshotsGui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class EntityGenericTreeView extends JPanel implements _View {
   
   private EntityTreeModel treeModel;
   private JTree tree;
   private JScrollPane treeView;
   
   public EntityGenericTreeView(EntityGui rootNode) {
      if (!rootNode.isGeneric()) {
         throw new IllegalArgumentException("Root node must be a generic entity");
      }
      
      init(rootNode);
   }
   
   public EntityGenericTreeView() {
      this(new HyperboxGui());
   }
   
   private void init(EntityGui rootNode) {
      treeModel = new EntityTreeModel(rootNode);
      tree = new JTree(treeModel);
      tree.setCellRenderer(new TreeCellRenderer());
      treeView = new JScrollPane(tree);
      treeView.setBorder(BorderFactory.createEmptyBorder());
      
      add(treeView);
      
      load((EntityGui) treeModel.getRoot());
   }
   
   private void load(EntityGui currentNode) {
      if (EntityType.Hyperbox.match(currentNode.getEntityTypeId())) {
         treeModel.insertNode(new ServersGui(), currentNode);
         tree.scrollPathToVisible(new TreePath(currentNode.getChildAt(0)));
      }
      if (EntityType.Server.match(currentNode.getEntityTypeId())) {
         treeModel.insertNode(new MachinesGui(), currentNode);
      }
      if (EntityType.Machine.match(currentNode.getEntityTypeId())) {
         treeModel.insertNode(new SnapshotsGui(), currentNode);
      }
   }
   
   @Override
   public JPanel getPanel() {
      return this;
   }
   
   private class TreeCellRenderer extends DefaultTreeCellRenderer {
      
      @Override
      public Component getTreeCellRendererComponent(JTree rawTree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row,
            boolean hasFocus) {
         super.getTreeCellRendererComponent(rawTree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
         EntityGui realValue = (EntityGui) value;
         
         setIcon(IconBuilder.getEntityType(realValue.getEntityTypeId()));
         
         return this;
      }
      
   }
   
}
