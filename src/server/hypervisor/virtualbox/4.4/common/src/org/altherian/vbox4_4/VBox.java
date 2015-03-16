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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.vbox4_4;

import org.virtualbox_4_4.ISession;
import org.virtualbox_4_4.IVirtualBox;
import org.virtualbox_4_4.VirtualBoxManager;

public class VBox {

   private static VirtualBoxManager mgr;

   public static void set(VirtualBoxManager mgr) {

      VBox.mgr = mgr;
   }

   public static IVirtualBox get() {
      return mgr.getVBox();
   }

   public static ISession getSession() {
      return mgr.getSessionObject();
   }

   public static VirtualBoxManager getManager() {
      return mgr;
   }

   public static void unset() {
      mgr = null;
   }

}
