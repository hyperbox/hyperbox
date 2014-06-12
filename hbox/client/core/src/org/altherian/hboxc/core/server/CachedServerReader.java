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

package org.altherian.hboxc.core.server;

import net.engio.mbassy.listener.Handler;

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
import org.altherian.hbox.comm.output.event.machine.MachineDataChangeEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineRegistrationEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineSnapshotDataChangedEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineStateEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotDeletedEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotModifiedEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotRestoredEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotTakenEventOutput;
import org.altherian.hbox.comm.output.event.task.TaskQueueEventOutput;
import org.altherian.hbox.comm.output.event.task.TaskStateEventOutput;
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
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.states.TaskQueueEvents;
import org.altherian.hboxc.event.CoreEventManager;
import org.altherian.hboxc.event.machine.MachineAddedEvent;
import org.altherian.hboxc.event.machine.MachineDataChangedEvent;
import org.altherian.hboxc.event.machine.MachineRemovedEvent;
import org.altherian.hboxc.event.machine.MachineStateChangedEvent;
import org.altherian.hboxc.event.snapshot.SnapshotDeletedEvent;
import org.altherian.hboxc.event.snapshot.SnapshotModifiedEvent;
import org.altherian.hboxc.event.snapshot.SnapshotRestoredEvent;
import org.altherian.hboxc.event.snapshot.SnapshotTakenEvent;
import org.altherian.hboxc.event.task.TaskAddedEvent;
import org.altherian.hboxc.event.task.TaskRemovedEvent;
import org.altherian.hboxc.event.task.TaskStateChangedEvent;
import org.altherian.hboxc.server._GuestReader;
import org.altherian.hboxc.server._HypervisorReader;
import org.altherian.hboxc.server._ServerReader;
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
   
   private Map<String, MachineOutput> mOutListCache;
   private Long mOutListCacheUpdate;
   private Map<String, MachineOutput> mOutCache;
   private Set<String> invalidMachineUuidSet;
   
   private Map<String, TaskOutput> tOutListCache;
   private Long tOutListCacheUpdate;
   private Map<String, TaskOutput> tOutCache;
   private Set<String> invalidTaskIdSet;
   
   private Map<String, MediumOutput> medOutCache;
   private Set<String> invalidMedOutSet;
   
   private Map<String, Map<String, SnapshotOutput>> snapOutCache;
   
   
   public CachedServerReader(_ServerReader reader) {
      this.reader = reader;
      
      mOutListCache = new ConcurrentHashMap<String, MachineOutput>();
      mOutCache = new ConcurrentHashMap<String, MachineOutput>();
      invalidMachineUuidSet = new LinkedHashSet<String>();
      
      tOutListCache = new ConcurrentHashMap<String, TaskOutput>();
      tOutCache = new ConcurrentHashMap<String, TaskOutput>();
      invalidTaskIdSet = new LinkedHashSet<String>();
      
      medOutCache = new HashMap<String, MediumOutput>();
      invalidMedOutSet = new LinkedHashSet<String>();
      
      snapOutCache = new HashMap<String, Map<String, SnapshotOutput>>();
      
      CoreEventManager.get().register(this);
   }
   
   private void insertSnapshot(String vmId, SnapshotOutput snapOut) {
      Logger.track();
      
      if (!snapOutCache.containsKey(vmId)) {
         snapOutCache.put(vmId, new HashMap<String, SnapshotOutput>());
      }
      snapOutCache.get(vmId).put(snapOut.getUuid(), snapOut);
   }
   
   private void updateSnapshot(String vmId, SnapshotOutput snapOut) {
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
         SnapshotOutput snapOut = reader.getSnapshot(vmUuid, snapUuid);
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
   public SnapshotOutput getRootSnapshot(String vmId) {
      return reader.getRootSnapshot(vmId);
      
   }
   
   @Override
   public SnapshotOutput getSnapshot(MachineInput mIn, SnapshotInput snapIn) {
      Logger.track();
      
      if (!snapOutCache.containsKey(mIn.getUuid()) || !snapOutCache.get(mIn.getUuid()).containsKey(snapIn.getUuid())) {
         refreshSnapshot(mIn.getUuid(), snapIn.getUuid());
      }
      
      return snapOutCache.get(mIn.getUuid()).get(snapIn.getUuid());
   }
   
   @Override
   public SnapshotOutput getCurrentSnapshot(MachineInput mIn) {
      Logger.track();
      
      if (mOutCache.containsKey(mIn.getId())) {
         return getSnapshot(mIn.getId(), mOutCache.get(mIn.getId()).getCurrentSnapshot());
      } else {
         return reader.getCurrentSnapshot(mIn);
      }
   }
   
   @Handler
   public void putMachineSnapDataChangedEvent(MachineSnapshotDataChangedEventOutput ev) {
      Logger.track();
      
      snapOutCache.remove(ev.getUuid());
   }
   
   @Handler
   public void putSnapshotTakenEvent(SnapshotTakenEventOutput ev) {
      Logger.track();
      
      updateMachine(ev.getMachine());
      refreshSnapshot(ev.getMachine().getUuid(), ev.getSnapshotUuid());
      refreshSnapshot(ev.getMachine().getUuid(), ev.getSnapshot().getParentUuid());
      CoreEventManager.post(new SnapshotTakenEvent(ev.getServer(), ev.getMachine(), ev.getSnapshot()));
   }
   
   @Handler
   public void putSnashotDeletedEvent(SnapshotDeletedEventOutput ev) {
      Logger.track();
      
      SnapshotOutput deletedSnap = snapOutCache.get(ev.getUuid()).get(ev.getSnapshotUuid());
      refreshSnapshot(ev.getMachine().getUuid(), deletedSnap.getParentUuid());
      for (String child : deletedSnap.getChildrenUuid()) {
         refreshSnapshot(ev.getUuid(), child);
      }
      updateMachine(ev.getMachine());
      removeSnapshot(ev.getMachine().getUuid(), ev.getSnapshotUuid());
      CoreEventManager.post(new SnapshotDeletedEvent(ev.getServer(), ev.getMachine(), ev.getSnapshot()));
   }
   
   @Handler
   public void putSnapshotRestoredEvent(SnapshotRestoredEventOutput ev) {
      Logger.track();
      
      updateMachine(ev.getMachine());
      updateSnapshot(ev.getMachine().getUuid(), ev.getSnapshot());
      CoreEventManager.post(new SnapshotRestoredEvent(ev.getServer(), ev.getMachine(), ev.getSnapshot()));
   }
   
   @Handler
   public void putSnashopModifiedEvent(SnapshotModifiedEventOutput ev) {
      Logger.track();
      
      updateSnapshot(ev.getMachine().getUuid(), ev.getSnapshot());
      CoreEventManager.post(new SnapshotModifiedEvent(ev.getServer(), ev.getMachine(), ev.getSnapshot()));
   }
   
   @Handler
   public void putMachineDataChangedEvent(MachineDataChangeEventOutput ev) {
      Logger.track();
      
      updateMachine(ev.getMachine());
      CoreEventManager.post(new MachineDataChangedEvent(reader.getId(), getMachine(ev.getMachine().getUuid())));
   }
   
   @Handler
   public void putMachineRegistrationEvent(MachineRegistrationEventOutput ev) {
      Logger.track();
      
      if (ev.isRegistered()) {
         insertMachine(ev.getMachine());
         CoreEventManager.post(new MachineAddedEvent(ev.getServerId(), ev.getMachine()));
      } else {
         deleteMachine(ev.getUuid());
         CoreEventManager.post(new MachineRemovedEvent(ev.getServerId(), ev.getMachine()));
      }
      
   }
   
   @Handler
   public void putMachineStateEvent(MachineStateEventOutput ev) {
      Logger.track();
      
      updateMachine(ev.getMachine());
      CoreEventManager.post(new MachineStateChangedEvent(reader.getId(), getMachine(ev.getMachine().getUuid())));
   }
   
   private void insertMachine(String vmId) {
      Logger.track();
      
      insertMachine(reader.getMachine(vmId));
   }
   
   private void insertMachine(MachineOutput mOut) {
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
   
   private void updateMachine(MachineOutput mOut) {
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
         if (e.getMessage().contains("0x80070005") || e.getMessage().contains("0x80BB0001")) {
            deleteMachine(vmId);
         }
         throw e;
      }
   }
   
   private void insertMedium(MediumInput medIn) {
      Logger.track();
      
      MediumOutput medOut = reader.getMedium(medIn);
      medOutCache.put(medOut.getUuid(), medOut);
      medOutCache.put(medOut.getLocation(), medOut);
      invalidMedOutSet.remove(medOut.getUuid());
   }
   
   private void updateMedium(MediumInput medIn) {
      Logger.track();
      
      MediumOutput medOut = reader.getMedium(medIn);
      medOutCache.put(medOut.getUuid(), medOut);
      medOutCache.put(medOut.getLocation(), medOut);
   }
   
   private void deleteMedium(MediumInput medIn) {
      Logger.track();
      
      Logger.debug("Removing Medium ID " + medIn.getId() + " from cache");
      MediumOutput medOut = getMedium(medIn);
      invalidMedOutSet.add(medOut.getUuid());
      medOutCache.remove(medOut.getUuid());
      medOutCache.remove(medOut.getLocation());
   }
   
   private void refreshMedium(MediumInput medIn) {
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
      for (MachineOutput mOut : reader.listMachines()) {
         mOutListCache.put(mOut.getUuid(), mOut);
      }
      
      mOutListCacheUpdate = System.currentTimeMillis();
   }
   
   @Override
   public List<MachineOutput> listMachines() {
      if (mOutListCacheUpdate == null) {
         updateMachineList();
      }
      
      return new ArrayList<MachineOutput>(mOutListCache.values());
   }
   
   @Override
   public MachineOutput getMachine(MachineInput mIn) {
      Logger.track();
      
      return getMachine(mIn.getUuid());
   }
   
   @Handler
   public void putTaskStateChangedEvent(TaskStateEventOutput ev) {
      Logger.track();
      
      updateTask(ev.getTask());
      CoreEventManager.post(new TaskStateChangedEvent(ev.getServer(), ev.getTask()));
   }
   
   @Handler
   public void putTaskQueueEvent(TaskQueueEventOutput ev) {
      Logger.track();
      
      if (ev.getQueueEvent().equals(TaskQueueEvents.TaskAdded)) {
         insertTask(ev.getTask());
         CoreEventManager.post(new TaskAddedEvent(ev.getServer(), ev.getTask()));
      }
      if (ev.getQueueEvent().equals(TaskQueueEvents.TaskRemoved)) {
         removeTask(ev.getTask());
         CoreEventManager.post(new TaskRemovedEvent(ev.getServer(), ev.getTask()));
      }
   }
   
   private void refreshTask(TaskInput tIn) {
      try {
         TaskOutput tOut = reader.getTask(tIn);
         insertTask(tOut);
         // TODO catch the proper exception
      } catch (HyperboxRuntimeException e) {
         invalidTaskIdSet.add(tIn.getId());
         removeTask(tIn.getId());
         throw e;
      }
   }
   
   private void insertTask(TaskOutput tOut) {
      tOutListCache.put(tOut.getId(), tOut);
      tOutCache.put(tOut.getId(), tOut);
   }
   
   private void updateTask(TaskOutput tOut) {
      tOutListCache.put(tOut.getId(), tOut);
      tOutCache.put(tOut.getId(), tOut);
   }
   
   private void removeTask(String taskId) {
      tOutListCache.remove(taskId);
      tOutCache.remove(taskId);
   }
   
   private void removeTask(TaskOutput tOut) {
      removeTask(tOut.getId());
   }
   
   private void updateTaskList() {
      Logger.track();
      
      tOutListCache.clear();
      for (TaskOutput tOut : reader.listTasks()) {
         tOutListCache.put(tOut.getId(), tOut);
      }
      
      tOutListCacheUpdate = System.currentTimeMillis();
   }
   
   @Override
   public List<TaskOutput> listTasks() {
      if (tOutListCacheUpdate == null) {
         updateTaskList();
      }
      
      return new ArrayList<TaskOutput>(tOutListCache.values());
   }
   
   @Override
   public TaskOutput getTask(TaskInput tIn) {
      if (invalidTaskIdSet.contains(tIn.getId())) {
         throw new HyperboxRuntimeException("Task was not found");
      }
      if (!tOutCache.containsKey(tIn.getId())) {
         refreshTask(tIn);
      }
      return tOutCache.get(tIn.getId());
   }
   
   @Override
   public MediumOutput getMedium(MediumInput medIn) {
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
   public SessionOutput getSession(SessionInput sIn) {
      return reader.getSession(sIn);
   }
   
   @Override
   public StorageControllerSubTypeOutput getStorageControllerSubType(StorageControllerSubTypeInput scstIn) {
      return reader.getStorageControllerSubType(scstIn);
   }
   
   @Override
   public StorageControllerTypeOutput getStorageControllerType(StorageControllerTypeInput sctIn) {
      return reader.getStorageControllerType(sctIn);
   }
   
   @Override
   public StoreOutput getStore(StoreInput sIn) {
      return reader.getStore(sIn);
   }
   
   @Override
   public StoreItemOutput getStoreItem(StoreItemInput siIn) {
      return reader.getStoreItem(siIn);
   }
   
   @Override
   public UserOutput getUser(UserInput uIn) {
      return reader.getUser(uIn);
   }
   
   @Override
   public List<StorageDeviceAttachmentOutput> listAttachments(StorageControllerInput scIn) {
      return reader.listAttachments(scIn);
   }
   
   @Override
   public List<String> listKeyboardMode(MachineInput mIn) {
      return reader.listKeyboardMode(mIn);
   }
   
   @Override
   public List<MediumOutput> listMediums() {
      return reader.listMediums();
   }
   
   @Override
   public List<String> listMouseMode(MachineInput mIn) {
      return reader.listMouseMode(mIn);
   }
   
   @Override
   public List<OsTypeOutput> listOsType() {
      return reader.listOsType();
   }
   
   @Override
   public List<OsTypeOutput> listOsType(MachineInput mIn) {
      return reader.listOsType(mIn);
   }
   
   @Override
   public List<SessionOutput> listSessions() {
      return reader.listSessions();
   }
   
   @Override
   public List<StorageControllerSubTypeOutput> listStorageControllerSubType(StorageControllerTypeInput sctIn) {
      return reader.listStorageControllerSubType(sctIn);
   }
   
   @Override
   public List<StorageControllerTypeOutput> listStorageControllerType() {
      return reader.listStorageControllerType();
   }
   
   @Override
   public List<StoreItemOutput> listStoreItems(StoreInput sIn) {
      return reader.listStoreItems(sIn);
   }
   
   @Override
   public List<StoreItemOutput> listStoreItems(StoreInput sIn, StoreItemInput siIn) {
      return reader.listStoreItems(sIn, siIn);
   }
   
   @Override
   public List<StoreOutput> listStores() {
      return reader.listStores();
   }
   
   @Override
   public List<UserOutput> listUsers() {
      return reader.listUsers();
   }
   
   @Override
   public List<NetworkInterfaceOutput> listNetworkInterfaces(MachineInput mIn) {
      return reader.listNetworkInterfaces(mIn);
   }
   
   @Override
   public List<NetworkAttachModeOutput> listNetworkAttachModes() {
      return reader.listNetworkAttachModes();
   }
   
   @Override
   public List<NetworkAttachNameOutput> listNetworkAttachNames(NetworkAttachModeInput namIn) {
      return reader.listNetworkAttachNames(namIn);
   }
   
   @Override
   public List<NetworkInterfaceTypeOutput> listNetworkInterfaceTypes() {
      return reader.listNetworkInterfaceTypes();
   }
   
   @Override
   public _HypervisorReader getHypervisor() {
      return reader.getHypervisor();
   }
   
   @Override
   public ScreenshotOutput getScreenshot(MachineInput mIn) {
      return reader.getScreenshot(mIn);
   }
   
   @Override
   public boolean isHypervisorConnected() {
      return reader.isHypervisorConnected();
   }
   
   @Override
   public List<HypervisorLoaderOutput> listHypervisors() {
      return reader.listHypervisors();
   }
   
   @Override
   public List<StorageDeviceAttachmentOutput> listAttachments(String machineUuid) {
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
   public MachineOutput getMachine(String vmId) {
      if (invalidMachineUuidSet.contains(vmId)) {
         throw new HyperboxRuntimeException(vmId + " does not relate to a machine");
      }
      if (!mOutCache.containsKey(vmId)) {
         refreshMachine(vmId);
      }
      return mOutCache.get(vmId);
   }
   
   @Override
   public List<PermissionOutput> listPermissions(UserInput usrIn) {
      return reader.listPermissions(usrIn);
   }
   
   @Override
   public HostOutput getHost() {
      return reader.getHost();
   }
   
   @Override
   public SnapshotOutput getSnapshot(String vmId, String snapId) {
      return getSnapshot(new MachineInput(vmId), new SnapshotInput(snapId));
   }
   
   @Override
   public SnapshotOutput getCurrentSnapshot(String vmId) {
      return getCurrentSnapshot(new MachineInput(vmId));
   }
   
}
