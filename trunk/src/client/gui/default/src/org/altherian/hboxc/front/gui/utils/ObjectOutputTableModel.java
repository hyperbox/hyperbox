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

package org.altherian.hboxc.front.gui.utils;

import org.altherian.hbox.comm.out.ObjectOut;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class ObjectOutputTableModel<T extends ObjectOut> extends AbstractTableModel {
   
   private List<String> columnNames = new ArrayList<String>();
   private List<Object> columnSettings = new ArrayList<Object>();
   
   private List<T> data = new ArrayList<T>();
   
   public ObjectOutputTableModel() {
      addColumns();
   }
   
   public void add(T item) {
      if (data.add(item)) {
         fireTableRowsInserted(data.size() - 1, data.size() - 1);
      }
   }
   
   public void add(List<T> itemList) {
      for (T item : itemList) {
         add(item);
      }
   }
   
   public void put(List<T> itemList) {
      clear();
      add(itemList);
   }
   
   public void clear() {
      if (!data.isEmpty()) {
         int lastIndex = data.size() - 1;
         data.clear();
         fireTableRowsDeleted(0, lastIndex);
      }
   }
   
   @Override
   public int getColumnCount() {
      return columnNames.size();
   }
   
   @Override
   public int getRowCount() {
      return data.size();
   }
   
   @Override
   public Object getValueAt(int rowIndex, int columnIndex) {
      if (data.get(rowIndex).hasSetting(columnSettings.get(columnIndex))) {
         return data.get(rowIndex).getSetting(columnSettings.get(columnIndex)).getString();
      } else {
         return "";
      }
   }
   
   public T getObjectAtRowId(int rowId) {
      return data.get(rowId);
   }
   
   @Override
   public String getColumnName(int columnIndex) {
      return columnNames.get(columnIndex);
   }
   
   /**
    * Add a new column with a name and bind its value to a setting.
    * 
    * @param name The name to display to the user
    * @param settingName The setting ID to link this column to on the output object
    */
   protected void addColumn(String name, Object settingName) {
      columnNames.add(name);
      columnSettings.add(settingName);
   }
   
   /**
    * Use {@link #addColumn(String, Object)}
    */
   protected abstract void addColumns();
   
}
