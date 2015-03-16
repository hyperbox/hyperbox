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

package org.altherian.hbox.comm.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import org.altherian.hbox.comm.in.MachineIn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MachineInputTest {

   private MachineIn mIn;

   @Before
   public void before() {
      mIn = new MachineIn();
   }

   @After
   public void after() {
      mIn = null;
   }

   @Test
   public void uuidTest() {
      assertNull("Unset uuid should be null", mIn.getUuid());
      assertFalse(mIn.hasNewData());
   }

}
