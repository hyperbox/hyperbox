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

package org.altherian.hboxd.core.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StorageControllerTest {
   
   public static void validateSimple(_StorageController sto) {
      assertNotNull(sto);
      assertFalse(sto.getId().isEmpty());
      assertFalse(sto.getMachineUuid().isEmpty());
      assertNotNull(sto.getType());
   }
   
   public static void validateFull(_StorageController sto) {
      validateSimple(sto);
      assertFalse(sto.getName().isEmpty());
      assertNotNull(sto.getMachine());
      assertNotNull(sto.getControllerType());
      assertNotNull(sto.getControllerSubType());
      assertTrue(sto.getMaxDeviceCount() > 0);
      assertTrue(sto.getMaxPortCount() >= 0);
      assertTrue(sto.getPortCount() >= 0);
   }
   
}
