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

package org.altherian.hbox.comm.out.security;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.ObjectOut;
import org.altherian.hbox.constant.PermissionAttribute;

public class PermissionOut extends ObjectOut {
   
   private String userId;
   
   protected PermissionOut() {
      // used for (de)serialisation
   }
   
   public PermissionOut(String userId, String itemTypeId, String actionId, boolean isAllowed) {
      this.userId = userId;
      setSetting(new StringSettingIO(PermissionAttribute.ItemType, itemTypeId));
      setSetting(new StringSettingIO(PermissionAttribute.Action, actionId));
      setSetting(new BooleanSettingIO(PermissionAttribute.IsGranted, isAllowed));
   }
   
   public PermissionOut(String userId, String itemTypeId, String actionId, String itemId, boolean isAllowed) {
      this(userId, itemTypeId, actionId, isAllowed);
      setSetting(new StringSettingIO(PermissionAttribute.Item, itemId));
   }
   
   /**
    * @return the userId
    */
   public String getUserId() {
      return userId;
   }
   
   /**
    * @return the itemTypeId
    */
   public String getItemTypeId() {
      return getSetting(PermissionAttribute.ItemType).getString();
   }
   
   /**
    * @return the actionId
    */
   public String getActionId() {
      return getSetting(PermissionAttribute.Action).getString();
   }
   
   /**
    * @return the itemId
    */
   public String getItemId() {
      return getSetting(PermissionAttribute.Item).getString();
   }
   
   /**
    * @return the isAllowed
    */
   public boolean isAllowed() {
      return getSetting(PermissionAttribute.IsGranted).getBoolean();
   }
   
}
