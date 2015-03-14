/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

import java.util.ArrayList;
import java.util.List;
import org.altherian.hbox.hypervisor._MachineLogFile;

public class MachineLogFileIO implements _MachineLogFile {
   
   private String id;
   private String filename;
   private List<String> log;
   
   @Override
   public String getId() {
      return id;
   }
   
   @Override
   public List<String> getLog() {
      return log;
   }
   
   @Override
   public String getFileName() {
      return filename;
   }
   
   protected MachineLogFileIO() {
      // only for serialization
   }
   
   public MachineLogFileIO(String id, String filename, List<String> log) {
      this.id = id;
      this.log = new ArrayList<String>(log);
      this.filename = filename;
   }
   
   public MachineLogFileIO(String id) {
      this.id = id;
   }
   
   /*   public void setId(String id) {
         this.id = id;
      }

      public void setLog(List<String> log) {
         this.log = log;
      }

      public void setFileName(String filename) {
         this.filename = filename;
      }
    */
   
}
