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

package org.altherian.hbox.comm.out.event.module;

import org.altherian.hbox.comm.out.ModuleOut;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.event.EventOut;

import java.util.Date;

public class ModuleEventOut extends EventOut {
   
   protected ModuleEventOut() {
      // Used for (de)serialization
   }
   
   public ModuleEventOut(Date time, Enum<?> id, ServerOut srvOut, ModuleOut modOut) {
      super(time, id, srvOut);
      set(ModuleOut.class, modOut);
   }
   
   public ModuleOut getModule() {
      return get(ModuleOut.class);
   }

}
