package org.altherian.hbox.comm.output.event.server;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.event.EventOutput;

import java.util.Date;

public abstract class ServerEventOutput extends EventOutput {
   
   protected ServerEventOutput() {
      // Used for serialization
   }
   
   public ServerEventOutput(Date time, Enum<?> id, ServerOutput srvOut) {
      super(time, id, srvOut);
   }
   
}
