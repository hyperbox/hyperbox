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
import org.altherian.hboxd.persistence._SecurityPersistor;

import java.util.List;

public interface _SecurityManager {
   
   public void init(_SecurityPersistor persistor) throws HyperboxException;
   
   public void start() throws HyperboxException;
   
   public void stop();
   
   public void authenticate(String login, char[] password);
   
   public void authorize(Request req);
   
   public boolean isAuthorized(_Event ev, _User u);
   
   public void authorize(SecurityAction action, SecurityItem item);
   
   public boolean isAuthorized(SecurityAction action, SecurityItem item);
   
   public List<_User> listUsers();
   
   public _User getUser(UserInput uIn);
   
   public _User addUser(UserInput uIn);
   
   public void removeUser(UserInput uIn);
   
   public _User modifyUser(UserInput uIn);
   
   public void setUserPassword(String userId, char[] password);
   
}
