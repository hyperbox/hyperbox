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

package org.altherian.hboxc.comm.input;

import org.altherian.hboxc.core._ConsoleViewerReader;
import org.altherian.hboxc.core._ConsoleViewerWriter;

public class ConsoleViewerInput implements _ConsoleViewerReader, _ConsoleViewerWriter {
   
   private String id;
   private String hypervisorId;
   private String moduleId;
   private String viewerPath;
   private String args;
   
   public ConsoleViewerInput() {
   }
   
   public ConsoleViewerInput(String id) {
      this.id = id;
   }
   
   public ConsoleViewerInput(String id, String hypervisorId) {
      this(id);
      setHypervisorId(hypervisorId);
   }
   
   public ConsoleViewerInput(String id, String hypervisorId, String moduleId) {
      this(id, hypervisorId);
      setModuleId(moduleId);
   }
   
   public ConsoleViewerInput(String id, String hypervisorId, String moduleId, String viewerPath) {
      this(id, hypervisorId, moduleId);
      setViewer(viewerPath);
   }
   
   @Override
   public String getId() {
      return id;
   }
   
   /**
    * @return the hypervisorId
    */
   @Override
   public String getHypervisorId() {
      return hypervisorId;
   }
   
   /**
    * @param hypervisorId the hypervisorId to set
    */
   @Override
   public void setHypervisorId(String hypervisorId) {
      this.hypervisorId = hypervisorId;
   }
   
   /**
    * @return the moduleId
    */
   @Override
   public String getModuleId() {
      return moduleId;
   }
   
   /**
    * @param moduleId the moduleId to set
    */
   @Override
   public void setModuleId(String moduleId) {
      this.moduleId = moduleId;
   }
   
   /**
    * @return the viewerPath
    */
   @Override
   public String getViewerPath() {
      return viewerPath;
   }
   
   /**
    * @param viewerPath the viewerPath to set
    */
   @Override
   public void setViewer(String viewerPath) {
      this.viewerPath = viewerPath;
   }
   
   @Override
   public void setArgs(String args) {
      this.args = args;
   }
   
   @Override
   public String getArgs() {
      return args;
   }
   
}
