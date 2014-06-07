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

package org.altherian.hboxc.front.gui.utils;

import org.altherian.hbox.comm.output.ObjectOutput;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class AbstractOutputListTableModel<T extends ObjectOutput> extends AbstractTableModel {
   
   private List<T> data;
   private List<String> columns;
   
   public AbstractOutputListTableModel() {
      init();
      addColumns();
   }
   
   public AbstractOutputListTableModel(List<T> list) {
      this();
      add(list);
   }
   
   private void init() {
      columns = new ArrayList<String>();
      reset();
   }
   
   private void reset() {
      data = new ArrayList<T>();
   }
   
   public void addColumn(String s) {
      columns.add(s);
      fireTableStructureChanged();
   }
   
   public void clear() {
      Logger.track();
      
      Integer maxIndex = data.size() - 1;
      reset();
      if (maxIndex > -1) {
         fireTableRowsDeleted(0, maxIndex);
      } else {
         fireTableDataChanged();
      }
   }
   
   public List<T> list() {
      return new ArrayList<T>(data);
   }
   
   public T getObjectAtRow(int rowId) {
      return data.get(rowId);
   }
   
   @Override
   public boolean isCellEditable(int row, int col) {
      return false;
   }
   
   @Override
   public int getColumnCount() {
      return columns.size();
   }
   
   @Override
   public String getColumnName(int columnIndex) {
      return columns.get(columnIndex);
   }
   
   @Override
   public int getRowCount() {
      return data.size();
   }
   
   @Override
   public Object getValueAt(int rowId, int columnId) {
      return getValueAt(data.get(rowId), getColumnName(columnId));
   }
   
   protected abstract Object getValueAt(T obj, String columnName);
   
   public int getRowForObj(T oOut) {
      return data.indexOf(oOut);
   }
   
   public boolean has(T oOut) {
      return data.contains(oOut);
   }
   
   public void add(T oOut) {
      if (oOut != null) {
         int index = data.size();
         data.add(index, oOut);
         fireTableRowsInserted(index, index);
      }
   }
   
   public void update(T oOut) {
      if (oOut != null) {
         int index = getRowForObj(oOut);
         if (index > -1) {
            data.set(index, oOut);
            fireTableRowsUpdated(index, index);
         }
      }
   }
   
   public void merge(T oOut) {
      if (has(oOut)) {
         update(oOut);
      } else {
         add(oOut);
      }
   }
   
   public void put(T oOut) {
      clear();
      add(oOut);
   }
   
   public void remove(T oOut) {
      if (oOut != null) {
         int index = getRowForObj(oOut);
         if (index > -1) {
            Logger.track();
            data.remove(index);
            fireTableRowsDeleted(index, index);
         }
      }
   }
   
   public void add(List<T> list) {
      for (T oOut : list) {
         add(oOut);
      }
   }
   
   public void update(List<T> list) {
      for (T oOut : list) {
         update(oOut);
      }
   }
   
   public void merge(List<T> list) {
      for (T oOut : list) {
         merge(oOut);
      }
   }
   
   public void put(List<T> list) {
      clear();
      add(list);
   }
   
   protected abstract void addColumns();
   
}
