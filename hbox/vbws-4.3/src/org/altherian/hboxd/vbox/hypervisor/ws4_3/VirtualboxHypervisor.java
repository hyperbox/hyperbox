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

package org.altherian.hboxd.vbox.hypervisor.ws4_3;

import org.altherian.hbox.Configuration;
import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.data.Machine;
import org.altherian.hbox.exception.FeatureNotImplementedException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hbox.exception.ServiceException;
import org.altherian.hboxd.event._EventManager;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor._RawOsType;
import org.altherian.hboxd.hypervisor.host._RawHost;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerSubType;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerType;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.service._Service;
import org.altherian.hboxd.vbox.hypervisor.ws4_3.host.VirtualboxHost;
import org.altherian.hboxd.vbox.hypervisor.ws4_3.storage.VirtualboxMedium;
import org.altherian.hboxd.vbox.hypervisor.ws4_3.vm.VBoxMachine;
import org.altherian.hboxd.vbox4_3.VbStorageControllerSubType;
import org.altherian.hboxd.vbox4_3.VbStorageControllerType;
import org.altherian.hboxd.vbox4_3.ws.factory.ConnectionManager;
import org.altherian.hboxd.vbox4_3.ws.factory.OsTypeFactory;
import org.altherian.hboxd.vbox4_3.ws.manager.VbSessionManager;
import org.altherian.hboxd.vbox4_3.ws.service.EventsManagementService;
import org.altherian.hboxd.vbox4_3.ws.utils.Mappings;
import org.altherian.tool.StringTools;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.virtualbox_4_3.AccessMode;
import org.virtualbox_4_3.CleanupMode;
import org.virtualbox_4_3.DeviceType;
import org.virtualbox_4_3.HostNetworkInterfaceType;
import org.virtualbox_4_3.IGuestOSType;
import org.virtualbox_4_3.IHostNetworkInterface;
import org.virtualbox_4_3.IMachine;
import org.virtualbox_4_3.IMedium;
import org.virtualbox_4_3.IMediumFormat;
import org.virtualbox_4_3.IProgress;
import org.virtualbox_4_3.KeyboardHIDType;
import org.virtualbox_4_3.MediumState;
import org.virtualbox_4_3.MediumVariant;
import org.virtualbox_4_3.NetworkAdapterType;
import org.virtualbox_4_3.NetworkAttachmentType;
import org.virtualbox_4_3.PointingHIDType;
import org.virtualbox_4_3.ProcessorFeature;
import org.virtualbox_4_3.StorageBus;
import org.virtualbox_4_3.StorageControllerType;
import org.virtualbox_4_3.VBoxException;

public class VirtualboxHypervisor implements _Hypervisor {
   
   public final static String ID = "vbox-4.3-ws";
   public final static String TYPE_ID = "Web Services";
   public final static String PRODUCT = "Virtualbox";
   public final static String VENDOR = "Oracle";
   
   private VirtualboxHost host;
   private _EventManager evMgr;
   // TODO keep this register up-to-date
   private Map<String, VirtualboxMedium> mediumRegister;
   private _Service evMgrSvc;
   
   private List<_RawOsType> osTypeCache;
   
   @Override
   public void setEventManager(_EventManager evMgr) {
      Logger.track();
      
      this.evMgr = evMgr;
   }
   
   @Override
   public void connect(String options) throws HypervisorException {
      Logger.track();
      
      long start = System.currentTimeMillis();
      Logger.info("Connecting to Virtualbox...");
      ConnectionManager.start(options);
      this.host = new VirtualboxHost();
      
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
      /*
      try {
         VbSettingManager.load();
      } catch (HyperboxException e) {
         throw new HypervisorException("Unable to load any setting", e);
      }
       */
      
      Logger.info("Connected in " + (System.currentTimeMillis() - start) + "ms to " + host.getHostname());
      Logger.info("VB Version: " + ConnectionManager.getBox().getVersion());
      Logger.info("VB Revision: " + ConnectionManager.getBox().getRevision());
      Logger.info("Host OS: " + ConnectionManager.getBox().getHost().getOperatingSystem() + " " + ConnectionManager.getBox().getHost().getOSVersion());
   }
   
   private void updateMediumRegistry() {
      Logger.track();
      
      mediumRegister.clear();
      registerMediums(ConnectionManager.getBox().getDVDImages());
      registerMediums(ConnectionManager.getBox().getHardDisks());
      registerMediums(ConnectionManager.getBox().getFloppyImages());
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
      return ConnectionManager.getBox().openMedium(rawMed.getLocation(), DeviceType.fromValue(rawMed.getDeviceType()), AccessMode.ReadOnly, false);
   }
   
   private void buildOsTypeCache() {
      Logger.track();
      
      List<_RawOsType> osTypes = new ArrayList<_RawOsType>();
      for (IGuestOSType osType : ConnectionManager.getBox().getGuestOSTypes()) {
         osTypes.add(OsTypeFactory.get(osType));
      }
      osTypeCache = osTypes;
   }
   
