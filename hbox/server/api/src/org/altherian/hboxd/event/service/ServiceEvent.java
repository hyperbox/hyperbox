package org.altherian.hboxd.event.service;

import org.altherian.hbox.event.Event;
import org.altherian.hboxd.service._Service;

public class ServiceEvent extends Event {
   
   public ServiceEvent(Enum<?> eventId, _Service service) {
      super(eventId);
      set(_Service.class, service);
   }
   
   public _Service getService() {
      return get(_Service.class);
   }

}
