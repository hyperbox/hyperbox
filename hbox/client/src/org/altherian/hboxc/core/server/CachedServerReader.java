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


public class CachedServerReader /* implements _ServerReader */{
   
   /*
   
   private _ServerReader reader;
   
   private Map<String, MachineOutput> mOutListCache;
   private Long mOutListCacheUpdate;
   private Map<String, MachineOutput> mOutCache;
   private Set<String> invalidMachineUuidSet;
   
   private Map<String, TaskOutput> tOutListCache;
   private Long tOutListCacheUpdate;
   private Map<String, TaskOutput> tOutCache;
   private Set<String> invalidTaskIdSet;
   
   private Map<String, Map<String, SnapshotOutput>> snapOutCache;
   
   public CachedServerReader(_ServerReader reader) {
      this.reader = reader;
      
      mOutListCache = new ConcurrentHashMap<String, MachineOutput>();
      mOutCache = new ConcurrentHashMap<String, MachineOutput>();
      invalidMachineUuidSet = new LinkedHashSet<String>();
      
      tOutListCache = new ConcurrentHashMap<String, TaskOutput>();
      tOutCache = new ConcurrentHashMap<String, TaskOutput>();
      invalidTaskIdSet = new LinkedHashSet<String>();
      
      snapOutCache = new HashMap<String, Map<String, SnapshotOutput>>();
      
      CoreEventManager.get().register(this);
   }
   
   private void addSnap(String vmUuid, SnapshotOutput snapOut) {
      if (!snapOutCache.containsKey(vmUuid)) {
         snapOutCache.put(vmUuid, new HashMap<String, SnapshotOutput>());
      }
      snapOutCache.get(vmUuid).put(snapOut.getUuid(), snapOut);
   }
   
   private void remSnap(String vmUuid, String snapUuid) {
      snapOutCache.get(vmUuid).remove(snapUuid);
      if (snapOutCache.get(vmUuid).isEmpty()) {
         snapOutCache.remove(vmUuid);
      }
   }
   
   private void refreshSnapData(String vmUuid, String snapUuid) {
      try {
         SnapshotOutput snapOut = reader.getSnapshot(new MachineInput(reader.getId(), vmUuid), new SnapshotInput(snapUuid));
         addSnap(vmUuid, snapOut);
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
   public SnapshotOutput getRootSnapshot(MachineInput mIn) {
      return reader.getRootSnapshot(mIn);
   }
   
   @Override
   public SnapshotOutput getSnapshot(MachineInput mIn, SnapshotInput snapIn) {
      if (!snapOutCache.containsKey(mIn.getUuid()) || !snapOutCache.get(mIn.getUuid()).containsKey(snapIn.getUuid())) {
         refreshSnapData(mIn.getUuid(), snapIn.getUuid());
      }
      return snapOutCache.get(mIn.getUuid()).get(snapIn.getUuid());
   }
   
   @Override
   public SnapshotOutput getCurrentSnapshot(MachineInput mIn) {
      return reader.getCurrentSnapshot(mIn);
   }
   
   @Handler
   public void putMachineSnapDataChangedEvent(MachineSnapshotDataChangedEventOutput ev) {
      snapOutCache.clear();
      FrontEventManager.post(ev);
   }
   
   @Handler
   public void putSnapshotTakenEvent(SnapshotTakenEventOutput ev) {
      Logger.track();
      
      refreshSnapData(ev.getUuid(), ev.getSnapshotUuid());
      FrontEventManager.post(ev);
   }
   
   @Handler
   public void putSnashotDeletedEvent(SnapshotDeletedEventOutput ev) {
      Logger.track();
      
      remSnap(ev.getMachine().getUuid(), ev.getSnapshotUuid());
      FrontEventManager.post(ev);
   }
   
   @Handler
   public void putSnapshotRestoredEvent(SnapshotRestoredEventOutput ev) {
      Logger.track();
      
      refreshSnapData(ev.getMachine().getUuid(), ev.getSnapshotUuid());
      FrontEventManager.post(ev);
   }
   
   @Handler
   public void putSnashopModifiedEvent(SnapshotModifiedEventOutput ev) {
      Logger.track();
      
      refreshSnapData(ev.getMachine().getUuid(), ev.getSnapshotUuid());
      FrontEventManager.post(ev);
   }
   
   @Handler
   public void putMachineDataChangedEvent(MachineDataChangeEventOutput ev) {
      Logger.track();
      
      MachineInput mIn = new MachineInput(ev.getServerId(),ev.getUuid());
      if (tryRefreshMachine(mIn)) {
         FrontEventManager.post(new MachineDataChangedEvent(reader.getId(), getMachine(mIn)));
      }
   }
   
   @Handler
   public void putMachineRegistrationEvent(MachineRegistrationEventOutput ev) {
      Logger.track();
      
      if (ev.isRegistered()) {
         if (tryRefreshMachine(new MachineInput(ev.getServerId(), ev.getUuid()))) {
            FrontEventManager.post(ev);
         }
      } else {
         deleteMachine(new MachineInput(ev.getServerId(), ev.getUuid()));
      }
      
   }
   
   @Handler
   public void putMachineStateEvent(MachineStateEventOutput ev) {
      Logger.track();
      
      MachineInput mIn = new MachineInput(ev.getServerId(), ev.getUuid());
      if (tryRefreshMachine(mIn)) {
         FrontEventManager.post(new MachineStateChangedEvent(reader.getId(), getMachine(mIn)));
      }
   }
   
   private void insertMachine(MachineInput mIn) {
      Logger.track();
      
      MachineOutput mOut = reader.getMachine(mIn);
      mOutCache.put(mOut.getUuid(), mOut);
      mOutListCache.put(mOut.getUuid(), mOut);
      invalidMachineUuidSet.remove(mIn.getUuid());
      FrontEventManager.post(new MachineAddedEvent(reader.getId(), mOut));
   }
   
   private void updateMachine(MachineInput mIn) {
      Logger.track();
      
      MachineOutput mOut = reader.getMachine(mIn);
      mOutCache.put(mOut.getUuid(), mOut);
      mOutListCache.put(mOut.getUuid(), mOut);
      FrontEventManager.post(new MachineDataChangedEvent(reader.getId(), mOut));
   }
   
   private void deleteMachine(MachineInput mIn) {
      Logger.track();
      
      MachineOutput mOut = getMachine(mIn);
      invalidMachineUuidSet.add(mIn.getUuid());
      mOutCache.remove(mIn.getUuid());
      mOutListCache.remove(mIn.getUuid());
      FrontEventManager.post(new MachineRemovedEvent(reader.getId(), mOut));
   }
   
   private void refreshMachine(MachineInput mIn) {
      try {
         if (!mOutCache.containsKey(mIn.getUuid()) && !mOutListCache.containsKey(mIn.getUuid())) {
            insertMachine(mIn);
         } else {
            updateMachine(mIn);
         }
         // TODO catch the proper exception
      } catch (HyperboxRuntimeException e) {
         // Virtualbox error meaning "Machine not found", so we remove from cache
         if (e.getMessage().contains("0x80070005") || e.getMessage().contains("0x80BB0001")) {
            deleteMachine(mIn);
         }
         throw e;
      }
   }
   
   private boolean tryRefreshMachine(MachineInput mIn) {
      try {
         refreshMachine(mIn);
         return true;
      } catch (HyperboxRuntimeException e) {
         return false;
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
      if (invalidMachineUuidSet.contains(mIn.getUuid())) {
         throw new HyperboxRuntimeException(mIn.getUuid() + " does not relate to a machine");
      }
      if (!mOutCache.containsKey(mIn.getUuid())) {
         refreshMachine(mIn);
      }
      return mOutCache.get(mIn.getUuid());
   }
   
   @Handler
   public void putTaskStateChangedEvent(TaskStateEventOutput ev) {
      Logger.track();
      
      insertTask(ev.getTask());
      FrontEventManager.post(ev);
   }
   
   @Handler
   public void putTaskQueueEvent(TaskQueueEventOutput ev) {
      Logger.track();
      
      if (ev.getQueueEvent().equals(TaskQueueEvents.TaskAdded)) {
         insertTask(ev.getTask());
         FrontEventManager.post(ev);
      }
      if (ev.getQueueEvent().equals(TaskQueueEvents.TaskRemoved)) {
         removeTask(ev.getTask());
         FrontEventManager.post(ev);
      }
   }
   
   @Handler
   public void putAllOtherEvents(EventOutput ev) {
      Logger.track();
      
      FrontEventManager.post(ev);
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
   public MediumOutput getMedium(MediumInput mIn) {
      return reader.getMedium(mIn);
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
   
    */
   
}
