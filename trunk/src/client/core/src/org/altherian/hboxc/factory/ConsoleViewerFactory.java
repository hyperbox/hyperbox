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

package org.altherian.hboxc.factory;

import org.altherian.hboxc.core._ConsoleViewer;
import org.altherian.hboxc.core.console.viewer.ConsoleViewer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConsoleViewerFactory {
   
   public static final String WINDOWS_REMOTE_DESKTOP = "C:/Windows/system32/mstsc.exe";
   public static final String LINUX_RDESKTOP = "/usr/bin/rdesktop";

   private ConsoleViewerFactory() {
   }

   public static _ConsoleViewer get(String hypervisorId, String moduleId, String viewerPath, String args) {
      String id = hypervisorId + moduleId;
      return new ConsoleViewer(id, hypervisorId, moduleId, viewerPath, args);
   }

   public static List<_ConsoleViewer> getDefaults() {
      List<_ConsoleViewer> viewers = new ArrayList<_ConsoleViewer>();

      if ((new File(WINDOWS_REMOTE_DESKTOP)).isFile()) {
         viewers.add(get(".*", "Oracle VM VirtualBox Extension Pack", WINDOWS_REMOTE_DESKTOP, "/v:%SA%:%SP%"));
      }
      if ((new File(LINUX_RDESKTOP)).isFile()) {
         viewers.add(get(".*", "Oracle VM VirtualBox Extension Pack", LINUX_RDESKTOP, "%SA%:%SP%"));
      }

      return viewers;
   }

}
