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

package org.altherian.hboxd.business;

import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.states.ACPI;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.settings._Settable;

import java.util.List;
import java.util.Set;

public interface _VM extends _Settable {
   
   public String getServerId();

   public String getUuid();
   
   public String getName();
   
   public MachineStates getState();
   
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
   
   public Set<_NIC> listNetworkInterfaces();
   
   public _NIC getNetworkInterface(long nicId);
   
   public Set<_StorageController> listStoroageControllers();
   
   public _StorageController getStorageController(String name);
   
   public _StorageController addStorageController(String type, String name);
   
   public _StorageController addStorageController(StorageControllerType type, String name);
   
   public void removeStorageController(String name);
   
   public _USB getUsb();
   
}
