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

package org.altherian.hbox.constant;

/**
 * This class holds the setting IDs used for the VM. This list has all the Virtualbox core settings for VM.<br/>
 * Any extra custom setting can be set using customs IDs, but these settings will require a custom handler.<br/>
 * To use these with the settings classes into the vbox.constant package of the Hyperbox API
 *
 * @author noteirak
 */
// TODO should be separated into devices
public enum MachineAttribute {
   
   ServerId(EntityType.Machine),
   IsAccessible(EntityType.Machine),
   Location(EntityType.Machine),
   
   // ////////////////////////////////////
   // Audio Settings
   // ////////////////////////////////////
   AudioController(EntityType.Audio),
   AudioDriver(EntityType.Audio),
   AudioEnable(EntityType.Audio),
   
   // /////////////////////////////////////
   // CPU Settings
   // /////////////////////////////////////
   /**
    * Set the number of CPUs for the VM
    */
   CpuCount(EntityType.CPU),
   /**
    * Set if CPUs should be hot plug - Requires compatible OS
    */
   CpuHotPlug(EntityType.CPU),
   /**
    * Set the CPUs execution cap - This value will set the cap for every virtual CPU attached to the guest and is in percentage of the Host CPU power
    */
   CpuExecCap(EntityType.CPU),
   /**
    * Set if Virtualbox should show virtual CPU to the guest, or give the host's CPU information.
    */
   SyntheticCPU(EntityType.CPU),
   
   // /////////////////////////////////////
   // General settings
   // /////////////////////////////////////
   /**
    * Machine name
    */
   Name(EntityType.Machine),
   /**
    * OS Type of the machine<br/>
    */
   OsType(EntityType.Machine),
   /**
    * Open description about the VM
    */
   Description(EntityType.Machine),
   
   // /////////////////////////////////////
   // Keyboard Settings
   // /////////////////////////////////////
   
   KeyboardMode(EntityType.Keyboard),
   
   // /////////////////////////////////////
   // Memory Settings
   // /////////////////////////////////////
   /**
    * RAM amount in Megabytes
    */
   Memory(EntityType.Memory),
   /**
    * Is large page activated or not
    */
   LargePages(EntityType.Memory),
   /**
    * Is page fusion activated or not
    */
   PageFusion(EntityType.Memory),
   /**
    * Set Nested Paging support for the VM
    */
   NestedPaging(EntityType.Memory),
   /**
    * Set VT-x support for the VM (only for Intel CPU)
    */
   Vtxvpid(EntityType.Memory),
   /**
    * Ballon size in Mega Bytes
    */
   GuestMemoryBalloon(EntityType.Memory),
   
   // /////////////////////////////////////
   // Motherboard settings
   // /////////////////////////////////////
   /**
    * Set the VM firmware (bios/efi)<br/>
    * Not currently implemented
    */
   // TODO implement
   Firmware(EntityType.Motherboard),
   /**
    * Set the VM chipset<br/>
    * Not currently implemented
    */
   // TODO implement
   Chipset(EntityType.Motherboard),
   /**
    * Set status of ACPI support for the VM
    */
   ACPI(EntityType.Motherboard),
   /**
    * Set status of I/O APIC support for the VM
    */
   IoAPIC(EntityType.Motherboard),
   /**
    * Change the hardware UUID (usually the same as the VM UUID)<br/>
    * Not currently implemented
    */
   // TODO implement
   HardwareUuid(EntityType.Motherboard),
   
   // /////////////////////////////////////
   // Mouse
   // /////////////////////////////////////
   MouseMode(EntityType.Mouse),
   
   // /////////////////////////////////////
   // USB settings
   // /////////////////////////////////////
   /**
    * Enable/Disable USB
    */
   UsbOhci(EntityType.USB),
   /**
    * Enable/Disable USB 2.0<br/>
    * Not currently implemented
    */
   // TODO implement
   UsbEhci(EntityType.USB),
   
   // /////////////////////////////////////
   // Video settings
   // /////////////////////////////////////
   /**
    * Number of monitors attached the Guest OS
    */
   MonitorCount(EntityType.Display),
   /**
    * Amount of Video memory in Megabytes
    */
   VRAM(EntityType.Display),
   /**
    * Enable 2D acceleration
    */
   Accelerate2dVideo(EntityType.Display),
   /**
    * Enable 3D acceleration
    */
   Accelerate3d(EntityType.Display),
   
   // /////////////////////////////////////
   // Virtualisation specific settings
   // /////////////////////////////////////
   /**
    * Set if PAE and NX capabilities of the host CPU will be exposed to the virtual machine<br/>
    * Not currently implemented
    */
   // TODO implement
   PAE(EntityType.Machine),
   /**
    * Set if High Precision Event Timer should be turned on or off<br/>
    * Not currently implemented
    */
   // TODO implement
   HPET(EntityType.Machine),
   /**
    * If Intel VT-x / AMD-V should be used
    */
   HwVirtEx(EntityType.Machine),
   /**
    * Set if there should be an exclusive use of VT-x/AMD-V
    */
   HwVirtExExcl(EntityType.Machine),
   
   // /////////////////////////////////////
   // Snapshot infos
   // /////////////////////////////////////
   RootSnapshotUuid(EntityType.Machine),
   CurrentSnapshotUuid(EntityType.Machine),
   HasSnapshot(EntityType.Machine),
   
   /*
    * VRDE settings
    */
   VrdeAuthType(EntityType.Console),
   VrdeAuthLibrary(EntityType.Console),
   VrdeAuthTimeout(EntityType.Console),
   VrdeEnabled(EntityType.Console),
   VrdeMultiConnection(EntityType.Console),
   VrdeAddress(EntityType.Console),
   VrdePort(EntityType.Console),
   VrdeModule(EntityType.Console);
   
   private EntityType dt;
   
   private MachineAttribute(EntityType type) {
      dt = type;
   }
   
   public EntityType getDeviceType() {
      return dt;
   }
   
   public String getId() {
      return toString();
   }
   
}
