package org.altherian.hboxd.security;

import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;

public class ItemPermission extends ActionPermission implements _ItemPermission {
   
   private String itemId;
   
   public ItemPermission(String userId, SecurityItem item, SecurityAction action, String itemId, boolean isAllowed) {
      super(userId, item, action, isAllowed);
      this.itemId = itemId;
   }
   
   @Override
   public String getItemId() {
      return itemId;
   }
   
}
