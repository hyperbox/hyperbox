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

import org.altherian.helper.sql.EasyPreparedStatement;
import org.altherian.tool.logging.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingSQL {
   
   public static final String TABLE = "settings";
   
   public static final String NAME = "settingName";
   public static final String VALUE = "settingValue";
   
   public static void init(SqlPersistor sql) throws SQLException {
      Logger.track();
      
      createTables(sql);
   }
   
   public static void createTables(SqlPersistor sql) throws SQLException {
      Logger.track();
      
      sql.getConn()
      .createStatement()
      .executeUpdate(
            "CREATE TABLE IF NOT EXISTS `" + TABLE + "` (`" + NAME
                        + "` VARCHAR(255) NOT NULL,`" + VALUE + "` VARCHAR(255),PRIMARY KEY (`" + NAME + "`))");
   }
   
   public static String getSetSettingQuery() {
      return "MERGE INTO " + TABLE + " (" + NAME + "," + VALUE + ") VALUES (?,?)";
   }
   
   public static void populateSetSettingQuery(EasyPreparedStatement stmt, String name, String value) throws SQLException {
      Logger.debug("Saving setting " + name + " with value " + value);
      stmt.setString(name);
      stmt.setString(value);
   }
   
   public static String getLoadSettingQuery() {
      return "SELECT * FROM "+TABLE+" WHERE settingName = ?";
   }
   
   public static void populateLoadSettingQuery(EasyPreparedStatement stmt, String name) throws SQLException {
      stmt.setString(name);
   }
   
   public static String extractSetting(ResultSet rSet) throws SQLException {
      return rSet.getString(VALUE);
   }
   
}
