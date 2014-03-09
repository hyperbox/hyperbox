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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerOutput;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.core.model._Machine;
import org.altherian.hboxd.core.model._NetworkInterface;
import org.altherian.hboxd.core.model._StorageController;
import org.altherian.hboxd.settings._Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MachineIoFactory {
   
   private MachineIoFactory() {
      // static class, cannot be instantiated
   }
   
   public static MachineOutput get(String uuid) {
      return get(uuid, true);
   }
   
   public static MachineOutput get(String uuid, boolean isAvailable) {
      return new MachineOutput(HBoxServer.get().getId(), uuid, isAvailable);
   }
   
   public static MachineOutput getSimple(String uuid, String state, List<_Setting> settings) {
      return new MachineOutput(HBoxServer.get().getId(), uuid, state, SettingIoFactory.getList(settings));
   }
   
   public static MachineOutput getSimple(_Machine m) {
      List<_Setting> settings = new ArrayList<_Setting>();
      if (m.isAccessible()) {
         settings.addAll(Arrays.asList(
               m.getSetting(MachineAttributes.Name.getId()),
               m.getSetting(MachineAttributes.CurrentSnapshotUuid.getId()),
               m.getSetting(MachineAttributes.HasSnapshot.getId())));
         return getSimple(m.getUuid(), m.getState().getId(), settings);
      } else {
         return get(m.getUuid(), false);
      }
      
   }
   
   public static MachineOutput get(_Machine m) {
      if (m.isAccessible()) {
         String serverId = HBoxServer.get().getId();
         List<StorageControllerOutput> scOutList = new ArrayList<StorageControllerOutput>();
         for (_StorageController sc : m.listStorageControllers()) {
            scOutList.add(StorageControllerIoFactory.get(sc));
         }
         
         List<NetworkInterfaceOutput> nicOutList = new ArrayList<NetworkInterfaceOutput>();
         for (_NetworkInterface nic : m.listNetworkInterfaces()) {
            nicOutList.add(NetworkInterfaceIoFactory.get(nic));
         }
         
         MachineOutput mOut = new MachineOutput(serverId, m.getUuid(), m.getState(), SettingIoFactory.getList(m.getSettings()), scOutList, nicOutList);
         return mOut;
      } else {
         return get(m.getUuid(), false);
      }
   }
   
}
