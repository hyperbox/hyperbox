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

package org.altherian.hboxd.vbox4_2.ws.factory;

import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.KeyboardModes;
import org.altherian.hbox.constant.MouseModes;
import org.altherian.hbox.constant.StorageControllerSettings;
import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.data.Device;
import org.altherian.hbox.data.Machine;
import org.altherian.hbox.settings.vbox.cpu.PaeSetting;
import org.altherian.hbox.settings.vbox.general.KeyboardModeSetting;
import org.altherian.hbox.settings.vbox.general.MouseModeSetting;
import org.altherian.hbox.settings.vbox.medium.MediumSizeSetting;
import org.altherian.hbox.settings.vbox.memory.MemorySetting;
import org.altherian.hbox.settings.vbox.motherboard.ChipsetSetting;
import org.altherian.hbox.settings.vbox.motherboard.FirmwareSetting;
import org.altherian.hbox.settings.vbox.network.NicAdapterTypeSetting;
import org.altherian.hbox.settings.vbox.usb.UsbOhciSetting;
import org.altherian.hbox.settings.vbox.video.Accelerate2dVideoSetting;
import org.altherian.hbox.settings.vbox.video.Accelerate3dSetting;
import org.altherian.hbox.settings.vbox.video.VRamSetting;
import org.altherian.hbox.settings.vbox.virtual.HpetSetting;
import org.altherian.hbox.settings.vbox.virtual.HwVirtExExclSetting;
import org.altherian.hboxd.comm.io.factory.SettingIoFactory;
import org.altherian.hboxd.hypervisor._RawOsType;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.hboxd.vbox.hypervisor.ws4_2.VirtualboxOsType;
import org.altherian.hboxd.vbox4_2.ws.utils.Mappings;

import org.virtualbox_4_2.IGuestOSType;

public final class OsTypeFactory {
   
   private OsTypeFactory() {
      // class is static
   }
   
   public static _RawOsType get(IGuestOSType guestOs) {
      return new VirtualboxOsType(guestOs);
   }
   
   public static Machine getSettings(IGuestOSType guestOs) {
      Machine vm = new Machine();
      
      vm.setSetting(SettingIoFactory.get(new Accelerate2dVideoSetting(guestOs.getRecommended2DVideoAcceleration())));
      vm.setSetting(SettingIoFactory.get(new Accelerate3dSetting(guestOs.getRecommended3DAcceleration())));
      // TODO settings.add(new AudioControllerSetting(guestOs.getRecommendedAudioController()));
      vm.setSetting(SettingIoFactory.get(new ChipsetSetting(guestOs.getRecommendedChipset().toString())));
      
      vm.setSetting(SettingIoFactory.get(new PaeSetting(guestOs.getRecommendedPAE())));
      vm.setSetting(SettingIoFactory.get(new MemorySetting(guestOs.getRecommendedRAM())));
      // TODO add RTCuseUTC
      vm.setSetting(SettingIoFactory.get(new UsbOhciSetting(guestOs.getRecommendedUSB())));
      if (guestOs.getRecommendedUSBHID()) {
         vm.setSetting(SettingIoFactory.get(new KeyboardModeSetting(KeyboardModes.Usb)));
         vm.setSetting(SettingIoFactory.get(new MouseModeSetting(MouseModes.Usb)));
      }
      if (guestOs.getRecommendedUSBTablet()) {
         vm.setSetting(SettingIoFactory.get(new MouseModeSetting(MouseModes.UsbTablet)));
      }
      vm.setSetting(SettingIoFactory.get(new VRamSetting(guestOs.getRecommendedVRAM())));
      vm.setSetting(SettingIoFactory.get(new FirmwareSetting(Mappings.get(guestOs.getRecommendedFirmware()))));
      vm.setSetting(SettingIoFactory.get(new HpetSetting(guestOs.getRecommendedHPET())));
      vm.setSetting(SettingIoFactory.get(new HwVirtExExclSetting(guestOs.getRecommendedVirtEx())));
      
      if (guestOs.getRecommendedFloppy()) {
         Device dev = new Device();
         dev.setTypeId(EntityTypes.StorageController.getId());
         dev.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerSettings.Type, StorageControllerType.Floppy.getId())));
         vm.addDevice(dev);
      }
      
      Device dvdStorCtrl = new Device(EntityTypes.DvdDrive.getId());
      dvdStorCtrl.setTypeId(EntityTypes.DvdDrive.getId());
      dvdStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerSettings.Type, guestOs.getRecommendedDVDStorageBus().toString())));
      dvdStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerSettings.SubType, guestOs.getRecommendedDVDStorageController().toString())));
      vm.addDevice(dvdStorCtrl);
      
      Device hddStorCtrl = new Device(EntityTypes.DiskDrive.getId());
      hddStorCtrl.setTypeId(EntityTypes.DiskDrive.getId());
      hddStorCtrl.setSetting(SettingIoFactory.get(new MediumSizeSetting(guestOs.getRecommendedHDD())));
      hddStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerSettings.Type, guestOs.getRecommendedHDStorageBus().toString())));
      hddStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerSettings.SubType, guestOs.getRecommendedHDStorageController().toString())));
      vm.addDevice(hddStorCtrl);
      
      Device networkDevice = new Device();
      networkDevice.setTypeId(EntityTypes.Network.getId());
      networkDevice.setSetting(SettingIoFactory.get(new NicAdapterTypeSetting(guestOs.getAdapterType().toString())));
      
      return vm;
   }
   
}
