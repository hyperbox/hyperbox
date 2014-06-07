package org.altherian.hboxc.event.storage;

import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hboxc.event.ClientEvents;

public class MediumAddedEvent extends MediumEvent {
   
   public MediumAddedEvent(MediumOutput medOut) {
      super(ClientEvents.MediumAdd, medOut);
   }
   
}
