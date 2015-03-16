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

package org.altherian.hboxd.persistence;

import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;
import org.altherian.hbox.exception.FeatureNotImplementedException;
import org.altherian.hboxd.exception.PersistorException;
import org.altherian.hboxd.security._ActionPermission;
import org.altherian.hboxd.security._ItemPermission;
import org.altherian.hboxd.security._User;
import org.altherian.hboxd.security._UserGroup;
import org.altherian.hboxd.store._Store;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyPersistor implements _Persistor {

   private static Map<String, _User> users = new HashMap<String, _User>();
   private static Map<String, byte[]> userPass = new HashMap<String, byte[]>();

   private static Map<String, _UserGroup> userGroups = new HashMap<String, _UserGroup>();

   private static Map<String, _Store> stores = new HashMap<String, _Store>();

   private static Map<String, String> settings = new HashMap<String, String>();

   private static Map<String, _ActionPermission> actPerms = new HashMap<String, _ActionPermission>();
   private static Map<String, _ItemPermission> itemPerms = new HashMap<String, _ItemPermission>();

   @Override
   public void init() throws PersistorException {
      // nothing to do here
   }

   @Override
   public void start() throws PersistorException {
      // nothing to do here
   }

   @Override
   public void stop() {
      // nothing to do here
   }

   @Override
   public void destroy() {
      // nothing to do here
   }

   @Override
   public void insertUser(_User user) {
      users.put(user.getId(), user);
   }

   @Override
   public void insertGroup(_UserGroup group) {
      userGroups.put(group.getId(), group);
   }

   @Override
   public void link(_User user, _UserGroup group) {
      throw new FeatureNotImplementedException();
   }

   @Override
   public void unlink(_User user, _UserGroup group) {
      throw new FeatureNotImplementedException();
   }

   @Override
   public _User getUser(String userId) {
      return users.get(userId);
   }

   @Override
   public byte[] getUserPassword(String userId) {
      return userPass.get(userId);
   }

   @Override
   public void setUserPassword(_User user, byte[] password) {
      userPass.put(user.getId(), password);
   }

   @Override
   public _UserGroup getGroup(String groupId) {
      return userGroups.get(groupId);
   }

   @Override
   public List<_User> listUsers(_UserGroup group) {
      throw new FeatureNotImplementedException();
   }

   @Override
   public List<_User> listUsers() {
      return new ArrayList<_User>(users.values());
   }

   @Override
   public List<_UserGroup> listGroups() {
      return new ArrayList<_UserGroup>(userGroups.values());
   }

   @Override
   public void deleteUser(_User user) {
      users.remove(user.getId());
   }

   @Override
   public void deleteGroup(_UserGroup group) {
      userGroups.remove(group.getId());
   }

   @Override
   public void updateUser(_User user) {
      insertUser(user);
   }

   @Override
   public void updateGroup(_UserGroup group) {
      insertGroup(group);
   }

   @Override
   public void insertStore(_Store store) {
      stores.put(store.getId(), store);
   }

   @Override
   public void updateStore(_Store store) {
      insertStore(store);
   }

   @Override
   public void deleteStore(_Store store) {
      stores.remove(store.getId());
   }

   @Override
   public _Store getStore(String id) {
      return stores.get(id);
   }

   @Override
   public List<_Store> listStores() {
      return new ArrayList<_Store>(stores.values());
   }

   @Override
   public void storeSetting(String name, String value) {
      settings.put(name, value);
   }

   @Override
   public String loadSetting(String name) {
      return settings.get(name);
   }

   @Override
   public _ActionPermission getPermission(_User usr, SecurityItem item, SecurityAction action) {
      return actPerms.get(usr.getDomainLogonName() + item.toString() + action.toString());
   }

   @Override
   public _ItemPermission getPermission(_User usr, SecurityItem item, SecurityAction action, String itemId) {
      return itemPerms.get(usr.getDomainLogonName() + item.toString() + action.toString() + itemId);
   }

   @Override
   public void insertPermission(_User usr, SecurityItem item, SecurityAction action, boolean isAllowed) {
      // TODO Auto-generated method stub

   }

   @Override
   public void insertPermission(_User usr, SecurityItem item, SecurityAction action, String itemId, boolean isAllowed) {
      // TODO Auto-generated method stub

   }

   @Override
   public List<_ActionPermission> listActionPermissions(_User usr) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<_ItemPermission> listItemPermissions(_User usr) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void deletePermission(_User usr) {
      // TODO Auto-generated method stub

   }

   @Override
   public void deletePermission(_User usr, SecurityItem item, SecurityAction action) {
      // TODO Auto-generated method stub

   }

   @Override
   public void deletePermission(_User usr, SecurityItem item, SecurityAction action, String itemId) {
      // TODO Auto-generated method stub

   }

}
