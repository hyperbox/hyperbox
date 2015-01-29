/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.hboxc.front.gui.net;

import org.altherian.hbox.comm.out.network.NetServiceOut;
import org.altherian.hbox.constant.NetServiceType;

public class NetServiceEditFactory {
   
   private NetServiceEditFactory() {
      // only static
   }
   
   /**
    * Get the appropriate net service edit panel
    *
    * @param modeId Mode ID to edit
    * @param netSvcOut The current mode data, or null if none exists already
    * @return The implementation responsible for the mode or null if none exists
    */
   public static _NetServiceEditor get(String modeId, NetServiceOut netSvcOut) {
      if (NetServiceType.IPv4.is(modeId)) {
         return new NetServiceIPv4EditPanel(netSvcOut);
      }
      return null;
   }
   
}
