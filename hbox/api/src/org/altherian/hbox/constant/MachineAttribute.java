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
public enum MachineAttribute {
   
   ServerId(Entity.Machine),
   IsAccessible(Entity.Machine),
   
   // ////////////////////////////////////
   // Audio Settings
   // ////////////////////////////////////
   AudioController(Entity.Audio),
   AudioDriver(Entity.Audio),
   AudioEnable(Entity.Audio),
   
   // /////////////////////////////////////
   // CPU Settings
   // /////////////////////////////////////
   /**
    * Set the number of CPUs for the VM
    */
   CpuCount(Entity.CPU),
   /**
    * Set if CPUs should be hot-pluggable - Requires compatible OS
    */
   CpuHotPlug(Entity.CPU),
   /**
    * Set the CPUs execution cap - This value will set the cap for every virtual CPU attached to the guest and is in percentage of the Host CPU power
    */
   CpuExecCap(Entity.CPU),
   /**
    * Set if Virtualbox should show virtual CPU to the guest, or give the host's CPU information.
    */
   SyntheticCPU(Entity.CPU),
   
   // /////////////////////////////////////
   // General settings
   // /////////////////////////////////////
   /**
    * Machine name
    */
   Name(Entity.Machine),
   /**
    * OS Type of the machine<br/>
    */
   OsType(Entity.Machine),
   /**
    * Open description about the VM
    */
   Description(Entity.Machine),
   
   // /////////////////////////////////////
   // Keyboard Settings
   // /////////////////////////////////////
   
   KeyboardMode(Entity.Keyboard),
   
   // /////////////////////////////////////
   // Memory Settings
   // /////////////////////////////////////
   /**
    * RAM amount in Megabytes
    */
   Memory(Entity.Memory),
   /**
    * Is large page activated or not
    */
   LargePages(Entity.Memory),
   /**
    * Is page fusion activated or not
    */
   PageFusion(Entity.Memory),
   /**
    * Set Nested Paging support for the VM
    */
   NestedPaging(Entity.Memory),
   /**
    * Set VT-x support for the VM (only for Intel CPU)
    */
   Vtxvpid(Entity.Memory),
   /**
    * Ballon size in Mega Bytes
    */
   GuestMemoryBalloon(Entity.Memory),
   
   // /////////////////////////////////////
   // Motherboard settings
   // /////////////////////////////////////
   /**
    * Set the VM firmware (bios/efi)<br/>
    * Not currently implemented
    */
   // TODO implement
   Firmware(Entity.Motherboard),
   /**
    * Set the VM chipset<br/>
    * Not currently implemented
    */
   // TODO implement
   Chipset(Entity.Motherboard),
   /**
    * Set status of ACPI support for the VM
    */
   ACPI(Entity.Motherboard),
   /**
    * Set status of I/O APIC support for the VM
    */
   IoAPIC(Entity.Motherboard),
   /**
    * Change the hardware UUID (usually the same as the VM UUID)<br/>
    * Not currently implemented
    */
   // TODO implement
   HardwareUuid(Entity.Motherboard),
   
   // /////////////////////////////////////
   // Mouse
   // /////////////////////////////////////
   MouseMode(Entity.Mouse),
   
   // /////////////////////////////////////
   // USB settings
   // /////////////////////////////////////
   /**
    * Enable/Disable USB
    */
   UsbOhci(Entity.USB),
   /**
    * Enable/Disable USB 2.0<br/>
    * Not currently implemented
    */
   // TODO implement
   UsbEhci(Entity.USB),
   
   // /////////////////////////////////////
   // Video settings
   // /////////////////////////////////////
   /**
    * Number of monitors attached the Guest OS
    */
   MonitorCount(Entity.Display),
   /**
    * Amount of Video memory in Megabytes
    */
   VRAM(Entity.Display),
   /**
    * Enable 2D acceleration
    */
   Accelerate2dVideo(Entity.Display),
   /**
    * Enable 3D acceleration
    */
   Accelerate3d(Entity.Display),
   
   // /////////////////////////////////////
   // Virtualisation specific settings
   // /////////////////////////////////////
   /**
    * Set if PAE and NX capabilities of the host CPU will be exposed to the virtual machine<br/>
    * Not currently implemented
    */
   // TODO implement
   PAE(Entity.Machine),
   /**
    * Set if High Precision Event Timer should be turned on or off<br/>
    * Not currently implemented
    */
   // TODO implement
   HPET(Entity.Machine),
   /**
    * If Intel VT-x / AMD-V should be used
    */
   HwVirtEx(Entity.Machine),
   /**
    * Set if there should be an exclusive use of VT-x/AMD-V
    */
   HwVirtExExcl(Entity.Machine),
   
   // /////////////////////////////////////
   // Snapshot infos
   // /////////////////////////////////////
   RootSnapshotUuid(Entity.Machine),
   CurrentSnapshotUuid(Entity.Machine),
   HasSnapshot(Entity.Machine),
   
   /*
    * VRDE settings
    */
   VrdeAuthType(Entity.Console),
   VrdeAuthLibrary(Entity.Console),
   VrdeAuthTimeout(Entity.Console),
   VrdeEnabled(Entity.Console),
   VrdeMultiConnection(Entity.Console),
   VrdeAddress(Entity.Console),
   VrdePort(Entity.Console),
   VrdeModule(Entity.Console);
   
   private Entity dt;
   
   private MachineAttribute(Entity type) {
      dt = type;
   }
   
   public Entity getDeviceType() {
      return dt;
   }
   
   public String getId() {
      return toString();
   }
   
}
