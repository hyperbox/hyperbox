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

package org.altherian.vbox.events;

import org.altherian.hbox.event.Event;
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hboxd.hypervisor.event._HypervisorDisconnected;

public class VBoxDisconnected extends Event implements _HypervisorDisconnected {
   
   private String errorMsg = "";
   
   public VBoxDisconnected() {
      super(HyperboxEvents.HypervisorDisconnected);
   }
   
   public VBoxDisconnected(String errorMsg) {
      this();
      if (errorMsg != null) {
         this.errorMsg = errorMsg;
      }
   }
   
   @Override
   public String getMessage() {
      return errorMsg;
   }
   
}
