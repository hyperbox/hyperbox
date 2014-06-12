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

public class NetworkInterfaceTest {
   
   public static void validateFull(_NetworkInterface nic) {
      assertNotNull(nic);
      assertFalse(nic.getId().isEmpty());
      assertTrue(nic.getNicId() >= 0);
      assertNotNull(nic.getMachine());
      assertNotNull(nic.getType());
      assertFalse(nic.getMacAddress().isEmpty());
      assertFalse(nic.getAdapterType().isEmpty());
      assertFalse(nic.getAttachMode().isEmpty());
      assertNotNull(nic.getAttachName());
   }
   
}
