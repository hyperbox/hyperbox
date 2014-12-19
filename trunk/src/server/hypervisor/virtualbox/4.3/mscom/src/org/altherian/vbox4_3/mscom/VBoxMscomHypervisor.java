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

package org.altherian.vbox4_3.mscom;

import org.altherian.hboxd.hypervisor.Hypervisor;
import org.altherian.vbox4_3.VBoxHypervisor;
import org.virtualbox_4_3.VirtualBoxManager;

@Hypervisor(
      id = "vbox-4.3-mscom",
      typeId = "xpcom",
      vendor = "Oracle",
      product = "Virtualbox",
      schemes = { "vbox-4.3-mscom" })
public class VBoxMscomHypervisor extends VBoxHypervisor {
   
   @Override
   protected VirtualBoxManager connect(String options) {
      return VirtualBoxManager.createInstance(null);
   }
   
   @Override
   protected void disconnect() {
      System.gc();
   }
   
}
