package org.altherian.vbox4_3;

import org.altherian.tool.logging.Logger;

import org.virtualbox_4_3.ISession;
import org.virtualbox_4_3.IVirtualBox;
import org.virtualbox_4_3.VirtualBoxManager;

public class VBox {
   
   private static VirtualBoxManager mgr;
   
   public static void set(VirtualBoxManager mgr) {
      Logger.track();
      
      VBox.mgr = mgr;
   }
   
   public static IVirtualBox get() {
      return mgr.getVBox();
   }
   
   public static ISession getSession() {
      return mgr.getSessionObject();
   }
   
   public static VirtualBoxManager getManager() {
      return mgr;
   }

}
