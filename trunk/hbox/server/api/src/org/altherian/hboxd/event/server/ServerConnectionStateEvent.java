package org.altherian.hboxd.event.server;

import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.states.ServerConnectionState;
import org.altherian.hboxd.server._Server;

public class ServerConnectionStateEvent extends ServerEvent {
   
   public ServerConnectionStateEvent(_Server srv, ServerConnectionState state) {
      super(HyperboxEvents.ServerConnectionState, srv);
      set(ServerConnectionState.class, state);
   }
   
   public ServerConnectionState getState() {
      return get(ServerConnectionState.class);
   }

}
