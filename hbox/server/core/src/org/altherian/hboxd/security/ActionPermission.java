package org.altherian.hboxd.security;

import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;

public class ActionPermission implements _ActionPermission {
   
   private String userId;
   private SecurityItem item;
   private SecurityAction action;
   private boolean isAllowed;
   
   public ActionPermission(String userId, SecurityItem item, SecurityAction action, boolean isAllowed) {
      this.userId = userId;
      this.item = item;
      this.action = action;
      this.isAllowed = isAllowed;
   }
   
   @Override
   public String getUserId() {
      return userId;
   }
   
   @Override
   public SecurityItem getItemType() {
      return item;
   }
   
   @Override
   public SecurityAction getAction() {
      return action;
   }
   
   @Override
   public boolean isAllowed() {
      return isAllowed;
   }
   
}
