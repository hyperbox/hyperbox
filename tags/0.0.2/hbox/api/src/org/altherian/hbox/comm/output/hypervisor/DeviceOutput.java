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

package org.altherian.hbox.comm.output.hypervisor;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.output.ObjectOutput;

import java.util.List;

public class DeviceOutput extends ObjectOutput {
   
   private String devType;
   
   protected DeviceOutput() {
      // Used for serialization
   }
   
   public DeviceOutput(List<SettingIO> settings) {
      super("0", settings);
   }
   
   public DeviceOutput(String id, List<SettingIO> settings) {
      super(id, settings);
   }
   
   /**
    * @return the devType
    */
   public String getDevType() {
      return devType;
   }
   
}
