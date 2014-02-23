package org.altherian.hboxd.security;

import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;

public interface _ActionPermission {
   
   public String getUserId();

   public SecurityItem getItemType();
   
   public SecurityAction getAction();
   
   public boolean isAllowed();
   
}
