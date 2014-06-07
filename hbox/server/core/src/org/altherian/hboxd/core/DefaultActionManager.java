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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxd.core;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.exception.HyperboxCommunicationException;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.core.action._ActionManager;
import org.altherian.hboxd.core.action._HyperboxAction;
import org.altherian.tool.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultActionManager implements _ActionManager {
   
   private Map<String, _HyperboxAction> actions = new HashMap<String, _HyperboxAction>();;
   
   @Override
   public void start() throws HyperboxException {
      Logger.track();
      
      Set<_HyperboxAction> subTypes = HBoxServer.getAtLeastOneOrFail(_HyperboxAction.class);
      for (_HyperboxAction action : subTypes) {
         List<String> mappings = action.getRegistrations();
         if ((mappings == null) || (mappings.size() == 0)) {
            Logger.error("Failed to load " + action.getClass().getSimpleName() + " : No provided mappings");
         } else {
            for (String mapping : mappings) {
               actions.put(mapping, action);
               Logger.debug("Loaded " + action.getClass().getSimpleName() + " and mapped under " + mapping);
            }
         }
      }
   }
   
   @Override
   public _HyperboxAction get(Request req) {
      return get(req.getCommand() + req.getName());
   }
   
   @Override
   public _HyperboxAction get(String id) {
      if (actions.containsKey(id)) {
         _HyperboxAction ca = actions.get(id);
         Logger.debug("Found " + ca.getClass().getSimpleName() + " for " + id);
         return ca;
      } else {
         Logger.debug("No matching action for " + id);
         throw new HyperboxCommunicationException("No matching action for " + id);
      }
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      actions.clear();
   }
   
}
