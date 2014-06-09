package org.altherian.hboxd.event.server;

import org.altherian.hbox.event.Event;
import org.altherian.hboxd.server._Server;

public class ServerEvent extends Event {
   
   public ServerEvent(Enum<?> eventId, _Server srv) {
      super(eventId);
      set(_Server.class, srv);
   }
   
   public _Server getServer() {
      return get(_Server.class);
   }
   
}
