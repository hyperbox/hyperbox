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

package org.altherian.vbox4_3.storage;

import org.altherian.hboxd.hypervisor.storage._RawStorageControllerType;

public enum VbStorageControllerType implements _RawStorageControllerType {
   
   Floppy(1, 1, 2),
   IDE(1, 2, 2),
   SATA(1, 30, 1),
   SCSI(1, 16, 1),
   SAS(1, 8, 1);
   
   private long minPort;
   private long maxPort;
   private long maxDev;
   
   private VbStorageControllerType(long minPort, long maxPort, long maxDev) {
      this.minPort = minPort;
      this.maxPort = maxPort;
      this.maxDev = maxDev;
   }
   
   @Override
   public String getId() {
      return toString();
   }
   
   @Override
   public long getMinPort() {
      return minPort;
   }
   
   @Override
   public long getMaxPort() {
      return maxPort;
   }
   
   @Override
   public long getMaxDevPerPort() {
      return maxDev;
   }
   
}
