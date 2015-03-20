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

package org.altherian.hboxd.hypervisor;

import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.data.Machine;
import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hbox.exception.net.InvalidNetworkModeException;
import org.altherian.hbox.exception.net.NetworkAdaptorNotFoundException;
import org.altherian.hbox.hypervisor._MachineLogFile;
import org.altherian.hbox.hypervisor.net._NetAdaptor;
import org.altherian.hbox.hypervisor.net._NetMode;
import org.altherian.hboxd.event._EventManager;
import org.altherian.hboxd.hypervisor.host._RawHost;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerSubType;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerType;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.setting._Setting;
import java.util.List;

public interface _Hypervisor {

   public String getId();

   public String getTypeId();

   public String getVendor();

   public String getProduct();

   public String getVersion();

   public String getRevision();

   public void start(String options) throws HypervisorException;

   public void stop();

   public boolean isRunning();

   public _RawHost getHost();

   public _RawMedium createHardDisk(String filePath, String format, Long logicalSize);

   public _RawVM createMachine(String name);

   public _RawVM createMachine(String name, String osTypeId);

   public _RawVM createMachine(String uuid, String name, String osTypeId);

   public _RawVM createMachine(String uuid, String name, String osTypeId, boolean applyTemplate);

   public _RawVM getMachine(String id);

   public Machine getMachineSettings(String osTypeId);

   public void deleteMachine(String uuid);

   public void deleteMedium(String uuid);

   public _RawMedium getMedium(String uuid);

   public _RawMedium getMedium(String filePath, EntityType mediumType);

   public _RawMedium getMedium(String filePath, String mediumType);

   public _RawOsType getOsType(String id);

   public _RawStorageControllerSubType getStorageControllerSubType(String id);

   public _RawStorageControllerType getStorageControllerType(String id);

   public List<String> listDeviceTypes();

   public List<String> listHardDiskFormats();

   public List<String> listKeyboardModes();

   public List<_RawVM> listMachines();

   public boolean hasToolsMedium();

   /**
    * Get the medium for the hypervisor tools that can be attached to a machine, or null if none exists.
    *
    * @return _RawMedium the medium or null if none exists.
    */
   public _RawMedium getToolsMedium();

   public List<_RawMedium> listMediums();

   public List<String> listMouseModes();

   public List<String> listNicAdapterTypes();

   public List<String> listNicAttachModes();

   public List<String> listNicAttachNames(String attachMode);

   public List<_RawOsType> listOsTypes();

   public List<_RawStorageControllerSubType> listStorageControllerSubType(String type);

   public List<_RawStorageControllerType> listStorageControllerType();

   public _RawVM registerMachine(String path);

   public void setEventManager(_EventManager evMgr);

   public void unregisterMachine(String uuid);

   public List<_Setting> getSettings();

   public void configure(List<_Setting> listIo);

   /**
    * List all supported network modes for the adaptors
    *
    * @return List of network modes or empty list if none is found
    */
   public List<_NetMode> listNetworkModes();

   public _NetMode getNetworkMode(String id);

   /**
    * List Network adaptors accessible to the VMs
    *
    * @return List of network adaptors or empty list if none is found
    */
   public List<_NetAdaptor> listAdaptors();

   /**
    * List all network adaptors for the given network mode
    *
    * @param modeId Network mode ID to match
    * @return List of network adaptor of the specified network mode, or empty list if none is found
    * @throws InvalidNetworkModeException If the netmork mode does not exist
    */
   public List<_NetAdaptor> listAdaptors(String modeId) throws InvalidNetworkModeException;

   public _NetAdaptor createAdaptor(String modeId, String name) throws InvalidNetworkModeException;

   public void removeAdaptor(String modeId, String adaptorId) throws InvalidNetworkModeException;

   public _NetAdaptor getNetAdaptor(String modId, String adaptorId) throws NetworkAdaptorNotFoundException;

   public List<String> getLogFileList(String vmId);

   public _MachineLogFile getLogFile(String vmId, long logid);
   
   public void importAppliance(String applianceFile);

}
