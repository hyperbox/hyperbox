package org.altherian.hboxd.event.hypervisor;

import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hboxd.server._Server;

public class HypervisorConfigurationUpdateEvent extends HypervisorEvent {
   
   public HypervisorConfigurationUpdateEvent(_Server srv) {
      super(HyperboxEvents.HypervisorConfigured, srv);
   }

}
