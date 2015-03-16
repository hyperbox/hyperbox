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

package org.altherian.hboxd.security;

import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.tool.AxStrings;

public class User implements _User {

   private String id;
   private String name;
   private String domain;
   private String salt;

   public User(String id, String name) {
      this.id = id;
      setName(name);
      setSalt(name);
   }

   public User(String id, String name, String domain) {
      this(id, name);
      setDomain(domain);
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getDomain() {
      return domain;
   }

   @Override
   public String getDomainLogonName() {
      if ((getDomain() != null) && !getDomain().isEmpty()) {
         return getDomain() + "\\" + getName();
      } else {
         return getName();
      }

   }

   @Override
   public boolean isAnnonyomous() {
      return false;
   }

   @Override
   public void setName(String name) {
      this.name = name;
   }

   @Override
   public void setDomain(String domain) {
      this.domain = domain;
   }

   @Override
   public String getSalt() {
      return salt;
   }

   @Override
   public void setSalt(String salt) {
      this.salt = salt;
   }

   @Override
   public void save() {
      if (AxStrings.isEmpty(getId())) {
         throw new HyperboxRuntimeException("ID cannot be empty");
      }
      if (AxStrings.isEmpty(getName())) {
         throw new HyperboxRuntimeException("Name cannot be empty");
      }
   }

   @Override
   public void delete() {
      if (getId().contentEquals("0")) {
         throw new HyperboxRuntimeException("Cannot delete the default admin account");
      }
   }

}
