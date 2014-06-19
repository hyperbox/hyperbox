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

package org.altherian.hboxc.core.connector;

import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.comm.io.factory.ConnectorIoFactory;
import org.altherian.hboxc.event.CoreEventManager;
import org.altherian.hboxc.event.connector.ConnectorConnectedEvent;
import org.altherian.hboxc.event.connector.ConnectorDisconnectedEvent;
import org.altherian.hboxc.event.connector.ConnectorStateChangedEvent;
import org.altherian.hboxc.event.server.ServerDisconnectedEvent;
import org.altherian.hboxc.factory.ServerFactory;
import org.altherian.hboxc.server._Server;
import org.altherian.hboxc.state.ConnectionState;
import org.altherian.tool.AxStrings;
import org.altherian.tool.logging.Logger;

public class Connector implements _Connector {
   
   private String id;
   private String label;
   private String address;
   private String username;
   private String backendId;
   private String serverId;
   private _Server server;
   private ConnectionState state;
   
   public Connector(String id, String label, String address, String username, String backendId) {
      Logger.track();
      
      this.id = id;
      setLabel(label);
      setAddress(address);
      setUsername(username);
      setBackendId(backendId);
   }
   
   private void setState(ConnectionState state) {
      Logger.track();
      
      if ((this.state == null) || !this.state.equals(state)) {
         this.state = state;
         if (state.equals(ConnectionState.Connected)) {
            CoreEventManager.post(new ConnectorConnectedEvent(ConnectorIoFactory.get(this)));
         } else if (state.equals(ConnectionState.Disconnected)) {
            CoreEventManager.post(new ConnectorDisconnectedEvent(ConnectorIoFactory.get(this)));
         } else {
            CoreEventManager.post(new ConnectorStateChangedEvent(ConnectorIoFactory.get(this), state));
         }
      } else {
         Logger.debug("Ignoring setState() - " + getState() + " is same as current");
      }
   }
   
   @Override
   public String getId() {
      return id;
   }
   
   @Override
   public String getLabel() {
      return AxStrings.isEmpty(label) ? getAddress() : label;
   }
   
   @Override
   public void setLabel(String label) {
      this.label = label;
   }
   
   @Override
   public String getAddress() {
      return address;
   }
   
   @Override
   public String getUsername() {
      return username;
   }
   
   @Override
   public void setUsername(String username) {
      this.username = username;
   }
   
   @Override
   public void setAddress(String address) {
      Logger.track();
      
      if (address.isEmpty()) {
         throw new HyperboxRuntimeException("Address cannot be empty");
      }
      
      this.address = address;
   }
   
   @Override
   public String getBackendId() {
      return backendId;
   }
   
   @Override
   public void setBackendId(String backendId) {
      Logger.track();
      
      this.backendId = backendId;
   }
   
   @Override
   public _Server connect(UserInput usrIn) {
      Logger.track();
      
      setState(ConnectionState.Connecting);
      
      try {
         CoreEventManager.register(this);
         server = ServerFactory.get();
         server.connect(address, backendId, usrIn);
         serverId = server.getId();
         label = server.getName();
         setState(ConnectionState.Connected);
         
         return server;
      } catch (Throwable e) {
         Logger.exception(e);
         disconnect();
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public void disconnect() {
      Logger.track();
      
      if (getState().equals(ConnectionState.Connected) || getState().equals(ConnectionState.Connecting)) {
         setState(ConnectionState.Disconnecting);
         
         if (server != null) {
            server.disconnect();
            server = null;
         }
         
         setState(ConnectionState.Disconnected);
         CoreEventManager.unregister(this);
      } else {
         Logger.debug("Ignoring disconnect call, already in " + getState() + " state");
      }
   }
   
   @Override
   public boolean isConnected() {
      return (server != null) && server.isConnected();
   }
   
   @Override
   public _Server getServer() {
      if (!isConnected()) {
         throw new HyperboxRuntimeException("Server is not connected");
      }
      return server;
   }
   
   @Override
   public ConnectionState getState() {
      return state == null ? ConnectionState.Disconnected : state;
   }
   
   @Handler
   public void putServerDisconnectEvent(ServerDisconnectedEvent ev) {
      Logger.track();
      
      if (ev.getServer().getId().equals(server.getId())) {
         Logger.track();
         disconnect();
      } else {
         Logger.track();
      }
   }
   
   @Override
   public String getServerId() {
      return serverId;
   }
   
   
   
}
