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

package org.altherian.hboxc.core.server;

import net.engio.mbassy.listener.Handler;
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
import org.altherian.hbox.comm.out.event.machine.MachineDataChangeEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineRegistrationEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineSnapshotDataChangedEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineStateEventOut;
import org.altherian.hbox.comm.out.event.module.ModuleEventOut;
import org.altherian.hbox.comm.out.event.snapshot.SnapshotDeletedEventOut;
import org.altherian.hbox.comm.out.event.snapshot.SnapshotModifiedEventOut;
import org.altherian.hbox.comm.out.event.snapshot.SnapshotRestoredEventOut;
import org.altherian.hbox.comm.out.event.snapshot.SnapshotTakenEventOut;
import org.altherian.hbox.comm.out.event.task.TaskQueueEventOut;
import org.altherian.hbox.comm.out.event.task.TaskStateEventOut;
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
import org.altherian.hbox.event.HyperboxEvents;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.states.TaskQueueEvents;
import org.altherian.hboxc.event.EventManager;
import org.altherian.hboxc.event.machine.MachineAddedEvent;
import org.altherian.hboxc.event.machine.MachineDataChangedEvent;
import org.altherian.hboxc.event.machine.MachineRemovedEvent;
import org.altherian.hboxc.event.machine.MachineStateChangedEvent;
import org.altherian.hboxc.event.module.ModuleEvent;
import org.altherian.hboxc.event.snapshot.SnapshotDeletedEvent;
import org.altherian.hboxc.event.snapshot.SnapshotModifiedEvent;
import org.altherian.hboxc.event.snapshot.SnapshotRestoredEvent;
import org.altherian.hboxc.event.snapshot.SnapshotTakenEvent;
import org.altherian.hboxc.event.task.TaskAddedEvent;
import org.altherian.hboxc.event.task.TaskRemovedEvent;
import org.altherian.hboxc.event.task.TaskStateChangedEvent;
import org.altherian.hboxc.server._GuestReader;
import org.altherian.hboxc.server._Hypervisor;
import org.altherian.hboxc.server._Machine;
import org.altherian.hboxc.server._ServerReader;
import org.altherian.hboxc.server.task._Task;
import org.altherian.tool.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CachedServerReader implements _ServerReader {
   
   private _ServerReader reader;
   
   private Map<String, MachineOut> mOutListCache;
   private Long mOutListCacheUpdate;
   private Map<String, MachineOut> mOutCache;
   private Set<String> invalidMachineUuidSet;
   
   private Map<String, TaskOut> tOutListCache;
   private Long tOutListCacheUpdate;
   private Map<String, TaskOut> tOutCache;
   private Set<String> invalidTaskIdSet;
   
   private Map<String, MediumOut> medOutCache;
   private Set<String> invalidMedOutSet;
   
   private Map<String, Map<String, SnapshotOut>> snapOutCache;
   
   public CachedServerReader(_ServerReader reader) {
      this.reader = reader;
      
      mOutListCache = new ConcurrentHashMap<String, MachineOut>();
      mOutCache = new ConcurrentHashMap<String, MachineOut>();
      invalidMachineUuidSet = new LinkedHashSet<String>();
      
      tOutListCache = new ConcurrentHashMap<String, TaskOut>();
      tOutCache = new ConcurrentHashMap<String, TaskOut>();
      invalidTaskIdSet = new LinkedHashSet<String>();
      
      medOutCache = new HashMap<String, MediumOut>();
      invalidMedOutSet = new LinkedHashSet<String>();
      
      snapOutCache = new HashMap<String, Map<String, SnapshotOut>>();
      
      EventManager.get().register(this);
   }
   
   private void insertSnapshot(String vmId, SnapshotOut snapOut) {
      Logger.track();
      
      if (!snapOutCache.containsKey(vmId)) {
         snapOutCache.put(vmId, new HashMap<String, SnapshotOut>());
      }
      snapOutCache.get(vmId).put(snapOut.getUuid(), snapOut);
   }
   
   private void updateSnapshot(String vmId, SnapshotOut snapOut) {
      if (snapOutCache.containsKey(vmId)) {
         snapOutCache.get(vmId).put(snapOut.getUuid(), snapOut);
      }
   }
   
   private void removeSnapshot(String vmId, String snapId) {
      if (snapOutCache.containsKey(vmId)) {
         snapOutCache.get(vmId).remove(snapId);
      }
   }
   
   private void refreshSnapshot(String vmUuid, String snapUuid) {
      Logger.track();
      
      try {
         SnapshotOut snapOut = reader.getSnapshot(vmUuid, snapUuid);
         insertSnapshot(vmUuid, snapOut);
      } catch (Throwable t) {
         Logger.error("Unable to refresh Snapshot #" + snapUuid + " of VM #" + vmUuid + ": " + t.getMessage());
      }
   }
   
   @Override
   public String getId() {
      return reader.getId();
   }
   
   @Override
   public String getName() {
      return reader.getName();
   }
   
   @Override
   public String getType() {
      return reader.getType();
   }
   
   @Override
   public String getVersion() {
      return reader.getVersion();
   }
   
   @Override
   public SnapshotOut getRootSnapshot(String vmId) {
      return reader.getRootSnapshot(vmId);
      
   }
   
   @Override
   public SnapshotOut getSnapshot(MachineIn mIn, SnapshotIn snapIn) {
      Logger.track();
      
      if (!snapOutCache.containsKey(mIn.getUuid()) || !snapOutCache.get(mIn.getUuid()).containsKey(snapIn.getUuid())) {
         refreshSnapshot(mIn.getUuid(), snapIn.getUuid());
      }
      
      return snapOutCache.get(mIn.getUuid()).get(snapIn.getUuid());
   }
   
   @Override
   public SnapshotOut getCurrentSnapshot(MachineIn mIn) {
      Logger.track();
      
      if (mOutCache.containsKey(mIn.getId())) {
         return getSnapshot(mIn.getId(), mOutCache.get(mIn.getId()).getCurrentSnapshot());
      } else {
         return reader.getCurrentSnapshot(mIn);
      }
   }
   
   @Handler
   public void putMachineSnapDataChangedEvent(MachineSnapshotDataChangedEventOut ev) {
      Logger.track();
      
      snapOutCache.remove(ev.getUuid());
   }
   
   @Handler
   public void putSnapshotTakenEvent(SnapshotTakenEventOut ev) {
      Logger.track();
      
      updateMachine(ev.getMachine());
      refreshSnapshot(ev.getMachine().getUuid(), ev.getSnapshotUuid());
      refreshSnapshot(ev.getMachine().getUuid(), ev.getSnapshot().getParentUuid());
      EventManager.post(new SnapshotTakenEvent(ev.getServer(), ev.getMachine(), ev.getSnapshot()));
   }
   
   @Handler
   public void putSnashotDeletedEvent(SnapshotDeletedEventOut ev) {
      Logger.track();
      
      SnapshotOut deletedSnap = snapOutCache.get(ev.getUuid()).get(ev.getSnapshotUuid());
      refreshSnapshot(ev.getMachine().getUuid(), deletedSnap.getParentUuid());
      for (String child : deletedSnap.getChildrenUuid()) {
         refreshSnapshot(ev.getUuid(), child);
      }
      updateMachine(ev.getMachine());
      removeSnapshot(ev.getMachine().getUuid(), ev.getSnapshotUuid());
      EventManager.post(new SnapshotDeletedEvent(ev.getServer(), ev.getMachine(), ev.getSnapshot()));
   }
   
   @Handler
   public void putSnapshotRestoredEvent(SnapshotRestoredEventOut ev) {
      Logger.track();
      
      updateMachine(ev.getMachine());
      updateSnapshot(ev.getMachine().getUuid(), ev.getSnapshot());
      EventManager.post(new SnapshotRestoredEvent(ev.getServer(), ev.getMachine(), ev.getSnapshot()));
   }
   
   @Handler
   public void putSnashopModifiedEvent(SnapshotModifiedEventOut ev) {
      Logger.track();
      
      updateSnapshot(ev.getMachine().getUuid(), ev.getSnapshot());
      EventManager.post(new SnapshotModifiedEvent(ev.getServer(), ev.getMachine(), ev.getSnapshot()));
   }
   
   @Handler
   public void putMachineDataChangedEvent(MachineDataChangeEventOut ev) {
      Logger.track();
      
      updateMachine(ev.getMachine());
      EventManager.post(new MachineDataChangedEvent(reader.getId(), getMachine(ev.getMachine().getUuid())));
   }
   
   @Handler
   public void putMachineRegistrationEvent(MachineRegistrationEventOut ev) {
      Logger.track();
      
      if (ev.isRegistered()) {
         insertMachine(ev.getMachine());
         EventManager.post(new MachineAddedEvent(ev.getServerId(), ev.getMachine()));
      } else {
         deleteMachine(ev.getUuid());
         EventManager.post(new MachineRemovedEvent(ev.getServerId(), ev.getMachine()));
      }
      
   }
   
   @Handler
   public void putMachineStateEvent(MachineStateEventOut ev) {
      Logger.track();
      
      updateMachine(ev.getMachine());
      EventManager.post(new MachineStateChangedEvent(reader.getId(), getMachine(ev.getMachine().getUuid())));
   }
   
   private void insertMachine(String vmId) {
      Logger.track();
      
      insertMachine(reader.getMachine(vmId));
   }
   
   private void insertMachine(MachineOut mOut) {
      Logger.track();
      
      snapOutCache.remove(mOut.getId());
      mOutCache.put(mOut.getId(), mOut);
      mOutListCache.put(mOut.getId(), mOut);
      invalidMachineUuidSet.remove(mOut.getId());
   }
   
   private void updateMachine(String vmId) {
      Logger.track();
      
      updateMachine(reader.getMachine(vmId));
   }
   
   private void updateMachine(MachineOut mOut) {
      Logger.track();
      
      mOutCache.put(mOut.getId(), mOut);
      mOutListCache.put(mOut.getId(), mOut);
   }
   
   private void deleteMachine(String vmId) {
      Logger.track();
      
      invalidMachineUuidSet.add(vmId);
      mOutCache.remove(vmId);
      mOutListCache.remove(vmId);
      snapOutCache.remove(vmId);
   }
   
   private void refreshMachine(String vmId) {
      Logger.track();
      
      try {
         if (!mOutCache.containsKey(vmId) && !mOutListCache.containsKey(vmId)) {
            insertMachine(vmId);
         } else {
            updateMachine(vmId);
         }
         // TODO catch the proper exception
      } catch (HyperboxRuntimeException e) {
         // Virtualbox error meaning "Machine not found", so we remove from cache
         if ((e.getMessage() != null) && (e.getMessage().contains("0x80070005") || e.getMessage().contains("0x80BB0001"))) {
            deleteMachine(vmId);
         }
         throw e;
      }
   }
   
   private void insertMedium(MediumIn medIn) {
      Logger.track();
      
      MediumOut medOut = reader.getMedium(medIn);
      medOutCache.put(medOut.getUuid(), medOut);
      medOutCache.put(medOut.getLocation(), medOut);
      invalidMedOutSet.remove(medOut.getUuid());
   }
   
   private void updateMedium(MediumIn medIn) {
      Logger.track();
      
      MediumOut medOut = reader.getMedium(medIn);
      medOutCache.put(medOut.getUuid(), medOut);
      medOutCache.put(medOut.getLocation(), medOut);
   }
   
   private void deleteMedium(MediumIn medIn) {
      Logger.track();
      
      Logger.debug("Removing Medium ID " + medIn.getId() + " from cache");
      MediumOut medOut = getMedium(medIn);
      invalidMedOutSet.add(medOut.getUuid());
      medOutCache.remove(medOut.getUuid());
      medOutCache.remove(medOut.getLocation());
   }
   
   private void refreshMedium(MediumIn medIn) {
      Logger.debug("Refreshing medium ID " + medIn.getId());
      try {
         if (!medOutCache.containsKey(medIn.getId())) {
            Logger.debug("Unknown medium, fetching data & adding to cache");
            insertMedium(medIn);
         } else {
            Logger.debug("Known medium, updating cache");
            updateMedium(medIn);
         }
      } catch (HyperboxRuntimeException e) {
         Logger.debug("Cannot fetch information from server, removing from cache if exists");
         deleteMedium(medIn);
      }
   }
   
   private void updateMachineList() {
      Logger.track();
      
      mOutListCache.clear();
      for (MachineOut mOut : reader.listMachines()) {
         mOutListCache.put(mOut.getUuid(), mOut);
      }
      
      mOutListCacheUpdate = System.currentTimeMillis();
   }
   
   @Override
   public List<MachineOut> listMachines() {
      if (mOutListCacheUpdate == null) {
         updateMachineList();
      }
      
      return new ArrayList<MachineOut>(mOutListCache.values());
   }
   
   @Override
   public MachineOut getMachine(MachineIn mIn) {
      Logger.track();
      
      return getMachine(mIn.getUuid());
   }
   
   @Handler
   public void putTaskStateChangedEvent(TaskStateEventOut ev) {
      Logger.track();
      
      updateTask(ev.getTask());
      EventManager.post(new TaskStateChangedEvent(ev.getServer(), ev.getTask()));
   }
   
   @Handler
   public void putTaskQueueEvent(TaskQueueEventOut ev) {
      Logger.track();
      
      if (ev.getQueueEvent().equals(TaskQueueEvents.TaskAdded)) {
         insertTask(ev.getTask());
         EventManager.post(new TaskAddedEvent(ev.getServer(), ev.getTask()));
      }
      if (ev.getQueueEvent().equals(TaskQueueEvents.TaskRemoved)) {
         removeTask(ev.getTask());
         EventManager.post(new TaskRemovedEvent(ev.getServer(), ev.getTask()));
      }
   }
   
   @Handler
   public void putModuleEventOutput(ModuleEventOut ev) {
      Logger.track();
      
      EventManager.post(new ModuleEvent(HyperboxEvents.ModuleLoaded, ev.getServer(), ev.getModule()));
   }
   
   private void refreshTask(TaskIn tIn) {
      try {
         TaskOut tOut = reader.getTask(tIn);
         insertTask(tOut);
         // TODO catch the proper exception
      } catch (HyperboxRuntimeException e) {
         invalidTaskIdSet.add(tIn.getId());
         removeTask(tIn.getId());
         throw e;
      }
   }
   
   private void insertTask(TaskOut tOut) {
      tOutListCache.put(tOut.getId(), tOut);
      tOutCache.put(tOut.getId(), tOut);
   }
   
   private void updateTask(TaskOut tOut) {
      tOutListCache.put(tOut.getId(), tOut);
      tOutCache.put(tOut.getId(), tOut);
   }
   
   private void removeTask(String taskId) {
      tOutListCache.remove(taskId);
      tOutCache.remove(taskId);
   }
   
   private void removeTask(TaskOut tOut) {
      removeTask(tOut.getId());
   }
   
   private void updateTaskList() {
      Logger.track();
      
      tOutListCache.clear();
      for (TaskOut tOut : reader.listTasks()) {
         tOutListCache.put(tOut.getId(), tOut);
      }
      
      tOutListCacheUpdate = System.currentTimeMillis();
   }
   
   @Override
   public List<TaskOut> listTasks() {
      if (tOutListCacheUpdate == null) {
         updateTaskList();
      }
      
      return new ArrayList<TaskOut>(tOutListCache.values());
   }
   
   @Override
   public TaskOut getTask(TaskIn tIn) {
      if (invalidTaskIdSet.contains(tIn.getId())) {
         throw new HyperboxRuntimeException("Task was not found");
      }
      if (!tOutCache.containsKey(tIn.getId())) {
         refreshTask(tIn);
      }
      return tOutCache.get(tIn.getId());
   }
   
   @Override
   public MediumOut getMedium(MediumIn medIn) {
      if (invalidMedOutSet.contains(medIn.getUuid())) {
         throw new HyperboxRuntimeException(medIn.getUuid() + " does not relate to a medium");
      }
      if (!medOutCache.containsKey(medIn.getUuid()) || !medOutCache.containsKey(medIn.getLocation())) {
         refreshMedium(medIn);
      }
      
      if (medOutCache.containsKey(medIn.getUuid())) {
         return medOutCache.get(medIn.getUuid());
      } else if (medOutCache.containsKey(medIn.getLocation())) {
         return medOutCache.get(medIn.getLocation());
      } else {
         return null;
      }
   }
   
   @Override
   public SessionOut getSession(SessionIn sIn) {
      return reader.getSession(sIn);
   }
   
   @Override
   public StorageControllerSubTypeOut getStorageControllerSubType(StorageControllerSubTypeIn scstIn) {
      return reader.getStorageControllerSubType(scstIn);
   }
   
   @Override
   public StorageControllerTypeOut getStorageControllerType(StorageControllerTypeIn sctIn) {
      return reader.getStorageControllerType(sctIn);
   }
   
   @Override
   public StoreOut getStore(StoreIn sIn) {
      return reader.getStore(sIn);
   }
   
   @Override
   public StoreItemOut getStoreItem(StoreItemIn siIn) {
      return reader.getStoreItem(siIn);
   }
   
   @Override
   public UserOut getUser(UserIn uIn) {
      return reader.getUser(uIn);
   }
   
   @Override
   public List<StorageDeviceAttachmentOut> listAttachments(StorageControllerIn scIn) {
      return reader.listAttachments(scIn);
   }
   
   @Override
   public List<String> listKeyboardMode(MachineIn mIn) {
      return reader.listKeyboardMode(mIn);
   }
   
   @Override
   public List<MediumOut> listMediums() {
      return reader.listMediums();
   }
   
   @Override
   public List<String> listMouseMode(MachineIn mIn) {
      return reader.listMouseMode(mIn);
   }
   
   @Override
   public List<OsTypeOut> listOsType() {
      return reader.listOsType();
   }
   
   @Override
   public List<OsTypeOut> listOsType(MachineIn mIn) {
      return reader.listOsType(mIn);
   }
   
   @Override
   public List<SessionOut> listSessions() {
      return reader.listSessions();
   }
   
   @Override
   public List<StorageControllerSubTypeOut> listStorageControllerSubType(StorageControllerTypeIn sctIn) {
      return reader.listStorageControllerSubType(sctIn);
   }
   
   @Override
   public List<StorageControllerTypeOut> listStorageControllerType() {
      return reader.listStorageControllerType();
   }
   
   @Override
   public List<StoreItemOut> listStoreItems(StoreIn sIn) {
      return reader.listStoreItems(sIn);
   }
   
   @Override
   public List<StoreItemOut> listStoreItems(StoreIn sIn, StoreItemIn siIn) {
      return reader.listStoreItems(sIn, siIn);
   }
   
   @Override
   public List<StoreOut> listStores() {
      return reader.listStores();
   }
   
   @Override
   public List<UserOut> listUsers() {
      return reader.listUsers();
   }
   
   @Override
   public List<NetworkInterfaceOut> listNetworkInterfaces(MachineIn mIn) {
      return reader.listNetworkInterfaces(mIn);
   }
   
   @Override
   public List<NetworkAttachModeOut> listNetworkAttachModes() {
      return reader.listNetworkAttachModes();
   }
   
   @Override
   public List<NetworkAttachNameOut> listNetworkAttachNames(NetworkAttachModeIn namIn) {
      return reader.listNetworkAttachNames(namIn);
   }
   
   @Override
   public List<NetworkInterfaceTypeOut> listNetworkInterfaceTypes() {
      return reader.listNetworkInterfaceTypes();
   }
   
   @Override
   public _Hypervisor getHypervisor() {
      return reader.getHypervisor();
   }
   
   @Override
   public ScreenshotOut getScreenshot(MachineIn mIn) {
      return reader.getScreenshot(mIn);
   }
   
   @Override
   public boolean isHypervisorConnected() {
      return reader.isHypervisorConnected();
   }
   
   @Override
   public List<HypervisorLoaderOut> listHypervisors() {
      return reader.listHypervisors();
   }
   
   @Override
   public List<StorageDeviceAttachmentOut> listAttachments(String machineUuid) {
      return reader.listAttachments(machineUuid);
   }
   
   @Override
   public String getProtocolVersion() {
      return reader.getProtocolVersion();
   }
   
   @Override
   public _GuestReader getGuest(String machineUuid) {
      return reader.getGuest(machineUuid);
   }
   
   @Override
   public MachineOut getMachine(String vmId) {
      if (invalidMachineUuidSet.contains(vmId)) {
         throw new HyperboxRuntimeException(vmId + " does not relate to a machine");
      }
      if (!mOutCache.containsKey(vmId)) {
         refreshMachine(vmId);
      }
      return mOutCache.get(vmId);
   }
   
   @Override
   public List<PermissionOut> listPermissions(UserIn usrIn) {
      return reader.listPermissions(usrIn);
   }
   
   @Override
   public HostOut getHost() {
      return reader.getHost();
   }
   
   @Override
   public SnapshotOut getSnapshot(String vmId, String snapId) {
      return getSnapshot(new MachineIn(vmId), new SnapshotIn(snapId));
   }
   
   @Override
   public SnapshotOut getCurrentSnapshot(String vmId) {
      return getCurrentSnapshot(new MachineIn(vmId));
   }
   
   @Override
   public List<ModuleOut> listModules() {
      return reader.listModules();
   }
   
   @Override
   public ModuleOut getModule(String modId) {
      return reader.getModule(modId);
   }
   
   @Override
   public Set<String> listLogLevel() {
      return reader.listLogLevel();
   }
   
   @Override
   public String getLogLevel() {
      return reader.getLogLevel();
   }
   
   @Override
   public _Machine getMachineReader(String id) {
      return reader.getMachineReader(id);
   }
   
   @Override
   public _Task getTask(String id) {
      return reader.getTask(id);
   }
   
}
