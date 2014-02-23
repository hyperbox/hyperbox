package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.output.security.PermissionOutput;
import org.altherian.hboxd.security._ActionPermission;
import org.altherian.hboxd.security._ItemPermission;
import org.altherian.hboxd.security._User;

public class PermissionIoFactory {
   
   private PermissionIoFactory() {
      // static only
   }
   
   public static PermissionOutput get(_User usr, _ActionPermission perm) {
      return new PermissionOutput(usr.getId(), perm.getItemType().toString(), perm.getAction().toString(), perm.isAllowed());
   }
   
   public static PermissionOutput get(_User usr, _ItemPermission perm) {
      return new PermissionOutput(usr.getId(), perm.getItemType().toString(), perm.getAction().toString(), perm.getItemId(), perm.isAllowed());
   }
   
}
