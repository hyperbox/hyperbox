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

package org.altherian.hbox.comm;

import org.altherian.hbox.comm.input.GuestNetworkInterfaceInput;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.NetworkInterfaceInput;
import org.altherian.hbox.comm.input.StorageControllerInput;
import org.altherian.hbox.comm.input.StoreItemInput;
import org.altherian.hbox.comm.output.hypervisor.GuestNetworkInterfaceOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerOutput;

// TODO document required data (javadoc-like) and returned data
/**
 * Core tasks that can be performed on the Hyperbox server.<br/>
 * These core tasks are the Virtualbox Tasks.
 * 
 * @author noteirak
 */
public enum HypervisorTasks {
   
   /**
    * Get the list of registered VMs
    * <p>
    * Request Object : None<br/>
    * Answer Object : {@link MachineOutput}<br/>
    * Answer Type : Multi
    * </p>
    */
   MachineList,
   
   /**
    * Get the details of a VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : {@link MachineOutput}<br/>
    * Answer Type : Single
    * </p>
    */
   MachineGet,
   
   /**
    * Request PowerOn of a PoweredOff VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : None<br/>
    * Answer Type : Single
    * </p>
    */
   MachinePowerOn,
   
   /**
    * Request PowerOff on a PoweredOn or Paused VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : None<br/>
    * Answer Type : Single
    * </p>
    */
   MachinePowerOff,
   
   /**
    * Request Pause of a Running VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : None<br/>
    * Answer Type : Single
    * </p>
    */
   MachinePause,
   
   /**
    * Request Resume of a paused VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : None<br/>
    * Answer Type : Single
    * </p>
    */
   MachineResume,
   
   /**
    * Request to save the state of a Running VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : None<br/>
    * Answer Type : Single
    * </p>
    */
   MachineSaveState,
   
   /**
    * Request to reset a PoweredOn VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : None<br/>
    * Answer Type : Single
    * </p>
    */
   MachineReset,
   
   /**
    * Request to send a Sleep Button ACPI event (Press Sleep Button) on a Running VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : None<br/>
    * Answer Type : Single
    * </p>
    */
   MachineAcpiSleepButton,
   
   /**
    * Request to send a Power Button ACPI event (Press Sleep Button) on a Running VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : None<br/>
    * Answer Type : Single
    * </p>
    */
   MachineAcpiPowerButton,
   
   /**
    * Request to modify settings of a VM
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : None<br/>
    * Answer Type : Single
    * </p>
    */
   MachineModify,
   
   /**
    * Create a blank new VM, without pre-configured settings - Name & OsType must be provided.<br/>
    * This will Answer with a MachineIO.<br/>
    * If a VM with specific settings needs to be created, {@link #MachineCreate} then {@link #MachineModify} needs to be called.
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : {@link MachineOutput}<br/>
    * Answer Type : Single
    * </p>
    */
   MachineCreate,
   
   /**
    * Register a VM given its machine description file
    * <p>
    * Request Object : {@link StoreItemInput}<br/>
    * Answer Object : {@link MachineOutput}<br/>
    * Answer Type : Single
    * </p>
    */
   MachineRegister,
   
   /**
    * Unregister the VM, without deleting any file
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : {@link MachineOutput}<br/>
    * Answer Type : Single
    * </p>
    */
   MachineUnregister,
   
   /**
    * Unregister and delete all the VM files
    * <p>
    * Request Object : {@link MachineInput}<br/>
    * Answer Object : {@link MachineOutput}<br/>
    * Answer Type : Single
    * </p>
    */
   MachineDelete,
   
   MachineDisplayGetScreenshot,
   
   NetworkInterfaceList,
   
   /**
    * Get the details of a Network Interface
    * <p>
    * Request Object : {@link NetworkInterfaceInput}<br/>
    * Answer Object : {@link NetworkInterfaceOutput}<br/>
    * Answer Type : Single
    * </p>
    */
   NetworkInterfaceGetInfo,
   NetworkInterfaceModify,
   
   NetworkAttachModeList,
   NetworkAttachNameList,
   NetworkAdapterTypeList,
   
   StorageControllerList,
   
   /**
    * Get the details of a Storage Controller
    * <p>
    * Request Object : {@link StorageControllerInput}<br/>
    * Answer Object : {@link StorageControllerOutput}<br/>
    * Answer Type : Single
    * </p>
    */
   StorageControllerGet,
   StorageControllerAdd,
   StorageControllerModify,
   StorageControllerRemove,
   
   StorageControllerMediumAttachmentList,
   StorageControllerMediumAttachmentGet,
   StorageControllerMediumAttachmentAdd,
   StorageControllerMediumAttachmentRemove,
   
   StorageControllerTypeList,
   StorageControllerTypeGet,
   StorageControllerSubTypeList,
   StorageControllerSubTypeGet,
   
   GuestGet,
   GuestNetworkInterfaceList,
   GuestNetworkInterfaceGet,
   /**
    * <p>
    * Request Objects : {@link MachineInput}, {@link GuestNetworkInterfaceInput}<br/>
    * Answer Object : {@link GuestNetworkInterfaceOutput}<br/>
    * Answer Type : Single
    * </p>
    */
   GuestNetworkInterfaceFind,
   
   MediumList,
   MediumGet,
   MediumCreate,
   MediumModify,
   MediumDelete,
   MediumRegister,
   MediumUnregister,
   
   /**
    * Mount a medium to a storage attachment
    * <p>
    * Request Objects: MachineInput, StorageDeviceAttachmentInput, MediumInput<br/>
    * Answer Object : none<br/>
    * Answer Type : Single
    * </p>
    */
   MediumMount,
   MediumUnmount,
   
   OsTypeList,
   OsTypeGetMachineSettings,
   
   SnapshotGet,
   SnapshotGetCurrent,
   SnapshotGetRoot,
   SnapshotModify,
   SnapshotTake,
   SnapshotRestore,
   SnapshotDelete,
   
   KeyboardModeList,
   MouseModeList,
   
   /**
    * <p>
    * Request Object: none<br/>
    * Answer Object : MediumOutput<br/>
    * Anwer Type : Single
    * </p>
    */
   ToolsMediumGet;
   
   public String getId() {
      return toString();
   }
   
}
