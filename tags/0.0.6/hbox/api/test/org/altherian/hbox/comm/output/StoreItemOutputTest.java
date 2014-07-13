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

import org.altherian.hbox.constant.StoreItemAttributes;

public class StoreItemOutputTest {
   
   public static void validateFull(StoreItemOutput siOut) {
      validateSimple(siOut);
   }
   
   public static void validateSimple(StoreItemOutput siOut) {
      assertNotNull(siOut);
      
      assertTrue(siOut.hasSetting(StoreItemAttributes.Name));
      assertNotNull(siOut.getSetting(StoreItemAttributes.Name).getString());
      assertFalse(siOut.getSetting(StoreItemAttributes.Name).getString().isEmpty());
      
      assertTrue(siOut.hasSetting(StoreItemAttributes.Path));
      assertNotNull(siOut.getSetting(StoreItemAttributes.Path).getString());
      assertFalse(siOut.getSetting(StoreItemAttributes.Path).getString().isEmpty());
      
      assertTrue(siOut.hasSetting(StoreItemAttributes.Size));
      assertNotNull(siOut.getSetting(StoreItemAttributes.Size).getNumber());
      
      assertTrue(siOut.hasSetting(StoreItemAttributes.IsContainer));
      assertNotNull(siOut.getSetting(StoreItemAttributes.IsContainer).getBoolean());
   }
   
}
