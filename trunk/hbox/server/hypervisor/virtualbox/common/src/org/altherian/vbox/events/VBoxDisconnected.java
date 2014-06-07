package org.altherian.vbox.events;

import org.altherian.hbox.event.Event;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hboxd.hypervisor.event._HypervisorDisconnected;

public class VBoxDisconnected extends Event implements _HypervisorDisconnected {
   
   private String errorMsg = "";
   
   public VBoxDisconnected() {
      super(HyperboxEvents.HypervisorDisconnected);
   }
   
   public VBoxDisconnected(String errorMsg) {
      this();
      if (errorMsg != null) {
         this.errorMsg = errorMsg;
      }
   }
   
   @Override
   public String getMessage() {
      return errorMsg;
   }
   
}
