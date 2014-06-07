package org.altherian.hboxc.event.storage;

import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hboxc.event.ClientEvents;

public class MediumRemovedEvent extends MediumEvent {
   
   public MediumRemovedEvent(MediumOutput medOut) {
      super(ClientEvents.MediumRemoved, medOut);
   }

}
