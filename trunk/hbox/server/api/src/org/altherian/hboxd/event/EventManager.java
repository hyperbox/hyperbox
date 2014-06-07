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

package org.altherian.hboxd.event;

import org.altherian.hbox.Configuration;
import org.altherian.hbox.event._Event;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.security._User;
import org.altherian.tool.logging.Logger;

public final class EventManager {
   
   private static _EventManager evMgr;
   
   static {
      String className = Configuration.getSetting("core.eventmgr.class", DefaultEventManager.class.getName());
      Logger.debug("Creating Event manager using : " + className);
      evMgr = HBoxServer.loadClass(_EventManager.class, className);
   }
   
   private EventManager() {
      // not to be used
   }
   
   public static _EventManager get() {
      return evMgr;
   }
   
   public static void start(_User usr) throws HyperboxException {
      get().start(usr);
   }
   
   public static void stop() {
      get().stop();
   }
   
   public static void register(Object o) {
      get().register(o);
   }
   
   public static void unregister(Object o) {
      get().unregister(o);
   }
   
   public static void post(_Event ev) {
      get().post(ev);
   }
   
}
