/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxd.vbox4_3.ws;

import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hboxd.hypervisor.Hypervisor;
import org.altherian.tool.logging.Logger;
import org.altherian.vbox4_3.VBoxHypervisor;

import java.net.URI;
import java.net.URISyntaxException;

import org.virtualbox_4_3.VBoxException;
import org.virtualbox_4_3.VirtualBoxManager;

@Hypervisor(
      id = "vbox-4.3-ws",
      typeId = "ws",
      vendor = "Oracle",
      product = "Virtualbox",
      schemes = { "vbox-4.3-ws" })
public final class VBoxWSHypervisor extends VBoxHypervisor {
   
   protected final String defaultHost = "localhost";
   protected final int defaultPort = 18083;
   protected final String defaultUser = "";
   protected final String defaultPass = "";
   protected String hostname;
   
   @Override
   public String getId() {
      return this.getClass().getAnnotation(Hypervisor.class).id();
   }
   
   @Override
   public String getTypeId() {
      return this.getClass().getAnnotation(Hypervisor.class).typeId();
   }
   
   @Override
   public String getVendor() {
      return this.getClass().getAnnotation(Hypervisor.class).vendor();
   }
   
   @Override
   public String getProduct() {
      return this.getClass().getAnnotation(Hypervisor.class).product();
   }
   
   @Override
   protected VirtualBoxManager connect(String options) {
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
         } catch (URISyntaxException e) {
            throw new HypervisorException("Invalid options syntax: " + e.getMessage(), e);
         }
      }
      
      try {
         hostname = host;
         Logger.debug("Using Web Services");
         
         VirtualBoxManager mgr = VirtualBoxManager.createInstance(null);
         
         String connInfo = "http://" + host + ":" + port;
         Logger.debug("Connection info: " + connInfo);
         Logger.debug("User: " + username);
         Logger.debug("Password given: " + ((password != null) && !password.isEmpty()));
         mgr.connect("http://" + host + ":" + port, username, password);
         
         return mgr;
      } catch (VBoxException e) {
         throw new HypervisorException("Unable to connect to the Virtualbox WebServices : " + e.getMessage(), e);
      }
   }
   
   @Override
   protected void disconnect() {
      try {
         vbMgr.disconnect();
      } catch (Throwable t) {
         Logger.debug("Error when disconnecting : " + t.getMessage());
      }
   }
   
   @Override
   public void finalize() {
      Logger.warning(this + " has been called for garbage collection");
   }
   
}
