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

import org.altherian.hbox.exception.FeatureNotImplementedException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.exception.PersistorException;
import org.altherian.hboxd.factory.StoreFactory;
import org.altherian.hboxd.security._User;
import org.altherian.hboxd.security._UserGroup;
import org.altherian.hboxd.store._Store;
import org.altherian.helper.sql.EasyPreparedStatement;
import org.altherian.tool.logging.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SqlPersistor implements _Persistor {
   
   @Override
   public void insertStore(_Store store) {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(StoreSQL.getInsertQuery()));
         
         try {
            stmt.set(store.getId());
            stmt.set(store.getType());
            stmt.set(store.getLabel());
            stmt.set(store.getLocation());
            stmt.set(store.getState());
            
            stmt.executeUpdate();
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to insert Store", e);
      }
   }
   
   @Override
   public void updateStore(_Store store) {
      String stmtSql = "UPDATE " + StoreSQL.TABLE + " SET " + StoreSQL.NAME + " = ?, " + StoreSQL.PATH + " = ?, " + StoreSQL.STATUS + " = ? WHERE " + StoreSQL.ID + " = ?";
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(stmtSql));
         
         try {
            stmt.set(store.getLabel());
            stmt.set(store.getLocation());
            stmt.set(store.getState());
            stmt.set(store.getId());
            
            stmt.executeUpdate();
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to update Store", e);
      }
   }
   
   @Override
   public void deleteStore(_Store store) {
      try {
         getConn().createStatement().executeUpdate("DELETE FROM " + StoreSQL.TABLE + " WHERE " + StoreSQL.ID + " = " + store.getId() + " LIMIT 1");
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to delete store", e);
      }
   }
   
   @Override
   public _Store getStore(String id) {
      try {
         EasyPreparedStatement prepStmt = new EasyPreparedStatement(getConn().prepareStatement("SELECT * FROM " + StoreSQL.TABLE + "WHERE " + StoreSQL.ID + " = " + id));
         
         try {
            ResultSet rSet = prepStmt.executeQuery();
            if(!rSet.next()) {
               throw new HyperboxRuntimeException("No Store by this ID: "+id);
            }
            
            String storeId = rSet.getString(StoreSQL.ID);
            String moduleId = rSet.getString(StoreSQL.MODULE_ID);
            String storeName = rSet.getString(StoreSQL.NAME);
            String storePath = rSet.getString(StoreSQL.PATH);
            String storeStatus = rSet.getString(StoreSQL.STATUS);
            return StoreFactory.get(moduleId, storeId, storeName, storePath, storeStatus);
         } finally {
            prepStmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to retrieve list of stores", e);
      }
   }
   
   @Override
   public List<_Store> listStores() {
      List<_Store> storeList = new ArrayList<_Store>();
      try {
         EasyPreparedStatement prepStmt = new EasyPreparedStatement(getConn().prepareStatement("SELECT * FROM " + StoreSQL.TABLE));
         ResultSet rSet = prepStmt.executeQuery();
         
         try {
            while (rSet.next()) {
               String storeId = rSet.getString(StoreSQL.ID);
               String moduleId = rSet.getString(StoreSQL.MODULE_ID);
               String storeName = rSet.getString(StoreSQL.NAME);
               String storePath = rSet.getString(StoreSQL.PATH);
               String storeStatus = rSet.getString(StoreSQL.STATUS);
               storeList.add(StoreFactory.get(moduleId, storeId, storeName, storePath, storeStatus));
            }
         } finally {
            rSet.close();
            prepStmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to retrieve list of stores", e);
      }
      return storeList;
   }
   
   @Override
   public void insertUser(_User user) {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(UserSQL.getInsertQuery()));
         
         try {
            UserSQL.populateInsertQuert(stmt, user);
            stmt.executeUpdate();
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to insert User", e);
      }
   }
   
   @Override
   public void insertGroup(_UserGroup group) {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public void link(_User user, _UserGroup group) {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public void unlink(_User user, _UserGroup group) {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public _User getUser(String userId) {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(UserSQL.getSelectQuery()));
         
         try {
            UserSQL.populateSelectQuery(stmt, userId);
            ResultSet rSet = stmt.executeQuery();
            if (!rSet.next()) {
               throw new HyperboxRuntimeException("No User by this ID: " + userId);
            }
            
            return UserSQL.extractUser(rSet);
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to retrieve User with ID " + userId, e);
      }
   }
   
   @Override
   public byte[] getUserPassword(String userId) {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(UserSQL.getSelectPasswordQuery()));
         
         try {
            UserSQL.populateSelectPasswordQuery(stmt, userId);
            ResultSet rSet = stmt.executeQuery();
            if (!rSet.next()) {
               throw new HyperboxRuntimeException("No User by this ID: " + userId);
            }
            
            return UserSQL.extractPassword(rSet);
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to retrieve User with ID " + userId, e);
      }
   }
   
   @Override
   public _UserGroup getGroup(String groupId) {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public List<_User> listUsers(_UserGroup group) {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public List<_User> listUsers() {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(UserSQL.getListQuery()));
         ResultSet rSet = stmt.executeQuery();
         
         try {
            return UserSQL.extractUsers(rSet);
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to list User", e);
      }
   }
   
   @Override
   public List<_UserGroup> listGroups() {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public void deleteUser(_User user) {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(UserSQL.getDeleteQuery()));
         
         try {
            UserSQL.populateDeleteQuery(stmt, user);
            stmt.executeUpdate();
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to delete User", e);
      }
   }
   
   @Override
   public void deleteGroup(_UserGroup group) {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public void updateUser(_User user) {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(UserSQL.getUpdateQuery()));
         
         try {
            UserSQL.populateUpdateQuery(stmt, user);
            stmt.executeUpdate();
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to update User: " + e.getMessage(), e);
      }
   }
   
   @Override
   public void updateGroup(_UserGroup group) {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public void setUserPassword(_User user, byte[] password) {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(UserSQL.getUpdatePasswordQuery()));
         try {
            UserSQL.populateUpdatePasswordQuery(stmt, user, password);
            if (stmt.executeUpdate() < 1) {
               throw new HyperboxRuntimeException("No password was updated");
            }
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to set password", e);
      }
   }
   
   @Override
   public void storeSetting(String name, String value) {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(SettingSQL.getSetSettingQuery()));
         try {
            SettingSQL.populateSetSettingQuery(stmt, name, value);
            if (stmt.executeUpdate() < 1) {
               throw new HyperboxRuntimeException("Unable to set setting "+name);
            }
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to save setting " + name + ": " + e.getMessage(), e);
      }
   }
   
   @Override
   public String loadSetting(String name) {
      try {
         EasyPreparedStatement stmt = new EasyPreparedStatement(getConn().prepareStatement(SettingSQL.getLoadSettingQuery()));
         try {
            SettingSQL.populateLoadSettingQuery(stmt, name);
            ResultSet rSet = stmt.executeQuery();
            if (!rSet.next()) {
               throw new HyperboxRuntimeException("No setting found for: " + name);
            }
            
            return SettingSQL.extractSetting(rSet);
         } finally {
            stmt.close();
         }
      } catch (SQLException e) {
         throw new HyperboxRuntimeException("Unable to load setting " + name + ": " + e.getMessage(), e);
         
      }
   }
   
   @Override
   public void start() throws PersistorException {
      Logger.track();
      
      try {
         StoreSQL.init(this);
         UserSQL.init(this);
         SettingSQL.init(this);
      } catch (SQLException e) {
         throw new PersistorException("Unable to init StoreSQL", e);
      }
   }
   
   @Override
   public void stop() {
      Logger.track();

      // nothing to do here
   }
   
   public abstract Connection getConn();
   
}
