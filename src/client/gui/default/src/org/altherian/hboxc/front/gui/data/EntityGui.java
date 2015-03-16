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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.front.gui.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class EntityGui implements MutableTreeNode {

   private String typeId;
   private String id;
   private boolean isGeneric;

   private EntityGui parent;
   private List<EntityGui> childs = new ArrayList<EntityGui>();

   public EntityGui(String typeId, String id, boolean isGeneric) {
      this.typeId = typeId;
      this.id = id;
      this.isGeneric = isGeneric;
   }

   public EntityGui(String typeId, String id) {
      this(typeId, id, false);
   }

   @Override
   public String toString() {
      return typeId + " #" + id;
   }

   /**
    * @return the typeId
    */
   public String getEntityTypeId() {
      return typeId;
   }

   /**
    * @return the id
    */
   public String getId() {
      return id;
   }

   public boolean isGeneric() {
      return isGeneric;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((id == null) ? 0 : id.hashCode());
      result = (prime * result) + ((typeId == null) ? 0 : typeId.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof EntityGui)) {
         return false;
      }
      EntityGui other = (EntityGui) obj;
      if (id == null) {
         if (other.id != null) {
            return false;
         }
      } else if (!id.equals(other.id)) {
         return false;
      }
      if (typeId == null) {
         if (other.typeId != null) {
            return false;
         }
      } else if (!typeId.equals(other.typeId)) {
         return false;
      }
      return true;
   }

   /*--------------------- TreeNode methods ---------------------*/
   @Override
   public EntityGui getChildAt(int childIndex) {
      return childs.get(childIndex);
   }

   @Override
   public int getChildCount() {
      return childs.size();
   }

   @Override
   public EntityGui getParent() {
      return parent;
   }

   @Override
   public int getIndex(TreeNode node) {
      return childs.indexOf(node);
   }

   @Override
   public boolean getAllowsChildren() {
      return true;
   }

   @Override
   public boolean isLeaf() {
      return childs.isEmpty();
   }

   @Override
   public Enumeration<EntityGui> children() {
      return Collections.enumeration(childs);
   }

   /*--------------------- Custom Node methods ---------------------*/
   public void setParent(EntityGui parent) {
      this.parent = parent;
   }

   public void add(EntityGui child) {
      childs.add(childs.size(), child);
   }

   public void update(EntityGui child) {
      childs.add(childs.indexOf(child), child);
   }

   public boolean findAndUpdate(EntityGui potentialChild) {
      if (childs.contains(potentialChild)) {
         update(potentialChild);
         return true;
      } else {
         if (isLeaf()) {
            return false;
         }
         for (EntityGui child : childs) {
            if (child.findAndUpdate(potentialChild)) {
               return true;
            }
         }
         return false;
      }
   }

   public void remove(EntityGui child) {
      childs.remove(child);
   }

   public void remove(String id) {
      childs.remove(id);
   }

   public void removeAll() {
      childs.clear();
   }

   /*--------------------- MutableTreeNode methods ---------------------*/
   @Override
   public void insert(MutableTreeNode child, int index) {
      childs.add(index, (EntityGui) child);
   }

   @Override
   public void remove(int index) {
      childs.remove(index);
   }

   @Override
   public void remove(MutableTreeNode node) {
      remove((EntityGui) node);
   }

   @Override
   public void setUserObject(Object object) {
      // stub
   }

   @Override
   public void removeFromParent() {
      if (getParent() != null) {
         getParent().remove(this);
      }
   }

   @Override
   public void setParent(MutableTreeNode newParent) {
      setParent((EntityGui) newParent);
   }

}
