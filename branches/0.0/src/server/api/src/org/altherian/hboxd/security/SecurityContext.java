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

package org.altherian.hboxd.security;

import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.exception.security.SecurityException;
import org.altherian.tool.logging.Logger;

import java.util.Map;
import java.util.WeakHashMap;

public class SecurityContext {
   
   private static _SecurityManager secMgr;
   private static Map<Thread, Thread> adminThreads;
   private static _User adminUsr;
   private static ThreadLocal<_User> userHolder = new ThreadLocal<_User>();
   
   
   public static void init() {
      Logger.track();
      
      if (adminThreads != null) {
         throw new SecurityException("SecurityContext is already initialized");
      }
      
      adminThreads = new WeakHashMap<Thread, Thread>();
      adminThreads.put(Thread.currentThread(), Thread.currentThread());
      
      Logger.debug("Security Context has been initialized");
   }
   
   public static void addAdminThread(Thread thread) {
      Logger.track();
      
      if (isAdminThread()) {
         adminThreads.put(thread, thread);
      } else {
         throw new SecurityException("Cannot promoted thread: Current thread is not admin: #" + Thread.currentThread().getId() + " - "
               + Thread.currentThread().getName());
      }
   }
   
   private static boolean isAdminThread() {
      return (adminThreads != null) && (adminThreads.isEmpty() || adminThreads.values().contains(Thread.currentThread()));
   }
   
   public static void initSecurityManager(_SecurityManager secMgr) {
      Logger.track();
      
      if (SecurityContext.secMgr != null) {
         throw new HyperboxRuntimeException("Security Manager is already defined, cannot be redefined");
      }
      SecurityContext.secMgr = secMgr;
   }
   
   public static void setAdminUser(_User u) {
      Logger.track();
      
      if (!isAdminThread()) {
         throw new SecurityException("Cannot set admin user: Current thread is not admin: #" + Thread.currentThread().getId() + " - "
               + Thread.currentThread().getName());
      }
      
      adminUsr = u;
   }
   
   public static void setUser(_User u) {
      if ((getUser() == null) || isAdminThread()) {
         userHolder.set(u);
      } else {
         throw new SecurityException();
      }
   }
   
   public static void setUser(_SecurityManager secMgr, _User u) {
      if (SecurityContext.secMgr == null) {
         throw new SecurityException("Security Manager is not initialized!");
      }
      if (!SecurityContext.secMgr.equals(secMgr)) {
         throw new SecurityException("User can only be set by the original security manager");
      }
      
      userHolder.set(u);
   }
   
   public static _User getUser() {
      if (isAdminThread()) {
         return adminUsr;
      } else {
         return userHolder.get();
      }
   }
   
   public static _SecurityManager get() {
      return secMgr;
   }
   
   public static void clear() {
      userHolder.set(null);
   }
   
}
