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

package org.altherian.hbox.comm.output;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.altherian.hbox.constant.StoreAttributes;

public final class StoreOutputTest {
   
   public static void validateFull(StoreOutput sOut) {
      validateSimple(sOut);
      
      assertTrue(sOut.hasSetting(StoreAttributes.Location));
      assertNotNull(sOut.getSetting(StoreAttributes.Location).getString());
      assertFalse(sOut.getSetting(StoreAttributes.Location).getString().isEmpty());
   }
   
   public static void validateSimple(StoreOutput sOut) {
      assertNotNull(sOut);
      assertNotNull(sOut.getId());
      assertFalse(sOut.getId().isEmpty());
      
      assertTrue(sOut.hasSetting(StoreAttributes.Label));
      assertNotNull(sOut.getSetting(StoreAttributes.Label).getString());
      assertFalse(sOut.getSetting(StoreAttributes.Label).getString().isEmpty());
      
      assertTrue(sOut.hasSetting(StoreAttributes.State));
      assertNotNull(sOut.getSetting(StoreAttributes.State).getString());
      assertFalse(sOut.getSetting(StoreAttributes.State).getString().isEmpty());
   }
   
}
