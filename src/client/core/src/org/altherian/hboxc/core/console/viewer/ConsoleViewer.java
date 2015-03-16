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

package org.altherian.hboxc.core.console.viewer;

import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.comm.io.factory.ConsoleViewerIoFactory;
import org.altherian.hboxc.core._ConsoleViewer;
import org.altherian.hboxc.event.EventManager;
import org.altherian.hboxc.event.consoleviewer.ConsoleViewerModifiedEvent;
import java.io.File;

public class ConsoleViewer implements _ConsoleViewer {

   private String id;
   private String hypervisorId;
   private String moduleId;
   private File viewerPath;
   private String args;

   public ConsoleViewer(String id, String hypervisorId, String moduleId, String viewerPath, String args) {
      this.id = id;
      this.hypervisorId = hypervisorId;
      this.moduleId = moduleId;
      this.viewerPath = new File(viewerPath);
      this.args = args;
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public String getHypervisorId() {
      return hypervisorId;
   }

   @Override
   public String getModuleId() {
      return moduleId;
   }

   @Override
   public String getViewerPath() {
      return viewerPath.getAbsolutePath();
   }

   @Override
   public String getArgs() {
      return args;
   }

   @Override
   public void setHypervisorId(String hypervisorId) {
      this.hypervisorId = hypervisorId;
   }

   @Override
   public void setModuleId(String moduleId) {
      this.moduleId = moduleId;
   }

   @Override
   public void setViewer(String viewerPath) {
      this.viewerPath = new File(viewerPath);
   }

   @Override
   public void setArgs(String args) {
      this.args = args;
   }

   @Override
   public void remove() {
      // nothing to do for now
   }

   @Override
   public void save() {
      if (!viewerPath.exists()) {
         throw new HyperboxRuntimeException(viewerPath + " does not exist");
      }
      if (!viewerPath.isAbsolute()) {
         throw new HyperboxRuntimeException(viewerPath + " is not an absolute path");
      }
      if (!viewerPath.isFile()) {
         throw new HyperboxRuntimeException(viewerPath + " is not a file");
      }

      EventManager.get().post(new ConsoleViewerModifiedEvent(ConsoleViewerIoFactory.getOut(this)));
   }

}
