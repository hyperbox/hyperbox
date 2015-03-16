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

package org.altherian.hboxc.core.connector;

import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hboxc.server._Server;
import org.altherian.hboxc.state.ConnectionState;

public interface _Connector {

   public String getId();

   public String getLabel();

   public String getUsername();

   public void setUsername(String username);

   public void setLabel(String label);

   public String getAddress();

   public void setAddress(String address);

   public String getBackendId();

   public void setBackendId(String backendId);

   public _Server connect(UserIn usrIn);

   public void disconnect();

   public boolean isConnected();

   public ConnectionState getState();

   public _Server getServer();

   /**
    * Last known Server ID or null if never connected
    * 
    * @return Server ID as String
    */
   public String getServerId();

}
