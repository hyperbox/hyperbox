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

package org.altherian.hbox.comm.input;

import org.altherian.hbox.comm.output.security.PermissionOutput;

public class PermissionInput extends ObjectInput {
   
   private String userId;
   private String itemTypeId;
   private String actionId;
   private String itemId;
   private boolean isAllowed;
   
   public PermissionInput() {
   }
   
   public PermissionInput(String userId, String itemTypeId, String actionId, boolean isAllowed) {
      this.userId = userId;
      this.itemTypeId = itemTypeId;
      this.actionId = actionId;
      this.isAllowed = isAllowed;
   }
   
   public PermissionInput(String userId, String itemTypeId, String actionId, String itemId, boolean isAllowed) {
      this(userId, itemTypeId, actionId, isAllowed);
      this.itemId = itemId;
   }
   
   public PermissionInput(PermissionOutput permOut) {
      this(permOut.getUserId(), permOut.getItemTypeId(), permOut.getActionId(), permOut.getItemId(), permOut.isAllowed());
   }
   
   /**
    * @return the usrId
    */
   public String getUserId() {
      return userId;
   }
   
   /**
    * @param usrId the usrId to set
    */
   public void setUserId(String usrId) {
      this.userId = usrId;
   }
   
   /**
    * @return the itemTypeId
    */
   public String getItemTypeId() {
      return itemTypeId;
   }
   
   /**
    * @param itemTypeId the itemTypeId to set
    */
   public void setItemTypeId(String itemTypeId) {
      this.itemTypeId = itemTypeId;
   }
   
   /**
    * @return the actionId
    */
   public String getActionId() {
      return actionId;
   }
   
   /**
    * @param actionId the actionId to set
    */
   public void setActionId(String actionId) {
      this.actionId = actionId;
   }
   
   /**
    * @return the itemId
    */
   public String getItemId() {
      return itemId;
   }
   
   /**
    * @param itemId the itemId to set
    */
   public void setItemId(String itemId) {
      this.itemId = itemId;
   }
   
   /**
    * @return the isAllowed
    */
   public boolean isAllowed() {
      return isAllowed;
   }
   
   /**
    * @param isAllowed the isAllowed to set
    */
   public void setAllowed(boolean isAllowed) {
      this.isAllowed = isAllowed;
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((actionId == null) ? 0 : actionId.hashCode());
      result = (prime * result) + (isAllowed ? 1231 : 1237);
      result = (prime * result) + ((itemId == null) ? 0 : itemId.hashCode());
      result = (prime * result) + ((itemTypeId == null) ? 0 : itemTypeId.hashCode());
      result = (prime * result) + ((userId == null) ? 0 : userId.hashCode());
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
      if (!(obj instanceof PermissionInput)) {
         return false;
      }
      PermissionInput other = (PermissionInput) obj;
      if (actionId == null) {
         if (other.actionId != null) {
            return false;
         }
      } else if (!actionId.equals(other.actionId)) {
         return false;
      }
      if (isAllowed != other.isAllowed) {
         return false;
      }
      if (itemId == null) {
         if (other.itemId != null) {
            return false;
         }
      } else if (!itemId.equals(other.itemId)) {
         return false;
      }
      if (itemTypeId == null) {
         if (other.itemTypeId != null) {
            return false;
         }
      } else if (!itemTypeId.equals(other.itemTypeId)) {
         return false;
      }
      if (userId == null) {
         if (other.userId != null) {
            return false;
         }
      } else if (!userId.equals(other.userId)) {
         return false;
      }
      return true;
   }
   
}
