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

package org.altherian.vbox4_3.data;

import org.altherian.hbox.constant.AudioController;
import org.altherian.hbox.constant.AudioDriver;
import org.altherian.hbox.constant.Firmware;
import org.altherian.hbox.constant.KeyboardMode;
import org.altherian.hbox.constant.MouseMode;
import org.altherian.hbox.states.MachineSessionStates;
import org.altherian.hbox.states.MachineStates;
import org.altherian.tool.BiEnumMap;
import org.altherian.tool.logging.Logger;

import org.virtualbox_4_3.AudioControllerType;
import org.virtualbox_4_3.AudioDriverType;
import org.virtualbox_4_3.FirmwareType;
import org.virtualbox_4_3.KeyboardHIDType;
import org.virtualbox_4_3.MachineState;
import org.virtualbox_4_3.PointingHIDType;
import org.virtualbox_4_3.SessionState;

public class Mappings {
   
   private static BiEnumMap<MouseMode, PointingHIDType> mappingMouse;
   private static BiEnumMap<MachineStates, MachineState> mappingMachine;
   private static BiEnumMap<MachineSessionStates, SessionState> mappingMachineSession;
   private static BiEnumMap<KeyboardMode, KeyboardHIDType> mappingKeyboard;
   private static BiEnumMap<Firmware, FirmwareType> mappingFirmware;
   private static BiEnumMap<AudioDriver, AudioDriverType> mappingAudioDriver;
   private static BiEnumMap<AudioController, AudioControllerType> mappingAudioController;
   
   public static void load() {
      Logger.track();
      
      Logger.debug("Loading Virtualbox Mappings");
      
      mappingMouse = new BiEnumMap<MouseMode, PointingHIDType>(MouseMode.class, PointingHIDType.class);
      mappingMachine = new BiEnumMap<MachineStates, MachineState>(MachineStates.class, MachineState.class);
      
      mappingMachineSession = new BiEnumMap<MachineSessionStates, SessionState>(MachineSessionStates.class, SessionState.class);
      mappingKeyboard = new BiEnumMap<KeyboardMode, KeyboardHIDType>(KeyboardMode.class, KeyboardHIDType.class);
      mappingFirmware = new BiEnumMap<Firmware, FirmwareType>(Firmware.class, FirmwareType.class);
      mappingAudioDriver = new BiEnumMap<AudioDriver, AudioDriverType>(AudioDriver.class, AudioDriverType.class);
      mappingAudioController = new BiEnumMap<AudioController, AudioControllerType>(AudioController.class, AudioControllerType.class);
      
      mappingMouse.put(MouseMode.None, PointingHIDType.None);
      mappingMouse.put(MouseMode.Ps2, PointingHIDType.PS2Mouse);
      mappingMouse.put(MouseMode.Usb, PointingHIDType.USBMouse);
      mappingMouse.put(MouseMode.UsbTablet, PointingHIDType.USBTablet);
      mappingMouse.put(MouseMode.ComboMouse, PointingHIDType.ComboMouse);
      
      mappingMachine.put(MachineStates.PoweredOff, MachineState.PoweredOff);
      mappingMachine.put(MachineStates.Saved, MachineState.Saved);
      mappingMachine.put(MachineStates.Teleported, MachineState.Teleported);
      mappingMachine.put(MachineStates.Aborted, MachineState.Aborted);
      mappingMachine.put(MachineStates.Running, MachineState.Running);
      mappingMachine.put(MachineStates.Paused, MachineState.Paused);
      mappingMachine.put(MachineStates.Stuck, MachineState.Stuck);
      
      mappingMachine.put(MachineStates.Starting, MachineState.Starting);
      mappingMachine.put(MachineStates.Stopping, MachineState.Stopping);
      mappingMachine.put(MachineStates.Saving, MachineState.Saving);
      mappingMachine.put(MachineStates.Restoring, MachineState.Restoring);
      mappingMachine.put(MachineStates.Teleporting, MachineState.Teleporting);
      mappingMachine.put(MachineStates.TeleportingPaused, MachineState.TeleportingPausedVM);
      mappingMachine.put(MachineStates.TeleportingIn, MachineState.TeleportingIn);
      mappingMachine.put(MachineStates.FaultTolerantSyncing, MachineState.FaultTolerantSyncing);
      mappingMachine.put(MachineStates.SnapshotLive, MachineState.LiveSnapshotting);
      mappingMachine.put(MachineStates.SnapshotDeletingOnline, MachineState.DeletingSnapshotOnline);
      mappingMachine.put(MachineStates.SnapshotDeletingPaused, MachineState.DeletingSnapshotPaused);
      mappingMachine.put(MachineStates.SnapshotDeletingOff, MachineState.DeletingSnapshot);
      mappingMachine.put(MachineStates.SnapshotRestoring, MachineState.RestoringSnapshot);
      mappingMachine.put(MachineStates.SettingUp, MachineState.SettingUp);
      
      mappingMachineSession.put(MachineSessionStates.Locking, SessionState.Spawning);
      mappingMachineSession.put(MachineSessionStates.Locked, SessionState.Locked);
      mappingMachineSession.put(MachineSessionStates.Unlocking, SessionState.Unlocking);
      mappingMachineSession.put(MachineSessionStates.Unlocked, SessionState.Unlocked);
      
      mappingKeyboard.put(KeyboardMode.None, KeyboardHIDType.None);
      mappingKeyboard.put(KeyboardMode.Ps2, KeyboardHIDType.PS2Keyboard);
      mappingKeyboard.put(KeyboardMode.Usb, KeyboardHIDType.USBKeyboard);
      mappingKeyboard.put(KeyboardMode.Combo, KeyboardHIDType.ComboKeyboard);
      
      mappingFirmware.put(Firmware.Bios, FirmwareType.BIOS);
      mappingFirmware.put(Firmware.Efi, FirmwareType.EFI);
      mappingFirmware.put(Firmware.Efi32, FirmwareType.EFI32);
      mappingFirmware.put(Firmware.Efi64, FirmwareType.EFI64);
      mappingFirmware.put(Firmware.EfiDual, FirmwareType.EFIDUAL);
      
      mappingAudioDriver.put(AudioDriver.ALSA, AudioDriverType.ALSA);
      mappingAudioDriver.put(AudioDriver.CoreAudio, AudioDriverType.CoreAudio);
      mappingAudioDriver.put(AudioDriver.DirectSound, AudioDriverType.DirectSound);
      mappingAudioDriver.put(AudioDriver.Null, AudioDriverType.Null);
      mappingAudioDriver.put(AudioDriver.OSS, AudioDriverType.OSS);
      mappingAudioDriver.put(AudioDriver.Pulse, AudioDriverType.Pulse);
      mappingAudioDriver.put(AudioDriver.SolAudio, AudioDriverType.SolAudio);
      mappingAudioDriver.put(AudioDriver.WinMM, AudioDriverType.WinMM);
      
      mappingAudioController.put(AudioController.AC97, AudioControllerType.AC97);
      mappingAudioController.put(AudioController.HDA, AudioControllerType.HDA);
      mappingAudioController.put(AudioController.SB16, AudioControllerType.SB16);
   }
   
