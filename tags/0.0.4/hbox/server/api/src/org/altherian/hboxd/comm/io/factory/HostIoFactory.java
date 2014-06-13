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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.output.host.HostOutput;
import org.altherian.hbox.constant.HostAttributes;
import org.altherian.hboxd.host._Host;

import java.util.ArrayList;
import java.util.List;

public class HostIoFactory {
   
   private HostIoFactory() {
      // static class, cannot be instantiated
   }
   
   public static HostOutput get(_Host host) {
      List<SettingIO> sIoList = new ArrayList<SettingIO>();
      sIoList.add(new SettingIO(HostAttributes.Hostname, host.getHostname()));
      sIoList.add(new SettingIO(HostAttributes.OperatingSystemName, host.getOSName()));
      sIoList.add(new SettingIO(HostAttributes.OperatingSystemVersion, host.getOSVersion()));
      sIoList.add(new SettingIO(HostAttributes.MemoryTotal, host.getMemorySize()));
      sIoList.add(new SettingIO(HostAttributes.MemoryAvailable, host.getMemoryAvailable()));
      return new HostOutput(sIoList);
   }
   
}
