package org.altherian.hboxd.comm.io.factory.event;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.event.server.ServerConnectionStateEventOutput;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.event._Event;
import org.altherian.hbox.states.ServerConnectionState;
import org.altherian.hboxd.comm.io.factory.ServerIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.event.server.ServerConnectionStateEvent;
import org.altherian.hboxd.event.server.ServerEvent;

public class ServerEventIoFactory implements _EventIoFactory {
   
   @Override
   public Enum<?>[] getHandles() {
      return new Enum<?>[] {
            HyperboxEvents.ServerConnectionState
      };
   }
   
   @Override
   public EventOutput get(_Hyperbox hbox, _Event ev) {
      if (!(ev instanceof ServerEvent)) {
         return null;
      }
      
      ServerOutput srvOut = ServerIoFactory.get();
      switch ((HyperboxEvents) ev.getEventId()) {
         case ServerConnectionState:
            ServerConnectionState state = ((ServerConnectionStateEvent) ev).getState();
            return new ServerConnectionStateEventOutput(ev.getTime(), srvOut, state);
         default:
            return null;
      }
   }
   
}
