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

package org.altherian.hboxc.factory;

import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.HyperboxClient;
import org.altherian.hboxc.back._Backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BackendFactory {
   
   private static Map<String, String> backends;
   
   static {
      backends = new HashMap<String, String>();
      
      try {
         Set<_Backend> backs = HyperboxClient.getAllOrFail(_Backend.class);
         for (_Backend backend : backs) {
            backends.put(backend.getId(), backend.getClass().getName());
         }
      } catch (HyperboxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   public static _Backend get(String id) {
      // TODO throw exception if not found
      return HyperboxClient.loadClass(_Backend.class, backends.get(id));
   }
   
   public static List<String> list() {
      return new ArrayList<String>(backends.keySet());
   }
   
}
