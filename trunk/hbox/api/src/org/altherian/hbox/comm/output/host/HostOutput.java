/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hbox.comm.output.host;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.output.ObjectOutput;
import org.altherian.hbox.constant.HostAttributes;

import java.util.Collection;

public class HostOutput extends ObjectOutput {
   
   @SuppressWarnings("unused")
   private HostOutput() {
      // Used for serialization
   }
   
   public HostOutput(Collection<SettingIO> sIoList) {
      setSetting(sIoList);
   }
   
   public String getHostname() {
      return getSetting(HostAttributes.Hostname).getString();
   }
   
   public String getOSName() {
      return getSetting(HostAttributes.OperatingSystemName).getString();
   }
   
   public String getOSVersion() {
      return getSetting(HostAttributes.OperatingSystemVersion).getString();
   }
   
   public Long getMemorySize() {
      return getSetting(HostAttributes.MemoryTotal).getNumber();
   }
   
   public Long getMemoryAvailable() {
      return getSetting(HostAttributes.MemoryAvailable).getNumber();
   }
   
}