   public static MouseMode get(PointingHIDType type) {
      return mappingMouse.getReverse(type);
   }
   
   public static PointingHIDType get(MouseMode type) {
      return mappingMouse.get(type);
   }
   
   public static MachineStates get(MachineState type) {
      if (mappingMachine.containsReverse(type)) {
         return mappingMachine.getReverse(type);
      } else {
         Logger.error("Unable to find a Machine State mapping for " + type);
         return MachineStates.Unknown;
      }
   }
   
   public static MachineState get(MachineStates type) {
      return mappingMachine.get(type);
   }
   
   public static MachineSessionStates get(SessionState type) {
      return mappingMachineSession.getReverse(type);
   }
   
   public static SessionState get(MachineSessionStates type) {
      return mappingMachineSession.get(type);
   }
   
   public static KeyboardMode get(KeyboardHIDType type) {
      return mappingKeyboard.getReverse(type);
   }
   
   public static KeyboardHIDType get(KeyboardMode type) {
      return mappingKeyboard.get(type);
   }
   
   public static FirmwareType get(Firmware firmware) {
      return mappingFirmware.get(firmware);
   }
   
   public static Firmware get(FirmwareType firmware) {
      return mappingFirmware.getReverse(firmware);
   }
   
   public static AudioDriverType get(AudioDriver type) {
      return mappingAudioDriver.get(type);
   }
   
   public static AudioDriver get(AudioDriverType type) {
      return mappingAudioDriver.getReverse(type);
   }
   
   public static AudioControllerType get(AudioController type) {
      return mappingAudioController.get(type);
   }
   
   public static AudioController get(AudioControllerType type) {
      return mappingAudioController.getReverse(type);
   }
   
}
