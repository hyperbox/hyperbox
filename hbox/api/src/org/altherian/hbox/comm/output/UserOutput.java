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

import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.UserAttributes;

public class UserOutput extends ObjectOutput {
   
   protected UserOutput() {

   }
   
   public UserOutput(String id, String username, String domain) {
      super(id);
      setSetting(new StringSettingIO(UserAttributes.Username, username));
      setSetting(new StringSettingIO(UserAttributes.Domain, domain));
   }
   
   public String getUsername() {
      return getSetting(UserAttributes.Username).getString();
   }
   
   public String getDomain() {
      return getSetting(UserAttributes.Domain).getString();
   }
   
   public String getDomainLogonName() {
      if (getDomain() != null) {
         return getDomain() + "\\" + getUsername();
      } else {
         return getUsername();
      }
   }
   
}
