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

package org.altherian.hbox.comm.output.security;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.ObjectOutput;
import org.altherian.hbox.constant.PermissionAttributes;

public class PermissionOutput extends ObjectOutput {
   
   private String userId;
   
   protected PermissionOutput() {
      // used for (de)serialisation
   }
   
   public PermissionOutput(String userId, String itemTypeId, String actionId, boolean isAllowed) {
      this.userId = userId;
      setSetting(new StringSettingIO(PermissionAttributes.ItemType, itemTypeId));
      setSetting(new StringSettingIO(PermissionAttributes.Action, actionId));
      setSetting(new BooleanSettingIO(PermissionAttributes.IsGranted, isAllowed));
   }
   
   public PermissionOutput(String userId, String itemTypeId, String actionId, String itemId, boolean isAllowed) {
      this(userId, itemTypeId, actionId, isAllowed);
      setSetting(new StringSettingIO(PermissionAttributes.Item, itemId));
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
      return getSetting(PermissionAttributes.ItemType).getString();
   }
   
   /**
    * @return the actionId
    */
   public String getActionId() {
      return getSetting(PermissionAttributes.Action).getString();
   }
   
   /**
    * @return the itemId
    */
   public String getItemId() {
      return getSetting(PermissionAttributes.Item).getString();
   }
   
   /**
    * @return the isAllowed
    */
   public boolean isAllowed() {
      return getSetting(PermissionAttributes.IsGranted).getBoolean();
   }
   
}
