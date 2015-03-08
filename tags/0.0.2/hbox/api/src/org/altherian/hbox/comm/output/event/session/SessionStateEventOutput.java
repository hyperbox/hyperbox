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

package org.altherian.hbox.comm.output.event.session;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.SessionOutput;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.states.SessionStates;

import java.util.Date;

public final class SessionStateEventOutput extends EventOutput {
   
   private SessionStates state;
   
   @SuppressWarnings("unused")
   private SessionStateEventOutput() {
      // Used for serialization
   }
   
   public SessionStateEventOutput(Date time, ServerOutput srvOut, SessionOutput sOut, SessionStates state) {
      super(time, HyperboxEvents.SessionState, srvOut);
      set(SessionOutput.class, sOut);
      this.state = state;
   }
   
   public SessionStates getState() {
      return state;
   }
   
   public SessionOutput getSession() {
      return get(SessionOutput.class);
   }
   
   public String getSesionId() {
      return getSession().getId();
   }
   
   @Override
   public String toString() {
      return "Session ID #" + getSesionId() + " | State changed to " + getState() + " @ " + getTime();
   }
   
}