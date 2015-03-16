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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.factory;

import org.altherian.hboxd.security.User;
import org.altherian.hboxd.security._User;

public class SecurityUserFactory {

   private SecurityUserFactory() {
      throw new RuntimeException("Not allowed");
   }

   public static _User get(String userId, String username) {
      return new User(userId, username);
   }

   public static _User get(String userId, String userName, String userDomain) {
      return new User(userId, userName, userDomain);
   }

   public static _User get(String userId, String userName, String userDomain, String salt) {
      _User user = get(userId, userName, userDomain);
      user.setSalt(salt);
      return user;
   }

}
