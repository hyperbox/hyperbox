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

import java.sql.SQLException;

public class StoreSQL {
   
   public static final String TABLE = "stores";
   
   public static final String ID = "storeId";
   public static final String NAME = "storeName";
   public static final String PATH = "storePath";
   public static final String MODULE_ID = "storeModuleId";
   
   public static void init(SqlPersistor sql) throws SQLException {
      
      createTables(sql);
   }
   
   public static void createTables(SqlPersistor sql) throws SQLException {
      
      sql.getConn()
            .createStatement()
            .executeUpdate(
                  "CREATE TABLE IF NOT EXISTS `" + TABLE + "` (`" + ID
                        + "` VARCHAR(50) NOT NULL,`" + NAME + "` VARCHAR(300) NOT NULL,`" + PATH + "` VARCHAR(300) NOT NULL,`" + MODULE_ID
                        + "` VARCHAR(300) NOT NULL, PRIMARY KEY (`" + ID + "`))");
   }
   
   public static String getInsertQuery() {
      return "INSERT INTO " + TABLE + " (" + ID + "," + MODULE_ID + "," + NAME + "," + PATH + ") VALUES (?,?,?,?)";
   }
   
}
