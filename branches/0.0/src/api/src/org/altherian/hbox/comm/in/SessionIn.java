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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.constant.EntityType;

public class SessionIn extends ObjectIn<EntityType> {
   
   private String domain;
   private String username;
   private String password;
   
   private SessionIn() {
      super(EntityType.Session);
   }
   
   public SessionIn(String id) {
      super(EntityType.Session, id);
   }
   
   /**
    * @return the domain
    */
   public String getDomain() {
      return domain;
   }
   
   /**
    * @param domain the domain to set
    */
   public void setDomain(String domain) {
      this.domain = domain;
   }
   
   /**
    * @return the username
    */
   public String getUsername() {
      return username;
   }
   
   /**
    * @param username the username to set
    */
   public void setUsername(String username) {
      this.username = username;
   }
   
   /**
    * @return the password
    */
   public String getPassword() {
      return password;
   }
   
   /**
    * @param password the password to set
    */
   public void setPassword(String password) {
      this.password = password;
   }
   
}
