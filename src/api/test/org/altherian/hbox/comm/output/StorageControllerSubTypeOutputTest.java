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
import org.altherian.hbox.comm.out.storage.StorageControllerSubTypeOut;

public final class StorageControllerSubTypeOutputTest {
   
   public static void validate(StorageControllerSubTypeOut scstOut) {
      assertFalse(scstOut.getId().isEmpty());
      assertFalse(scstOut.getParentType().isEmpty());
   }
   
   public static void compare(StorageControllerSubTypeOut scstOut1, StorageControllerSubTypeOut scstOut2) {
      assertTrue(scstOut1.getId().contentEquals(scstOut2.getId()));
      assertTrue(scstOut1.getParentType().contentEquals(scstOut2.getParentType()));
   }
   
}
