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

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.factory.SettingIoFactory;
import org.altherian.hbox.comm.out.storage.StorageControllerOut;
import org.altherian.hbox.comm.out.storage.StorageDeviceAttachmentOut;
import org.altherian.hboxd.core.model._MediumAttachment;
import org.altherian.hboxd.core.model._StorageController;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;

import java.util.ArrayList;
import java.util.List;

public final class StorageControllerIoFactory {
   
   private StorageControllerIoFactory() {
      // Static class - cannot be instantiated
   }
   
   public static StorageControllerOut get(_StorageController sc) {
      List<SettingIO> settingsOut = SettingIoFactory.getList(sc.getSettings());

      List<StorageDeviceAttachmentOut> attachmentsOut = new ArrayList<StorageDeviceAttachmentOut>();
      for (_MediumAttachment attachment : sc.listMediumAttachment()) {
         attachmentsOut.add(MediumAttachmentIoFactory.get(attachment));
      }
      
      StorageControllerOut scIo = new StorageControllerOut(sc.getMachineUuid(), sc.getId(), settingsOut, attachmentsOut);
      return scIo;
   }
   
   public static StorageControllerOut get(_RawStorageController sc) {
      StorageControllerOut scIo = new StorageControllerOut(sc.getMachineUuid(), sc.getName(), SettingIoFactory.getList(sc.listSettings()));
      return scIo;
   }
   
}
