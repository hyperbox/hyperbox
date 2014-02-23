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

package org.altherian.helper.sql;

import org.altherian.tool.logging.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EasyPreparedStatement {
   
   private Integer count = 1;
   private PreparedStatement stmt;
   
   public EasyPreparedStatement(PreparedStatement stmt) {
      setPreparedStatement(stmt);
   }
   
   public void setPreparedStatement(PreparedStatement stmt) {
      this.stmt = stmt;
      resetCount();
   }
   
   public PreparedStatement getPreparedStatement() {
      return stmt;
   }
   
   public void resetCount() {
      count = 1;
   }
   
   public void setBytes(byte[] bytes) throws SQLException {
      stmt.setBytes(count, bytes);
      count++;
   }
   
   public void setChars(char[] chars) throws SQLException {
      stmt.setString(count, String.copyValueOf(chars));
      count++;
   }
   
   public void setBoolean(Boolean b) throws SQLException {
      if (b == null) {
         stmt.setNull(count, java.sql.Types.INTEGER);
      } else {
         stmt.setBoolean(count, b);
      }
      count++;
   }
   
   public void setInt(Integer i) throws SQLException {
      if (i == null) {
         stmt.setNull(count, java.sql.Types.INTEGER);
      } else {
         stmt.setInt(count, i);
      }
      count++;
   }
   
   public void setString(Object o) throws SQLException {
      if (o == null) {
         stmt.setNull(count, java.sql.Types.VARCHAR);
      } else {
         stmt.setString(count, o.toString());
      }
      count++;
   }
   
   public void setFloat(Float f) throws SQLException {
      if (f == null) {
         stmt.setNull(count, java.sql.Types.FLOAT);
      } else {
         stmt.setFloat(count, f);
      }
      count++;
   }
   
   public void setDouble(Double d) throws SQLException {
      if (d == null) {
         stmt.setNull(count, java.sql.Types.DOUBLE);
      } else {
         stmt.setDouble(count, d);
      }
      count++;
   }
   
   public void setLong(Long l) throws SQLException {
      if (l == null) {
         stmt.setNull(count, java.sql.Types.DOUBLE);
      } else {
         stmt.setLong(count, l);
      }
      count++;
   }
   
   public void setDate(Date d) throws SQLException {
      if (d == null) {
         stmt.setNull(count, java.sql.Types.DATE);
      } else {
         stmt.setDate(count, new java.sql.Date(d.getTime()));
      }
      count++;
   }
   
   public void setDate(Long l) throws SQLException {
      if (l == null) {
         stmt.setNull(count, java.sql.Types.DATE);
      } else {
         stmt.setTimestamp(count, new java.sql.Timestamp(l));
      }
      count++;
   }
   
   public void setNull(int type) throws SQLException {
      stmt.setNull(count, type);
      count++;
   }
   
   public Boolean execute() throws SQLException {
      resetCount();
      Logger.sql("SQL Query : " + stmt.toString());
      return stmt.execute();
   }
   
   public Integer executeUpdate() throws SQLException {
      resetCount();
      Logger.sql("SQL Query : " + stmt.toString());
      this.stmt.getConnection().setAutoCommit(true);
      return stmt.executeUpdate();
   }
   
   public ResultSet executeQuery() throws SQLException {
      resetCount();
      Logger.sql("SQL Query : " + stmt.toString());
      this.stmt.getConnection().setAutoCommit(true);
      return stmt.executeQuery();
   }
   
   /**
    * Supported types :
    * <ul>
    * <li>Integer</li>
    * <li>Boolean</li>
    * <li>String</li>
    * <li>Float</li>
    * <li>Date</li>
    * <li>Null</li>
    * </ul>
    * 
    * @param o the object to set
    * @throws SQLException if something wrong happened
    */
   public void set(Object o) throws SQLException {
      if (Integer.class.isInstance(o)) {
         setInt((Integer) o);
      } else if (Boolean.class.isInstance(o)) {
         setBoolean((Boolean) o);
      } else if (String.class.isInstance(o)) {
         setString(o);
      } else if (Float.class.isInstance(o)) {
         setFloat((Float) o);
      } else if (Date.class.isInstance(o)) {
         setDate((Date) o);
      } else {
         if (o != null) {
            setString(o.toString());
         } else {
            setNull(java.sql.Types.VARCHAR);
         }
      }
   }
   
   public void addBatch() throws SQLException {
      if (stmt.getConnection().getAutoCommit()) {
         stmt.getConnection().setAutoCommit(false);
      }
      stmt.addBatch();
      resetCount();
   }
   
   public int[] executeBatch() throws SQLException {
      resetCount();
      int[] returnValue = stmt.executeBatch();
      if (!stmt.getConnection().getAutoCommit()) {
         stmt.getConnection().commit();
         stmt.getConnection().setAutoCommit(true);
      }
      return returnValue;
   }
   
   public void close() throws SQLException {
      stmt.close();
   }
   
}
