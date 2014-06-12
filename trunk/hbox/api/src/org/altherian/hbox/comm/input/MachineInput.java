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

package org.altherian.hbox.comm.input;

import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hbox.exception.HyperboxRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IO object used to send or receive data about a VM
 * 
 * @author noteirak
 */
public final class MachineInput extends ObjectInput {
   
   private String uuid;
   private Map<Long, NetworkInterfaceInput> nics = new HashMap<Long, NetworkInterfaceInput>();
   private Map<String, StorageControllerInput> strCtrs = new HashMap<String, StorageControllerInput>();
   
   public MachineInput() {
      super();
   }
   
   /**
    * Build a machine message with the given ID.
    * 
    * @param id The ID of the machine to send data about.
    */
   public MachineInput(String id) {
      super(id);
      setUuid(id);
   }
   
   public MachineInput(MachineOutput mOut) {
      this(mOut.getServerId(), mOut.getUuid());
   }
   
   public MachineInput(String srvId, String vmId) {
      super(vmId);
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
      return getSetting(MachineAttributes.Name).getString();
   }
   
   /**
    * Set the machine name.<br/>
    * Helper method that created a new StringSettingIO with <code>MachineSettings.Name</code> and set the value to <i>name</i>
    * 
    * @param name a String containing the name
    */
   public void setName(String name) {
      setSetting(new StringSettingIO(MachineAttributes.Name, name));
   }
   
   public String getServerId() {
      return getSetting(MachineAttributes.ServerId).getString();
   }
   
   public void setServerId(String id) {
      setSetting(new StringSettingIO(MachineAttributes.ServerId, id));
   }
   
   public void addStorageController(StorageControllerInput strCtrIo) {
      if (strCtrs.containsKey(strCtrIo.getId())) {
         if (strCtrs.get(strCtrIo.getId()).getAction().equals(Action.Delete)) {
            strCtrIo.setAction(Action.Replace);
         }
      }
      strCtrs.put(strCtrIo.getId(), strCtrIo);
   }
   
   public void modifyStorageController(StorageControllerInput strCtrIo) {
      if (strCtrs.containsKey(strCtrIo.getId()) && !getStorageController(strCtrIo.getId()).getAction().equals(Action.Modify)) {
         throw new HyperboxRuntimeException("Cannot mofidy a Storage Controller [" + strCtrIo.getId() + "]");
      }
      
      strCtrIo.setAction(Action.Modify);
      strCtrs.put(strCtrIo.getId(), strCtrIo);
   }
   
   public void removeStorageController(String name) {
      if (strCtrs.containsKey(name)) {
         StorageControllerInput scIn = strCtrs.get(name);
         if (scIn.getAction().equals(Action.Create)) {
            strCtrs.remove(name);
         } else {
            scIn.setAction(Action.Delete);
         }
      } else {
         StorageControllerInput scIn = new StorageControllerInput(uuid, name, "");
         scIn.setAction(Action.Delete);
         strCtrs.put(name, scIn);
      }
   }
   
   public List<StorageControllerInput> listStorageController() {
      return new ArrayList<StorageControllerInput>(strCtrs.values());
   }
   
   public StorageControllerInput getStorageController(String id) {
      return strCtrs.get(id);
   }
   
   public void addNetworkInterface(NetworkInterfaceInput nicIo) {
      nics.put(nicIo.getNicId(), nicIo);
   }
   
   public void removeNetworkInterface(long nicId) {
      nics.remove(nicId);
   }
   
   public List<NetworkInterfaceInput> listNetworkInterface() {
      return new ArrayList<NetworkInterfaceInput>(nics.values());
   }
   
   public NetworkInterfaceInput getNetworkInterface(Long id) {
      return nics.get(id);
   }
   
   @Override
   public String toString() {
      return getName();
   }
   
}
