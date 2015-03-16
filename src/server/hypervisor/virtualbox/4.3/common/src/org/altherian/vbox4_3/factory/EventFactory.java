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

package org.altherian.vbox4_3.factory;

import org.altherian.hbox.event._Event;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.HBoxServer;
import org.altherian.tool.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.virtualbox_4_3.IEvent;
import org.virtualbox_4_3.VBoxEventType;

public class EventFactory {

   private static Map<VBoxEventType, _PreciseEventFactory> factories;

   static {
      factories = new HashMap<VBoxEventType, _PreciseEventFactory>();

      try {
         Logger.debug("Current class loader: " + EventFactory.class.getClassLoader());
         Logger.debug("Interface class loader: " + _PreciseEventFactory.class.getClassLoader());
         Set<_PreciseEventFactory> factoriesSet = HBoxServer.getAllOrFail(_PreciseEventFactory.class);
         for (_PreciseEventFactory factory : factoriesSet) {
            factories.put(factory.getType(), factory);
         }
      } catch (HyperboxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }

   public static IEvent getRaw(IEvent rawEvent) {
      if (factories.containsKey(rawEvent.getType())) {
         try {
            return factories.get(rawEvent.getType()).getRaw(rawEvent);
         } catch (Throwable t) {
            Logger.error("Unable to process event: " + t.getMessage());
            return null;
         }
      } else {
         Logger.debug("Unknown event : " + rawEvent.getType());
         return null;
      }
   }

   public static _Event get(IEvent rawEvent) {
      if (factories.containsKey(rawEvent.getType())) {
         try {
            return factories.get(rawEvent.getType()).getEvent(rawEvent);
         } catch (Throwable t) {
            Logger.error("Unable to process event: " + t.getMessage());
            return null;
         }
      } else {
         Logger.debug("Unknown event : " + rawEvent.getType());
         return null;
      }
   }

}
