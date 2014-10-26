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

package org.altherian.hboxd.hypervisor.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RawStorageControllerSubTypeTest {
   
   public static void validate(_RawStorageControllerSubType rawSubtype) {
      assertFalse(rawSubtype.getId().isEmpty());
      assertFalse(rawSubtype.getParentType().isEmpty());
   }
   
   public static void compare(_RawStorageControllerSubType rawSubtype1, _RawStorageControllerSubType rawSubtype2) {
      assertTrue(rawSubtype1.getId().contentEquals(rawSubtype2.getId()));
      assertTrue(rawSubtype1.getParentType().contentEquals(rawSubtype2.getParentType()));
   }
   
}
