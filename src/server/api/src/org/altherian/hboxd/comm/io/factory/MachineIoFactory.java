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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.io.factory.SettingIoFactory;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.comm.out.network.NetworkInterfaceOut;
import org.altherian.hbox.comm.out.storage.StorageControllerOut;
import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.core.model._Machine;
import org.altherian.hboxd.core.model._NetworkInterface;
import org.altherian.hboxd.core.model._StorageController;
import org.altherian.setting._Setting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MachineIoFactory {

   private MachineIoFactory() {
      // static class, cannot be instantiated
   }

   public static MachineOut get(String uuid, String state) {
      return get(uuid, state, true);
   }

   public static MachineOut get(String uuid, String state, boolean isAvailable) {
      return new MachineOut(HBoxServer.get().getId(), uuid, state, isAvailable);
   }

   public static MachineOut getSimple(String id) {
      return new MachineOut(HBoxServer.get().getId(), id);
   }

   public static MachineOut getSimple(String uuid, String state, List<_Setting> settings) {
      return new MachineOut(HBoxServer.get().getId(), uuid, state, SettingIoFactory.getList(settings));
   }

   public static MachineOut getSimple(_Machine m) {
      List<_Setting> settings = new ArrayList<_Setting>();
      if (m.isAccessible()) {
         settings.addAll(Arrays.asList(
               m.getSetting(MachineAttribute.Name.getId()),
               m.getSetting(MachineAttribute.HasSnapshot.getId()),
               m.getSetting(MachineAttribute.CurrentSnapshotUuid.getId())
               ));
         return getSimple(m.getUuid(), m.getState().getId(), settings);
      } else {
         return get(m.getUuid(), MachineStates.Inaccessible.getId(), false);
      }

   }

   public static MachineOut get(_Machine m) {
      if (m.isAccessible()) {
         String serverId = HBoxServer.get().getId();
         List<StorageControllerOut> scOutList = new ArrayList<StorageControllerOut>();
         for (_StorageController sc : m.listStorageControllers()) {
            scOutList.add(StorageControllerIoFactory.get(sc));
         }

         List<NetworkInterfaceOut> nicOutList = new ArrayList<NetworkInterfaceOut>();
         for (_NetworkInterface nic : m.listNetworkInterfaces()) {
            nicOutList.add(NetworkInterfaceIoFactory.get(nic));
         }

         MachineOut mOut = new MachineOut(serverId, m.getUuid(), m.getState(), SettingIoFactory.getList(m.getSettings()), scOutList, nicOutList);
         return mOut;
      } else {
         return get(m.getUuid(), MachineStates.Inaccessible.getId(), false);
      }
   }

}
