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

import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;
import org.altherian.hboxd.security._ActionPermission;
import org.altherian.hboxd.security._ItemPermission;
import org.altherian.hboxd.security._User;
import org.altherian.hboxd.security._UserGroup;

import java.util.List;

public interface _SecurityPersistor {
   
   /*--- User Management ---*/
   public List<_User> listUsers();
   
   public _User getUser(String userId);
   
   public void insertUser(_User user);
   
   public void updateUser(_User user);
   
   public void deleteUser(_User user);
   
   /*--- Password Management ---*/
   public byte[] getUserPassword(String userId);
   
   public void setUserPassword(_User user, byte[] password);
   
   
   /*--- Group Management ---*/
   public List<_UserGroup> listGroups();
   
   public _UserGroup getGroup(String groupId);
   
   public void insertGroup(_UserGroup group);
   
   public void updateGroup(_UserGroup group);
   
   public void deleteGroup(_UserGroup group);
   
   
   /*--- Group Membership ---*/
   public List<_User> listUsers(_UserGroup group);
   
   public void link(_User user, _UserGroup group);
   
   public void unlink(_User user, _UserGroup group);
   
   /*-- Permissions Management ---*/
   public void insertPermission(_User usr, SecurityItem item, SecurityAction action, boolean isAllowed);
   
   public void insertPermission(_User usr, SecurityItem item, SecurityAction action, String itemId, boolean isAllowed);
   
   public void deletePermission(_User usr);
   
   public void deletePermission(_User usr, SecurityItem item, SecurityAction action);
   
   public void deletePermission(_User usr, SecurityItem item, SecurityAction action, String itemId);

   public List<_ActionPermission> listActionPermissions(_User usr);
   
   public List<_ItemPermission> listItemPermissions(_User usr);
   
   public _ActionPermission getPermission(_User usr, SecurityItem item, SecurityAction action);
   
   public _ItemPermission getPermission(_User usr, SecurityItem item, SecurityAction action, String itemId);
}
