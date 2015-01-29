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
   
   public static final String VERSION_UNKNOWN = "0.0";
   public static final String REVISION_UNKNOWN = "0";
   public static final String NET_PROTOCOL_VERSION_UNKNOWN = "0";
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
         version = buildProperties.getProperty("version", VERSION_UNKNOWN);
         revision = buildProperties.getProperty("revision", REVISION_UNKNOWN);
         protocolVersion = Long.parseLong(buildProperties.getProperty("protocol", NET_PROTOCOL_VERSION_UNKNOWN));
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
      return getFullVersion(getVersion(), getRevision());
   }

   public static String getFullVersion(String version, String revision) {
      return version + "." + revision;
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

   public static String getLogHeader(String fullVersion) {
      StringBuilder header = new StringBuilder();
      header.append("Hyperbox " + fullVersion);
      header.append(" - ");
      header.append("Java " + System.getProperty("java.version") + " " + System.getProperty("java.vm.name") + " "
            + System.getProperty("java.vm.version"));
      header.append(" - ");
      header.append(System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
      return header.toString();
   }
   
}
