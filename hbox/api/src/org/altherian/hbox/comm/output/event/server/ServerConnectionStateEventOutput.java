package org.altherian.hbox.comm.output.event.server;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.states.ServerConnectionState;

import java.util.Date;

public class ServerConnectionStateEventOutput extends ServerEventOutput {
   
   private ServerConnectionState state;
   
   @SuppressWarnings("unused")
   private ServerConnectionStateEventOutput() {
      // Used for serialization
   }
   
   public ServerConnectionStateEventOutput(Date time, ServerOutput srvOut, ServerConnectionState state) {
      super(time, HyperboxEvents.ServerConnectionState, srvOut);
      this.state = state;
   }
   
   public ServerConnectionState getState() {
      return state;
   }
   
}
