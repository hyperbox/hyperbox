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

package org.altherian.hboxd.vbox4_2.ws.utils;

import org.altherian.hbox.constant.AudioController;
import org.altherian.hbox.constant.AudioDriver;
import org.altherian.hbox.constant.Firmwares;
import org.altherian.hbox.constant.KeyboardModes;
import org.altherian.hbox.constant.MouseModes;
import org.altherian.hbox.states.MachineSessionStates;
import org.altherian.hbox.states.MachineStates;
import org.altherian.tool.BiEnumMap;
import org.altherian.tool.logging.Logger;

import org.virtualbox_4_2.AudioControllerType;
import org.virtualbox_4_2.AudioDriverType;
import org.virtualbox_4_2.FirmwareType;
import org.virtualbox_4_2.KeyboardHIDType;
import org.virtualbox_4_2.MachineState;
import org.virtualbox_4_2.PointingHIDType;
import org.virtualbox_4_2.SessionState;

public class Mappings {
   
   private static BiEnumMap<MouseModes, PointingHIDType> mappingMouse;
   private static BiEnumMap<MachineStates, MachineState> mappingMachine;
   private static BiEnumMap<MachineSessionStates, SessionState> mappingMachineSession;
   private static BiEnumMap<KeyboardModes, KeyboardHIDType> mappingKeyboard;
   private static BiEnumMap<Firmwares, FirmwareType> mappingFirmware;
   private static BiEnumMap<AudioDriver, AudioDriverType> mappingAudioDriver;
   private static BiEnumMap<AudioController, AudioControllerType> mappingAudioController;
   
   public static void load() {
      Logger.track();
      
      Logger.debug("Loading Virtualbox Mappings");
      
      mappingMouse = new BiEnumMap<MouseModes, PointingHIDType>(MouseModes.class, PointingHIDType.class);
      mappingMachine = new BiEnumMap<MachineStates, MachineState>(MachineStates.class, MachineState.class);
      
      mappingMachineSession = new BiEnumMap<MachineSessionStates, SessionState>(MachineSessionStates.class, SessionState.class);
      mappingKeyboard = new BiEnumMap<KeyboardModes, KeyboardHIDType>(KeyboardModes.class, KeyboardHIDType.class);
      mappingFirmware = new BiEnumMap<Firmwares, FirmwareType>(Firmwares.class, FirmwareType.class);
      mappingAudioDriver = new BiEnumMap<AudioDriver, AudioDriverType>(AudioDriver.class, AudioDriverType.class);
      mappingAudioController = new BiEnumMap<AudioController, AudioControllerType>(AudioController.class, AudioControllerType.class);
      
      mappingMouse.put(MouseModes.None, PointingHIDType.None);
      mappingMouse.put(MouseModes.Ps2, PointingHIDType.PS2Mouse);
      mappingMouse.put(MouseModes.Usb, PointingHIDType.USBMouse);
      mappingMouse.put(MouseModes.UsbTablet, PointingHIDType.USBTablet);
      mappingMouse.put(MouseModes.ComboMouse, PointingHIDType.ComboMouse);
      
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
      
      mappingKeyboard.put(KeyboardModes.None, KeyboardHIDType.None);
      mappingKeyboard.put(KeyboardModes.Ps2, KeyboardHIDType.PS2Keyboard);
      mappingKeyboard.put(KeyboardModes.Usb, KeyboardHIDType.USBKeyboard);
      mappingKeyboard.put(KeyboardModes.Combo, KeyboardHIDType.ComboKeyboard);
      
      mappingFirmware.put(Firmwares.Bios, FirmwareType.BIOS);
      mappingFirmware.put(Firmwares.Efi, FirmwareType.EFI);
      mappingFirmware.put(Firmwares.Efi32, FirmwareType.EFI32);
      mappingFirmware.put(Firmwares.Efi64, FirmwareType.EFI64);
      mappingFirmware.put(Firmwares.EfiDual, FirmwareType.EFIDUAL);
      
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
   
   public static MouseModes get(PointingHIDType type) {
      return mappingMouse.getReverse(type);
   }
   
   public static PointingHIDType get(MouseModes type) {
      return mappingMouse.get(type);
   }
   
   public static MachineStates get(MachineState type) {
      if (mappingMachine.containsReverse(type)) {
         return mappingMachine.getReverse(type);
      } else {
         Logger.error("Unable to find a Machine State mapping for " + type);
         return MachineStates.UNKNOWN;
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
   
   public static KeyboardModes get(KeyboardHIDType type) {
      return mappingKeyboard.getReverse(type);
   }
   
   public static KeyboardHIDType get(KeyboardModes type) {
      return mappingKeyboard.get(type);
   }
   
   public static FirmwareType get(Firmwares firmware) {
      return mappingFirmware.get(firmware);
   }
   
   public static Firmwares get(FirmwareType firmware) {
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
