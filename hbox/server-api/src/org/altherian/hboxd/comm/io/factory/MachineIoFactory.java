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
import org.altherian.hboxd.business._VM;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.hypervisor.vm.device._RawNetworkInterface;
import org.altherian.hboxd.settings._Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MachineIoFactory {
   
   private MachineIoFactory() {
      // static class, cannot be instantiated
   }
   
   public static MachineOutput get(String uuid) {
      return new MachineOutput(HBoxServer.get().getId(), uuid);
   }
   
   public static MachineOutput getSimple(String uuid, String state, List<_Setting> settings) {
      return new MachineOutput(HBoxServer.get().getId(), uuid, state, SettingIoFactory.getList(settings));
   }
   
   public static MachineOutput getSimple(_VM m) {
      return getSimple(m.getUuid(), m.getState().getId(), null);
   }
   
   public static MachineOutput getSimple(_RawVM m) {
      List<_Setting> settings = Arrays.asList(
            m.getSetting(MachineAttributes.Name),
            m.getSetting(MachineAttributes.CurrentSnapshotUuid),
            m.getSetting(MachineAttributes.HasSnapshot));
      return getSimple(m.getUuid(), m.getState().getId(), settings);
   }
   
   public static MachineOutput get(_VM m) {
      // TODO update settings in mOut
      return getSimple(m);
   }
   
   public static MachineOutput get(_RawVM m) {
      String serverId = HBoxServer.get().getId();
      List<StorageControllerOutput> scOutList = new ArrayList<StorageControllerOutput>();
      for (_RawStorageController sc : m.listStoroageControllers()) {
         scOutList.add(StorageControllerIoFactory.get(sc));
      }
      
      List<NetworkInterfaceOutput> nicOutList = new ArrayList<NetworkInterfaceOutput>();
      for (_RawNetworkInterface nic : m.listNetworkInterfaces()) {
         nicOutList.add(NetworkInterfaceIoFactory.get(nic));
      }
      
      MachineOutput mOut = new MachineOutput(serverId, m.getUuid(), m.getState(), SettingIoFactory.getList(m.listSettings()), scOutList, nicOutList);
      return mOut;
   }
   
}
