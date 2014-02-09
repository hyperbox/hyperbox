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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxd.security;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hbox.event._Event;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.security.UserAddedEvent;
import org.altherian.hboxd.event.security.UserModifiedEvent;
import org.altherian.hboxd.event.security.UserRemovedEvent;
import org.altherian.hboxd.factory.SecurityUserFactory;
import org.altherian.hboxd.persistence._SecurityPersistor;
import org.altherian.security.PasswordEncryptionService;
import org.altherian.tool.logging.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityManager implements _SecurityManager {
   
   private UserIdGenerator userIdGen;
   private Map<String, _User> users;
   private Map<String, _User> usernames;
   private _SecurityPersistor persistor;
   
   @Override
   public void init(_SecurityPersistor persistor) throws HyperboxException {
      Logger.track();
      
      this.persistor = persistor;
   }
   
   @Override
   public void start() throws HyperboxException {
      Logger.track();
      
      userIdGen = new UserIdGenerator();
      users = new HashMap<String, _User>();
      usernames = new HashMap<String, _User>();
      
      List<_User> userList = persistor.listUsers();
      
      // We assume this is an empty database
      if (userList.isEmpty()) {
         _User u = SecurityUserFactory.get("0", "admin");
         persistor.insertUser(u);
         setUserPassword(u, "hyperbox".toCharArray());
         
         userList.add(u);
         Logger.info("Created initial account");
      }
      
      for (_User u : userList) {
         users.put(u.getId(), u);
         usernames.put(u.getDomainLogonName(), u);
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
         throw new HyperboxRuntimeException("Invalid credentials");
      }
      
      _User user = usernames.get(login);
      
      try {
         byte[] encryptSubmitPass = PasswordEncryptionService.getEncryptedPassword(submitedPassword, user.getSalt().getBytes());
         byte[] encryptPass = persistor.getUserPassword(user.getId());
         
         if (!Arrays.equals(encryptSubmitPass, encryptPass)) {
            Logger.debug("Invalid password for user " + user.getDomainLogonName());
            throw new HyperboxRuntimeException("Invalid credentials");
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
   public boolean isAuthorized(_Event ev, _User u) {
      Logger.track();
      
      // TODO Auto-generated method stub
      return true;
   }
   
   @Override
   public void authorize(SecurityAction action, SecurityItem item) {
      Logger.track();
      
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public boolean isAuthorized(SecurityAction action, SecurityItem item) {
      Logger.track();
      
      // TODO Auto-generated method stub
      return true;
   }
   
   @Override
   public List<_User> listUsers() {
      return new ArrayList<_User>(users.values());
   }
   
   protected void loadUser(String id) {
      _User u = persistor.getUser(id);
      users.put(u.getId(), u);
      usernames.put(u.getDomainLogonName(), u);
   }
   
   protected void unloadUser(String id) {
      usernames.remove(id);
      users.remove(id);
   }
   
   @Override
   public _User getUser(UserInput uIn) {
      return getUser(uIn.getId());
   }
   
   protected _User getUser(String userId) {
      if (!users.containsKey(userId)) {
         loadUser(userId);
      }
      
      return users.get(userId);
   }
   
   @Override
   public _User addUser(UserInput uIn) {
      Logger.track();
      
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
   public void removeUser(UserInput uIn) {
      Logger.track();
      
      _User user = getUser(uIn);
      user.delete();
      persistor.deleteUser(user);
      unloadUser(user.getId());
      
      EventManager.post(new UserRemovedEvent(user));
   }
   
   @Override
   public _User modifyUser(UserInput uIn) {
      Logger.track();
      
      _User user = persistor.getUser(uIn.getId());
      
      if (uIn.getUsername() != null) {
         user.setName(uIn.getUsername());
      }
      if (uIn.getDomain() != null) {
         user.setDomain(uIn.getDomain());
      }
      
      user.save();
      
      persistor.updateUser(user);
      loadUser(user.getId());
      
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
   
   
   
}
