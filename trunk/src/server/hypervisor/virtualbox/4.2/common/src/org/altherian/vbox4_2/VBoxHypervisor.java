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

package org.altherian.vbox4_2;

import org.altherian.hbox.Configuration;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.data.Machine;
import org.altherian.hbox.exception.FeatureNotImplementedException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hbox.exception.MachineException;
import org.altherian.hbox.exception.ServiceException;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event._EventManager;
import org.altherian.hboxd.event.hypervisor.HypervisorConnectedEvent;
import org.altherian.hboxd.event.hypervisor.HypervisorDisconnectedEvent;
import org.altherian.hboxd.hypervisor.Hypervisor;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor._RawOsType;
import org.altherian.hboxd.hypervisor.host._RawHost;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerSubType;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerType;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.service._Service;
import org.altherian.setting.StringSetting;
import org.altherian.setting._Setting;
import org.altherian.tool.AxStrings;
import org.altherian.tool.logging.Logger;
import org.altherian.vbox4_2.data.Mappings;
import org.altherian.vbox4_2.factory.OsTypeFactory;
import org.altherian.vbox4_2.host.VirtualboxHost;
import org.altherian.vbox4_2.manager.VBoxSessionManager;
import org.altherian.vbox4_2.service.EventsManagementService;
import org.altherian.vbox4_2.storage.VbStorageControllerSubType;
import org.altherian.vbox4_2.storage.VbStorageControllerType;
import org.altherian.vbox4_2.storage.VirtualboxMedium;
import org.altherian.vbox4_2.vm.VBoxMachine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.virtualbox_4_2.AccessMode;
import org.virtualbox_4_2.CleanupMode;
import org.virtualbox_4_2.DeviceType;
import org.virtualbox_4_2.HostNetworkInterfaceType;
import org.virtualbox_4_2.IGuestOSType;
import org.virtualbox_4_2.IHostNetworkInterface;
import org.virtualbox_4_2.IMachine;
import org.virtualbox_4_2.IMedium;
import org.virtualbox_4_2.IMediumFormat;
import org.virtualbox_4_2.IProgress;
import org.virtualbox_4_2.KeyboardHIDType;
import org.virtualbox_4_2.MediumState;
import org.virtualbox_4_2.NetworkAdapterType;
import org.virtualbox_4_2.NetworkAttachmentType;
import org.virtualbox_4_2.PointingHIDType;
import org.virtualbox_4_2.StorageBus;
import org.virtualbox_4_2.StorageControllerType;
import org.virtualbox_4_2.VBoxException;
import org.virtualbox_4_2.VirtualBoxManager;

@Hypervisor(
      id = "vbox-4.2-generic",
      typeId = "generic",
      vendor = "Oracle",
      product = "Virtualbox",
      schemes = {})
public abstract class VBoxHypervisor implements _Hypervisor {
   
   /**
    * Waiting coefficient to use on ISession::getTimeRemaining() with Thread.sleep() while waiting for task in progress to finish.<br/>
    * Virtualbox returns a waiting time in seconds, this coefficient allow to turn it into milliseconds and set a 'shorter' waiting time for a more
    * reactive update.<br/>
    * Default value waits half of the estimated time reported by Virtualbox.
    */
   private final int waitingCoef = 500;
   
   protected VirtualBoxManager vbMgr;
   
   private VirtualboxHost host;
   private _EventManager evMgr;
   // TODO keep this register up-to-date
   private Map<String, VirtualboxMedium> mediumRegister;
   private _Service evMgrSvc;
   
   private List<_RawOsType> osTypeCache;
   
   @Override
   public String getId() {
      return this.getClass().getAnnotation(Hypervisor.class).id();
   }
   
   @Override
   public String getTypeId() {
      return this.getClass().getAnnotation(Hypervisor.class).typeId();
   }
   
   @Override
   public String getVendor() {
      return this.getClass().getAnnotation(Hypervisor.class).vendor();
   }
   
   @Override
   public String getProduct() {
      return this.getClass().getAnnotation(Hypervisor.class).product();
   }
   
   @Override
   public void setEventManager(_EventManager evMgr) {
      Logger.track();
      
      this.evMgr = evMgr;
   }
   
