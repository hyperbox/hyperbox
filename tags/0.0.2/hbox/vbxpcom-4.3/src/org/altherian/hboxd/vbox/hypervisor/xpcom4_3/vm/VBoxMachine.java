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

package org.altherian.hboxd.vbox.hypervisor.xpcom4_3.vm;

import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.data.Machine;
import org.altherian.hbox.exception.FeatureNotImplementedException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.exception.MachineDisplayNotAvailableException;
import org.altherian.hbox.exception.MachineException;
import org.altherian.hbox.states.ACPI;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.hypervisor.perf._RawMetricMachine;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;
import org.altherian.hboxd.hypervisor.vm._RawVM;
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
import org.altherian.hboxd.settings._Setting;
import org.altherian.hboxd.vbox.hypervisor.xpcom4_3.storage.VirtualboxStorageController;
import org.altherian.hboxd.vbox.hypervisor.xpcom4_3.vm.guest.VBoxGuest;
import org.altherian.hboxd.vbox4_3.xpcom.factory.ConnectionManager;
import org.altherian.hboxd.vbox4_3.xpcom.manager.VbSessionManager;
import org.altherian.hboxd.vbox4_3.xpcom.manager.VbSettingManager;
import org.altherian.hboxd.vbox4_3.xpcom.utils.Mappings;
import org.altherian.tool.logging.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.virtualbox_4_3.Holder;
import org.virtualbox_4_3.IMachine;
import org.virtualbox_4_3.IProgress;
import org.virtualbox_4_3.ISession;
import org.virtualbox_4_3.ISnapshot;
import org.virtualbox_4_3.IStorageController;
import org.virtualbox_4_3.LockType;
import org.virtualbox_4_3.SessionState;
import org.virtualbox_4_3.StorageBus;
import org.virtualbox_4_3.VBoxException;

public final class VBoxMachine implements _RawVM {
   
   /**
    * Waiting coefficient to use on ISession::getTimeRemaining() with Thread.sleep() while waiting for task in progress to finish.<br/>
    * Virtualbox returns a waiting time in seconds, this coefficient allow to turn it into milliseconds and set a 'shorter' waiting time for a more reactive update.<br/>
    * Default value waits half of the estimated time reported by Virtualbox.
    */
   private final int waitingCoef = 500;
   
   private String uuid;
   
   private VBoxConsole console;
   private VBoxCPU cpu;
   private VBoxDisplay display;
   private VBoxKeyboard keyboard;
   private VBoxMemory memory;
   private VBoxMotherboard motherboard;
   private VBoxMouse mouse;
   private VBoxUSB usb;
   
   private VBoxGuest guest;
   
   private ISession session = null;
   
   public VBoxMachine(String uuid) {
      this.uuid = uuid;
      
      console = new VBoxConsole(this);
      cpu = new VBoxCPU(this);
      display = new VBoxDisplay(this);
      keyboard = new VBoxKeyboard(this);
      memory = new VBoxMemory(this);
      motherboard = new VBoxMotherboard(this);
      mouse = new VBoxMouse(this);
      usb = new VBoxUSB(this);
      guest = new VBoxGuest(this);
   }
   
   /**
    * Create a new VirtualboxMachine object with the given UUID and State
    * 
    * @param machine The machine to create this object from
    */
   public VBoxMachine(IMachine machine) {
      this(machine.getId());
   }
   
   @Override
   public void saveChanges() {
      Logger.track();
      
      try {
         getRaw().saveSettings();
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      }
   }
   
   @Override
   public void discardChanges() {
      Logger.track();
      
      try {
         getRaw().discardSettings();
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      }
   }
   
   protected void lock(LockType lockType) {
      session = VbSessionManager.get().lock(getUuid(), lockType);
   }
   
   @Override
   public void lock() {
      if (getRaw().getSessionState().equals(SessionState.Locked)) {
         lock(LockType.Shared);
      } else {
         lock(LockType.Write);
      }
   }
   
   protected void lockAutoWrite() {
      session = VbSessionManager.get().lockAuto(getUuid(), LockType.Write);
   }
   
   protected void lockAutoShared() {
      session = VbSessionManager.get().lockAuto(getUuid(), LockType.Shared);
   }
   
   protected void lockAuto() {
      session = VbSessionManager.get().lockAuto(getUuid());
   }
   
   @Override
   public void unlock() {
      unlock(true);
   }
   
   @Override
   public void unlock(boolean saveChanges) {
      VbSessionManager.get().unlock(getUuid(), saveChanges);
      session = null;
   }
   
   private void unlockAuto() {
      unlockAuto(false);
   }
   
   private void unlockAuto(boolean saveSettings) {
      VbSessionManager.get().unlockAuto(getUuid(), saveSettings);
      session = null;
   }
   
   @Override
   public String getUuid() {
      return uuid;
   }
   
   @Override
   public String getName() {
      return getSetting(MachineAttributes.Name).getString();
   }
   
   @Override
   public MachineStates getState() {
      return Mappings.get(getRaw().getState());
   }
   
