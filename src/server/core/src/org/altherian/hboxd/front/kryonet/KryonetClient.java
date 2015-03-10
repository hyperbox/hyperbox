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

package org.altherian.hboxd.front.kryonet;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm._Client;
import org.altherian.hbox.comm.out.event.EventOut;
import com.esotericsoftware.kryonet.Connection;

public class KryonetClient implements _Client {
   
   private Connection client;
   private String id;
   private String address;
   
   public KryonetClient(Connection client) {
      this.client = client;
      id = Integer.toString(client.getID());
      address = client.getRemoteAddressTCP().getAddress().getHostAddress() + ":" + client.getRemoteAddressTCP().getPort();
   }
   
   @Override
   public String getId() {
      return id;
   }
   
   @Override
   public String getAddress() {
      return address;
   }
   
   @Override
   public void putAnswer(Answer ans) {
      
      client.sendTCP(ans);
   }
   
   @Override
   public void post(EventOut evOut) {
      
      client.sendTCP(evOut);
   }
   
   @Override
   public String toString() {
      return "Client ID #" + getId() + " (" + getAddress() + ")";
   }
   
}
