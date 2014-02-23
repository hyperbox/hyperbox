package org.altherian.hboxd.core.action.security;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hboxd.comm.io.factory.PermissionIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.security.SecurityContext;
import org.altherian.hboxd.security._ActionPermission;
import org.altherian.hboxd.security._ItemPermission;
import org.altherian.hboxd.security._User;
import org.altherian.hboxd.session.SessionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionListAction extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.HBOX.getId() + HyperboxTasks.PermissionList.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      List<_ActionPermission> actionPermList = new ArrayList<_ActionPermission>();
      List<_ItemPermission> itemPermList = new ArrayList<_ItemPermission>();
      _User usr = SecurityContext.getUser();
      
      if (request.has(UserInput.class)) {
         UserInput usrIn = request.get(UserInput.class);
         usr = hbox.getSecurityManager().getUser(usrIn.getId());
         
         actionPermList.addAll(hbox.getSecurityManager().listActionPermissions(usr));
         itemPermList.addAll(hbox.getSecurityManager().listItemPermissions(usr));
      } else {
         actionPermList.addAll(hbox.getSecurityManager().listActionPermissions());
         itemPermList.addAll(hbox.getSecurityManager().listItemPermissions());
      }
      
      for (_ActionPermission perm : actionPermList) {
         SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, PermissionIoFactory.get(usr, perm)));
      }
      for (_ItemPermission perm : itemPermList) {
         SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, PermissionIoFactory.get(usr, perm)));
      }
   }
   
}
