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

package org.altherian.hboxd.vbox.hypervisor.ws4_3.vm;

import org.altherian.hbox.constant.NetworkInterfaceSettings;
import org.altherian.hbox.settings.vbox.network.NicAdapterTypeSetting;
import org.altherian.hbox.settings.vbox.network.NicAttachModeSetting;
import org.altherian.hbox.settings.vbox.network.NicAttachNameSetting;
import org.altherian.hbox.settings.vbox.network.NicCableConnectedSetting;
import org.altherian.hbox.settings.vbox.network.NicEnabledSetting;
import org.altherian.hbox.settings.vbox.network.NicMacAddressSetting;
import org.altherian.hboxd.hypervisor.vm.device._RawNetworkInterface;
import org.altherian.hboxd.settings.BooleanSetting;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.hboxd.vbox4_3.ws.manager.VbSettingManager;
import org.altherian.tool.logging.Logger;

import java.util.Arrays;
import java.util.List;

public class VBoxNetworkInterface implements _RawNetworkInterface {
   
   private String machineUuid;
   private long nicIndex;
   
   public VBoxNetworkInterface(VBoxMachine machine, long index) {
      this(machine.getUuid(), index);
   }
   
   public VBoxNetworkInterface(String machineUuid, long nicIndex) {
      this.machineUuid = machineUuid;
      this.nicIndex = nicIndex;
   }
   
   @Override
   public String getMachineUuid() {
      return machineUuid;
   }
   
   @Override
   public long getNicId() {
      return nicIndex;
   }
   
   @Override
   public boolean isEnabled() {
      return ((BooleanSetting) getSetting(NetworkInterfaceSettings.Enabled)).getValue();
   }
   
   @Override
   public void setEnabled(boolean isEnabled) {
      Logger.track();
      
      setSetting(new NicEnabledSetting(isEnabled));
   }
   
   @Override
   public String getMacAddress() {
      return ((StringSetting) getSetting(NetworkInterfaceSettings.MacAddress)).getValue();
   }
   
   @Override
   public void setMacAddress(String macAddress) {
      Logger.track();
      
      setSetting(new NicMacAddressSetting(macAddress));
   }
   
   @Override
   public boolean isCableConnected() {
      return ((BooleanSetting) getSetting(NetworkInterfaceSettings.CableConnected)).getValue();
   }
   
   @Override
   public void setCableConnected(boolean isConnected) {
      Logger.track();
      
      setSetting(new NicCableConnectedSetting(isConnected));
   }
   
   @Override
   public String getAttachMode() {
      return ((StringSetting) getSetting(NetworkInterfaceSettings.AttachMode)).getValue();
   }
   
   @Override
   public void setAttachMode(String attachMode) {
      Logger.track();
      
      setSetting(new NicAttachModeSetting(attachMode));
   }
   
   @Override
   public String getAttachName() {
      return ((StringSetting) getSetting(NetworkInterfaceSettings.AttachName)).getValue();
   }
   
   @Override
   public void setAttachName(String attachName) {
      Logger.track();
      
      setSetting(new NicAttachNameSetting(attachName));
   }
   
   @Override
   public String getAdapterType() {
      return ((StringSetting) getSetting(NetworkInterfaceSettings.AdapterType)).getValue();
   }
   
   @Override
   public void setAdapterType(String adapterType) {
      Logger.track();
      
      setSetting(new NicAdapterTypeSetting(adapterType));
   }
   
   @Override
   public List<_Setting> listSettings() {
      return VbSettingManager.list(this);
   }
   
   @Override
   public _Setting getSetting(Object getName) {
      return VbSettingManager.get(this, getName);
   }
   
   @Override
   public void setSetting(_Setting s) {
      setSetting(Arrays.asList(s));
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      VbSettingManager.set(this, s);
   }
   
}
