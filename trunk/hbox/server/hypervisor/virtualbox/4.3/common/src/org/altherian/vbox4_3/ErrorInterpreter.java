package org.altherian.vbox4_3;

import org.altherian.hbox.exception.HypervisorException;

import org.virtualbox_4_3.VBoxException;

public class ErrorInterpreter {
   
   private ErrorInterpreter() {
      // no instance
   }
   
   public static RuntimeException transform(VBoxException e) {
      return new HypervisorException(e);
   }
   
   public static RuntimeException transformAndThrow(VBoxException e) {
      throw transform(e);
   }
   
}
