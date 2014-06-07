package org.altherian.hboxd.factory;

import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;
import org.altherian.hboxd.security.ItemPermission;
import org.altherian.hboxd.security._ItemPermission;

public class ItemPermissionFactory {
   
   private ItemPermissionFactory() {
   }
   
   public static _ItemPermission get(String userId, String itemTypeId, String actionId, String itemId, boolean isAllowed) {
      return new ItemPermission(userId, SecurityItem.valueOf(itemTypeId), SecurityAction.valueOf(actionId), itemId, isAllowed);
   }
   
}
