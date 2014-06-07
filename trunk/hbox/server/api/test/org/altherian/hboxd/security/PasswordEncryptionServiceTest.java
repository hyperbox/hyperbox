/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxd.security;

import static org.junit.Assert.assertTrue;

import org.altherian.security.PasswordEncryptionService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import org.junit.Test;

public class PasswordEncryptionServiceTest {
   
   @Test
   public void test() throws NoSuchAlgorithmException, InvalidKeySpecException {
      byte[] firstPass = PasswordEncryptionService.getEncryptedPassword("test".toCharArray(), "AhahahEREZRRZ".getBytes());
      byte[] secondPass = PasswordEncryptionService.getEncryptedPassword("test".toCharArray(), "AhahahEREZRRZ".getBytes());
      System.out.println(firstPass.length);
      System.out.println(secondPass.length);
      assertTrue(Arrays.equals(firstPass, secondPass));
   }
   
}
