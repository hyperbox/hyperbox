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

import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.MediumIn;
import org.altherian.hbox.comm.in.NetworkAttachModeIn;
import org.altherian.hbox.comm.in.SessionIn;
import org.altherian.hbox.comm.in.SnapshotIn;
import org.altherian.hbox.comm.in.StorageControllerIn;
import org.altherian.hbox.comm.in.StorageControllerSubTypeIn;
import org.altherian.hbox.comm.in.StorageControllerTypeIn;
import org.altherian.hbox.comm.in.StoreIn;
import org.altherian.hbox.comm.in.StoreItemIn;
import org.altherian.hbox.comm.in.TaskIn;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.comm.out.ModuleOut;
import org.altherian.hbox.comm.out.SessionOut;
import org.altherian.hbox.comm.out.StoreItemOut;
import org.altherian.hbox.comm.out.StoreOut;
import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hbox.comm.out.host.HostOut;
import org.altherian.hbox.comm.out.hypervisor.HypervisorLoaderOut;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.comm.out.hypervisor.OsTypeOut;
import org.altherian.hbox.comm.out.hypervisor.ScreenshotOut;
import org.altherian.hbox.comm.out.hypervisor.SnapshotOut;
import org.altherian.hbox.comm.out.network.NetworkAttachModeOut;
import org.altherian.hbox.comm.out.network.NetworkAttachNameOut;
import org.altherian.hbox.comm.out.network.NetworkInterfaceOut;
import org.altherian.hbox.comm.out.network.NetworkInterfaceTypeOut;
import org.altherian.hbox.comm.out.security.PermissionOut;
import org.altherian.hbox.comm.out.security.UserOut;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hbox.comm.out.storage.StorageControllerSubTypeOut;
import org.altherian.hbox.comm.out.storage.StorageControllerTypeOut;
import org.altherian.hbox.comm.out.storage.StorageDeviceAttachmentOut;
import org.altherian.hboxc.server.task._Task;

import java.util.List;
import java.util.Set;

public interface _ServerReader {
   
   public String getId();
   
   public String getName();
   
   public String getType();
   
   public String getVersion();
   
   public String getProtocolVersion();
   
   public String getLogLevel();
   
   public _GuestReader getGuest(String vmId);
   
   public MachineOut getMachine(MachineIn mIn);
   
   public MachineOut getMachine(String vmId);
   
   public _Machine getMachineReader(String id);
   
   public MediumOut getMedium(MediumIn mIn);
   
   public SessionOut getSession(SessionIn sIn);
   
   public StorageControllerSubTypeOut getStorageControllerSubType(StorageControllerSubTypeIn scstIn);
   
   public StorageControllerTypeOut getStorageControllerType(StorageControllerTypeIn sctIn);
   
   public StoreOut getStore(StoreIn sIn);
   
   public StoreItemOut getStoreItem(StoreItemIn siIn);
   
   public TaskOut getTask(TaskIn tIn);
   
   public UserOut getUser(UserIn uIn);
   
   public List<StorageDeviceAttachmentOut> listAttachments(StorageControllerIn scIn);
   
   public List<StorageDeviceAttachmentOut> listAttachments(String machineUuid);
   
   public List<String> listKeyboardMode(MachineIn mIn);
   
   public List<MachineOut> listMachines();
   
   public List<MediumOut> listMediums();
   
   public List<NetworkInterfaceOut> listNetworkInterfaces(MachineIn mIn);
   
   public List<NetworkAttachModeOut> listNetworkAttachModes();
   
   public List<NetworkAttachNameOut> listNetworkAttachNames(NetworkAttachModeIn namIn);
   
   public List<NetworkInterfaceTypeOut> listNetworkInterfaceTypes();
   
   public List<String> listMouseMode(MachineIn mIn);
   
   public List<OsTypeOut> listOsType();
   
   public List<OsTypeOut> listOsType(MachineIn mIn);
   
   public List<SessionOut> listSessions();
   
   public List<StorageControllerSubTypeOut> listStorageControllerSubType(StorageControllerTypeIn sctIn);
   
   public List<StorageControllerTypeOut> listStorageControllerType();
   
   public List<StoreItemOut> listStoreItems(StoreIn sIn);
   
   public List<StoreItemOut> listStoreItems(StoreIn sIn, StoreItemIn siIn);
   
   public List<StoreOut> listStores();
   
   public List<TaskOut> listTasks();
   
   public List<UserOut> listUsers();
   
   public List<PermissionOut> listPermissions(UserIn usrIn);
   
   public SnapshotOut getRootSnapshot(String vmId);
   
   public SnapshotOut getSnapshot(String vmId, String snapUuid);
   
   public SnapshotOut getSnapshot(MachineIn mIn, SnapshotIn snapIn);
   
   public SnapshotOut getCurrentSnapshot(String vmId);
   
   public SnapshotOut getCurrentSnapshot(MachineIn mIn);
   
   public boolean isHypervisorConnected();
   
   public _HypervisorReader getHypervisor();
   
   public ScreenshotOut getScreenshot(MachineIn mIn);
   
   public List<HypervisorLoaderOut> listHypervisors();
   
   public HostOut getHost();
   
   public List<ModuleOut> listModules();
   
   public ModuleOut getModule(String modId);
   
   public Set<String> listLogLevel();
   
   public _Task getTask(String id);

}
