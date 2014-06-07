package org.altherian.hboxc.event.storage;

import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hbox.event.Event;

public abstract class MediumEvent extends Event {
   
   public MediumEvent(Enum<?> s, MediumOutput medOut) {
      super(s);
      set(MediumOutput.class, medOut);
   }
   
   public MediumOutput getMedium() {
      return get(MediumOutput.class);
   }
   
   @Override
   public String toString() {
      return "Event ID " + getEventId() + " for Medium " + getMedium().getUuid() + " located at " + getMedium().getLocation() + " occured @ "
            + getTime();
   }
   
}
