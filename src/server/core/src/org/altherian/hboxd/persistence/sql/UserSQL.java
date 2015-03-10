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

package org.altherian.hboxd.persistence.sql;

import org.altherian.hboxd.factory.SecurityUserFactory;
import org.altherian.hboxd.security._User;
import org.altherian.helper.sql.EasyPreparedStatement;
import org.altherian.tool.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSQL {
   
   public final static String TABLE = "users";
   
   public final static String ID = "userId";
   public final static String FULLNAME = "userFullName";
   public final static String USERNAME = "username";
   public final static String DOMAIN = "userDomain";
   public final static String PASSWORD = "userPass";
   public final static String SALT = "userSalt";
   
   public static void init(SqlPersistor sql) throws SQLException {
      
      
      createTables(sql);
   }
   
   public static void createTables(SqlPersistor sql) throws SQLException {
      
      
      sql.getConn()
            .createStatement()
            .executeUpdate(
                  "CREATE TABLE IF NOT EXISTS `" + TABLE + "` (`"
                        + ID + "` VARCHAR(255) NOT NULL, `"
                        + FULLNAME + "` VARCHAR(255) NULL DEFAULT NULL, `"
                        + USERNAME + "` VARCHAR(255) NOT NULL,`"
                        + DOMAIN + "` VARCHAR(255) NULL DEFAULT NULL,`"
                        + SALT + "` VARCHAR(255) NULL DEFAULT NULL,`"
                        + PASSWORD + "` VARBINARY(255) NULL DEFAULT NULL,PRIMARY KEY (`" + ID + "`))");
   }
   
   public static String getInsertQuery() {
      return "INSERT INTO " + TABLE + " (" + ID + "," + FULLNAME + "," + USERNAME + "," + DOMAIN + "," + SALT + ") VALUES (?,?,?,?,?)";
   }
   
   public static void populateInsertQuert(EasyPreparedStatement stmt, _User u) throws SQLException {
      stmt.setString(u.getId());
      stmt.setString(null);
      stmt.setString(u.getName());
      stmt.setString(u.getDomain());
      stmt.setString(u.getSalt());
   }
   
   public static String getUpdateQuery() {
      return "UPDATE " + TABLE + " SET " + USERNAME + " = ?, " + DOMAIN + " = ? WHERE " + ID + " = ?";
   }
   
   public static void populateUpdateQuery(EasyPreparedStatement stmt, _User u) throws SQLException {
      stmt.setString(u.getName());
      stmt.setString(u.getDomain());
      stmt.setString(u.getId());
   }
   
   public static String getDeleteQuery() {
      return "DELETE FROM " + TABLE + " WHERE " + ID + " = ?";
   }
   
   public static void populateDeleteQuery(EasyPreparedStatement stmt, _User u) throws SQLException {
      stmt.setString(u.getId());
   }
   
   public static String getListQuery() {
      return "SELECT * FROM " + TABLE;
   }
   
   public static String getSelectQuery() {
      return "SELECT * FROM " + TABLE + " WHERE " + ID + " = ?";
   }
   
   public static void populateSelectQuery(EasyPreparedStatement stmt, String userId) throws SQLException {
      stmt.setString(userId);
   }
   
   public static String getSelectPasswordQuery() {
      return "SELECT " + PASSWORD + " FROM " + TABLE + " WHERE " + ID + " = ?";
   }
   
   public static void populateSelectPasswordQuery(EasyPreparedStatement stmt, String userId) throws SQLException {
      stmt.setString(userId);
   }
   
   public static String getUpdatePasswordQuery() {
      return "UPDATE " + TABLE + " SET " + PASSWORD + " = ? WHERE " + ID + " = ?";
   }
   
   public static void populateUpdatePasswordQuery(EasyPreparedStatement stmt, _User u, byte[] password) throws SQLException {
      stmt.setBytes(password);
      stmt.setString(u.getId());
   }
   
   public static _User extractUser(ResultSet rSet) throws SQLException {
      String userId = rSet.getString(ID);
      String userName = rSet.getString(USERNAME);
      String userDomain = rSet.getString(DOMAIN);
      String salt = rSet.getString(SALT);
      
      return SecurityUserFactory.get(userId, userName, userDomain, salt);
   }
   
   public static List<_User> extractUsers(ResultSet rSet) throws SQLException {
      List<_User> userList = new ArrayList<_User>();
      while (rSet.next()) {
         userList.add(extractUser(rSet));
      }
      return userList;
   }
   
   public static byte[] extractPassword(ResultSet rSet) throws SQLException {
      return rSet.getBytes(PASSWORD);
   }
   
}
