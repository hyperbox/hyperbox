/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.helper.swing;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;


@SuppressWarnings("serial")
public class SortedTreeModel extends DefaultTreeModel {
   
   public static final int ASCENDING = -1;
   public static final int DESCENDING = 1;
   public static final int UNSORTED = 0;
   
   private int sorting = ASCENDING;
   
   public SortedTreeModel(TreeNode root) {
      super(root);
   }
   
   public SortedTreeModel(TreeNode root, int sorting) {
      this(root);
      setSorting(sorting);
   }

   public SortedTreeModel(TreeNode root, boolean asksAllowsChildren) {
      super(root, asksAllowsChildren);
   }
   
   public void insertNode(DefaultMutableTreeNode newChild, DefaultMutableTreeNode parent) {
      int insertIndex = parent.getChildCount();
      if (sorting != UNSORTED) {
         for (int i = 0; i < parent.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
            int compare = child.getUserObject().toString().compareTo(newChild.getUserObject().toString()) * sorting;
            if (compare < 0) {
               insertIndex = i;
               break;
            }
         }
      }
      insertNodeInto(newChild, parent, insertIndex);
   }

   public void setSorting(int sorting) {
      this.sorting = sorting;
   }
   
   public int getSorting() {
      return sorting;
   }
   
}
