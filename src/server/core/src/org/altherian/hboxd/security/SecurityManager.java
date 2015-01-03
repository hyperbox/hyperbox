/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.security;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.event._Event;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.security.UserAddedEvent;
import org.altherian.hboxd.event.security.UserModifiedEvent;
import org.altherian.hboxd.event.security.UserRemovedEvent;
import org.altherian.hboxd.exception.security.AccessDeniedException;
import org.altherian.hboxd.exception.security.InvalidCredentialsException;
import org.altherian.hboxd.factory.SecurityUserFactory;
import org.altherian.hboxd.persistence._SecurityPersistor;
import org.altherian.security.PasswordEncryptionService;
import org.altherian.tool.AxStrings;
import org.altherian.tool.logging.Logger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityManager implements _SecurityManager {
   
   private static final String permSeparator = "/";
   private UserIdGenerator userIdGen;
   
   private _User superUsr;
   private Map<String, _User> users;
   private Map<String, _User> usernames;
   private _SecurityPersistor persistor;
   private Map<String, Boolean> perms;
   
   private String getPermissionId(_User usr, SecurityItem item, String itemId) {
      if (usr == null) {
         return "";
      }
      
      return usr.getId() + permSeparator + item + permSeparator + SecurityAction.Any + permSeparator + itemId;
   }
   
   private String getPermissionId(_User usr, SecurityItem item, SecurityAction action) {
      if (usr == null) {
         return "";
      }
      
      return usr.getId() + permSeparator + item + permSeparator + action;
   }
   
   private String getPermissionId(_User usr, SecurityItem item, SecurityAction action, String itemId) {
      return getPermissionId(usr, item, action) + permSeparator + itemId;
   }
   
   private void loadPerm(_User usr, _ActionPermission acPerm) {
      Logger.track();
      
      perms.put(getPermissionId(usr, acPerm.getItemType(), acPerm.getAction()), acPerm.isAllowed());
   }
   
   private void loadPerm(_User usr, _ItemPermission itemPerm) {
      Logger.track();
      
      perms.put(getPermissionId(usr, itemPerm.getItemType(), itemPerm.getAction(), itemPerm.getItemId()), itemPerm.isAllowed());
   }
   
   private void loadPerms(_User usr) {
      Logger.track();
      
      for (_ActionPermission perm : listActionPermissions(usr)) {
         loadPerm(usr, perm);
      }
      
      for (_ItemPermission perm : listItemPermissions(usr)) {
         loadPerm(usr, perm);
      }
   }
   
   @Override
   public _User init(_SecurityPersistor persistor) throws HyperboxException {
      Logger.track();
      
      this.persistor = persistor;
      superUsr = new SystemUser();
      return superUsr;
   }
   
   @Override
   public void start() throws HyperboxException {
      Logger.track();
      
      userIdGen = new UserIdGenerator();
      users = new HashMap<String, _User>();
      usernames = new HashMap<String, _User>();
      perms = new HashMap<String, Boolean>();
      
      List<_User> userList = persistor.listUsers();
      
      // We assume this is an empty database
      if (userList.isEmpty()) {
         try {
            _User u = SecurityUserFactory.get("0", "admin");
            persistor.insertUser(u);
            setUserPassword(u, "hyperbox".toCharArray());
            
            userList.add(u);
            Logger.verbose("Created initial account");
            
            set(u, SecurityItem.Any, SecurityAction.Any, true);
            Logger.verbose("Granted full priviledges to initial account");
         } catch (Throwable t) {
            Logger.error("Unable to create initial admin account");
            Logger.exception(t);
            throw new HyperboxException(t);
         }
      }
      
      for (_User u : userList) {
         users.put(u.getId(), u);
         usernames.put(u.getDomainLogonName(), u);
         loadPerms(u);
      }
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      userIdGen = null;
      users = null;
      usernames = null;
   }
   
   @Override
   public void authenticate(String login, char[] submitedPassword) {
      Logger.track();
      
      if (!usernames.containsKey(login)) {
         Logger.debug("Unknown login: " + login);
         throw new InvalidCredentialsException();
      }
      
      _User user = usernames.get(login);
      
      try {
         byte[] encryptSubmitPass = PasswordEncryptionService.getEncryptedPassword(submitedPassword, user.getSalt().getBytes());
         byte[] encryptPass = persistor.getUserPassword(user.getId());
         
         if (!Arrays.equals(encryptSubmitPass, encryptPass)) {
            Logger.debug("Invalid password for user " + user.getDomainLogonName());
            throw new InvalidCredentialsException();
         }
      } catch (NoSuchAlgorithmException e) {
         throw new HyperboxRuntimeException("Unable to authenticate, internal error - " + e.getMessage(), e);
      } catch (InvalidKeySpecException e) {
         throw new HyperboxRuntimeException("Unable to authenticate, internal error - " + e.getMessage(), e);
      }
      
      SecurityContext.setUser(this, user);
   }
   
   @Override
   public void authorize(Request req) {
      Logger.track();
      
      // TODO Auto-generated method stub
   }
   
   @Override
   public boolean isAuthorized(_User u, _Event ev) {
      Logger.track();
      
      // TODO complete
      return true;
   }
   
   private boolean isAuthorized() {
      String permId = getPermissionId(SecurityContext.getUser(), SecurityItem.Any, SecurityAction.Any);
      Logger.debug("Checking for permission ID " + permId);
      Logger.debug("Possible values:");
      for (String key : perms.keySet()) {
         Logger.debug(key);
      }
      Logger.debug("-----------------------");
      return perms.containsKey(permId) && perms.get(permId);
   }
   
   private boolean isAuthorized(SecurityItem item) {
      if (isAuthorized()) {
         return true;
      }
      
      String permId = getPermissionId(SecurityContext.getUser(), item, SecurityAction.Any);
      Logger.debug("Checking for permission ID " + permId);
      return perms.containsKey(permId) && perms.get(permId);
   }
   
   private boolean isAuthorized(SecurityItem item, String itemId) {
      if (isAuthorized(item)) {
         return true;
      }
      
      String permId = getPermissionId(SecurityContext.getUser(), item, itemId);
      Logger.debug("Checking for permission ID " + permId);
      return perms.containsKey(permId) && perms.get(permId);
   }
   
   @Override
   public boolean isAuthorized(SecurityItem item, SecurityAction action) {
      if (isAuthorized(item)) {
         return true;
      }
      
      String permId = getPermissionId(SecurityContext.getUser(), item, action);
      Logger.debug("Checking for permission ID " + permId);
      return perms.containsKey(permId) && perms.get(permId);
   }
   
   @Override
   public boolean isAuthorized(SecurityItem item, SecurityAction action, String itemId) {
      if (isAuthorized(item, itemId) || isAuthorized(item, action)) {
         return true;
      }
      
      String permId = getPermissionId(SecurityContext.getUser(), item, action, itemId);
      Logger.debug("Checking for permission ID " + permId);
      return perms.containsKey(permId) && perms.get(permId);
   }
   
   @Override
   public void authorize(SecurityItem item, SecurityAction action) {
      Logger.track();
      
      if (!isAuthorized(item, action)) {
         throw new AccessDeniedException();
      }
   }
   
   @Override
   public void authorize(SecurityItem item, SecurityAction action, String itemId) {
      Logger.track();
      
      if (!isAuthorized(item, action, itemId)) {
         throw new AccessDeniedException();
      }
   }
   
   @Override
   public List<_User> listUsers() {
      authorize(SecurityItem.User, SecurityAction.List);
      
      return new ArrayList<_User>(users.values());
   }
   
   protected void loadUser(String id) {
      Logger.track();
      
      _User u = persistor.getUser(id);
      users.put(u.getId(), u);
      usernames.put(u.getDomainLogonName(), u);
   }
   
   protected void unloadUser(String id) {
      Logger.track();
      
      usernames.remove(id);
      users.remove(id);
   }

   protected void reloadUser(String id) {
      unloadUser(id);
      loadUser(id);
   }
   
   protected void reloadCache() {
      Map<String, _User> usernameCache = new HashMap<String, _User>();
      for (_User u : users.values()) {
         usernameCache.put(u.getDomainLogonName(), u);
      }
      usernames = usernameCache;
   }
   
   @Override
   public _User getUser(String usrId) {
      if (!users.containsKey(usrId)) {
         loadUser(usrId);
      }
      
      return users.get(usrId);
   }
   
   @Override
   public _User addUser(UserIn uIn) {
      Logger.track();
      
      authorize(SecurityItem.User, SecurityAction.Add);
      
      String id = userIdGen.get();
      
      _User user = SecurityUserFactory.get(id, uIn.getUsername(), uIn.getDomain());
      user.save();
      persistor.insertUser(user);
      
      loadUser(user.getId());
      
      EventManager.post(new UserAddedEvent(getUser(user.getId())));
      
      if ((uIn.getPassword() != null) && (uIn.getPassword().length > 0)) {
         setUserPassword(user.getId(), uIn.getPassword());
      }
      
      return getUser(user.getId());
   }
   
   @Override
   public void removeUser(String usrId) {
      Logger.track();
      
      authorize(SecurityItem.User, SecurityAction.Delete);
      
      _User user = getUser(usrId);
      user.delete();
      removePermission(user);
      persistor.deleteUser(user);
      unloadUser(user.getId());
      
      EventManager.post(new UserRemovedEvent(user));
   }
   
   @Override
   public _User modifyUser(UserIn uIn) {
      Logger.track();
      
      authorize(SecurityItem.User, SecurityAction.Modify);
      
      _User user = users.get(uIn.getId());
      
      if (uIn.getUsername() != null) {
         user.setName(uIn.getUsername());
      }
      if (uIn.getDomain() != null) {
         user.setDomain(uIn.getDomain());
      }
      
      user.save();
      
      persistor.updateUser(user);
      
      if ((uIn.getPassword() != null) && (uIn.getPassword().length > 0)) {
         setUserPassword(user, uIn.getPassword());
      }
      
      loadUser(user.getId());
      reloadCache();
      
      EventManager.post(new UserModifiedEvent(getUser(user.getId())));
      
      return user;
   }
   
   private class UserIdGenerator {
      
      private Integer nextId = 1;
      
      public String get() {
         Logger.track();
         
         while (users.containsKey(nextId.toString())) {
            nextId++;
         }
         return nextId.toString();
      }
   }
   
   private void setUserPassword(_User user, char[] password) {
      try {
         persistor.setUserPassword(user, PasswordEncryptionService.getEncryptedPassword(password, user.getSalt().getBytes()));
      } catch (NoSuchAlgorithmException e) {
         throw new HyperboxRuntimeException("Unable to encrypt password", e);
      } catch (InvalidKeySpecException e) {
         throw new HyperboxRuntimeException("Unable to encrypt password", e);
      }
   }
   
   @Override
   public void setUserPassword(String userId, char[] password) {
      _User user = getUser(userId);
      setUserPassword(user, password);
   }
   
   private void set(String permId, Boolean isAllowed) {
      Logger.debug("Setting Permission: " + permId + " - " + isAllowed);
      perms.put(permId, isAllowed);
   }
   
   @Override
   public void set(_User usr, SecurityItem itemType, SecurityAction action, boolean isAllowed) {
      Logger.debug("Setting permission: " + itemType + " - " + action + " : " + isAllowed);
      persistor.insertPermission(usr, itemType, action, isAllowed);
      set(getPermissionId(usr, itemType, action), isAllowed);
   }
   
   @Override
   public void remove(_User usr, SecurityItem itemType, SecurityAction action) {
      persistor.deletePermission(usr, itemType, action);
      perms.remove(getPermissionId(usr, itemType, action));
   }
   
   @Override
   public void set(_User usr, SecurityItem itemType, SecurityAction action, String itemId, boolean isAllowed) {
      if (AxStrings.isEmpty(itemId)) {
         set(usr, itemType, action, isAllowed);
      } else {
         Logger.debug("Setting permission: " + itemType + " - " + action + " - " + itemId + " : " + isAllowed);
         persistor.insertPermission(usr, itemType, action, itemId, isAllowed);
         set(getPermissionId(usr, itemType, action, itemId), isAllowed);
      }
   }
   
   @Override
   public void remove(_User usr, SecurityItem itemType, SecurityAction action, String itemId) {
      if (AxStrings.isEmpty(itemId)) {
         remove(usr, itemType, action);
      } else {
         persistor.deletePermission(usr, itemType, action, itemId);
         perms.remove(getPermissionId(usr, itemType, action, itemId));
      }
   }
   
   @Override
   public List<_ActionPermission> listActionPermissions(_User usr) {
      return persistor.listActionPermissions(usr);
   }
   
   @Override
   public List<_ItemPermission> listItemPermissions(_User usr) {
      return persistor.listItemPermissions(usr);
   }
   
   @Override
   public void removePermission(_User usr) {
      persistor.deletePermission(usr);
   }
   
   @Override
   public List<_EntityPermission> listPermission(String entityTypeId, String entityId) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public List<_UserPermission> listPermission(String entityTypeId, String entityId, _User usr) {
      // TODO Auto-generated method stub
      return null;
   }
   
}
