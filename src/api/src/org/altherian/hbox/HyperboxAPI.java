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

package org.altherian.hbox;

import org.altherian.tool.logging.Logger;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class HyperboxAPI {
   
   private static Properties buildProperties;
   private static String version;
   private static String revision;
   private static long protocolVersion;
   
   private static void failedToLoad(Exception e) {
      Logger.error("Unable to access or read the api.build.properties ressource: " + e.getMessage());
      Logger.error("API version and revision may not be accurate");
   }
   
   static {
      buildProperties = new Properties();
      try {
         buildProperties.load(HyperboxAPI.class.getResourceAsStream("/api.build.properties"));
         version = buildProperties.getProperty("version", "0.0.0");
         revision = buildProperties.getProperty("revision", "0");
         protocolVersion = Long.parseLong(buildProperties.getProperty("protocol", "0"));
      } catch (IOException e) {
         failedToLoad(e);
      } catch (NullPointerException e) {
         failedToLoad(e);
      } catch (NumberFormatException e) {
         failedToLoad(e);
      }
   }
   
   public static String getVersion() {
      return version;
   }
   
   public static String getRevision() {
      return revision;
   }
   
   public static String getFullVersion() {
      return getVersion() + "r" + getRevision();
   }
   
   public static long getProtocolVersion() {
      return protocolVersion;
   }
   
   public static void processArgs(Set<String> args) {
      if (args.contains("--apiversion")) {
         System.out.println(getVersion());
         System.exit(0);
      }
      if (args.contains("--apirevision")) {
         System.out.println(getRevision());
         System.exit(0);
      }
      if (args.contains("--netversion")) {
         System.out.println(HyperboxAPI.getProtocolVersion());
         System.exit(0);
      }
   }
   
}
