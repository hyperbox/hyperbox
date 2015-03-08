/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.hboxc.front.gui.net;

import org.altherian.hbox.hypervisor.net._NATRule;
import org.altherian.hboxc.front.gui.utils.AbstractIOTableModel;
import org.altherian.tool.AxStrings;

@SuppressWarnings("serial")
public class NATRuleTableModel extends AbstractIOTableModel<_NATRule> {

   public static String NAME = "Name";
   public static String PROTOCOL = "Protocol";
   public static String OUTSIDE_IP = "Outside IP";
   public static String OUTSIDE_PORT = "Outside Port";
   public static String INSIDE_IP = "Inside IP";
   public static String INSIDE_PORT = "Inside Port";

   @Override
   protected void addColumns() {
      addColumn(NAME);
      addColumn(PROTOCOL);
      addColumn(OUTSIDE_IP);
      addColumn(OUTSIDE_PORT);
      addColumn(INSIDE_IP);
      addColumn(INSIDE_PORT);
   }

   @Override
   protected boolean isCellEditable(int row, String colName) {
      return true;
   }

   @Override
   public Object getValueAt(_NATRule obj, String columnName) {
      if (columnName == NAME) {
         return obj.getName();
      }

      if (columnName == PROTOCOL) {
         return obj.getProtocol();
      }

      if (columnName == OUTSIDE_IP) {
         return obj.getPublicIp();
      }

      if (columnName == OUTSIDE_PORT) {
         return obj.getPublicPort();
      }

      if (columnName == INSIDE_IP) {
         return obj.getPrivateIp();
      }

      if (columnName == INSIDE_PORT) {
         return obj.getPrivatePort();
      }

      return null;
   }
   
   @Override
   public void setValueAt(Object value, _NATRule obj, String columnName) {
      if (columnName == NAME) {
         obj.setName(value.toString());
      }

      if (columnName == PROTOCOL) {
         obj.setProtocol(AxStrings.getNonEmpty(value, ""));
      }

      if (columnName == OUTSIDE_IP) {
         obj.setPublicIp(value.toString());
      }

      if (columnName == OUTSIDE_PORT) {
         obj.setPublicPort(value.toString());
      }

      if (columnName == INSIDE_IP) {
         obj.setPrivateIp(value.toString());
      }

      if (columnName == INSIDE_PORT) {
         obj.setPrivatePort(value.toString());
      }
   }

}