   private IMachine getRaw() {
      session = VbSessionManager.get().getLock(uuid);
      if ((session != null) && session.getState().equals(SessionState.Locked)) {
         return session.getMachine();
      } else {
         return ConnectionManager.findMachine(uuid);
      }
   }
   
   @Override
   public String toString() {
      return getName() + " (" + getUuid() + ")";
   }
   
   @Override
   public void powerOn() throws MachineException {
      Logger.track();
      
      IMachine rawMachine = getRaw();
      session = ConnectionManager.getSession();
      try {
         IProgress p = rawMachine.launchVMProcess(session, "headless", null);
         while (!p.getCompleted() || p.getCanceled()) {
            try {
               Thread.sleep(Math.abs(p.getTimeRemaining()) * waitingCoef);
            } catch (InterruptedException e) {
               Logger.exception(e);
            }
         }
         Logger.debug("VBox API Return code: " + p.getResultCode());
         if (p.getResultCode() != 0) {
            throw new MachineException(p.getErrorInfo().getText());
         }
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         if (session.getState().equals(SessionState.Locked)) {
            session.unlockMachine();
         } else {
            Logger.verbose("PowerOn session is not locked, did powering on failed?");
         }
      }
   }
   
   @Override
   public void powerOff() throws MachineException {
      Logger.track();
      
      try {
         lockAuto();
         IProgress p = session.getConsole().powerDown();
         while (!p.getCompleted() || p.getCanceled()) {
            try {
               Thread.sleep(Math.abs(p.getTimeRemaining()) * waitingCoef);
            } catch (InterruptedException e) {
               Logger.exception(e);
            }
         }
         Logger.debug("VBox API Return code: " + p.getResultCode());
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlock();
      }
   }
   
