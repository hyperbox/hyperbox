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

package org.altherian.hbox.constant;

/**
 * This class holds the setting IDs used for the VM. This list has all the Virtualbox core settings for VM.<br/>
 * Any extra custom setting can be set using customs IDs, but these settings will require a custom handler.<br/>
 * To use these with the settings classes into the vbox.constant package of the Hyperbox API
 * 
 * @author noteirak
 */
// TODO should be separated into devices
public enum MachineAttributes {
   
   ServerId(EntityTypes.Server),

   // ////////////////////////////////////
   // Audio Settings
   // ////////////////////////////////////
   AudioController(EntityTypes.Audio),
   AudioDriver(EntityTypes.Audio),
   AudioEnable(EntityTypes.Audio),
   
   // /////////////////////////////////////
   // CPU Settings
   // /////////////////////////////////////
   /**
    * Set the number of CPUs for the VM
    */
   CpuCount(EntityTypes.CPU),
   /**
    * Set if CPUs should be hot-pluggable - Requires compatible OS
    */
   CpuHotPlug(EntityTypes.CPU),
   /**
    * Set the CPUs execution cap - This value will set the cap for every virtual CPU attached to the guest and is in percentage of the Host CPU power
    */
   CpuExecCap(EntityTypes.CPU),
   /**
    * Set if Virtualbox should show virtual CPU to the guest, or give the host's CPU information.
    */
   SyntheticCPU(EntityTypes.CPU),
   
   // /////////////////////////////////////
   // General settings
   // /////////////////////////////////////
   /**
    * Machine name
    */
   Name(EntityTypes.Machine),
   /**
    * OS Type of the machine<br/>
    */
   OsType(EntityTypes.Machine),
   /**
    * Open description about the VM
    */
   Description(EntityTypes.Machine),
   
   // /////////////////////////////////////
   // Keyboard Settings
   // /////////////////////////////////////
   
   KeyboardMode(EntityTypes.Keyboard),
   
   // /////////////////////////////////////
   // Memory Settings
   // /////////////////////////////////////
   /**
    * RAM amount in Megabytes
    */
   Memory(EntityTypes.Memory),
   /**
    * Is large page activated or not
    */
   LargePages(EntityTypes.Memory),
   /**
    * Is page fusion activated or not
    */
   PageFusion(EntityTypes.Memory),
   /**
    * Set Nested Paging support for the VM
    */
   NestedPaging(EntityTypes.Memory),
   /**
    * Set VT-x support for the VM (only for Intel CPU)
    */
   Vtxvpid(EntityTypes.Memory),
   /**
    * Ballon size in Mega Bytes
    */
   GuestMemoryBalloon(EntityTypes.Memory),
   
   // /////////////////////////////////////
   // Motherboard settings
   // /////////////////////////////////////
   /**
    * Set the VM firmware (bios/efi)<br/>
    * Not currently implemented
    */
   // TODO implement
   Firmware(EntityTypes.Motherboard),
   /**
    * Set the VM chipset<br/>
    * Not currently implemented
    */
   // TODO implement
   Chipset(EntityTypes.Motherboard),
   /**
    * Set status of ACPI support for the VM
    */
   ACPI(EntityTypes.Motherboard),
   /**
    * Set status of I/O APIC support for the VM
    */
   IoAPIC(EntityTypes.Motherboard),
   /**
    * Change the hardware UUID (usually the same as the VM UUID)<br/>
    * Not currently implemented
    */
   // TODO implement
   HardwareUuid(EntityTypes.Motherboard),
   
   // /////////////////////////////////////
   // Mouse
   // /////////////////////////////////////
   MouseMode(EntityTypes.Mouse),
   
   // /////////////////////////////////////
   // USB settings
   // /////////////////////////////////////
   /**
    * Enable/Disable USB
    */
   UsbOhci(EntityTypes.USB),
   /**
    * Enable/Disable USB 2.0<br/>
    * Not currently implemented
    */
   // TODO implement
   UsbEhci(EntityTypes.USB),
   
   // /////////////////////////////////////
   // Video settings
   // /////////////////////////////////////
   /**
    * Number of monitors attached the Guest OS
    */
   MonitorCount(EntityTypes.Display),
   /**
    * Amount of Video memory in Megabytes
    */
   VRAM(EntityTypes.Display),
   /**
    * Enable 2D acceleration
    */
   Accelerate2dVideo(EntityTypes.Display),
   /**
    * Enable 3D acceleration
    */
   Accelerate3d(EntityTypes.Display),
   
   // /////////////////////////////////////
   // Virtualisation specific settings
   // /////////////////////////////////////
   /**
    * Set if PAE and NX capabilities of the host CPU will be exposed to the virtual machine<br/>
    * Not currently implemented
    */
   // TODO implement
   PAE(EntityTypes.Machine),
   /**
    * Set if High Precision Event Timer should be turned on or off<br/>
    * Not currently implemented
    */
   // TODO implement
   HPET(EntityTypes.Machine),
   /**
    * If Intel VT-x / AMD-V should be used
    */
   HwVirtEx(EntityTypes.Machine),
   /**
    * Set if there should be an exclusive use of VT-x/AMD-V
    */
   HwVirtExExcl(EntityTypes.Machine),
   
   // /////////////////////////////////////
   // Snapshot infos
   // /////////////////////////////////////
   RootSnapshotUuid(EntityTypes.Machine),
   CurrentSnapshotUuid(EntityTypes.Machine),
   HasSnapshot(EntityTypes.Machine),
   
   /*
    * VRDE settings
    */
   VrdeAuthType(EntityTypes.Console),
   VrdeAuthLibrary(EntityTypes.Console),
   VrdeAuthTimeout(EntityTypes.Console),
   VrdeEnabled(EntityTypes.Console),
   VrdeMultiConnection(EntityTypes.Console),
   VrdeAddress(EntityTypes.Console),
   VrdePort(EntityTypes.Console),
   VrdeModule(EntityTypes.Console);
   
   private EntityTypes dt;
   
   private MachineAttributes(EntityTypes type) {
      dt = type;
   }
   
   public EntityTypes getDeviceType() {
      return dt;
   }
   
   public String getId() {
      return toString();
   }
   
}
