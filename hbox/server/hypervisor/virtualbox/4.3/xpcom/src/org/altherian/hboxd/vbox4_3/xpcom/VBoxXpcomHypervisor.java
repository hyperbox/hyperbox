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

package org.altherian.hboxd.vbox4_3.xpcom;

import org.altherian.hboxd.hypervisor.Hypervisor;
import org.altherian.tool.StringTools;
import org.altherian.tool.logging.Logger;
import org.altherian.vbox4_3.VBoxHypervisor;

import org.virtualbox_4_3.VirtualBoxManager;

@Hypervisor(
      id = "vbox-4.3-xpcom",
      typeId = "xpcom",
      vendor = "Oracle",
      product = "Virtualbox",
      schemes = { "vbox-4.3-xpcom" })
public final class VBoxXpcomHypervisor extends VBoxHypervisor {
   
   protected final String defaultHome = "/usr/lib/virtualbox";
   protected String home;
   
   @Override
   protected VirtualBoxManager connect(String options) {
      if (!StringTools.isEmpty(options)) {
         home = options;
         Logger.verbose("Given Virtualbox home: " + options);
      } else {
         home = defaultHome;
         Logger.verbose("No Virtualbox home was given, using default value: " + options);
      }
      
      System.setProperty("vbox.home", home);
      return VirtualBoxManager.createInstance(null);
   }
   
   @Override
   protected void disconnect() {
      // nothing to do here
   }
   
}
