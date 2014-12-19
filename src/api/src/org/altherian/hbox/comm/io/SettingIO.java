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
import org.altherian.tool.logging.Logger;

/**
 * Setting Comm object, equivalent of _Setting but can be used for sending to or receiving from the client.<br/>
 * More precise implementation of this class are available to enforce a more precise type, like String, boolean, etc.
 * 
 * @see BooleanSettingIO
 * @see StringSettingIO
 * @see PositiveNumberSettingIO
 * @author noteirak
 */
public class SettingIO {
   
   private String name;
   private Object value;
   
   /**
    * Empty constructor for serialisation classes.<br/>
    * <ul>
    * <b>!! DO NOT USE !!</b>
    * </ul>
    */
   protected SettingIO() {
   }
   
   /**
    * Constructor for a new setting, with the given name and an object as value
    * 
    * @param name The name of this setting as String
    * @param value The value for this setting
    */
   public SettingIO(Object name, Object value) {
      this.name = Settings.getUniqueId(name);
      this.value = value;
   }
   
   /**
    * Get the name (also considered ID) of this setting
    * 
    * @return The name of this setting as String
    */
   public String getName() {
      return name;
   }
   
   /**
    * Get the raw value of this setting. Other methods can be used to get a more specific value.
    * 
    * @return The value for this setting as Object
    * @see #getBoolean()
    * @see #getNumber()
    * @see #getString()
    */
   public Object getRawValue() {
      return value;
   }
   
   /**
    * <p>
    * Get a boolean out of the stored raw setting value.
    * </p>
    * 
    * @return a boolean, using {@code Boolean.parseBoolean(settingValue.toString())}
    * @see Boolean#parseBoolean(String)
    */
   public Boolean getBoolean() {
      Logger.debug(value);
      return value == null ? null : Boolean.parseBoolean(value.toString());
   }
   
   /**
    * <p>
    * Return a text value out of the stored raw setting value.<br/>
    * This method use <code>toString()</code> on the object, thus never fails.
    * </p>
    * 
    * @return a String value for the setting
    */
   public String getString() {
      return value == null ? null : value.toString();
   }
   
   /**
    * Try to get a Long out of the string representation of the stored raw setting value.
    * 
    * @return a Long, using {@code Long.parseLong(settingValue.toString())}
    * @throws NumberFormatException in case the value cannot be parsed as a number
    * @see Long#parseLong(String)
    */
   public Long getNumber() {
      return value == null ? null : Long.parseLong(value.toString());
   }
   
}
