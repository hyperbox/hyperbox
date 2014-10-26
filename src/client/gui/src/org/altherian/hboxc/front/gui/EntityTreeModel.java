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

import org.altherian.hboxc.front.gui.data.EntityGui;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class EntityTreeModel extends DefaultTreeModel {
   
   public EntityTreeModel(TreeNode root) {
      super(root);
   }
   
   public EntityTreeModel(TreeNode root, boolean asksAllowsChildren) {
      super(root, asksAllowsChildren);
   }
   
   public void insertNode(EntityGui newChild, EntityGui parent) {
      parent.add(newChild);
   }
   
}
