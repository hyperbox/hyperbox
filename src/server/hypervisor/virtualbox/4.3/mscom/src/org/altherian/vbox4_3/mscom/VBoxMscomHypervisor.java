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
import org.altherian.vbox.VirtualBox;
import org.altherian.vbox4_3.VBoxHypervisor;
import org.virtualbox_4_3.ISession;
import org.virtualbox_4_3.VirtualBoxManager;

@Hypervisor(
      id = VirtualBox.ID.MSCOM_4_3,
      typeId = VirtualBox.Type.MSCOM,
      vendor = VirtualBox.VENDOR,
      product = VirtualBox.PRODUCT,
      schemes = { VirtualBox.ID.MSCOM_4_3 })
public class VBoxMscomHypervisor extends VBoxHypervisor {

   @Override
   protected VirtualBoxManager connect(String options) {
      return VirtualBoxManager.createInstance(null);
   }

   @Override
   protected void disconnect() {
      System.gc();
   }

   @Override
   protected ISession getSession() {
      return getMgr().getSessionObject();
   }

@Override
public void importAppliance(String applianceFile) {
	// TODO Auto-generated method stub
	
}

}
