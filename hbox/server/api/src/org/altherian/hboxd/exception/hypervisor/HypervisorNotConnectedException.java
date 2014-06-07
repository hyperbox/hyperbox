package org.altherian.hboxd.exception.hypervisor;

import org.altherian.hbox.exception.HyperboxRuntimeException;

@SuppressWarnings("serial")
public class HypervisorNotConnectedException extends HyperboxRuntimeException {
   
   public HypervisorNotConnectedException() {
      super("Hypervisor is not connected");
   }

   public HypervisorNotConnectedException(Throwable t) {
      super("Hypervisor is not connected", t);
   }
   
}
