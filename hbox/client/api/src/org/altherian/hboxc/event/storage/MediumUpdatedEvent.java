package org.altherian.hboxc.event.storage;

import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hboxc.event.ClientEvents;

public class MediumUpdatedEvent extends MediumEvent {
   
   public MediumUpdatedEvent(MediumOutput medOut) {
      super(ClientEvents.MediumUpdate, medOut);
   }
   
}
