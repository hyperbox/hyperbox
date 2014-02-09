/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
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
import org.altherian.hboxd.persistence._SecurityPersistor;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public final class NullSecurityManager implements _SecurityManager {
   
   @Override
   public void init(_SecurityPersistor persistor) throws HyperboxException {
      Logger.verbose("Initiating Null Security Manager");
   }
   
   @Override
   public void start() throws HyperboxException {
      Logger.verbose("Starting Null Security Manager");
   }
   
   @Override
   public void stop() {
      Logger.verbose("Stopping Null Security Manager");
   }
   
   @Override
   public void authenticate(String login, char[] password) {
      SecurityContext.setUser(this, new AnonymousUser());
   }
   
   @Override
   public void authorize(Request req) {
      Logger.debug(SecurityContext.getUser().getDomainLogonName() + " has been authorized for [" + req.getName() + "]");
   }
   
   @Override
   public boolean isAuthorized(_Event ev, _User u) {
      return true;
   }
   
   @Override
   public void authorize(SecurityAction action, SecurityItem item) {
      Logger.debug(SecurityContext.getUser().getDomainLogonName() + " has been authorized to perform " + action + " on " + item);
   }
   
   @Override
   public boolean isAuthorized(SecurityAction action, SecurityItem item) {
      return true;
   }
   
   @Override
   public List<_User> listUsers() {
      List<_User> users = new ArrayList<_User>();
      users.add(new AnonymousUser());
      return users;
   }
   
   @Override
   public _User getUser(UserInput uIn) {
      if (!uIn.getId().contentEquals("-1")) {
         throw new HyperboxRuntimeException("User not found");
      }
      
      return new AnonymousUser();
   }
   
   @Override
   public void removeUser(UserInput uIn) {
      throw new HyperboxRuntimeException("Action Not Available");
   }
   
   @Override
   public _User modifyUser(UserInput uIn) {
      throw new HyperboxRuntimeException("Action Not Available");
   }
   
   @Override
   public _User addUser(UserInput uIn) {
      throw new HyperboxRuntimeException("Action Not Available");
   }
   
   @Override
   public void setUserPassword(String userId, char[] password) {
      throw new HyperboxRuntimeException("Action Not Available");
   }
   
}
