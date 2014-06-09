package org.altherian.hboxd.event.hypervisor;

import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hboxd.hypervisor._Hypervisor;

public class HypervisorConfigurationUpdateEvent extends HypervisorEvent {
   
   public HypervisorConfigurationUpdateEvent(_Hypervisor hyp) {
      super(HyperboxEvents.HypervisorConfigured, hyp);
   }
   
}
