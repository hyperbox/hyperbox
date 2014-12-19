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

package org.altherian.hbox.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Machine extends Entity {
   
   private Map<String, Device> devices = new HashMap<String, Device>();
   private Map<String, Snapshot> snapshots = new HashMap<String, Snapshot>();
   private Snapshot currentSnapshot;
   
   public Machine() {
      
   }
   
   public Machine(String uuid) {
      super(uuid);
   }
   
   public String getUuid() {
      return getId();
   }
   
   public void addDevice(Device dev) {
      devices.put(dev.getId(), dev);
   }
   
   public void updateDevice(Device dev) {
      devices.put(dev.getId(), dev);
   }
   
   public void removeDevice(Device dev) {
      devices.remove(dev.getId());
   }
   
   public List<Device> listDevices() {
      return new ArrayList<Device>(devices.values());
   }
   
   public List<Device> listDevices(String typeId) {
      List<Device> devicesList = new ArrayList<Device>();
      for (Device dev : devices.values()) {
         if (dev.getTypeId().contentEquals(typeId)) {
            devicesList.add(dev);
         }
      }
      return devicesList;
   }
   
   public boolean hasSnapshot() {
      return !snapshots.isEmpty();
   }
   
   public Snapshot getSnapshot(String uuid) {
      return snapshots.get(uuid);
   }
   
   public Snapshot getCurrentSnapshot() {
      return currentSnapshot;
   }
   
   public void addSnapshot(Snapshot snap) {
      snapshots.put(snap.getUuid(), snap);
   }
   
   public void setCurrentSnapshot(String uuid) {
      currentSnapshot = getSnapshot(uuid);
   }
   
   public void deleteSnapshot(String uuid) {
      snapshots.remove(uuid);
   }
   
   public List<Snapshot> listSnapshots() {
      return new ArrayList<Snapshot>(snapshots.values());
   }
   
}
