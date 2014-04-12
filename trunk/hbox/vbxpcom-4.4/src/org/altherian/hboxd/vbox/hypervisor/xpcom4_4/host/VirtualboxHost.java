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

package org.altherian.hboxd.vbox.hypervisor.xpcom4_4.host;

import org.altherian.hboxd.hypervisor.host._RawHost;
import org.altherian.hboxd.vbox4_4.xpcom.factory.ConnectionManager;

public class VirtualboxHost implements _RawHost {
   
   @Override
   public String getHostname() {
      return ConnectionManager.getHostname();
   }
   
   @Override
   public String getOSName() {
      return ConnectionManager.getBox().getHost().getOperatingSystem();
   }
   
   @Override
   public String getOSVersion() {
      return ConnectionManager.getBox().getHost().getOSVersion();
   }
   
   @Override
   public long getMemorySize() {
      return ConnectionManager.getBox().getHost().getMemorySize();
   }
   
   @Override
   public long getMemoryAvailable() {
      return ConnectionManager.getBox().getHost().getMemoryAvailable();
   }

}
