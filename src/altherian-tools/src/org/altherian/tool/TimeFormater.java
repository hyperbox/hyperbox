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

package org.altherian.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormater {
   
   private static String timeFormat = "yy-MM-dd HH:mm:ss";
   private static SimpleDateFormat formater = new SimpleDateFormat(timeFormat);
   
   private TimeFormater() {
      // not used
   }
   
   public static void setDefaultFormat(String format) {
      timeFormat = format;
      formater = new SimpleDateFormat(timeFormat);
   }
   
   public static String get(Date date) {
      return formater.format(date);
   }
   
}
