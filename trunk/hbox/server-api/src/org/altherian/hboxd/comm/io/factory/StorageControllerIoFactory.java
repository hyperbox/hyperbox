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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.output.storage.StorageControllerOutput;
import org.altherian.hboxd.business._StorageController;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;

public final class StorageControllerIoFactory {
   
   private StorageControllerIoFactory() {
      // Static class - cannot be instantiated
   }
   
   public static StorageControllerOutput get(_StorageController sc) {
      StorageControllerOutput scIo = new StorageControllerOutput(sc.getMachineUuid(), sc.getId(), SettingIoFactory.getList(sc.getSettings()));
      return scIo;
   }
   
   public static StorageControllerOutput get(_RawStorageController sc) {
      StorageControllerOutput scIo = new StorageControllerOutput(sc.getMachineUuid(), sc.getName(), SettingIoFactory.getList(sc.listSettings()));
      return scIo;
   }
   
}
