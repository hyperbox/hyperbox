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

package org.altherian.vbox4_2.vm.guest;

import org.altherian.hboxd.hypervisor.vm.guest._RawGuestNetworkInterface;
import org.altherian.vbox4_2.VBox;
import org.virtualbox_4_2.IMachine;

public class VBoxGuestNetworkInterface implements _RawGuestNetworkInterface {

   private String machineUuid;
   private int nicId;

   private IMachine getVm() {
      return VBox.get().findMachine(machineUuid);
   }

   private String getProperty(String name) {
      return getVm().getGuestPropertyValue("/VirtualBox/GuestInfo/Net/" + nicId + "/" + name);
   }

   public VBoxGuestNetworkInterface(String machineUuid, int nicId) {
      this.machineUuid = machineUuid;
      this.nicId = nicId;
   }

   @Override
   public int getId() {
      return nicId;
   }

   @Override
   public boolean isUp() {
      return getProperty("Status").equalsIgnoreCase("up");
   }

   @Override
   public String getMacAddress() {
      return getProperty("MAC");
   }

   @Override
   public String getIp4Address() {
      return getProperty("V4/IP");
   }

   @Override
   public String getIp4Subnet() {
      return getProperty("/V4/Netmask");
   }

   @Override
   public String getIp6Address() {
      // TODO Auto-generated method stub
      return "";
   }

   @Override
   public String getIp6Subnet() {
      // TODO Auto-generated method stub
      return "";
   }

}
