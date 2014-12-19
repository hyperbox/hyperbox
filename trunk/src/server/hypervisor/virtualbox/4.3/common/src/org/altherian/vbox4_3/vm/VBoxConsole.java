/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.vbox4_3.vm;

import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.hbox.exception.ConfigurationException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.hypervisor.vm.device._RawConsole;
import org.altherian.setting.BooleanSetting;
import org.altherian.setting.PositiveNumberSetting;
import org.altherian.setting.StringSetting;
import org.altherian.setting._Setting;
import org.altherian.tool.logging.Logger;
import org.altherian.vbox4_3.manager.VBoxSessionManager;
import org.altherian.vbox4_3.manager.VBoxSettingManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.virtualbox_4_3.ISession;
import org.virtualbox_4_3.VBoxException;

public class VBoxConsole implements _RawConsole {
   
   private VBoxMachine machine;
   
   public VBoxConsole(VBoxMachine machine) {
      this.machine = machine;
   }
   
   @Override
   public List<_Setting> listSettings() {
      Set<_Setting> settings = new HashSet<_Setting>();
      for (MachineAttribute setting : MachineAttribute.values()) {
         if (setting.getDeviceType().equals(EntityType.Console)) {
            getSetting(setting);
         }
      }
      for (String id : listProperties()) {
         settings.add(new StringSetting(id, getProperty(id)));
      }
      return new ArrayList<_Setting>(settings);
   }
   
   @Override
   public _Setting getSetting(Object getName) {
      return VBoxSettingManager.get(machine, getName);
   }
   
   @Override
   public void setSetting(_Setting s) {
      machine.setSetting(Arrays.asList(s));
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      try {
         machine.setSetting(s);
      } catch (ConfigurationException e) {
         setProperty(s);
      }
   }
   
   @Override
   public Boolean isEnabled() {
      return getSetting(MachineAttribute.VrdeEnabled).getBoolean();
   }
   
   @Override
   public void setEnabled(Boolean enable) {
      setSetting(new BooleanSetting(MachineAttribute.VrdeEnabled, enable));
   }
   
   @Override
   public String getAuthType() {
      return getSetting(MachineAttribute.VrdeAuthType).getString();
   }
   
   @Override
   public void setAuthType(String authType) {
      setSetting(new StringSetting(MachineAttribute.VrdeAuthType, authType));
   }
   
   @Override
   public String getAuthLibrary() {
      return getSetting(MachineAttribute.VrdeAuthLibrary).getString();
   }
   
   @Override
   public void setAuthLibrary(String library) {
      setSetting(new StringSetting(MachineAttribute.VrdeAuthLibrary, library));
   }
   
   @Override
   public Long getAuthTimeout() {
      return getSetting(MachineAttribute.VrdeAuthTimeout).getNumber();
   }
   
   @Override
   public void setAuthTimeout(Long timeout) {
      setSetting(new PositiveNumberSetting(MachineAttribute.VrdeAuthTimeout, timeout));
   }
   
   @Override
   public Boolean getAllowMultiConnection() {
      return getSetting(MachineAttribute.VrdeMultiConnection).getBoolean();
   }
   
   @Override
   public void setAllowMultiConnection(Boolean allow) {
      setSetting(new BooleanSetting(MachineAttribute.VrdeMultiConnection, allow));
   }
   
   @Override
   public Set<String> listProperties() {
      return new HashSet<String>(VBoxSessionManager.get().getCurrent(machine.getUuid()).getVRDEServer().getVRDEProperties());
   }
   
   @Override
   public boolean hasProperty(String key) {
      return listProperties().contains(key);
   }
   
   @Override
   public String getProperty(String key) {
      return VBoxSessionManager.get().getCurrent(machine.getUuid()).getVRDEServer().getVRDEProperty(key);
   }
   
   public void setProperty(_Setting s) {
      setProperty(s.getName(), s.getString());
   }
   
   public void setProperty(List<_Setting> sList) {
      for (_Setting s : sList) {
         setProperty(s);
      }
   }
   
   @Override
   public void setProperty(String key, String value) {
      ISession session = VBoxSessionManager.get().lockAuto(machine.getUuid());
      try {
         Logger.debug("Setting VRDE Property with key " + key + " and value " + value);
         session.getMachine().getVRDEServer().setVRDEProperty(key, value);
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e.getMessage());
      } finally {
         VBoxSessionManager.get().unlockAuto(machine.getUuid(), true);
      }
   }
   
   @Override
   public void unsetProperty(String key) {
      setProperty(key, null);
   }
   
   @Override
   public Boolean isActive() {
      // TODO Detect if console is active or not
      return false;
   }
   
   @Override
   public String getAddress() {
      return getSetting(MachineAttribute.VrdeAddress).getString();
   }
   
   @Override
   public Long getPort() {
      return getSetting(MachineAttribute.VrdePort).getNumber();
   }
   
   @Override
   public String getProtocol() {
      return getSetting(MachineAttribute.VrdeModule).getString();
   }
   
   /*
   @Override
   public Boolean getReuseSingleConnection() {
      return getSetting(MachineAttributes.VrdeReuseSingleConnection).getBoolean();
   }
   
   @Override
   public void setReuseSingleConnection(Boolean reuse) {
      setSetting(new BooleanSetting(MachineAttributes.VrdeReuseSingleConnection, reuse));
   }
   
   @Override
   public String getModule() {
      return getSetting(MachineAttributes.VrdeExtPack).getString();
   }
   
   @Override
   public void setModule(String module) {
      setSetting(new StringSetting(MachineAttributes.VrdeExtPack, module));
   }
    */
   
}
