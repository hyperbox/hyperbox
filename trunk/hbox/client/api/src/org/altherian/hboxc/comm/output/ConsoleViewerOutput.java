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

package org.altherian.hboxc.comm.output;

import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.ObjectOutput;
import org.altherian.hboxc.constant.ConsoleViewerAttributes;
import org.altherian.hboxc.core._ConsoleViewerReader;

public class ConsoleViewerOutput extends ObjectOutput implements _ConsoleViewerReader {
   
   public ConsoleViewerOutput(String id, String hypervisorId, String moduleId, String viewerPath, String args) {
      super(id);
      setSetting(new StringSettingIO(ConsoleViewerAttributes.HypervisorTypeId, hypervisorId));
      setSetting(new StringSettingIO(ConsoleViewerAttributes.ModuleId, moduleId));
      setSetting(new StringSettingIO(ConsoleViewerAttributes.ViewerPath, viewerPath));
      setSetting(new StringSettingIO(ConsoleViewerAttributes.Args, args));
   }
   
   @Override
   public String getHypervisorId() {
      return getSetting(ConsoleViewerAttributes.HypervisorTypeId).getString();
   }
   
   @Override
   public String getModuleId() {
      return getSetting(ConsoleViewerAttributes.ModuleId).getString();
   }
   
   @Override
   public String getViewerPath() {
      return getSetting(ConsoleViewerAttributes.ViewerPath).getString();
   }
   
   @Override
   public String getArgs() {
      return getSetting(ConsoleViewerAttributes.Args).getString();
   }
   
}
