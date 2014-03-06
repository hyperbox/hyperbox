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

package org.altherian.hboxc.factory;

import org.altherian.hboxc.comm.input.ConnectorInput;
import org.altherian.hboxc.core.connector.Connector;
import org.altherian.hboxc.core.connector._Connector;

public class ConnectorFactory {
   
   private ConnectorFactory() {
      // only static
   }
   
   public static _Connector get(String id, String address, String username, String backendId) {
      return new Connector(id, address, address, username, backendId);
   }
   
   public static void update(_Connector conn, ConnectorInput conIn) {
      conn.setAddress(conIn.getAddress());
      conn.setBackendId(conIn.getBackendId());
   }
}
