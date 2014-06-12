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

package org.altherian.hboxc.server;

import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.MediumInput;
import org.altherian.hbox.comm.input.NetworkAttachModeInput;
import org.altherian.hbox.comm.input.SessionInput;
import org.altherian.hbox.comm.input.SnapshotInput;
import org.altherian.hbox.comm.input.StorageControllerInput;
import org.altherian.hbox.comm.input.StorageControllerSubTypeInput;
import org.altherian.hbox.comm.input.StorageControllerTypeInput;
import org.altherian.hbox.comm.input.StoreInput;
import org.altherian.hbox.comm.input.StoreItemInput;
import org.altherian.hbox.comm.input.TaskInput;
import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hbox.comm.output.SessionOutput;
import org.altherian.hbox.comm.output.StoreItemOutput;
import org.altherian.hbox.comm.output.StoreOutput;
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hbox.comm.output.host.HostOutput;
import org.altherian.hbox.comm.output.hypervisor.HypervisorLoaderOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.OsTypeOutput;
import org.altherian.hbox.comm.output.hypervisor.ScreenshotOutput;
import org.altherian.hbox.comm.output.hypervisor.SnapshotOutput;
import org.altherian.hbox.comm.output.network.NetworkAttachModeOutput;
import org.altherian.hbox.comm.output.network.NetworkAttachNameOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceTypeOutput;
import org.altherian.hbox.comm.output.security.PermissionOutput;
import org.altherian.hbox.comm.output.security.UserOutput;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerSubTypeOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerTypeOutput;
import org.altherian.hbox.comm.output.storage.StorageDeviceAttachmentOutput;

import java.util.List;

public interface _ServerReader {
   
   public String getId();
   
   public String getName();
   
   public String getType();
   
   public String getVersion();
   
   public String getProtocolVersion();
   
   public _GuestReader getGuest(String vmId);
   
   public MachineOutput getMachine(MachineInput mIn);
   
   public MachineOutput getMachine(String vmId);
   
   public MediumOutput getMedium(MediumInput mIn);
   
   public SessionOutput getSession(SessionInput sIn);
   
   public StorageControllerSubTypeOutput getStorageControllerSubType(StorageControllerSubTypeInput scstIn);
   
   public StorageControllerTypeOutput getStorageControllerType(StorageControllerTypeInput sctIn);
   
   public StoreOutput getStore(StoreInput sIn);
   
   public StoreItemOutput getStoreItem(StoreItemInput siIn);
   
   public TaskOutput getTask(TaskInput tIn);
   
   public UserOutput getUser(UserInput uIn);
   
   public List<StorageDeviceAttachmentOutput> listAttachments(StorageControllerInput scIn);
   
   public List<StorageDeviceAttachmentOutput> listAttachments(String machineUuid);
   
   public List<String> listKeyboardMode(MachineInput mIn);
   
   public List<MachineOutput> listMachines();
   
   public List<MediumOutput> listMediums();
   
   public List<NetworkInterfaceOutput> listNetworkInterfaces(MachineInput mIn);
   
   public List<NetworkAttachModeOutput> listNetworkAttachModes();
   
   public List<NetworkAttachNameOutput> listNetworkAttachNames(NetworkAttachModeInput namIn);
   
   public List<NetworkInterfaceTypeOutput> listNetworkInterfaceTypes();
   
   public List<String> listMouseMode(MachineInput mIn);
   
   public List<OsTypeOutput> listOsType();
   
   public List<OsTypeOutput> listOsType(MachineInput mIn);
   
   public List<SessionOutput> listSessions();
   
   public List<StorageControllerSubTypeOutput> listStorageControllerSubType(StorageControllerTypeInput sctIn);
   
   public List<StorageControllerTypeOutput> listStorageControllerType();
   
   public List<StoreItemOutput> listStoreItems(StoreInput sIn);
   
   public List<StoreItemOutput> listStoreItems(StoreInput sIn, StoreItemInput siIn);
   
   public List<StoreOutput> listStores();
   
   public List<TaskOutput> listTasks();
   
   public List<UserOutput> listUsers();
   
   public List<PermissionOutput> listPermissions(UserInput usrIn);
   
   public SnapshotOutput getRootSnapshot(String vmId);
   
   public SnapshotOutput getSnapshot(String vmId, String snapUuid);
   
   public SnapshotOutput getSnapshot(MachineInput mIn, SnapshotInput snapIn);
   
   public SnapshotOutput getCurrentSnapshot(String vmId);
   
   public SnapshotOutput getCurrentSnapshot(MachineInput mIn);
   
   public boolean isHypervisorConnected();
   
   public _HypervisorReader getHypervisor();
   
   public ScreenshotOutput getScreenshot(MachineInput mIn);
   
   public List<HypervisorLoaderOutput> listHypervisors();
   
   public HostOutput getHost();
   
}