   protected abstract VirtualBoxManager connect(String options);
   
   protected abstract void disconnect();
   
   @Override
   public void start(String options) throws HypervisorException {
      Logger.track();
      
      EventManager.register(this);
      
      long start = System.currentTimeMillis();
      
      vbMgr = connect(options);
      VBox.set(vbMgr);
      
      if (!vbMgr.getVBox().getAPIVersion().contentEquals("4_2")) {
         throw new HypervisorException("Missmatch API Connector: Server is " + vbMgr.getVBox().getAPIVersion() + " but the connector handles 4_2");
      }
      
      host = new VirtualboxHost(vbMgr.getVBox().getHost());
      
      Mappings.load();
      
      mediumRegister = new ConcurrentHashMap<String, VirtualboxMedium>();
      if (Configuration.getSetting("virtualbox.ws.cache.medium.autoload", "0").contentEquals("1")) {
         Logger.verbose("Loading media registry");
         updateMediumRegistry();
      }
      
      if (Configuration.getSetting("virtualbox.ws.cache.ostype.autoload", "0").contentEquals("1")) {
         Logger.verbose("Loading OS Types");
         buildOsTypeCache();
      }
      
      try {
         if (evMgr != null) {
            evMgrSvc = new EventsManagementService(evMgr);
            evMgrSvc.startAndRun();
         } else {
            throw new HypervisorException("No Event Manager was set to handle events from Virtualbox");
         }
      } catch (ServiceException e) {
         throw new HypervisorException("Unable to start the Event Manager Service : " + e.getMessage());
      }
      
      Logger.info("Connected in " + (System.currentTimeMillis() - start) + "ms to " + host.getHostname());
      Logger.info("VB Version: " + vbMgr.getVBox().getVersion());
      Logger.info("VB Revision: " + vbMgr.getVBox().getRevision());
      Logger.info("Host OS: " + host.getOSName() + " " + host.getOSVersion());
      
      EventManager.post(new HypervisorConnectedEvent(this));
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      host = null;
      mediumRegister = null;
      osTypeCache = null;
      
      if (evMgrSvc != null) {
         if (!evMgrSvc.stopAndDie(15000)) {
            Logger.warning("Error when trying to stop the Event Manager Service");
         }
         evMgrSvc = null;
      }
      
      disconnect();
      VBox.unset();
      if (vbMgr != null) {
         vbMgr.cleanup();
         vbMgr = null;
      }
      
      EventManager.post(new HypervisorDisconnectedEvent(this));
      EventManager.unregister(this);
   }
   
   @Override
   public boolean isRunning() {
      try {
         return !vbMgr.getVBox().getVersion().isEmpty();
      } catch (Throwable t) {
         return false;
      }
   }
   
   private void updateMediumRegistry() {
      Logger.track();
      
      mediumRegister.clear();
      registerMediums(vbMgr.getVBox().getDVDImages());
      registerMediums(vbMgr.getVBox().getHardDisks());
      registerMediums(vbMgr.getVBox().getFloppyImages());
   }
   
   private void registerMediums(List<IMedium> mediums) {
      Logger.track();
      
      for (IMedium medium : mediums) {
         mediumRegister.put(medium.getId(), new VirtualboxMedium(medium));
         registerMediums(medium.getChildren());
      }
   }
   
   private IMedium getRawMedium(String uuid) {
      updateMediumRegistry();
      _RawMedium rawMed = mediumRegister.get(uuid);
      return vbMgr.getVBox().openMedium(rawMed.getLocation(), DeviceType.fromValue(rawMed.getDeviceType()), AccessMode.ReadOnly, false);
   }
   
   private void buildOsTypeCache() {
      Logger.track();
      
      List<_RawOsType> osTypes = new ArrayList<_RawOsType>();
      for (IGuestOSType osType : vbMgr.getVBox().getGuestOSTypes()) {
         osTypes.add(OsTypeFactory.get(osType));
      }
      osTypeCache = osTypes;
   }
   
   @Override
   public _RawHost getHost() {
      return host;
   }
   
   @Override
   public _RawVM createMachine(String name, String osTypeId) {
      return createMachine(null, name, osTypeId);
   }
   
