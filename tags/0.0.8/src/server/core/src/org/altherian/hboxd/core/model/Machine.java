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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxd.core.model;

import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.states.ACPI;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.exception.machine.DeviceNotFoundException;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.hypervisor.vm.device._RawNetworkInterface;
import org.altherian.hboxd.hypervisor.vm.snapshot._RawSnapshot;
import org.altherian.hboxd.security.SecurityContext;
import org.altherian.hboxd.server._Server;
import org.altherian.setting._Setting;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Machine implements _Machine {
   
   private _Server server;
   private _Hypervisor hypervisor;
   private _RawVM rawVm;
   
   public Machine(_Server server, _Hypervisor hypervisor, _RawVM rawVm) {
      this.server = server;
      this.hypervisor = hypervisor;
      this.rawVm = rawVm;
   }
   
   @Override
   public List<_Setting> getSettings() {
      return rawVm.listSettings();
   }
   
   @Override
   public _Setting getSetting(String settingId) {
      return rawVm.getSetting(settingId);
   }
   
   @Override
   public void setSetting(_Setting setting) {
      rawVm.setSetting(setting);
   }
   
   @Override
   public void setSetting(List<_Setting> settings) {
      rawVm.setSetting(settings);
   }
   
   @Override
   public boolean hasSetting(String settingId) {
      try {
         rawVm.getSetting(settingId);
         return true;
      } catch (Throwable t) {
         // TODO catch better
         return false;
      }
   }
   
   @Override
   public _Server getServer() {
      return server;
   }
   
   @Override
   public String getUuid() {
      return rawVm.getUuid();
   }
   
   @Override
   public boolean isAccessible() {
      return rawVm.isAccessible();
   }
   
   @Override
   public String getName() {
      return rawVm.getName();
   }
   
   @Override
   public MachineStates getState() {
      return rawVm.getState();
   }
   
   @Override
   public String getLocation() {
      return rawVm.getLocation();
   }
   
   @Override
   public void powerOn() {
      SecurityContext.get().authorize(SecurityItem.Machine, SecurityAction.Start, getUuid());
      rawVm.powerOn();
   }
   
   @Override
   public void powerOff() {
      SecurityContext.get().authorize(SecurityItem.Machine, SecurityAction.Stop, getUuid());
      rawVm.powerOff();
   }
   
   @Override
   public void pause() {
      SecurityContext.get().authorize(SecurityItem.Machine, SecurityAction.Pause, getUuid());
      rawVm.pause();
   }
   
   @Override
   public void resume() {
      SecurityContext.get().authorize(SecurityItem.Machine, SecurityAction.Resume, getUuid());
      rawVm.resume();
   }
   
   @Override
   public void saveState() {
      SecurityContext.get().authorize(SecurityItem.Machine, SecurityAction.Save, getUuid());
      rawVm.saveState();
   }
   
   @Override
   public void reset() {
      SecurityContext.get().authorize(SecurityItem.Machine, SecurityAction.Reset, getUuid());
      rawVm.reset();
   }
   
   @Override
   public void sendAcpi(ACPI acpi) {
      // TODO add security check
      rawVm.sendAcpi(acpi);
   }
   
   @Override
   public List<_MachineMetric> getMetrics() {
      // TODO add security check
      return Collections.emptyList();
   }
   
   @Override
   public _CPU getCpu() {
      // TODO add security check
      return new CPU(rawVm.getCpu());
   }
   
   @Override
   public _Display getDisplay() {
      // TODO add security check
      return new Display(rawVm.getDisplay());
   }
   
   @Override
   public _Keyboard getKeyboard() {
      // TODO add security check
      return new Keyboard(rawVm.getKeyboard());
   }
   
   @Override
   public _Memory getMemory() {
      // TODO add security check
      return new Memory(rawVm.getMemory());
   }
   
   @Override
   public _Motherboard getMotherboard() {
      // TODO add security check
      return new Motherboard(rawVm.getMotherboard());
   }
   
   @Override
   public _Mouse getMouse() {
      // TODO add security check
      return new Mouse(rawVm.getMouse());
   }
   
   @Override
   public Set<_NetworkInterface> listNetworkInterfaces() {
      // TODO add security check
      Set<_NetworkInterface> nics = new HashSet<_NetworkInterface>();
      for (_RawNetworkInterface rawNic : rawVm.listNetworkInterfaces()) {
         // TODO add security check
         nics.add(new NetworkInterface(this, rawNic));
      }
      return nics;
   }
   
   @Override
   public _NetworkInterface getNetworkInterface(long nicId) {
      // TODO add security check
      return new NetworkInterface(this, rawVm.getNetworkInterface(nicId));
   }
   
   @Override
   public Set<_StorageController> listStorageControllers() {
      // TODO add security check
      Set<_StorageController> nics = new HashSet<_StorageController>();
      for (_RawStorageController rawSto : rawVm.listStoroageControllers()) {
         nics.add(new StorageController(this, hypervisor, rawSto));
      }
      return nics;
   }
   
   @Override
   public _StorageController getStorageController(String name) {
      // TODO add security check
      return new StorageController(this, hypervisor, rawVm.getStorageController(name));
   }
   
   @Override
   public _StorageController addStorageController(String type, String name) {
      // TODO add security check
      return new StorageController(this, hypervisor, rawVm.addStorageController(type, name));
   }
   
   @Override
   public _StorageController addStorageController(StorageControllerType type, String name) {
      // TODO add security check
      return new StorageController(this, hypervisor, rawVm.addStorageController(type, name));
   }
   
   @Override
   public void removeStorageController(String name) {
      // TODO add security check
      rawVm.removeStorageController(name);
   }
   
   @Override
   public _USB getUsb() {
      // TODO add security check
      return new USB(rawVm.getUsb());
   }
   
   @Override
   public _RawSnapshot getSnapshot(String snapshotId) {
      // TODO add security check
      return rawVm.getSnapshot(snapshotId);
   }
   
   @Override
   public void lock() {
      rawVm.lock();
   }
   
   @Override
   public void unlock(boolean saveChanges) {
      rawVm.unlock(saveChanges);
   }
   
   @Override
   public _Device getDevice(String deviceId) {
      if (EntityType.Console.getId().equals(deviceId)) {
         return new Console(this, rawVm.getConsole());
      } else {
         throw new DeviceNotFoundException("Device " + deviceId + " was not found");
      }
   }
   
}
