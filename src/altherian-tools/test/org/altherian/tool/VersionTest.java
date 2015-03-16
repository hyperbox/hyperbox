/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.tool;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class VersionTest {

   private final static String v1_0_0 = "1.0.0";
   private final static String v2_3_4 = "2.3.4";
   private final static String v3_4_5r67 = "3.4.5-67";
   private final static String invalid1 = "invalidversion";
   private final static String invalid2 = "8.9-0";
   private final static String invalid3 = "";
   private final static String invalid4 = null;

   @Test
   public void unknown() {
      Version unknown = new Version(Version.UNKNOWN.toString());
      assertTrue(Version.UNKNOWN.equals(unknown));
   }

   @Test
   public void valid() {
      assertTrue(Version.UNKNOWN.isValid());
      assertTrue(new Version(v1_0_0).isValid());
      assertTrue(new Version(v2_3_4).isValid());
      assertTrue(new Version(v3_4_5r67).isValid());

      assertFalse(new Version(invalid1).isValid());
      assertFalse(new Version(invalid2).isValid());
      assertFalse(new Version(invalid3).isValid());
      assertFalse(new Version(invalid4).isValid());
   }

   @Test
   public void equal() {
      assertTrue(Version.UNKNOWN.equals(Version.UNKNOWN));
      assertTrue((new Version(v1_0_0)).equals(new Version(v1_0_0)));
      assertTrue((new Version(v2_3_4)).equals(new Version(v2_3_4)));
      assertTrue((new Version(v3_4_5r67)).equals(new Version(v3_4_5r67)));
   }

}
