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

package org.altherian.hbox.comm;

import org.altherian.hbox.comm.out.event.EventOut;

/**
 * Represent a client into the communication process.<br/>
 * This will be created by classes implementing _Front within the server,</br>
 * to provide a way to send back information to the client from whom the request originated from.
 * 
 * @author noteirak
 */
public interface _Client extends _AnswerReceiver {
   
   /**
    * Get a unique identifier for the client.<br/>
    * This unique ID will typically be the username, or the IP:Port of the connection.<br/>
    * The value can potentially be anything and is left to the implementation, but always unique.
    * 
    * @return a String holding the unique ID for this client
    */
   public String getId();
   
   public String getAddress();
   
   /**
    * Allow to send an event to this particular client.<br/>
    * If you wish to broadcast an event to every client, use _Front.broadcast() in the Hyperbox Server API.
    * 
    * @param evOut The Event object to be send
    */
   public void post(EventOut evOut);
   
}
