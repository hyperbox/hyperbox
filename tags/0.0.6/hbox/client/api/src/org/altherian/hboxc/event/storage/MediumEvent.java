/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
 * 
 * http://hyperbox.altherian.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

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
