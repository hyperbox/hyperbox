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

package org.altherian.hboxc;

import org.altherian.tool.logging.Logger;

import java.io.IOException;
import java.util.Properties;

public class Hyperbox {
   
   private static Properties buildProperties;
   private static String version = "0.0.0";
   private static String revision = "0";
   
   private static void failedToLoad(Exception e) {
      Logger.error("Unable to access the build.properties file: " + e.getMessage());
      Logger.error("Version and revision will not be accurate");
   }
   
   static {
      buildProperties = new Properties();
      try {
         buildProperties.load(Hyperbox.class.getResourceAsStream("/client.build.properties"));
         version = buildProperties.getProperty("version", "0.0.0");
         revision = buildProperties.getProperty("revision", "0");
      } catch (IOException e) {
         failedToLoad(e);
      } catch (NullPointerException e) {
         failedToLoad(e);
      }
   }
   
   public static String getVersion() {
      return version;
   }
   
   public static String getRevision() {
      return revision;
   }
   
}
