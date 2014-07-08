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

package org.altherian.hbox;

import org.altherian.hbox.exception.ConfigurationException;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.tool.logging.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * To be used by any class that require a configuration file based on a namespace.
 * 
 * @author noteirak
 */
public class Configuration {
   
   public static final String CFGKEY_CONF_USER_DATA_PATH = "conf.user.data.path";
   private static Map<String, String> settings = new HashMap<String, String>();
   
   private Configuration() {
      // static only
   }
   
   
   public static String getUserDataPath() throws HyperboxException {
      if (hasSetting(CFGKEY_CONF_USER_DATA_PATH)) {
         return getSetting(CFGKEY_CONF_USER_DATA_PATH);
      } else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
         return getUserDataPathWin();
      } else {
         return getUserDataPathLinux();
      }
   }
   
   private static String getUserDataPathLinux() throws HyperboxException {
      return System.getProperty("user.home") + File.separator + ".hbox";
   }
   
   private static String getUserDataPathWin() throws HyperboxException {
      if (new File(System.getenv("APPDATA")).exists()) {
         return System.getenv("APPDATA") + File.separator + "Hyperbox";
      } else if (new File(System.getenv("LOCALAPPDATA")).exists()) {
         return System.getenv("LOCALAPPDATA") + File.separator + "Hyperbox";
      } else {
         return System.getProperty("user.home") + File.separator + "Hyperbox";
      }
   }
   
   private static String getEnvVarName(String key) {
      return "HBOX_" + key.toUpperCase().replace(".", "_");
   }
   
   public static void init() throws HyperboxException {
      // TODO
   }
   
   public static String getSetting(String key, String defaultValue) {
      Logger.debug("Trying to get setting " + key + " with default value " + defaultValue);
      Logger.debug("System.getProperties().containsKey(): " + System.getProperties().containsKey(key));
      Logger.debug("System.getenv().containsKey(getEnvVarName()): " + System.getenv().containsKey(getEnvVarName(key)));
      Logger.debug("hasSetting(): " + hasSetting(key));
      if (System.getProperties().containsKey(key)) {
         return System.getProperty(key);
      }
      else if (System.getenv().containsKey(getEnvVarName(key))) {
         return System.getenv(getEnvVarName(key));
      }
      else if (settings.containsKey(key)) {
         return settings.get(key);
      }
      else {
         return defaultValue;
      }
   }
   
   public static String getSetting(String key) {
      return getSetting(key, null);
   }
   
   public static boolean hasSetting(String key) {
      return System.getProperties().containsKey(key) || System.getenv().containsKey(getEnvVarName(key)) || settings.containsKey(key);
   }
   
   
   
   public static String getSettingOrFail(String key) throws ConfigurationException {
      if (!hasSetting(key)) {
         throw new ConfigurationException("Key is not defined in configuration: " + key);
      }
      return getSetting(key);
   }
   
   /**
    * Insert the defined value for the given key to the configuration
    * 
    * @param key a String value for the key
    * @param value String value of the object, obtained using <code>toString()</code>
    */
   public static void setSetting(String key, Object value) {
      settings.put(key, value.toString());
   }
   
}
