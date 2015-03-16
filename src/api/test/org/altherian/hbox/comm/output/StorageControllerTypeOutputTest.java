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
import static org.junit.Assert.assertTrue;
import org.altherian.hbox.comm.out.storage.StorageControllerTypeOut;

public final class StorageControllerTypeOutputTest {

   public static void validate(StorageControllerTypeOut sctOut) {
      assertFalse(sctOut.getId().isEmpty());
      assertFalse(sctOut.getMinPort() <= 0);
      assertFalse(sctOut.getMaxPort() <= 0);
      assertFalse(sctOut.getMinPort() > sctOut.getMaxPort());
      assertFalse(sctOut.getMaxDevicePerPort() <= 0);
   }

   public static void compare(StorageControllerTypeOut sctOut1, StorageControllerTypeOut sctOut2) {
      assertTrue(sctOut1.getId().contentEquals(sctOut2.getId()));
      assertTrue(sctOut1.getMinPort() == sctOut2.getMinPort());
      assertTrue(sctOut1.getMaxPort() == sctOut2.getMaxPort());
      assertTrue(sctOut1.getMaxDevicePerPort() == sctOut2.getMaxDevicePerPort());
   }

}
