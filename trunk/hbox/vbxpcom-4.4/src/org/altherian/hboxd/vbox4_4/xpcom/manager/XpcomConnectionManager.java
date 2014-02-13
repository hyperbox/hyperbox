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

package org.altherian.hboxd.vbox4_4.xpcom.manager;

import org.altherian.hbox.exception.HypervisorException;
import org.altherian.tool.StringTools;
import org.altherian.tool.logging.Logger;

import org.virtualbox_4_4.ISession;
import org.virtualbox_4_4.IVirtualBox;
import org.virtualbox_4_4.VirtualBoxManager;

public class XpcomConnectionManager {
   
   protected final String defaultHome = "/usr/lib/virtualbox";
   protected String home;
   protected VirtualBoxManager vboxManager;
   
   public void start(String options) throws HypervisorException {
      Logger.track();
      
      if (!StringTools.isEmpty(options)) {
         home = options;
      } else {
         home = defaultHome;
      }
      
      System.setProperty("vbox.home", home);
      vboxManager = VirtualBoxManager.createInstance(null);
   }
   
   public void stop() {
      Logger.track();
      
      vboxManager.cleanup();
      vboxManager = null;
      
      Logger.info("Successfully disconnected from Virtualbox");
   }
   
   public String getHostname() {
      return "localhost";
   }
   
   public IVirtualBox getBox() {
      return vboxManager.getVBox();
   }
   
   public ISession getSession() {
      return vboxManager.getSessionObject();
   }
   
   protected String type() {
      return "XPCOM";
   }
   
   public String scheme() {
      return "vboxxpcom";
   }
   
}
