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

package org.altherian.hbox.comm.out;

import org.altherian.hbox.comm.out.security.UserOut;
import org.altherian.hbox.constant.Entity;

import java.util.Date;

public class SessionOut extends ObjectOut {
   
   private UserOut user;
   private String state;
   private Date createTime;
   
   @SuppressWarnings("unused")
   private SessionOut() {
      // used for (de)serialisation
   }
   
   public SessionOut(String id, UserOut user, String state, Date createTime) {
      super(Entity.Session, id);
      this.user = user;
      this.state = state;
      this.createTime = createTime;
   }
   
   public UserOut getUser() {
      return user;
   }
   
   public String getState() {
      return state;
   }
   
   public Date getCreateTime() {
      return createTime;
   }
   
}
