/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.out.event.EventOut;
import org.altherian.hbox.comm.out.event.UnknownEventOut;
import org.altherian.hbox.event._Event;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.comm.io.factory.event._EventIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.tool.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class EventIoFactory {

   private static Map<Enum<?>, _EventIoFactory> factories;

   private EventIoFactory() {
      // static class, cannot be instantiated
   }

   static {
      factories = new HashMap<Enum<?>, _EventIoFactory>();
      try {
         Set<_EventIoFactory> preciseFactories = HBoxServer.getAtLeastOneOrFail(_EventIoFactory.class);
         for (_EventIoFactory preciseFactory : preciseFactories) {
            for (Enum<?> id : preciseFactory.getHandles()) {
               factories.put(id, preciseFactory);
            }
         }
      } catch (HyperboxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }

   private static EventOut getUnknown(_Event ev) {
      Logger.debug("Creating Unknown Event for ID " + ev.getEventId() + " @ " + ev.getTime() + ": " + ev);
      return new UnknownEventOut(ev.getTime(), ev.getEventId(), ServerIoFactory.get(HBoxServer.get()));
   }

   public static EventOut get(_Hyperbox hbox, _Event ev) {
      try {
         if (factories.containsKey(ev.getEventId())) {
            Logger.debug("Using " + factories.get(ev.getEventId()).getClass().getName() + " for " + ev.getEventId());
            EventOut evOut = factories.get(ev.getEventId()).get(hbox, ev);
            if (evOut != null) {
               return evOut;
            }
         }
      } catch (Throwable t) {
         Logger.error("Error while trying to Get EventOutput : " + t.getMessage());
         Logger.exception(t);
      }

      Logger.warning("No factory for Event ID " + ev.getEventId() + ", sending " + UnknownEventOut.class.getName() + " instead");
      return getUnknown(ev);
   }
}
