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

package org.altherian.hboxd.persistence;

import org.altherian.hboxd.security._User;
import org.altherian.hboxd.security._UserGroup;

import java.util.List;

public interface _SecurityPersistor {
   
   public void insertUser(_User user);
   
   public void insertGroup(_UserGroup group);
   
   public void link(_User user, _UserGroup group);
   
   public void unlink(_User user, _UserGroup group);
   
   public _User getUser(String userId);
   
   public byte[] getUserPassword(String userId);
   
   public void setUserPassword(_User user, byte[] password);
   
   public _UserGroup getGroup(String groupId);
   
   public List<_User> listUsers(_UserGroup group);
   
   public List<_User> listUsers();
   
   public List<_UserGroup> listGroups();
   
   public void deleteUser(_User user);
   
   public void deleteGroup(_UserGroup group);
   
   public void updateUser(_User user);
   
   public void updateGroup(_UserGroup group);
   
}
