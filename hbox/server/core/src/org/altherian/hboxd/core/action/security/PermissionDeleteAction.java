package org.altherian.hboxd.core.action.security;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;
import org.altherian.hbox.comm.input.PermissionInput;
import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.security._User;

import java.util.Arrays;
import java.util.List;

public class PermissionDeleteAction extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.HBOX.getId() + HyperboxTasks.PermissionDelete.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      UserInput usrIn = request.get(UserInput.class);
      PermissionInput permIn = request.get(PermissionInput.class);
      
      _User usr = hbox.getSecurityManager().getUser(usrIn.getId());
      SecurityItem itemType = SecurityItem.valueOf(permIn.getItemTypeId());
      SecurityAction action = SecurityAction.valueOf(permIn.getActionId());
      hbox.getSecurityManager().remove(usr, itemType, action, permIn.getItemId());
   }
   
}
