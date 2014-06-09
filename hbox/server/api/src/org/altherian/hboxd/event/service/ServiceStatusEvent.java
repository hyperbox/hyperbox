package org.altherian.hboxd.event.service;

import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hboxd.service.ServiceState;
import org.altherian.hboxd.service._Service;


public class ServiceStatusEvent extends ServiceEvent {
   
   public ServiceStatusEvent(_Service service, ServiceState state) {
      super(HyperboxEvents.ServiceStatus, service);
      set(ServiceState.class, state);
   }
   
   public ServiceState getState() {
      return get(ServiceState.class);
   }

}
