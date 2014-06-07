package org.altherian.hboxd.vbox4_3.ws;

import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hboxd.hypervisor.Hypervisor;
import org.altherian.tool.logging.Logger;
import org.altherian.vbox4_3.VBoxHypervisor;

import java.net.URI;
import java.net.URISyntaxException;

import org.virtualbox_4_3.VBoxException;

@Hypervisor(
      id = "vbox-4.3-ws",
      typeId = "ws",
      vendor = "Oracle",
      product = "Virtualbox",
      schemes = { "vbox-4.3-ws" })
public class VBoxWSHypervisor extends VBoxHypervisor {
   
   protected final String defaultHost = "localhost";
   protected final int defaultPort = 18083;
   protected final String defaultUser = "";
   protected final String defaultPass = "";
   protected String hostname;
   
   @Override
   protected void connect(String options) {
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
         
         String connInfo = "http://" + host + ":" + port;
         Logger.debug("Connection info: " + connInfo);
         Logger.debug("User: " + username);
         Logger.debug("Password given: " + ((password != null) && !password.isEmpty()));
         vbMgr.connect("http://" + host + ":" + port, username, password);
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
   
}
