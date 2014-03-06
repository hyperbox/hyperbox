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

package org.altherian.hboxd.vbox4_3.ws.manager;

import org.altherian.hbox.exception.HypervisorException;
import org.altherian.tool.logging.Logger;

import java.net.URI;
import java.net.URISyntaxException;

import org.virtualbox_4_3.ISession;
import org.virtualbox_4_3.IVirtualBox;
import org.virtualbox_4_3.VBoxException;
import org.virtualbox_4_3.VirtualBoxManager;

public class WebServiceConnectionManager {
   
   protected final String defaultHost = "localhost";
   protected final int defaultPort = 18083;
   protected final String defaultUser = "";
   protected final String defaultPass = "";
   protected String hostname;
   protected VirtualBoxManager vboxManager;
   
   public void start(String options) throws HypervisorException {
      Logger.track();
      
      String host = defaultHost;
      int port = defaultPort;
      String username = defaultUser;
      String password = defaultPass;
      
      if ((options != null) && !options.isEmpty()) {
         try {
            if (!options.contains("://")) {
               options = "http://" + options;
            }
            Logger.debug("Given connect options: " + options);
            URI uri = new URI(options);
            host = uri.getHost();
            if (uri.getPort() > 0) {
               port = uri.getPort();
            }
            if (uri.getUserInfo() != null) {
               String[] userInfo = uri.getUserInfo().split(":", 2);
               username = userInfo[0];
               if (userInfo.length == 2) {
                  password = userInfo[1];
               }
            }
         } catch (URISyntaxException e1) {
            throw new HypervisorException("Invalid options syntax: " + e1.getMessage(), e1);
         }
      }
      
      try {
         hostname = host;
         vboxManager = VirtualBoxManager.createInstance(null);
         Logger.debug("Using Web Services");
         connect(host, port, username, password);
         if (!vboxManager.getVBox().getAPIVersion().contentEquals("4_3")) {
            throw new HypervisorException("Missmatch API Connector: Server is " + vboxManager.getVBox().getAPIVersion() + " but the connector handles 4_3");
         }
      } catch (VBoxException e) {
         throw new HypervisorException("Unable to connect to the Virtualbox WebServices : " + e.getMessage(), e);
      }
   }
   
   public void stop() {
      Logger.track();
      
      try {
         disconnect();
         Logger.info("Successfully disconnected from Virtualbox");
      } catch (Throwable e) {
         Logger.warning("An error occurred while disconnecting: " + e.getMessage());
         Logger.exception(e);
      }
      
      hostname = null;
      vboxManager = null;
   }
   
   public String getHostname() {
      return hostname;
   }
   
   public IVirtualBox getBox() {
      return vboxManager.getVBox();
   }
   
   public ISession getSession() {
      return vboxManager.getSessionObject();
   }
   
   protected String type() {
      return "Web Service";
   }
   
   private void connect(String host, Integer port, String username, String pass) {
      Logger.track();
      
      String connInfo = "http://" + host + ":" + port;
      Logger.debug("Connection info: " + connInfo);
      Logger.debug("User: " + username);
      Logger.debug("Password given: " + ((pass != null) && !pass.isEmpty()));
      vboxManager.connect("http://" + host + ":" + port, username, pass);
   }
   
   private void disconnect() {
      Logger.track();
      
      vboxManager.disconnect();
   }
   
   public String scheme() {
      return "vboxws";
   }
   
}
