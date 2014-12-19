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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * IO object used to send or receive data about a VM
 * 
 * @author noteirak
 */
public final class MachineIn extends ObjectIn<EntityType> {
   
   private String uuid;
   private Map<Long, NetworkInterfaceIn> nics = new HashMap<Long, NetworkInterfaceIn>();
   private Map<String, StorageControllerIn> strCtrs = new HashMap<String, StorageControllerIn>();
   private Map<String, DeviceIn> devs = new HashMap<String, DeviceIn>();
   
   public MachineIn() {
      super(EntityType.Machine);
   }
   
   /**
    * Build a machine message with the given ID.
    * 
    * @param id The ID of the machine to send data about.
    */
   public MachineIn(String id) {
      super(EntityType.Machine, id);
      setUuid(id);
   }
   
   public MachineIn(MachineOut mOut) {
      this(mOut.getServerId(), mOut.getUuid());
   }
   
   public MachineIn(String srvId, String vmId) {
      super(EntityType.Machine, vmId);
      setServerId(srvId);
      setUuid(vmId);
   }
   
   protected void setUuid(String uuid) {
      this.uuid = uuid;
   }
   
   /**
    * Get the UUID for this machine
    * 
    * @return a String for this UUID
    */
   public String getUuid() {
      return uuid;
   }
   
   /**
    * Get the machine name.<br/>
    * Helper method that gets the setting name and return its value.
    * 
    * @return a String containing the name
    */
   public String getName() {
      return getSetting(MachineAttribute.Name).getString();
   }
   
   /**
    * Set the machine name.<br/>
    * Helper method that created a new StringSettingIO with <code>MachineSettings.Name</code> and set the value to <i>name</i>
    * 
    * @param name a String containing the name
    */
   public void setName(String name) {
      setSetting(new StringSettingIO(MachineAttribute.Name, name));
   }
   
   public String getServerId() {
      return getSetting(MachineAttribute.ServerId).getString();
   }
   
   public void setServerId(String id) {
      setSetting(new StringSettingIO(MachineAttribute.ServerId, id));
   }
   
   public void addStorageController(StorageControllerIn strCtrIo) {
      if (strCtrs.containsKey(strCtrIo.getId())) {
         if (strCtrs.get(strCtrIo.getId()).getAction().equals(Action.Delete)) {
            strCtrIo.setAction(Action.Replace);
         }
      }
      strCtrs.put(strCtrIo.getId(), strCtrIo);
   }
   
   public void modifyStorageController(StorageControllerIn strCtrIo) {
      if (strCtrs.containsKey(strCtrIo.getId()) && !getStorageController(strCtrIo.getId()).getAction().equals(Action.Modify)) {
         throw new HyperboxRuntimeException("Cannot mofidy a Storage Controller [" + strCtrIo.getId() + "]");
      }
      
      strCtrIo.setAction(Action.Modify);
      strCtrs.put(strCtrIo.getId(), strCtrIo);
   }
   
   public void removeStorageController(String name) {
      if (strCtrs.containsKey(name)) {
         StorageControllerIn scIn = strCtrs.get(name);
         if (scIn.getAction().equals(Action.Create)) {
            strCtrs.remove(name);
         } else {
            scIn.setAction(Action.Delete);
         }
      } else {
         StorageControllerIn scIn = new StorageControllerIn(uuid, name, "");
         scIn.setAction(Action.Delete);
         strCtrs.put(name, scIn);
      }
   }
   
   public List<StorageControllerIn> listStorageController() {
      return new ArrayList<StorageControllerIn>(strCtrs.values());
   }
   
   public StorageControllerIn getStorageController(String id) {
      return strCtrs.get(id);
   }
   
   public void addNetworkInterface(NetworkInterfaceIn nicIo) {
      nics.put(nicIo.getNicId(), nicIo);
   }
   
   public void removeNetworkInterface(long nicId) {
      nics.remove(nicId);
   }
   
   public void addDevice(DeviceIn dev) {
      devs.put(dev.getId(), dev);
   }
   
   public DeviceIn getDevice(String id) {
      return devs.get(id);
   }
   
   public Set<DeviceIn> listDevice() {
      return new HashSet<DeviceIn>(devs.values());
   }
   
   public List<NetworkInterfaceIn> listNetworkInterface() {
      return new ArrayList<NetworkInterfaceIn>(nics.values());
   }
   
   public NetworkInterfaceIn getNetworkInterface(Long id) {
      return nics.get(id);
   }
   
   @Override
   public String toString() {
      return getName();
   }
   
}
