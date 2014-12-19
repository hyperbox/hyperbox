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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hbox.comm.output;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.storage.StorageControllerOut;
import org.altherian.hbox.constant.StorageControllerAttribute;
import org.altherian.hbox.constant.StorageControllerSubType;
import org.altherian.hbox.constant.StorageControllerType;
import java.util.Arrays;
import java.util.UUID;
import org.junit.Test;

public final class StorageControllerOutputTest {
   
   @Test
   public void basicTest() {
      String id = "Test";
      SettingIO name = new StringSettingIO(StorageControllerAttribute.Name, "Test");
      SettingIO type = new StringSettingIO(StorageControllerAttribute.Type, StorageControllerType.SATA.toString());
      SettingIO subType = new StringSettingIO(StorageControllerAttribute.SubType, StorageControllerSubType.IntelAhci.toString());
      
      StorageControllerOut scOut = new StorageControllerOut(UUID.randomUUID().toString(), id, Arrays.asList(name, type, subType));
      validateSimple(scOut);
      assertTrue(scOut.getId().contentEquals(id));
      assertTrue(scOut.getName().contentEquals(name.getString()));
      assertTrue(scOut.getSetting(StorageControllerAttribute.Type).getString().contentEquals(type.getString()));
      assertTrue(scOut.getSetting(StorageControllerAttribute.SubType).getString().contentEquals(subType.getString()));
      assertTrue(scOut.getType().contentEquals(type.getString()));
      assertTrue(scOut.getSetting(StorageControllerAttribute.SubType).getString().contentEquals(subType.getString()));
      assertTrue(scOut.getSubType().contentEquals(subType.getString()));
   }
   
   public static void validateSimple(StorageControllerOut scOut) {
      assertNotNull(scOut);
      assertNotNull(scOut.getId());
      assertFalse(scOut.getId().isEmpty());
      assertNotNull(scOut.getName());
      assertFalse(scOut.getName().isEmpty());
   }
   
   public static void validateFull(StorageControllerOut scOut) {
      validateSimple(scOut);
      assertNotNull(scOut.getType());
      assertNotNull(scOut.getSubType());
      assertNotNull(scOut.getMinPortCount());
      assertNotNull(scOut.getMaxPortCount());
      assertNotNull(scOut.getMaxDeviceCount());
      assertTrue(scOut.getMaxPortCount() >= 0);
      assertTrue(scOut.getMinPortCount() >= 0);
      assertTrue(scOut.getMaxPortCount() >= scOut.getMinPortCount());
   }
   
}
