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

package org.altherian.hboxd.hypervisor;

import org.altherian.hbox.constant.Entity;
import org.altherian.hbox.data.Machine;
import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hboxd.event._EventManager;
import org.altherian.hboxd.hypervisor.host._RawHost;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerSubType;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerType;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.settings._Setting;

import java.util.List;

public interface _Hypervisor {
   
   public void start(String options) throws HypervisorException;
   
   public void stop();
   
   public boolean isRunning();
   
   public _RawMedium createHardDisk(String filePath, String format, Long logicalSize);
   
   public _RawVM createMachine(String name);
   
   public _RawVM createMachine(String name, String osTypeId);
   
   public _RawVM createMachine(String uuid, String name, String osTypeId);
   
   public _RawVM createMachine(String uuid, String name, String osTypeId, boolean applyTemplate);
   
   public void deleteMachine(String uuid);
   
   public void deleteMedium(String uuid);
   
   public _RawHost getHost();
   
   public String getId();
   
   public String getTypeId();
   
   public _RawVM getMachine(String id);
   
   public Machine getMachineSettings(String osTypeId);
   
   public _RawMedium getMedium(String uuid);
   
   public _RawMedium getMedium(String filePath, Entity mediumType);
   
   public _RawMedium getMedium(String filePath, String mediumType);
   
   public _RawOsType getOsType(String id);
   
   public String getProduct();
   
   public String getRevision();
   
   public _RawStorageControllerSubType getStorageControllerSubType(String id);
   
   public _RawStorageControllerType getStorageControllerType(String id);
   
   public String getVendor();
   
   public String getVersion();
   
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
   
}
