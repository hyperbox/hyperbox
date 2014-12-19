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

package org.altherian.hboxd.hypervisor.vm;

import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.data.Machine;
import org.altherian.hbox.states.ACPI;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.hypervisor._RawItem;
import org.altherian.hboxd.hypervisor.perf._RawMetricMachine;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;
import org.altherian.hboxd.hypervisor.vm.device._RawCPU;
import org.altherian.hboxd.hypervisor.vm.device._RawConsole;
import org.altherian.hboxd.hypervisor.vm.device._RawDisplay;
import org.altherian.hboxd.hypervisor.vm.device._RawKeyboard;
import org.altherian.hboxd.hypervisor.vm.device._RawMemory;
import org.altherian.hboxd.hypervisor.vm.device._RawMotherboard;
import org.altherian.hboxd.hypervisor.vm.device._RawMouse;
import org.altherian.hboxd.hypervisor.vm.device._RawNetworkInterface;
import org.altherian.hboxd.hypervisor.vm.device._RawUSB;
import org.altherian.hboxd.hypervisor.vm.guest._RawGuest;
import org.altherian.hboxd.hypervisor.vm.snapshot._RawSnapshot;
import java.util.List;
import java.util.Set;

public interface _RawVM extends _RawItem {
   
   public String getUuid();
   
   public boolean isAccessible();
   
   public String getLocation();
   
   public MachineStates getState();
   
   public String getName();
   
   /**
    * Action must be completed when this call returns
    */
   public void powerOn();
   
   /**
    * Action must be completed when this call returns
    */
   public void powerOff();
   
   /**
    * Action must be completed when this call returns
    */
   public void pause();
   
   /**
    * Action must be completed when this call returns
    */
   public void resume();
   
   /**
    * Action must be completed when this call returns
    */
   public void saveState();
   
   /**
    * Action must be completed when this call returns
    */
   public void reset();
   
   // TODO move to _Motherboard
   public void sendAcpi(ACPI acpi);
   
   public List<_RawMetricMachine> getMetrics();
   
   public _RawConsole getConsole();
   
   public _RawCPU getCpu();
   
   public _RawDisplay getDisplay();
   
   public _RawKeyboard getKeyboard();
   
   public _RawMemory getMemory();
   
   public _RawMotherboard getMotherboard();
   
   public _RawMouse getMouse();
   
   public Set<_RawNetworkInterface> listNetworkInterfaces();
   
   public _RawNetworkInterface getNetworkInterface(long nicId);
   
   public Set<_RawStorageController> listStoroageControllers();
   
   public _RawStorageController getStorageController(String name);
   
   public _RawStorageController addStorageController(String type, String name);
   
   public _RawStorageController addStorageController(StorageControllerType type, String name);
   
   public void removeStorageController(String name);
   
   public _RawUSB getUsb();
   
   public boolean hasSnapshot();
   
   public _RawSnapshot getRootSnapshot();
   
   public _RawSnapshot getCurrentSnapshot();
   
   public _RawSnapshot getSnapshot(String id);
   
   public _RawSnapshot takeSnapshot(String name, String description);
   
   public void deleteSnapshot(String id);
   
   public void restoreSnapshot(String id);
   
   public void lock();
   
   public void applyConfiguration(Machine rawData);
   
   public void saveChanges();
   
   public void discardChanges();
   
   public void unlock();
   
   public void unlock(boolean saveChanges);
   
   public byte[] takeScreenshot();
   
   public _RawGuest getGuest();
   
}
