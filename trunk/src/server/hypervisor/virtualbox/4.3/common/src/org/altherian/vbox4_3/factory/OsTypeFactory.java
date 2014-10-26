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

package org.altherian.vbox4_3.factory;

import org.altherian.hbox.constant.Entity;
import org.altherian.hbox.constant.KeyboardMode;
import org.altherian.hbox.constant.MouseMode;
import org.altherian.hbox.constant.StorageControllerAttribute;
import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.data.Device;
import org.altherian.hbox.data.Machine;
import org.altherian.hboxd.comm.io.factory.SettingIoFactory;
import org.altherian.hboxd.hypervisor._RawOsType;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.vbox.settings.cpu.PaeSetting;
import org.altherian.vbox.settings.general.KeyboardModeSetting;
import org.altherian.vbox.settings.general.MouseModeSetting;
import org.altherian.vbox.settings.medium.MediumSizeSetting;
import org.altherian.vbox.settings.memory.MemorySetting;
import org.altherian.vbox.settings.motherboard.ChipsetSetting;
import org.altherian.vbox.settings.motherboard.FirmwareSetting;
import org.altherian.vbox.settings.network.NicAdapterTypeSetting;
import org.altherian.vbox.settings.usb.UsbOhciSetting;
import org.altherian.vbox.settings.video.Accelerate2dVideoSetting;
import org.altherian.vbox.settings.video.Accelerate3dSetting;
import org.altherian.vbox.settings.video.VRamSetting;
import org.altherian.vbox.settings.virtual.HpetSetting;
import org.altherian.vbox.settings.virtual.HwVirtExExclSetting;
import org.altherian.vbox4_3.VBoxOsType;
import org.altherian.vbox4_3.data.Mappings;

import org.virtualbox_4_3.IGuestOSType;

public final class OsTypeFactory {
   
   private OsTypeFactory() {
      // class is static
   }
   
   public static _RawOsType get(IGuestOSType guestOs) {
      return new VBoxOsType(guestOs);
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
         vm.setSetting(SettingIoFactory.get(new KeyboardModeSetting(KeyboardMode.Usb)));
         vm.setSetting(SettingIoFactory.get(new MouseModeSetting(MouseMode.Usb)));
      }
      if (guestOs.getRecommendedUSBTablet()) {
         vm.setSetting(SettingIoFactory.get(new MouseModeSetting(MouseMode.UsbTablet)));
      }
      vm.setSetting(SettingIoFactory.get(new VRamSetting(guestOs.getRecommendedVRAM())));
      vm.setSetting(SettingIoFactory.get(new FirmwareSetting(Mappings.get(guestOs.getRecommendedFirmware()))));
      vm.setSetting(SettingIoFactory.get(new HpetSetting(guestOs.getRecommendedHPET())));
      vm.setSetting(SettingIoFactory.get(new HwVirtExExclSetting(guestOs.getRecommendedVirtEx())));
      
      if (guestOs.getRecommendedFloppy()) {
         Device dev = new Device();
         dev.setTypeId(Entity.StorageController.getId());
         dev.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerAttribute.Type, StorageControllerType.Floppy.getId())));
         vm.addDevice(dev);
      }
      
      Device dvdStorCtrl = new Device(Entity.DvdDrive.getId());
      dvdStorCtrl.setTypeId(Entity.DvdDrive.getId());
      dvdStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerAttribute.Type, guestOs.getRecommendedDVDStorageBus().toString())));
      dvdStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerAttribute.SubType, guestOs.getRecommendedDVDStorageController().toString())));
      vm.addDevice(dvdStorCtrl);
      
      Device hddStorCtrl = new Device(Entity.DiskDrive.getId());
      hddStorCtrl.setTypeId(Entity.DiskDrive.getId());
      hddStorCtrl.setSetting(SettingIoFactory.get(new MediumSizeSetting(guestOs.getRecommendedHDD())));
      hddStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerAttribute.Type, guestOs.getRecommendedHDStorageBus().toString())));
      hddStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerAttribute.SubType, guestOs.getRecommendedHDStorageController().toString())));
      vm.addDevice(hddStorCtrl);
      
      Device networkDevice = new Device();
      networkDevice.setTypeId(Entity.Network.getId());
      networkDevice.setSetting(SettingIoFactory.get(new NicAdapterTypeSetting(guestOs.getAdapterType().toString())));
      
      return vm;
   }
   
}
