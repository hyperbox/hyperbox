package org.altherian.hboxd.security;

import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.tool.logging.Logger;

public class SecurityContext {
   
   private static _SecurityManager secMgr;
   private static Thread adminThread;
   private static ThreadLocal<_User> userHolder = new ThreadLocal<_User>();
   
   public static void initAdminThread() {
      if (adminThread != null) {
         throw new HyperboxRuntimeException("Admin thread is already defined, cannot be redefined");
      }
      adminThread = Thread.currentThread();
      Logger.debug("Security Context Admin Thread has been initialized to: " + adminThread.getName());
   }
   
   public static void initSecurityManager(_SecurityManager secMgr) {
      if (SecurityContext.secMgr != null) {
         throw new HyperboxRuntimeException("Security Manager is already defined, cannot be redefined");
      }
      SecurityContext.secMgr = secMgr;
   }
   
   public static void setUser(_User u) {
      if ((getUser() == null) || ((SecurityContext.adminThread != null) && SecurityContext.adminThread.equals(Thread.currentThread()))) {
         userHolder.set(u);
      } else {
         // FIXME throw exception
      }
   }
   
   public static void setUser(_SecurityManager secMgr, _User u) {
      if (SecurityContext.secMgr == null) {
         throw new HyperboxRuntimeException("Security Manager is not initialized!");
      }
      if (!SecurityContext.secMgr.equals(secMgr)) {
         throw new HyperboxRuntimeException("User can only be set by the original security manager");
      }
      
      userHolder.set(u);
   }
   
   public static _User getUser() {
      return userHolder.get();
   }
   
   public static void clear() {
      userHolder.set(null);
   }
   
}
