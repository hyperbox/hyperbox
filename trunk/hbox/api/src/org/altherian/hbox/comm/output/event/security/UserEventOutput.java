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

package org.altherian.hbox.comm.output.event.security;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.security.UserOutput;

import java.util.Date;

public abstract class UserEventOutput extends EventOutput {
   
   protected UserEventOutput() {
      // Used for serialization
   }
   
   public UserEventOutput(Date time, Enum<?> id, ServerOutput srvOut, UserOutput user) {
      super(time, id.toString(), srvOut);
      set(UserOutput.class, user);
   }
   
   public UserOutput getUser() {
      return get(UserOutput.class);
   }
   
   @Override
   public String toString() {
      return "User event occured at " + getTime() + " for user " + getUser().getDomainLogonName();
   }
   
}
