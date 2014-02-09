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

package org.altherian.hbox.comm;

import java.util.Map;

public interface _Container {
   
   public void set(String label, Object object);
   
   public void set(Enum<?> e, Object object);
   
   public void set(Class<?> c, Object object);
   
   public void set(Object object);
   
   public <T> T get(Class<T> c);
   
   /**
    * Return the object mapped under the label given, or null if no object was included in this message.
    * 
    * @see Map
    * @param s The label to use for looking up the object.
    * @return The object mapped to this label, or null if no such map exist.
    */
   public Object get(String s);
   
   /**
    * Return the object mapped under this enum string representation, or null if no object was included in this message.
    * 
    * @see Map
    * @param e The Enum to use for looking up the object. The lookup will use <code>toString()</code>
    * @return The object mapped to this label, or null if no such map exist.
    */
   public Object get(Enum<?> e);
   
   /**
    * Checks if an object mapped to the given label is included in this message.
    * 
    * @param s The label to check
    * @return true if an object exists for this label, false if not.
    */
   public boolean has(String s);
   
   /**
    * Checks if an object mapped under the given class name is included in this message.
    * 
    * @param c The label to check
    * @return true if an object exists for this class name as label, false if not.
    */
   public boolean has(Class<?> c);
   
   public boolean has(Enum<?> e);
   
   public boolean hasData();
   
}