   @Override
   public void pause() throws MachineException {
      Logger.track();
      
      try {
         lockAuto();
         session.getConsole().pause();
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public void resume() throws MachineException {
      Logger.track();
      
      try {
         lockAuto();
         session.getConsole().resume();
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public void saveState() throws MachineException {
      Logger.track();
      
      try {
         lockAuto();
         IProgress p = session.getConsole().saveState();
         while (!p.getCompleted() || p.getCanceled()) {
            try {
               Thread.sleep(Math.abs(p.getTimeRemaining()) * waitingCoef);
            } catch (InterruptedException e) {
               Logger.exception(e);
            }
         }
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlock();
      }
   }
   
   @Override
   public void reset() throws MachineException {
      Logger.track();
      try {
         lockAuto();
         session.getConsole().reset();
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public void sendAcpi(ACPI acpi) throws MachineException {
      Logger.track();
      
      try {
         lockAuto();
         if (acpi.equals(ACPI.PowerButton)) {
            session.getConsole().powerButton();
         } else {
            session.getConsole().sleepButton();
         }
         if (!session.getConsole().getPowerButtonHandled()) {
            Logger.debug("ACPI Power Button event was not handled by the guest");
         }
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public List<_RawMetricMachine> getMetrics() {
      Logger.track();
      
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public _Setting getSetting(Object getName) {
      return VbSettingManager.get(this, getName);
   }
   
   @Override
   public List<_Setting> listSettings() {
      return VbSettingManager.list(this);
   }
   
   @Override
   public void setSetting(_Setting s) {
      VbSettingManager.set(this, Arrays.asList(s));
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      VbSettingManager.set(this, s);
   }
   
   @Override
   public _RawCPU getCpu() {
      return cpu;
   }
   
   @Override
   public _RawDisplay getDisplay() {
      return display;
   }
   
   @Override
   public _RawKeyboard getKeyboard() {
      return keyboard;
   }
   
   @Override
   public _RawMemory getMemory() {
      return memory;
   }
   
   @Override
   public _RawMotherboard getMotherboard() {
      return motherboard;
   }
   
   @Override
   public _RawMouse getMouse() {
      return mouse;
   }
   
   @Override
   public _RawUSB getUsb() {
      return usb;
   }
   
   @Override
   public Set<_RawNetworkInterface> listNetworkInterfaces() {
      Logger.track();
      
      Set<_RawNetworkInterface> nics = new HashSet<_RawNetworkInterface>();
      // TODO do this better to avoid endless loop - Check using ISystemProperties maybe?
      long i = 0;
      
      try {
         while (i < 8) {
            getRaw().getNetworkAdapter(i);
            nics.add(new VBoxNetworkInterface(this, i));
            i++;
         }
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException("Unable to list NICs", e);
      }
      
      return nics;
      
   }
   
   @Override
   public _RawNetworkInterface getNetworkInterface(long nicId) {
      try {
         // We try to get the interface simply for validation - this class is not "useful" as there are no link back to the VM from it.
         getRaw().getNetworkAdapter(nicId);
         return new VBoxNetworkInterface(this, nicId);
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException("Couldn't get NIC #" + nicId + " from " + getName() + " because : " + e.getMessage());
      }
   }
   
   @Override
   public Set<_RawStorageController> listStoroageControllers() {
      Set<_RawStorageController> storageCtrls = new HashSet<_RawStorageController>();
      try {
         for (IStorageController vboxStrCtrl : getRaw().getStorageControllers()) {
            storageCtrls.add(new VirtualboxStorageController(this, vboxStrCtrl));
         }
         return storageCtrls;
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public _RawStorageController getStorageController(String name) {
      return new VirtualboxStorageController(this, getRaw().getStorageControllerByName(name));
   }
   
   @Override
   public _RawStorageController addStorageController(String type, String name) {
      Logger.track();
      
      StorageBus bus = StorageBus.valueOf(type);
      
      lockAuto();
      try {
         IStorageController strCtrl = getRaw().addStorageController(name, bus);
         VirtualboxStorageController vbStrCtrl = new VirtualboxStorageController(this, strCtrl);
         return vbStrCtrl;
      } finally {
         unlockAuto(true);
      }
   }
   
   @Override
   public _RawStorageController addStorageController(StorageControllerType type, String name) {
      return addStorageController(type.getId(), name);
   }
   
   @Override
   public void removeStorageController(String name) {
      lockAuto();
      try {
         getRaw().removeStorageController(name);
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public boolean hasSnapshot() {
      return getRaw().getCurrentSnapshot() != null;
   }
   
   @Override
   public _RawSnapshot getRootSnapshot() {
      if (hasSnapshot()) {
         return getSnapshot(null);
      } else {
         return null;
      }
   }
   
   @Override
   public _RawSnapshot getSnapshot(String id) {
      try {
         ISnapshot snap = getRaw().findSnapshot(id);
         _RawSnapshot rawSnap = new VBoxSnapshot(snap);
         return rawSnap;
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public _RawSnapshot getCurrentSnapshot() {
      try {
         return new VBoxSnapshot(getRaw().getCurrentSnapshot());
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public _RawSnapshot takeSnapshot(String name, String description) {
      Logger.track();
      
      lockAuto();
      try {
         IProgress p = session.getConsole().takeSnapshot(name, description);
         while (!p.getCompleted() || p.getCanceled()) {
            try {
               Thread.sleep(Math.abs(p.getTimeRemaining()) * waitingCoef);
            } catch (InterruptedException e) {
               Logger.exception(e);
            }
         }
         Logger.debug("Return code : " + p.getResultCode());
         if (p.getResultCode() == 0) {
            return getSnapshot(name);
         } else {
            throw new MachineException(p.getErrorInfo().getText());
         }
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public void deleteSnapshot(String id) {
      Logger.track();
      
      lockAuto();
      try {
         IProgress p = session.getConsole().deleteSnapshot(id);
         while (!p.getCompleted() || p.getCanceled()) {
            try {
               Thread.sleep(Math.abs(p.getTimeRemaining()) * waitingCoef);
            } catch (InterruptedException e) {
               Logger.exception(e);
            }
         }
         Logger.debug("Return code : " + p.getResultCode());
         if (p.getResultCode() != 0) {
            throw new MachineException(p.getErrorInfo().getText());
         }
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public void restoreSnapshot(String id) {
      Logger.track();
      
      lockAuto();
      try {
         ISnapshot snapshot = getRaw().findSnapshot(id);
         IProgress p = session.getConsole().restoreSnapshot(snapshot);
         while (!p.getCompleted() || p.getCanceled()) {
            try {
               Thread.sleep(Math.abs(p.getTimeRemaining()) * waitingCoef);
            } catch (InterruptedException e) {
               Logger.exception(e);
            }
         }
         Logger.debug("Return code : " + p.getResultCode());
         if (p.getResultCode() != 0) {
            throw new MachineException(p.getErrorInfo().getText());
         }
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public _RawConsole getConsole() {
      return console;
   }
   
   @Override
   public void applyConfiguration(Machine rawData) {
      lockAuto();
      try {
         VbSettingManager.apply(session.getMachine(), rawData);
         saveChanges();
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public String getLocation() {
      File settingFiles = new File(getRaw().getSettingsFilePath());
      return settingFiles.getAbsoluteFile().getParent();
   }
   
   // TODO provide a screenshot size
   @Override
   public byte[] takeScreenshot() {
      lockAutoShared();
      try {
         Holder<Long> height = new Holder<Long>();
         Holder<Long> width = new Holder<Long>();
         Holder<Long> bpp = new Holder<Long>();
         Holder<Integer> xOrigin = new Holder<Integer>();
         Holder<Integer> yOrigin = new Holder<Integer>();
         
         try {
            session.getConsole().getDisplay().getScreenResolution(0l, width, height, bpp, xOrigin, yOrigin);
         } catch (NullPointerException e) {
            throw new MachineDisplayNotAvailableException();
         }
         byte[] screenshot = session.getConsole().getDisplay().takeScreenShotPNGToArray(0l, width.value, height.value);
         return screenshot;
      } catch (VBoxException e) {
         throw new MachineException(e.getMessage(), e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public _RawGuest getGuest() {
      return guest;
   }
   
}
