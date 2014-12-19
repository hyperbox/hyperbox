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

package org.altherian.hbox.utils;

public class Settings {
   
   /**
    * This will give a unique name for a given object.<br/>
    * This method is guarantee to always return the same unique ID for a setting for the class this object is made of.
    * 
    * @param name The object to use as ID
    * @return The unique String representation of this object, to be used as an ID
    */
   public static String getUniqueId(Object name) {
      if (name instanceof Enum) {
         return ((Enum<?>) name).toString();
      } else {
         return name.toString();
      }
   }
   
}