   @Override
   public void disconnect() {
      Logger.track();
      
      mediumRegister = null;
      
      if (evMgrSvc != null) {
         try {
            evMgrSvc.stopAndDie(15000);
         } catch (ServiceException e) {
            Logger.error("Error when trying to stop the Event Manager Service :" + e.getMessage());
         }
      }
      ConnectionManager.stop();
   }
   
   @Override
   public _RawHost getHost() {
      Logger.track();
      
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
         if (ConnectionManager.getBox().getHost().getProcessorFeature(ProcessorFeature.HWVirtEx)) {
            osTypeId = "Other_64";
         } else {
            osTypeId = "Other";
         }
      }
      
      Logger.debug("Creating Machine - UUID: " + uuid + " - Name: " + name + " - OS Type: " + osTypeId);
      
      try {
         IMachine machine = ConnectionManager.getBox().createMachine(null, name, null, osTypeId, uuid);
         machine.saveSettings();
         ConnectionManager.getBox().registerMachine(machine);
         uuid = machine.getId();
         return new VBoxMachine(uuid);
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e.getMessage());
      }
   }
   
   @Override
   public List<_RawVM> listMachines() {
      try {
         List<IMachine> rawMachines = ConnectionManager.getBox().getMachines();
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
         return new VBoxMachine(ConnectionManager.findMachine(uuid));
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
         IMedium med = ConnectionManager.getBox().createHardDisk(format, filePath);
         IProgress p = med.createBaseStorage(logicalSize, Arrays.asList(MediumVariant.Standard));
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
         IMedium medium = ConnectionManager.getBox().openMedium(filePath, DeviceType.valueOf(mediumType), AccessMode.ReadOnly, false);
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
   public _RawMedium getMedium(String filePath, EntityTypes mediumType) {
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
            for (IHostNetworkInterface nic : ConnectionManager.getBox().getHost().getNetworkInterfaces()) {
               if (nic.getInterfaceType().equals(HostNetworkInterfaceType.Bridged)) {
                  listInfo.add(nic.getName());
               }
            }
            break;
         case Generic:
            listInfo.addAll(ConnectionManager.getBox().getGenericNetworkDrivers());
            break;
         case HostOnly:
            for (IHostNetworkInterface nic : ConnectionManager.getBox().getHost().getNetworkInterfaces()) {
               if (nic.getInterfaceType().equals(HostNetworkInterfaceType.HostOnly)) {
                  listInfo.add(nic.getName());
               }
            }
            break;
         case Internal:
            listInfo.addAll(ConnectionManager.getBox().getInternalNetworks());
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
      VbSessionManager.get().unlock(uuid);
      IMachine machine = ConnectionManager.findMachine(uuid);
      
      try {
         List<IMedium> hdds = machine.unregister(CleanupMode.DetachAllReturnHardDisksOnly);
         machine.deleteConfig(hdds);
      } catch (VBoxException e) {
         throw new HypervisorException("Error while deleting machine", e);
      }
   }
   
   @Override
   public Machine getMachineSettings(String osTypeId) {
      IGuestOSType rawOsType = ConnectionManager.getBox().getGuestOSType(osTypeId);
      return OsTypeFactory.getSettings(rawOsType);
   }
   
   @Override
   public _RawOsType getOsType(String id) {
      IGuestOSType rawOsType = ConnectionManager.getBox().getGuestOSType(id);
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
      
      IMachine machine = ConnectionManager.getBox().openMachine(path);
      ConnectionManager.getBox().registerMachine(machine);
      return getMachine(machine.getId());
   }
   
   @Override
   public void unregisterMachine(String uuid) {
      Logger.track();
      
      VbSessionManager.get().unlock(uuid);
      IMachine machine = ConnectionManager.findMachine(uuid);
      machine.unregister(CleanupMode.DetachAllReturnHardDisksOnly);
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
      for (IMediumFormat format : ConnectionManager.getBox().getSystemProperties().getMediumFormats()) {
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
   public String getVendor() {
      return "Oracle";
   }
   
   @Override
   public String getProduct() {
      return "Virtualbox";
   }
   
   @Override
   public String getVersion() {
      return ConnectionManager.getBox().getVersion();
   }
   
   @Override
   public String getRevision() {
      return ConnectionManager.getBox().getRevision().toString();
   }
   
   @Override
   public String getId() {
      return ID;
   }
   
   @Override
   public _RawVM createMachine(String name) {
      return createMachine(name, null);
   }
   
   @Override
   public String getTypeId() {
      return "virtualbox";
   }
   
   @Override
   public boolean isConnected() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public _RawMedium getToolsMedium() {
      String path = ConnectionManager.getBox().getSystemProperties().getDefaultAdditionsISO();
      if (StringTools.isEmpty(path)) {
         return null;
      } else {
         return getMedium(path, EntityTypes.DVD);
      }
   }
   
   @Override
   public boolean hasToolsMedium() {
      return !StringTools.isEmpty(ConnectionManager.getBox().getSystemProperties().getDefaultAdditionsISO());
   }
   
}
