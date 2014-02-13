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

package org.altherian.hboxd.vbox4_4.xpcom.factory;

import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hboxd.vbox4_4.xpcom.manager.XpcomConnectionManager;

import org.virtualbox_4_4.IMachine;
import org.virtualbox_4_4.ISession;
import org.virtualbox_4_4.IVirtualBox;

public class ConnectionManager {
   
   private static XpcomConnectionManager connMgr;
   
   private static XpcomConnectionManager get() {
      if (connMgr == null) {
         connMgr = new XpcomConnectionManager();
      }
      return connMgr;
   }
   
   public static void start(String options) throws HypervisorException {
      get().start(options);
   }
   
   public static void stop() {
      get().stop();
   }
   
   public static IVirtualBox getBox() {
      return get().getBox();
   }
   
   public static String getHostname() {
      return get().getHostname();
   }
   
   public static IMachine findMachine(String id) {
      return get().getBox().findMachine(id);
   }
   
   public static ISession getSession() {
      return get().getSession();
   }
   
}
