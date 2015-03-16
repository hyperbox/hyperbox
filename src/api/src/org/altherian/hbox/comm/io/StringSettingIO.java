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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hbox.comm.io;

import org.altherian.hbox.utils.Settings;

/**
 * Helper class to implement a String type for the Setting Comm object
 * 
 * @author noteirak
 */
public final class StringSettingIO extends SettingIO {

   /**
    * Empty constructor for serialisation classes.<br/>
    * <ul>
    * <b>!! DONOT USE !!</b>
    * </ul>
    */
   @SuppressWarnings("unused")
   private StringSettingIO() {
   }

   /**
    * Constructor for a new String setting, with the given name and value.<br/>
    * {@link Settings#getUniqueId(Object)} will be used on the Object name to get a unique ID.
    * 
    * @param name The Object value returned by to use as name.
    * @param value The String value of this setting
    */
   public StringSettingIO(Object name, String value) {
      super(name, value);
   }

   /**
    * Get the value of this setting as String
    * 
    * @return String value of this setting
    */
   public String getValue() {
      return (String) getRawValue();
   }

}
