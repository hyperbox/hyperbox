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

package org.altherian.hboxc.core.back.kryonet;

import org.altherian.hbox.Configuration;
import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._AnswerReceiver;
import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.kryonet.KryoRegister;
import org.altherian.hbox.kryonet.KryoUncaughtExceptionHandler;
import org.altherian.hbox.kryonet.KryonetDefaultSettings;
import org.altherian.hboxc.back._Backend;
import org.altherian.hboxc.event.EventManager;
import org.altherian.hboxc.event.backend.BackendConnectionStateEvent;
import org.altherian.hboxc.event.backend.BackendStateEvent;
import org.altherian.hboxc.state.BackendConnectionState;
import org.altherian.hboxc.state.BackendStates;
import org.altherian.tool.logging.Logger;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public final class KryonetClientBack implements _Backend, UncaughtExceptionHandler {
   
   private Map<String, _AnswerReceiver> ansReceivers;
   private Client client;
   private volatile BackendStates state = BackendStates.Stopped;
   private volatile BackendConnectionState connState = BackendConnectionState.Disconnected;
   
   @Override
   public String getId() {
      return "Kryonet";
   }
   
   private void setState(BackendStates state) {
      Logger.track();
      
      if ((state != null) && !this.state.equals(state)) {
         this.state = state;
         EventManager.post(new BackendStateEvent(this, state));
      } else {
         Logger.debug("Got a null state or state matches current one");
      }
   }
   
   private void setState(BackendConnectionState connState) {
      Logger.track();
      
      if ((connState != null) && !this.connState.equals(connState)) {
         this.connState = connState;
         EventManager.post(new BackendConnectionStateEvent(this, connState));
      } else {
         Logger.debug("Got a null state or state matches current one");
      }
   }
   
   @Override
   public void start() throws HyperboxException {
      Logger.track();
      
      setState(BackendStates.Starting);
      try {
         Logger.info("Backend Init Sequence started");
         ansReceivers = new HashMap<String, _AnswerReceiver>();
         int netBufferWriteSize = Integer.parseInt(Configuration.getSetting(KryonetDefaultSettings.CFGKEY_KRYO_NET_WRITE_BUFFER_SIZE,
               KryonetDefaultSettings.CFGVAL_KRYO_NET_WRITE_BUFFER_SIZE));
         int netBufferObjectSize = Integer.parseInt(Configuration.getSetting(KryonetDefaultSettings.CFGVAL_KRYO_NET_OBJECT_BUFFER_SIZE,
               KryonetDefaultSettings.CFGVAL_KRYO_NET_OBJECT_BUFFER_SIZE));
         client = new Client(netBufferWriteSize, netBufferObjectSize);
         client.start();
         KryoRegister.register(client.getKryo());
         client.addListener(new MainListener());
         client.getUpdateThread().setUncaughtExceptionHandler(new KryoUncaughtExceptionHandler());
         Logger.info("Backend Init Sequence completed");
         setState(BackendStates.Started);
      } catch (NumberFormatException e) {
         Logger.error("Invalid configuration value");
         stop(e);
      } catch (Throwable e) {
         stop(e);
      }
      
   }
   
   private void stop(Throwable e) throws HyperboxException {
      Logger.error("Backend Init Sequence failed");
      stop();
      throw new HyperboxException("Unable to connect to init Kryonet backend : " + e.getMessage());
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      setState(BackendStates.Stopping);
      disconnect();
      if (client != null) {
         client.stop();
      }
      setState(BackendStates.Stopped);
   }
   
   @Override
   public void setAnswerReceiver(String requestId, _AnswerReceiver ar) {
      ansReceivers.put(requestId, ar);
   }
   
   @Override
   public void connect(String address) throws HyperboxException {
      Logger.track();
      
      if (!state.equals(BackendStates.Started)) {
         throw new HyperboxException("Backend is not initialized");
      }
      
      setState(BackendConnectionState.Connecting);
      
      String[] options = address.split(":", 2);
      String host = options[0];
      Integer port = options.length == 2 ? Integer.parseInt(options[1]) : Integer.parseInt(KryonetDefaultSettings.CFGVAL_KRYO_NET_TCP_PORT);
      if (options.length == 2) {
         try {
            port = Integer.parseInt(options[1]);
         } catch (NumberFormatException e) {
            throw new HyperboxException("Invalid port number: " + options[1]);
         }
      }
      
      try {
         client.connect(5000, host, port);
         client.getUpdateThread().setUncaughtExceptionHandler(new KryoUncaughtExceptionHandler());
         Logger.info("Backend connected");
         setState(BackendConnectionState.Connected);
      } catch (IOException e) {
         String message = "Backend connect error - " + e.getClass().getSimpleName() + ": " + e.getMessage();
         if (e.getCause() != null) {
            message = message + e.getCause().getMessage();
         }
         Logger.debug(message);
         disconnect();
         throw new HyperboxException(message, e);
      }
   }
   
   @Override
   public void disconnect() {
      Logger.track();
      
      if ((client != null) && client.isConnected()) {
         setState(BackendConnectionState.Disconnecting);
         client.close();
      }
      setState(BackendConnectionState.Disconnected);
   }
   
   @Override
   public boolean isConnected() {
      return (client != null) && client.isConnected();
   }
   
   @Override
   public void putRequest(Request req) {
      Logger.track();
      
      if (!isConnected()) {
         Logger.debug("Tried to send a message but client is not connected");
         throw new HyperboxRuntimeException("Client is not connected to a server");
      }
      
      Logger.debug("Sending request");
      client.sendTCP(req);
      Logger.debug("Send request");
   }
   
   private class MainListener extends Listener {
      
      @Override
      public void connected(Connection connection) {
         Logger.track();
         
         Logger.info(connection.getRemoteAddressTCP().getAddress() + " connected.");
         setState(BackendConnectionState.Connected);
      }
      
      @Override
      public void received(Connection connection, Object object) {
         if (object.getClass().equals(Answer.class)) {
            Answer ans = (Answer) object;
            Logger.debug("Received answer from server : " + ans.getExchangeId() + " - " + ans.getType() + " - " + ans.getCommand() + " - " + ans.getName());
            if (ansReceivers.containsKey(ans.getExchangeId())) {
               ansReceivers.get(ans.getExchangeId()).putAnswer(ans);
               if (ans.isExchangedFinished() && !ans.getType().equals(AnswerType.QUEUED)) {
                  ansReceivers.remove(ans.getExchangeId());
               }
            } else {
               Logger.warning("Oprhan answer: " + ans.getExchangeId() + " - " + ans.getType() + " - " + ans.getCommand() + " - " + ans.getName());
            }
         }
         if (object instanceof EventOut) {
            EventManager.get().post(object);
         }
      }
      
      @Override
      public void disconnected(Connection connection) {
         Logger.track();
         
         Logger.info("Disconnected from Hyperbox server");
         disconnect();
      }
   }
   
   @Override
   public void uncaughtException(Thread t, Throwable e) {
      Logger.error("Error in protocol: " + e.getMessage());
      stop();
      throw new HyperboxRuntimeException("Error in protocol", e);
   }
   
}
