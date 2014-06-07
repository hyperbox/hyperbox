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

package org.altherian.hboxd.session;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._RequestReceiver;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.states.SessionStates;
import org.altherian.hboxd.security._User;

import java.util.Date;

public interface _Session extends Runnable, _RequestReceiver {
   
   public void close();
   
   public String getId();
   
   public Date getCreateTime();
   
   public SessionStates getState();
   
   public _User getUser();
   
   @Override
   public void putRequest(Request r);
   
   public void post(EventOutput evOut);
   
   public void login();

   public void logout();
   
}
