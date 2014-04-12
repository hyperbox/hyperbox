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

package org.altherian.hboxd.core.model;

import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.states.ACPI;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.hypervisor.vm.snapshot._RawSnapshot;
import org.altherian.hboxd.server._Server;
import org.altherian.hboxd.settings._Settable;

import java.util.List;
import java.util.Set;

public interface _Machine extends _Settable {
   
   public _Server getServer();
   
   public String getUuid();
   
   public boolean isAccessible();
   
   public String getName();
   
   public MachineStates getState();
   
   public String getLocation();
   
   public void lock();
   
   public void unlock(boolean success);

   public void powerOn();
   
   public void powerOff();
   
   public void pause();
   
   public void resume();
   
   public void saveState();
   
   public void reset();
   
   public void sendAcpi(ACPI acpi);
   
   public List<_MachineMetric> getMetrics();
   
   public _CPU getCpu();
   
   public _Display getDisplay();
   
   public _Keyboard getKeyboard();
   
   public _Memory getMemory();
   
   public _Motherboard getMotherboard();
   
   public _Mouse getMouse();
   
   public Set<_NetworkInterface> listNetworkInterfaces();
   
   public _NetworkInterface getNetworkInterface(long nicId);
   
   public Set<_StorageController> listStorageControllers();
   
   public _StorageController getStorageController(String name);
   
   public _StorageController addStorageController(String type, String name);
   
   public _StorageController addStorageController(StorageControllerType type, String name);
   
   public void removeStorageController(String name);
   
   public _USB getUsb();
   
   public _RawSnapshot getSnapshot(String snapshotId);
   
}
