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

package org.altherian.hbox.comm.io;

/**
 * Helper class to implement a positive number type for the Setting Comm object
 * 
 * @author noteirak
 */
public final class PositiveNumberSettingIO extends SettingIO {
   
   /**
    * Empty constructor for serialisation classes.<br/>
    * <ul>
    * <b>!! DONOT USE !!</b>
    * </ul>
    */
   @SuppressWarnings("unused")
   private PositiveNumberSettingIO() {
   }
   
   /**
    * Constructor for a new boolean setting, with the given name and value
    * 
    * @param name The name of this setting as Object
    * @param value The long value for this setting - If the value is less than 0, Math.abs() will be used to get a positive value.
    */
   public PositiveNumberSettingIO(Object name, long value) {
      super(name, value);
   }
   
   /**
    * Get the value of this setting as long
    * 
    * @return long value of this setting
    */
   public Long getValue() {
      return (Long) getRawValue();
   }
   
}
