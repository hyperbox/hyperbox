/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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
import org.altherian.hbox.Configuration;
import org.altherian.hbox.HyperboxAPI;
import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.CommObjets;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.RequestProcessType;
import org.altherian.hbox.comm._AnswerReceiver;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.MediumIn;
import org.altherian.hbox.comm.in.ModuleIn;
import org.altherian.hbox.comm.in.NetworkAttachModeIn;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.in.SessionIn;
import org.altherian.hbox.comm.in.SnapshotIn;
import org.altherian.hbox.comm.in.StorageControllerIn;
import org.altherian.hbox.comm.in.StorageControllerSubTypeIn;
import org.altherian.hbox.comm.in.StorageControllerTypeIn;
import org.altherian.hbox.comm.in.StoreIn;
import org.altherian.hbox.comm.in.StoreItemIn;
import org.altherian.hbox.comm.in.TaskIn;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.comm.out.HelloOut;
import org.altherian.hbox.comm.out.ModuleOut;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.SessionOut;
import org.altherian.hbox.comm.out.StoreItemOut;
import org.altherian.hbox.comm.out.StoreOut;
import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorEventOut;
import org.altherian.hbox.comm.out.event.server.ServerPropertyChangedEventOut;
import org.altherian.hbox.comm.out.event.server.ServerShutdownEventOut;
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
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.back._Backend;
import org.altherian.hboxc.comm.io.factory.ServerIoFactory;
import org.altherian.hboxc.comm.utils.Transaction;
import org.altherian.hboxc.event.EventManager;
import org.altherian.hboxc.event.backend.BackendConnectionStateEvent;
import org.altherian.hboxc.event.server.ServerConnectedEvent;
import org.altherian.hboxc.event.server.ServerDisconnectedEvent;
import org.altherian.hboxc.event.server.ServerModifiedEvent;
import org.altherian.hboxc.factory.BackendFactory;
import org.altherian.hboxc.server._GuestReader;
import org.altherian.hboxc.server._Hypervisor;
import org.altherian.hboxc.server._Machine;
import org.altherian.hboxc.server._Server;
import org.altherian.hboxc.server.task._Task;
import org.altherian.hboxc.state.ConnectionState;
import org.altherian.tool.AxBooleans;
import org.altherian.tool.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Server implements _Server, _AnswerReceiver {

   private Map<String, _AnswerReceiver> ansRecv;
   private _Backend backend;

   private String id;
   private String name;
   private String type;
   private String version;
   private String protocolVersion;
   private String logLevel;
   private boolean isHypConnected = false;

   private ConnectionState state = ConnectionState.Disconnected;
   private _Hypervisor hypReader;

   private void setState(ConnectionState state) {
      if (this.state.equals(state)) {
         Logger.debug("Ignoring setState(" + state + ") - same as current");
      }

      this.state = state;
      Logger.info("Changed Server #" + id + " object state to " + state);
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getType() {
      return type;
   }

   @Override
   public String getVersion() {
      return version;
   }

   @Override
   public String getProtocolVersion() {
      return protocolVersion;
   }

   @Override
   public String getLogLevel() {
      return logLevel;
   }

   @Override
   public ConnectionState getState() {
      return state;
   }

   @Override
   public Transaction sendRequest(Request req) {
      return sendRequest(req, RequestProcessType.WaitForRequest);
   }

   @Override
   public Transaction sendRequest(Request req, RequestProcessType type) {
      if (type.equals(RequestProcessType.NoWait)) {
         backend.putRequest(req);
      }
      Transaction t = getTransaction(req);

      if (type.equals(RequestProcessType.WaitForRequest)) {
         if (!t.sendAndWait()) {
            throw new HyperboxRuntimeException(t.getError());
         }
      }
      if (type.equals(RequestProcessType.WaitForTask)) {
         if (!t.sendAndWaitForTask()) {
            throw new HyperboxRuntimeException(t.getError());
         }
      }

      return t;
   }

   @Override
   public MachineOut createMachine(MachineIn mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public MachineOut registerMachine(MachineIn mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public MachineOut modifyMachine(MachineIn mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public MachineOut unregisterMachine(MachineIn mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public MachineOut deleteMachine(MachineIn mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public void startMachine(MachineIn mIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachinePowerOn, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to start the VM : " + trans.getError());
      }
   }

   @Override
   public void stopMachine(MachineIn mIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachinePowerOff, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to stop the VM : " + trans.getError());
      }
   }

   @Override
   public void acpiPowerMachine(MachineIn mIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachineAcpiPowerButton, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to send ACPI Power signal to the VM : " + trans.getError());
      }
   }

   @Override
   public UserOut addUser(UserIn uIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public UserOut modifyUser(UserIn uIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public UserOut deleteUser(UserIn uIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public SessionOut closeSession(SessionIn sIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public MachineOut getMachine(MachineIn mIn) {

      Transaction t = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachineGet, mIn));
      if (!t.sendAndWait()) {
         throw new HyperboxRuntimeException(t.getError());
      }

      MachineOut mOut = t.extractItem(MachineOut.class);
      return mOut;
   }

   @Override
   public MachineOut getMachine(String machineId) {
      return getMachine(new MachineIn(machineId));
   }

   @Override
   public MediumOut getMedium(MediumIn mIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MediumGet, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve medium : " + trans.getError());
      }

      return trans.extractItem(MediumOut.class);
   }

   @Override
   public SessionOut getSession(SessionIn sIn) {

      // TODO Auto-generated method stub
      throw new HyperboxRuntimeException("Not implemented");
   }

   @Override
   public StorageControllerSubTypeOut getStorageControllerSubType(StorageControllerSubTypeIn scstIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerSubTypeGet, scstIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve Storage Controller SubType [" + scstIn.getId() + "] : " + trans.getError());
      }

      StorageControllerSubTypeOut scstOut = trans.extractItem(StorageControllerSubTypeOut.class);
      return scstOut;
   }

   @Override
   public StorageControllerTypeOut getStorageControllerType(StorageControllerTypeIn sctIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerTypeGet, sctIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve Storage Controller Type [" + sctIn.getId() + "] : " + trans.getError());
      }

      StorageControllerTypeOut sctOut = trans.extractItem(StorageControllerTypeOut.class);
      return sctOut;
   }

   @Override
   public StoreOut getStore(StoreIn sIn) {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.StoreGet, sIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores : " + trans.getError());
      }
      return trans.extractItem(StoreOut.class);
   }

   @Override
   public StoreItemOut getStoreItem(StoreItemIn siIn) {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.StoreItemGet, siIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores : " + trans.getError());
      }

      return trans.getBody().poll().get(StoreItemOut.class);
   }

   private Transaction getTransaction(Request req) {

      if ((backend == null) || !backend.isConnected() || (getId() == null)) {
         throw new HyperboxRuntimeException("Not connected to the server");
      }

      if (!req.has(ServerIn.class)) {
         req.set(new ServerIn(id));
      }
      Transaction t = new Transaction(backend, req);
      ansRecv.put(req.getExchangeId(), t);
      return t;
   }

   @Override
   public UserOut getUser(UserIn uIn) {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.UserGet, uIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve user : " + trans.getError());
      }

      return trans.extractItem(UserOut.class);
   }

   @Override
   public List<StorageDeviceAttachmentOut> listAttachments(StorageControllerIn scIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerMediumAttachmentList, scIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of attachments : " + trans.getError());
      }

      List<StorageDeviceAttachmentOut> attachList = trans.extractItems(StorageDeviceAttachmentOut.class);
      return attachList;
   }

   @Override
   public List<String> listKeyboardMode(MachineIn mIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.KeyboardModeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Keyboard Types : " + trans.getError());
      }

      List<String> keyboardMode = new ArrayList<String>();
      for (Answer ans : trans.getBody()) {
         keyboardMode.add((String) ans.get(CommObjets.KeyboardMode));
      }
      return keyboardMode;
   }

   @Override
   public List<MachineOut> listMachines() {

      Transaction t = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachineList));
      if (!t.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of machines: " + t.getError());
      }

      List<MachineOut> objOutList = t.extractItems(MachineOut.class);
      return objOutList;
   }

   @Override
   public List<MediumOut> listMediums() {

      Transaction t = getTransaction(new Request(Command.VBOX, HypervisorTasks.MediumList));
      if (!t.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of mediums: " + t.getError());
      }

      List<MediumOut> objOutList = t.extractItems(MediumOut.class);
      return objOutList;
   }

   @Override
   public List<String> listMouseMode(MachineIn mIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MouseModeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Mouse Types : " + trans.getError());
      }

      List<String> keyboardMode = new ArrayList<String>();
      for (Answer ans : trans.getBody()) {
         keyboardMode.add((String) ans.get(CommObjets.MouseMode));
      }
      return keyboardMode;
   }

   @Override
   public List<OsTypeOut> listOsType() {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.OsTypeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of OS : " + trans.getError());
      }

      List<OsTypeOut> osOutList = new ArrayList<OsTypeOut>();
      for (Answer ans : trans.getBody()) {
         osOutList.add(ans.get(OsTypeOut.class));
      }
      return osOutList;
   }

   @Override
   public List<OsTypeOut> listOsType(MachineIn mIn) {

      return listOsType();
   }

   @Override
   public List<SessionOut> listSessions() {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.SessionList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of sessions : " + trans.getError());
      }

      List<SessionOut> objOutList = trans.extractItems(SessionOut.class);
      return objOutList;
   }

   @Override
   public List<StorageControllerSubTypeOut> listStorageControllerSubType(StorageControllerTypeIn sctIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerSubTypeList, sctIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Storage Controller Sub types : " + trans.getError());
      }

      List<StorageControllerSubTypeOut> objOutList = trans.extractItems(StorageControllerSubTypeOut.class);
      return objOutList;
   }

   @Override
   public List<StorageControllerTypeOut> listStorageControllerType() {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerTypeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Storage Controller Types : " + trans.getError());
      }

      List<StorageControllerTypeOut> objOutList = trans.extractItems(StorageControllerTypeOut.class);
      return objOutList;
   }

   @Override
   public List<StoreItemOut> listStoreItems(StoreIn sIn) {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.StoreItemList, sIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores Items : " + trans.getError());
      }

      List<StoreItemOut> objOutList = trans.extractItems(StoreItemOut.class);
      return objOutList;
   }

   @Override
   public List<StoreItemOut> listStoreItems(StoreIn sIn, StoreItemIn siIn) {

      Request req = new Request(Command.HBOX, HyperboxTasks.StoreItemList);
      req.set(sIn);
      req.set(siIn);
      Transaction trans = getTransaction(req);
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores Items : " + trans.getError());
      }

      List<StoreItemOut> objOutList = trans.extractItems(StoreItemOut.class);
      return objOutList;
   }

   @Override
   public List<StoreOut> listStores() {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.StoreList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores : " + trans.getError());
      }

      List<StoreOut> objOutList = trans.extractItems(StoreOut.class);
      return objOutList;
   }

   @Override
   public List<TaskOut> listTasks() {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.TaskList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of tasks : " + trans.getError());
      }

      List<TaskOut> objOutList = trans.extractItems(TaskOut.class);
      return objOutList;
   }

   @Override
   public List<UserOut> listUsers() {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.UserList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of users : " + trans.getError());
      }

      List<UserOut> objOutList = trans.extractItems(UserOut.class);
      return objOutList;
   }

   @Override
   public List<NetworkInterfaceOut> listNetworkInterfaces(MachineIn mIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.NetworkInterfaceList, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Network interfaces for [" + mIn + "] : " + trans.getError());
      }

      List<NetworkInterfaceOut> objOutList = trans.extractItems(NetworkInterfaceOut.class);
      return objOutList;
   }

   @Override
   public List<NetworkAttachModeOut> listNetworkAttachModes() {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.NetworkAttachModeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Network Attach modes : " + trans.getError());
      }

      List<NetworkAttachModeOut> objOutList = trans.extractItems(NetworkAttachModeOut.class);
      return objOutList;
   }

   @Override
   public List<NetworkAttachNameOut> listNetworkAttachNames(NetworkAttachModeIn namIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.NetworkAttachNameList, namIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Network Attach modes : " + trans.getError());
      }

      List<NetworkAttachNameOut> objOutList = trans.extractItems(NetworkAttachNameOut.class);
      return objOutList;
   }

   @Override
   public List<NetworkInterfaceTypeOut> listNetworkInterfaceTypes() {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.NetworkAdapterTypeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Network Interface types : " + trans.getError());
      }

      List<NetworkInterfaceTypeOut> objOutList = trans.extractItems(NetworkInterfaceTypeOut.class);
      return objOutList;
   }

   @Override
   public TaskOut getTask(TaskIn tIn) {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.TaskGet, tIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to get Task: " + trans.getError());
      }
      TaskOut tOut = trans.extractItem(TaskOut.class);
      return tOut;
   }

   @Override
   public SnapshotOut getRootSnapshot(String vmId) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.SnapshotGetRoot, new MachineIn(vmId)));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve snapshot information: " + trans.getError());
      }

      SnapshotOut snapOut = trans.extractItem(SnapshotOut.class);
      return snapOut;
   }

   @Override
   public SnapshotOut getSnapshot(MachineIn mIn, SnapshotIn snapIn) {

      Request req = new Request(Command.VBOX, HypervisorTasks.SnapshotGet);
      req.set(mIn);
      req.set(snapIn);
      Transaction trans = getTransaction(req);
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve snapshot information: " + trans.getError());
      }

      SnapshotOut snapOut = trans.extractItem(SnapshotOut.class);
      return snapOut;
   }

   @Override
   public SnapshotOut getCurrentSnapshot(MachineIn mIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.SnapshotGetCurrent, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve snapshot information: " + trans.getError());
      }

      SnapshotOut snapOut = trans.extractItem(SnapshotOut.class);
      return snapOut;
   }

   @Override
   public _Hypervisor getHypervisor() {
      return hypReader;
   }

   @Override
   public ScreenshotOut getScreenshot(MachineIn mIn) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachineDisplayGetScreenshot, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve screenshot information: " + trans.getError());
      }

      ScreenshotOut screen = trans.extractItem(ScreenshotOut.class);
      return screen;
   }

   @Override
   public List<StorageDeviceAttachmentOut> listAttachments(String machineUuid) {

      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerMediumAttachmentList, new MachineIn(machineUuid)));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Attachment : " + trans.getError());
      }

      List<StorageDeviceAttachmentOut> objOutList = trans.extractItems(StorageDeviceAttachmentOut.class);
      return objOutList;
   }

   @Override
   public boolean isHypervisorConnected() {
      return isHypConnected;
   }

   @Override
   public List<HypervisorLoaderOut> listHypervisors() {
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.HypervisorList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to list hypervisors: " + trans.getError());
      }

      return trans.extractItems(HypervisorLoaderOut.class);
   }

   @Override
   public void putAnswer(Answer ans) {
      Logger.error("Oprhan answer: " + ans);
   }

   private void refreshInfo() {
      Transaction srvTrans = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.ServerGet));
      if (!srvTrans.sendAndWait()) {
         disconnect();
         throw new HyperboxRuntimeException("Unable to retrieve server information: " + srvTrans.getError());
      }
      ServerOut srvOut = srvTrans.extractItem(ServerOut.class);

      id = srvOut.getId();
      name = srvOut.getName();
      type = srvOut.getType();
      version = srvOut.getVersion();
      protocolVersion = srvOut.getNetworkProtocolVersion();
      isHypConnected = srvOut.isHypervisorConnected();
      logLevel = srvOut.getLogLevel();
      if (isHypConnected && (hypReader == null)) {
         hypReader = new Hypervisor(this);
      }
      if (!isHypConnected && (hypReader != null)) {
         hypReader = null;
      }

      EventManager.post(new ServerModifiedEvent(ServerIoFactory.get(this)));
   }

   @Override
   public void connect(String address, String backendId, UserIn usrIn) {

      setState(ConnectionState.Connecting);

      ansRecv = new HashMap<String, _AnswerReceiver>();
      try {
         EventManager.register(this);
         backend = BackendFactory.get(backendId);
         backend.start();
         backend.connect(address);
         Logger.info("Connected to Hyperbox Server");

         Transaction helloTrans = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.Hello));
         if (!helloTrans.sendAndWait()) {
            throw new HyperboxException("Unable to welcome the server : " + helloTrans.getError());
         }

         if (AxBooleans.get(Configuration.getSetting(CFGKEY_SERVER_VALIDATE, CFGVAL_SERVER_VALIDATE))) {
            HelloOut helloOut = helloTrans.extractItem(HelloOut.class);
            if (helloOut == null) {
               throw new HyperboxRuntimeException("Server did not present itself, will disconnect");
            }
            Logger.info("Server Network Protocol Version: " + helloOut.getProtocolVersion());

            if (AxBooleans.get(Configuration.getSetting(CFGKEY_SERVER_VALIDATE_VERSION, CFGVAL_SERVER_VALIDATE_VERSION))) {
               if (!HyperboxAPI.getProtocolVersion().isCompatible(helloOut.getProtocolVersion())) {
                  throw new HyperboxRuntimeException("Client and Server Network protocol do not match, cannot connect: Client version is "
                        + HyperboxAPI.getProtocolVersion() + " and Server version is " + helloOut.getProtocolVersion());
               }
            }
         }

         Transaction loginTrans = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.Login, usrIn));
         if (!loginTrans.sendAndWait()) {
            Logger.error("Login failure : " + loginTrans.getError());
            disconnect();
            throw new HyperboxException("Login failure : " + loginTrans.getError());
         } else {
            Logger.info("Authentication successfull");
         }

         refreshInfo();

         setState(ConnectionState.Connected);
         EventManager.post(new ServerConnectedEvent(ServerIoFactory.get(this)));
      } catch (Throwable e) {
         Logger.exception(e);
         disconnect();
         throw new HyperboxRuntimeException(e);
      }
   }

   @Override
   public void disconnect() {

      if (!getState().equals(ConnectionState.Disconnected)) {
         setState(ConnectionState.Disconnecting);
         try {
            if ((backend != null) && backend.isConnected()) {
               Transaction logOffTrans = getTransaction(new Request(Command.HBOX, HyperboxTasks.Logout));
               if (!logOffTrans.sendAndWait()) {
                  Logger.warning("Couldn't logout from the server before disconnecting: " + logOffTrans.getError());
               } else {
                  Logger.verbose("Successful logout from server");
               }
               backend.disconnect();
               backend.stop();
            }
         } catch (Throwable t) {
            Logger.error("Error in backend when trying to disconnect: " + t.getMessage());
            Logger.exception(t);
         }
         backend = null;
         ansRecv = null;
         setState(ConnectionState.Disconnected);
         EventManager.post(new ServerDisconnectedEvent(ServerIoFactory.get(this)));
         EventManager.unregister(this);
      }

   }

   @Override
   public boolean isConnected() {
      return state.equals(ConnectionState.Connected);
   }

   @Handler
   protected final void putBackendConnectionStateEvent(BackendConnectionStateEvent ev) {

      if (ev.getBackend().equals(backend)) {
         Logger.debug("Server " + getName() + " got backend event: " + ev.getEventId());
         if (!backend.isConnected() && isConnected()) {
            Logger.debug("and we are disconnecting");
            disconnect();
         }
      }
   }

   @Handler
   protected void putHypervisorEvent(HypervisorEventOut ev) {

      if (id.equals(ev.getServerId())) {
         refreshInfo();
      }
   }

   @Handler
   protected final void putServerShutdownEvent(ServerShutdownEventOut ev) {

      if (id.equals(ev.getServerId())) {
         disconnect();
      }
   }

   @Handler
   private void putServerPropertyChanged(ServerPropertyChangedEventOut ev) {

      if (id.equals(ev.getServerId())) {
         refreshInfo();
      }
   }

   @Override
   public _GuestReader getGuest(String machineUuid) {

      // TODO Add machine validation
      return new GuestReader(this, machineUuid);
   }

   @Override
   public List<PermissionOut> listPermissions(UserIn usrIn) {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.PermissionList, usrIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of permissions : " + trans.getError());
      }

      List<PermissionOut> permOutList = trans.extractItems(PermissionOut.class);
      return permOutList;
   }

   @Override
   public HostOut getHost() {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.HostGet));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve host information: " + trans.getError());
      }

      HostOut objOut = trans.extractItem(HostOut.class);
      return objOut;
   }

   @Override
   public SnapshotOut getSnapshot(String vmUuid, String snapUuid) {
      return getSnapshot(new MachineIn(vmUuid), new SnapshotIn(snapUuid));
   }

   @Override
   public SnapshotOut getCurrentSnapshot(String vmUuid) {
      return getCurrentSnapshot(new MachineIn(vmUuid));
   }

   @Override
   public List<ModuleOut> listModules() {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.ModuleList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of modules : " + trans.getError());
      }

      List<ModuleOut> objOutList = trans.extractItems(ModuleOut.class);
      return objOutList;
   }

   @Override
   public ModuleOut getModule(String modId) {

      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.ModuleGet, new ModuleIn(modId)));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve module information: " + trans.getError());
      }

      ModuleOut objOut = trans.extractItem(ModuleOut.class);
      return objOut;
   }

   @Override
   public Set<String> listLogLevel() {
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.ServerLogLevelList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve Log levels : " + trans.getError());
      }

      List<String> objOutList = trans.extractItems(String.class);
      return new HashSet<String>(objOutList);
   }

   @Override
   public _Machine getMachineReader(String id) {
      return new Machine(this, id);
   }

   @Override
   public _Task getTask(String id) {
      return new Task(this, id);
   }

}