   @Override
   public _RawVM createMachine(String uuid, String name, String osTypeId) {
      Logger.track();
      
      if (uuid != null) {
         uuid = "UUID=" + uuid;
      }
      if (osTypeId == null) {
         osTypeId = "Other";
      }
      try {
         IMachine machine = vbMgr.getVBox().createMachine(null, name, null, osTypeId, uuid);
         machine.saveSettings();
         vbMgr.getVBox().registerMachine(machine);
         uuid = machine.getId();
         return new VBoxMachine(uuid);
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e.getMessage());
      }
   }
   
   @Override
   public List<_RawVM> listMachines() {
      try {
         List<IMachine> rawMachines = vbMgr.getVBox().getMachines();
         List<_RawVM> machines = new ArrayList<_RawVM>();
         for (IMachine rawMachine : rawMachines) {
            machines.add(new VBoxMachine(rawMachine));
         }
         return machines;
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e.getMessage());
      }
   }
   
   @Override
   public _RawVM getMachine(String uuid) {
      try {
         return new VBoxMachine(vbMgr.getVBox().findMachine(uuid));
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e.getMessage());
      }
   }
   
   @Override
   public _RawMedium createHardDisk(String filePath, String format, Long logicalSize) {
      Logger.track();
      
      // TODO find a way to know the smallest size for a given format, set to 2MB for now.
      if (logicalSize < 2048000) {
         logicalSize = 2048000l;
      }
      
      try {
         // TODO check via ISytemProperties if the format is valid
         IMedium med = vbMgr.getVBox().createHardDisk(format, filePath);
         IProgress p = med.createBaseStorage(logicalSize, 0l);
         p.waitForCompletion(-1);
         if (p.getResultCode() != 0) {
            throw new HypervisorException("Unable to create harddisk: " + p.getErrorInfo().getResultCode() + " | " + p.getErrorInfo().getText());
         }
         updateMediumRegistry();
         return getMedium(filePath, DeviceType.HardDisk.toString());
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e.getMessage(), e);
      }
   }
   
   @Override
   public _RawMedium getMedium(String uuid) {
      // use events instead of rescanning the data if data is not found
      if (!mediumRegister.containsKey(uuid)) {
         updateMediumRegistry();
      }
      if (mediumRegister.containsKey(uuid)) {
         return mediumRegister.get(uuid);
      } else {
         throw new HypervisorException("No medium found under UUID #" + uuid);
      }
   }
   
   @Override
   public _RawMedium getMedium(String filePath, String mediumType) {
      // TODO check for mediumType validity
      try {
         IMedium medium = vbMgr.getVBox().openMedium(filePath, DeviceType.valueOf(mediumType), AccessMode.ReadOnly, false);
         if (medium.refreshState().equals(MediumState.Inaccessible)) {
            medium.close();
            throw new HypervisorException("Unable to get " + filePath + " : " + medium.getLastAccessError());
         }
         return new VirtualboxMedium(medium);
      } catch (VBoxException e) {
         throw new HypervisorException(e);
      }
   }
   
   @Override
   public _RawMedium getMedium(String filePath, EntityType mediumType) {
      return getMedium(filePath, mediumType.toString());
   }
   
   @Override
   public List<String> listNicAdapterTypes() {
      List<String> listInfo = new ArrayList<String>();
      for (NetworkAdapterType adapterType : NetworkAdapterType.values()) {
         if (!adapterType.equals(NetworkAdapterType.Null)) {
            listInfo.add(adapterType.toString());
         }
      }
      return listInfo;
   }
   
   @Override
   public List<String> listNicAttachModes() {
      List<String> listInfo = new ArrayList<String>();
      for (NetworkAttachmentType attachType : NetworkAttachmentType.values()) {
         listInfo.add(attachType.toString());
      }
      return listInfo;
   }
   
   @Override
   public List<String> listNicAttachNames(String attachMode) {
      List<String> listInfo = new ArrayList<String>();
      NetworkAttachmentType type = NetworkAttachmentType.valueOf(attachMode);
      switch (type) {
         case Bridged:
            for (IHostNetworkInterface nic : vbMgr.getVBox().getHost().getNetworkInterfaces()) {
               if (nic.getInterfaceType().equals(HostNetworkInterfaceType.Bridged)) {
                  listInfo.add(nic.getName());
               }
            }
            break;
         case Generic:
            listInfo.addAll(vbMgr.getVBox().getGenericNetworkDrivers());
            break;
         case HostOnly:
            for (IHostNetworkInterface nic : vbMgr.getVBox().getHost().getNetworkInterfaces()) {
               if (nic.getInterfaceType().equals(HostNetworkInterfaceType.HostOnly)) {
                  listInfo.add(nic.getName());
               }
            }
            break;
         case Internal:
            listInfo.addAll(vbMgr.getVBox().getInternalNetworks());
            break;
         case NAT:
            break;
         case Null:
            break;
         default:
            throw new HypervisorException(attachMode + " is not supported as an attachement mode");
      }
      return listInfo;
   }
   
   @Override
   public _RawStorageControllerType getStorageControllerType(String id) {
      try {
         // We validate that the type exist in Virtualbox
         StorageBus.valueOf(id);
         
         return VbStorageControllerType.valueOf(id);
      } catch (IllegalArgumentException e) {
         throw new HypervisorException(id + " is not a supported Controller Type");
      }
   }
   
   @Override
   public List<_RawStorageControllerType> listStorageControllerType() {
      // TODO improve so _Raw... has a concrete implementation class and use the enum to fetch the min/max values
      // valueOf(id) should be used on VbStorageControllerType with the StorageBus.toString() as its ID.
      // This way we ensure that every StorageBus has a corresponding value with data and none is missed.
      // Reminder : must skip StorageBus.Null
      //
      
      return Arrays.asList((_RawStorageControllerType[]) VbStorageControllerType.values());
   }
   
   @Override
   public _RawStorageControllerSubType getStorageControllerSubType(String id) {
      try {
         // We validate that the type exist in Virtualbox
         StorageControllerType.valueOf(id);
         
         return VbStorageControllerSubType.valueOf(id);
      } catch (IllegalArgumentException e) {
         throw new HypervisorException(id + " is not a supported Controller SubType");
      }
      
   }
   
   @Override
   public List<_RawStorageControllerSubType> listStorageControllerSubType(String type) {
      try {
         List<_RawStorageControllerSubType> subTypes = new ArrayList<_RawStorageControllerSubType>();
         // TODO Must validate using the same logic than listStorageControllerType - use IVirtualbox::StorageControllerType.toString() as lookup ID, skipping Null
         for (VbStorageControllerSubType subType : VbStorageControllerSubType.values()) {
            if (subType.getParentType().contentEquals(type)) {
               subTypes.add(subType);
            }
         }
         return subTypes;
      } catch (IllegalArgumentException e) {
         throw new HypervisorException(type + " is not a supported Controller Type");
      }
   }
   
   @Override
   public List<_RawOsType> listOsTypes() {
      if ((osTypeCache == null) || osTypeCache.isEmpty()) {
         buildOsTypeCache();
      }
      
      return new ArrayList<_RawOsType>(osTypeCache);
   }
   
   @Override
   public void deleteMachine(String uuid) {
      // TODO improve with multi-step exeception handling, as well as a separate method for hdd deletion
      VBoxSessionManager.get().unlock(uuid);
      IMachine machine = vbMgr.getVBox().findMachine(uuid);
      
      try {
         List<IMedium> hdds = machine.unregister(CleanupMode.DetachAllReturnHardDisksOnly);
         IProgress p = machine.delete(hdds);
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
         throw new HypervisorException("Error while deleting machine", e);
      }
   }
   
   @Override
   public Machine getMachineSettings(String osTypeId) {
      IGuestOSType rawOsType = vbMgr.getVBox().getGuestOSType(osTypeId);
      return OsTypeFactory.getSettings(rawOsType);
   }
   
   @Override
   public _RawOsType getOsType(String id) {
      IGuestOSType rawOsType = vbMgr.getVBox().getGuestOSType(id);
      return OsTypeFactory.get(rawOsType);
   }
   
   @Override
   public List<String> listDeviceTypes() {
      List<String> listDeviceTypes = new ArrayList<String>();
      for (DeviceType dt : DeviceType.values()) {
         listDeviceTypes.add(dt.toString());
      }
      return listDeviceTypes;
   }
   
   @Override
   public _RawVM registerMachine(String path) {
      Logger.track();
      
      IMachine machine = vbMgr.getVBox().openMachine(path);
      vbMgr.getVBox().registerMachine(machine);
      return getMachine(machine.getId());
   }
   
   @Override
   public void unregisterMachine(String uuid) {
      Logger.track();
      
      VBoxSessionManager.get().unlock(uuid);
      IMachine machine = vbMgr.getVBox().findMachine(uuid);
      machine.unregister(CleanupMode.DetachAllReturnNone);
   }
   
   @Override
   public List<String> listKeyboardModes() {
      List<String> listKeyboardModes = new ArrayList<String>();
      for (KeyboardHIDType type : KeyboardHIDType.values()) {
         if (Mappings.get(type) != null) {
            listKeyboardModes.add(Mappings.get(type).toString());
         }
      }
      return listKeyboardModes;
   }
   
   @Override
   public List<String> listMouseModes() {
      List<String> listMouseModes = new ArrayList<String>();
      for (PointingHIDType type : PointingHIDType.values()) {
         if (Mappings.get(type) != null) {
            listMouseModes.add(Mappings.get(type).toString());
         }
      }
      return listMouseModes;
   }
   
   @Override
   public List<_RawMedium> listMediums() {
      updateMediumRegistry();
      
      return new ArrayList<_RawMedium>(mediumRegister.values());
   }
   
   @Override
   public void deleteMedium(String uuid) {
      Logger.track();
      
      try {
         IMedium medium = getRawMedium(uuid);
         IProgress p = medium.deleteStorage();
         p.waitForCompletion(-1);
         if (p.getResultCode() != 0) {
            throw new HypervisorException(p.getErrorInfo().getText());
         } else {
            mediumRegister.remove(uuid);
         }
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e.getMessage(), e);
      }
   }
   
   @Override
   public List<String> listHardDiskFormats() {
      List<String> formats = new ArrayList<String>();
      for (IMediumFormat format : vbMgr.getVBox().getSystemProperties().getMediumFormats()) {
         formats.add(format.getId());
      }
      return formats;
   }
   
   @Override
   public _RawVM createMachine(String uuid, String name, String osTypeId, boolean applyTemplate) {
      Logger.track();
      
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public String getVersion() {
      return vbMgr.getVBox().getVersion();
   }
   
   @Override
   public String getRevision() {
      return vbMgr.getVBox().getRevision().toString();
   }
   
   @Override
   public _RawVM createMachine(String name) {
      return createMachine(name, null);
   }
   
   @Override
   public _RawMedium getToolsMedium() {
      String path = vbMgr.getVBox().getSystemProperties().getDefaultAdditionsISO();
      if (AxStrings.isEmpty(path)) {
         return null;
      } else {
         return getMedium(path, EntityType.DVD);
      }
   }
   
   @Override
   public boolean hasToolsMedium() {
      return !AxStrings.isEmpty(vbMgr.getVBox().getSystemProperties().getDefaultAdditionsISO());
   }
   
   @Override
   public void configure(List<_Setting> listIo) {
      for (_Setting setting : listIo) {
         if (setting.getName().equalsIgnoreCase("vbox.global.machineFolder")) {
            vbMgr.getVBox().getSystemProperties().setDefaultMachineFolder(setting.getString());
         }
         if (setting.getName().equalsIgnoreCase("vbox.global.consoleModule")) {
            vbMgr.getVBox().getSystemProperties().setDefaultVRDEExtPack(setting.getString());
         }
      }
   }
   
   @Override
   public List<_Setting> getSettings() {
      List<_Setting> settings = new ArrayList<_Setting>();
      settings.add(new StringSetting("vbox.global.machineFolder", vbMgr.getVBox().getSystemProperties().getDefaultMachineFolder()));
      settings.add(new StringSetting("vbox.global.consoleModule", vbMgr.getVBox().getSystemProperties().getDefaultVRDEExtPack()));
      return settings;
   }
   
}
