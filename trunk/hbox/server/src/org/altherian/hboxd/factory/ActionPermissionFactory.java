package org.altherian.hboxd.factory;

import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;
import org.altherian.hboxd.security.ActionPermission;
import org.altherian.hboxd.security._ActionPermission;

public class ActionPermissionFactory {
   
   private ActionPermissionFactory() {
   }
   
   public static _ActionPermission get(String userId, String itemTypeId, String actionId, boolean isAllowed) {
      return new ActionPermission(userId, SecurityItem.valueOf(itemTypeId), SecurityAction.valueOf(actionId), isAllowed);
   }
   
}
