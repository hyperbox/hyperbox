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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.core.server;

import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.CommObjets;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.RequestProcessType;
import org.altherian.hbox.comm._AnswerReceiver;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.MediumInput;
import org.altherian.hbox.comm.input.NetworkAttachModeInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.input.SessionInput;
import org.altherian.hbox.comm.input.SnapshotInput;
import org.altherian.hbox.comm.input.StorageControllerInput;
import org.altherian.hbox.comm.input.StorageControllerSubTypeInput;
import org.altherian.hbox.comm.input.StorageControllerTypeInput;
import org.altherian.hbox.comm.input.StoreInput;
import org.altherian.hbox.comm.input.StoreItemInput;
import org.altherian.hbox.comm.input.TaskInput;
import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.SessionOutput;
import org.altherian.hbox.comm.output.StoreItemOutput;
import org.altherian.hbox.comm.output.StoreOutput;
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineDataChangeEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineStateEventOutput;
import org.altherian.hbox.comm.output.event.server.ServerShutdownEventOutput;
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
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.back._Backend;
import org.altherian.hboxc.comm.io.factory.ServerIoFactory;
import org.altherian.hboxc.comm.utils.Transaction;
import org.altherian.hboxc.event.CoreEventManager;
import org.altherian.hboxc.event.backend.BackendConnectionStateEvent;
import org.altherian.hboxc.event.machine.MachineDataChangedEvent;
import org.altherian.hboxc.event.machine.MachineStateChangedEvent;
import org.altherian.hboxc.event.server.ServerConnectedEvent;
import org.altherian.hboxc.event.server.ServerDisconnectedEvent;
import org.altherian.hboxc.event.server.ServerModifiedEvent;
import org.altherian.hboxc.factory.BackendFactory;
import org.altherian.hboxc.server._GuestReader;
import org.altherian.hboxc.server._HypervisorReader;
import org.altherian.hboxc.server._Server;
import org.altherian.hboxc.state.ConnectionState;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperboxServer implements _Server, _AnswerReceiver {
   
   private Map<String, _AnswerReceiver> ansRecv;
   private _Backend backend;
   
   private String id;
   private String name;
   private String type;
   private String version;
   private boolean isHypConnected = false;
   
   private ConnectionState state = ConnectionState.Disconnected;
   private _HypervisorReader hypReader;
   
   private void setState(ConnectionState state) {
      if (!this.state.equals(state)) {
         this.state = state;
      } else {
         Logger.debug("Ignoring setState() - same as current");
      }
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
   public MachineOutput createMachine(MachineInput mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public MachineOutput registerMachine(MachineInput mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public MachineOutput modifyMachine(MachineInput mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public MachineOutput unregisterMachine(MachineInput mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public MachineOutput deleteMachine(MachineInput mIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public void startMachine(MachineInput mIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachinePowerOn, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to start the VM : " + trans.getError());
      }
   }
   
   @Override
   public void stopMachine(MachineInput mIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachinePowerOff, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to stop the VM : " + trans.getError());
      }
   }
   
   @Override
   public void acpiPowerMachine(MachineInput mIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachineAcpiPowerButton, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to send ACPI Power signal to the VM : " + trans.getError());
      }
   }
   
   @Override
   public UserOutput addUser(UserInput uIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public UserOutput modifyUser(UserInput uIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public UserOutput deleteUser(UserInput uIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public SessionOutput closeSession(SessionInput sIn) {
      // TODO To implement
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public MachineOutput getMachine(MachineInput mIn) {
      Logger.track();
      
      Transaction t = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachineGet, mIn));
      if (!t.sendAndWait()) {
         throw new HyperboxRuntimeException(t.getError());
      }
      
      MachineOutput mOut = t.extractItem(MachineOutput.class);
      return mOut;
   }
   
   @Override
   public MachineOutput getMachine(String machineId) {
      return getMachine(new MachineInput(machineId));
   }
   
   @Override
   public MediumOutput getMedium(MediumInput mIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MediumGet, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve medium : " + trans.getError());
      }
      
      return trans.extractItem(MediumOutput.class);
   }
   
   @Override
   public SessionOutput getSession(SessionInput sIn) {
      Logger.track();
      
      // TODO Auto-generated method stub
      throw new HyperboxRuntimeException("Not implemented");
   }
   
   @Override
   public StorageControllerSubTypeOutput getStorageControllerSubType(StorageControllerSubTypeInput scstIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerSubTypeGet, scstIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve Storage Controller SubType [" + scstIn.getId() + "] : " + trans.getError());
      }
      
      StorageControllerSubTypeOutput scstOut = trans.extractItem(StorageControllerSubTypeOutput.class);
      return scstOut;
   }
   
   @Override
   public StorageControllerTypeOutput getStorageControllerType(StorageControllerTypeInput sctIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerTypeGet, sctIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve Storage Controller Type [" + sctIn.getId() + "] : " + trans.getError());
      }
      
      StorageControllerTypeOutput sctOut = trans.extractItem(StorageControllerTypeOutput.class);
      return sctOut;
   }
   
   @Override
   public StoreOutput getStore(StoreInput sIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.StoreGet, sIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores : " + trans.getError());
      }
      return trans.extractItem(StoreOutput.class);
   }
   
   @Override
   public StoreItemOutput getStoreItem(StoreItemInput siIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.StoreItemGet, siIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores : " + trans.getError());
      }
      
      return trans.getBody().poll().get(StoreItemOutput.class);
   }
   
   private Transaction getTransaction(Request req) {
      Logger.track();
      
      if ((backend == null) || !backend.isConnected() || (getId() == null)) {
         throw new HyperboxRuntimeException("Not connected to the server");
      }
      req.set(new ServerInput(id));
      Transaction t = new Transaction(backend, req);
      ansRecv.put(req.getExchangeId(), t);
      return t;
   }
   
   @Override
   public UserOutput getUser(UserInput uIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.UserGet, uIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve user : " + trans.getError());
      }
      
      return trans.extractItem(UserOutput.class);
   }
   
   @Override
   public List<StorageDeviceAttachmentOutput> listAttachments(StorageControllerInput scIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerMediumAttachmentList, scIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of attachments : " + trans.getError());
      }
      
      List<StorageDeviceAttachmentOutput> attachList = trans.extractItems(StorageDeviceAttachmentOutput.class);
      return attachList;
   }
   
   @Override
   public List<String> listKeyboardMode(MachineInput mIn) {
      Logger.track();
      
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
   public List<MachineOutput> listMachines() {
      Logger.track();
      
      Transaction t = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachineList));
      if (!t.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of machines: " + t.getError());
      }
      
      List<MachineOutput> mOutList = t.extractItems(MachineOutput.class);
      return mOutList;
   }
   
   @Override
   public List<MediumOutput> listMediums() {
      Logger.track();
      
      Transaction t = getTransaction(new Request(Command.VBOX, HypervisorTasks.MediumList));
      if (!t.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of mediums: " + t.getError());
      }
      
      List<MediumOutput> medOutList = t.extractItems(MediumOutput.class);
      return medOutList;
   }
   
   @Override
   public List<String> listMouseMode(MachineInput mIn) {
      Logger.track();
      
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
   public List<OsTypeOutput> listOsType() {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.OsTypeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of OS : " + trans.getError());
      }
      
      List<OsTypeOutput> osOutList = new ArrayList<OsTypeOutput>();
      for (Answer ans : trans.getBody()) {
         osOutList.add(ans.get(OsTypeOutput.class));
      }
      return osOutList;
   }
   
   @Override
   public List<OsTypeOutput> listOsType(MachineInput mIn) {
      Logger.track();
      
      return listOsType();
   }
   
   @Override
   public List<SessionOutput> listSessions() {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.SessionList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of sessions : " + trans.getError());
      }
      
      List<SessionOutput> sOutList = trans.extractItems(SessionOutput.class);
      return sOutList;
   }
   
   @Override
   public List<StorageControllerSubTypeOutput> listStorageControllerSubType(StorageControllerTypeInput sctIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerSubTypeList, sctIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Storage Controller Sub types : " + trans.getError());
      }
      
      List<StorageControllerSubTypeOutput> scstOutList = trans.extractItems(StorageControllerSubTypeOutput.class);
      return scstOutList;
   }
   
   @Override
   public List<StorageControllerTypeOutput> listStorageControllerType() {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerTypeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Storage Controller Types : " + trans.getError());
      }
      
      List<StorageControllerTypeOutput> sctOutList = trans.extractItems(StorageControllerTypeOutput.class);
      return sctOutList;
   }
   
   @Override
   public List<StoreItemOutput> listStoreItems(StoreInput sIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.StoreItemList, sIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores Items : " + trans.getError());
      }
      
      List<StoreItemOutput> storeItemList = trans.extractItems(StoreItemOutput.class);
      return storeItemList;
   }
   
   @Override
   public List<StoreItemOutput> listStoreItems(StoreInput sIn, StoreItemInput siIn) {
      Logger.track();
      
      Request req = new Request(Command.HBOX, HyperboxTasks.StoreItemList);
      req.set(sIn);
      req.set(siIn);
      Transaction trans = getTransaction(req);
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores Items : " + trans.getError());
      }
      
      List<StoreItemOutput> storeItemList = trans.extractItems(StoreItemOutput.class);
      return storeItemList;
   }
   
   @Override
   public List<StoreOutput> listStores() {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.StoreList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Stores : " + trans.getError());
      }
      
      List<StoreOutput> storeList = trans.extractItems(StoreOutput.class);
      return storeList;
   }
   
   @Override
   public List<TaskOutput> listTasks() {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.TaskList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of tasks : " + trans.getError());
      }
      
      List<TaskOutput> tOutList = trans.extractItems(TaskOutput.class);
      return tOutList;
   }
   
   @Override
   public List<UserOutput> listUsers() {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.UserList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of users : " + trans.getError());
      }
      
      List<UserOutput> userOutList = trans.extractItems(UserOutput.class);
      return userOutList;
   }
   
   @Override
   public List<NetworkInterfaceOutput> listNetworkInterfaces(MachineInput mIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.NetworkInterfaceList, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Network interfaces for [" + mIn + "] : " + trans.getError());
      }
      
      List<NetworkInterfaceOutput> nicOutList = trans.extractItems(NetworkInterfaceOutput.class);
      return nicOutList;
   }
   
   @Override
   public List<NetworkAttachModeOutput> listNetworkAttachModes() {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.NetworkAttachModeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Network Attach modes : " + trans.getError());
      }
      
      List<NetworkAttachModeOutput> namOutList = trans.extractItems(NetworkAttachModeOutput.class);
      return namOutList;
   }
   
   @Override
   public List<NetworkAttachNameOutput> listNetworkAttachNames(NetworkAttachModeInput namIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.NetworkAttachNameList, namIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Network Attach modes : " + trans.getError());
      }
      
      List<NetworkAttachNameOutput> nanOutList = trans.extractItems(NetworkAttachNameOutput.class);
      return nanOutList;
   }
   
   @Override
   public List<NetworkInterfaceTypeOutput> listNetworkInterfaceTypes() {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.NetworkAdapterTypeList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Network Interface types : " + trans.getError());
      }
      
      List<NetworkInterfaceTypeOutput> nitOutList = trans.extractItems(NetworkInterfaceTypeOutput.class);
      return nitOutList;
   }
   
   @Override
   public TaskOutput getTask(TaskInput tIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.TaskGet, tIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to get Task");
      }
      TaskOutput tOut = trans.extractItem(TaskOutput.class);
      return tOut;
   }
   
   @Override
   public SnapshotOutput getRootSnapshot(MachineInput mIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.SnapshotGetRoot, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve snapshot information: " + trans.getError());
      }
      
      SnapshotOutput snapOut = trans.extractItem(SnapshotOutput.class);
      return snapOut;
   }
   
   @Override
   public SnapshotOutput getSnapshot(MachineInput mIn, SnapshotInput snapIn) {
      Logger.track();
      
      Request req = new Request(Command.VBOX, HypervisorTasks.SnapshotGet);
      req.set(mIn);
      req.set(snapIn);
      Transaction trans = getTransaction(req);
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve snapshot information: " + trans.getError());
      }
      
      SnapshotOutput snapOut = trans.extractItem(SnapshotOutput.class);
      return snapOut;
   }
   
   @Override
   public SnapshotOutput getCurrentSnapshot(MachineInput mIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.SnapshotGetCurrent, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve snapshot information: " + trans.getError());
      }
      
      SnapshotOutput snapOut = trans.extractItem(SnapshotOutput.class);
      return snapOut;
   }
   
   @Override
   public _HypervisorReader getHypervisor() {
      return hypReader;
   }
   
   @Override
   public ScreenshotOutput getScreenshot(MachineInput mIn) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.MachineDisplayGetScreenshot, mIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve screenshot information: " + trans.getError());
      }
      
      ScreenshotOutput screen = trans.extractItem(ScreenshotOutput.class);
      return screen;
   }
   
   @Override
   public List<StorageDeviceAttachmentOutput> listAttachments(String machineUuid) {
      Logger.track();
      
      Transaction trans = getTransaction(new Request(Command.VBOX, HypervisorTasks.StorageControllerMediumAttachmentList, new MachineInput(machineUuid)));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of Attachment : " + trans.getError());
      }
      
      List<StorageDeviceAttachmentOutput> sdaOutList = trans.extractItems(StorageDeviceAttachmentOutput.class);
      return sdaOutList;
   }
   
   @Override
   public boolean isHypervisorConnected() {
      return isHypConnected;
   }
   
   @Override
   public List<HypervisorLoaderOutput> listHypervisors() {
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.HypervisorList));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to list hypervisors: " + trans.getError());
      }
      
      return trans.extractItems(HypervisorLoaderOutput.class);
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
      ServerOutput srvOut = srvTrans.extractItem(ServerOutput.class);
      id = srvOut.getId();
      name = srvOut.getName();
      type = srvOut.getType();
      version = srvOut.getVersion();
      isHypConnected = srvOut.isHypervisorConnected();
      if (isHypConnected && (hypReader == null)) {
         hypReader = new HypervisorReader(this);
      }
      if (!isHypConnected && (hypReader != null)) {
         hypReader = null;
      }
      CoreEventManager.post(new ServerModifiedEvent(ServerIoFactory.get(this)));
   }
   
   @Override
   public void connect(String address, String backendId, UserInput usrIn) {
      Logger.track();
      
      setState(ConnectionState.Connecting);
      
      ansRecv = new HashMap<String, _AnswerReceiver>();
      try {
         CoreEventManager.register(this);
         backend = BackendFactory.get(backendId);
         backend.start();
         backend.connect(address);
         Logger.info("Connected to Hyperbox Server");
         
         Transaction loginTrans = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.Login, usrIn));
         if (!loginTrans.sendAndWait()) {
            Logger.error("Login failure : " + loginTrans.getError());
            disconnect();
            throw new HyperboxException("Login failure : " + loginTrans.getError());
         } else {
            Logger.info("Authentication successfull");
         }
         Transaction helloTrans = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.Hello));
         if (!helloTrans.sendAndWait()) {
            throw new HyperboxException("Unable to welcome the server : " + helloTrans.getError());
         }
         Answer ans = helloTrans.getBody().poll();
         String version = ans.get("Hello").toString();
         Logger.debug("Got Hello packet - Version : " + version);
         
         refreshInfo();
         
         setState(ConnectionState.Connected);
         CoreEventManager.post(new ServerConnectedEvent(ServerIoFactory.get(this)));
      } catch (Throwable e) {
         Logger.exception(e);
         disconnect();
         throw new HyperboxRuntimeException(e);
      }
   }
   
   @Override
   public void disconnect() {
      Logger.track();
      
      if (!getState().equals(ConnectionState.Disconnected)) {
         setState(ConnectionState.Disconnecting);
         try {
            if ((backend != null) && backend.isConnected()) {
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
         CoreEventManager.post(new ServerDisconnectedEvent(ServerIoFactory.get(this)));
         CoreEventManager.unregister(this);
      }
      
   }
   
   @Override
   public boolean isConnected() {
      return state.equals(ConnectionState.Connected);
   }
   
   @Handler
   protected final void putBackendConnectionStateEvent(BackendConnectionStateEvent ev) {
      Logger.track();
      
      if (ev.getBackend().equals(backend)) {
         Logger.debug("Server " + getName() + " got backend event: " + ev.getEventId());
         if (!backend.isConnected() && isConnected()) {
            Logger.debug("and we are disconnecting");
            disconnect();
         }
      } else {
         Logger.debug("Got event for backend, but not ours");
      }
   }
   
   @Handler
   protected void putHypervisorEvent(HypervisorEventOutput ev) {
      
      if (id.equals(ev.getServerId())) {
         refreshInfo();
      }
   }
   
   @Handler
   protected void putBackendEvent(EventOutput ev) {
      Logger.track();
      
      if (id.equals(ev.getServerId())) {
         Logger.warning("Received Unknown Event from Server " + getId() + ": " + ev.toString());
      }
   }
   
   @Handler
   protected void putMachineDataEvent(MachineDataChangeEventOutput ev) {
      Logger.track();
      
      if (id.equals(ev.getServerId())) {
         CoreEventManager.post(new MachineDataChangedEvent(getId(), getMachine(new MachineInput(ev.getMachine()))));
      }
   }
   
   @Handler
   protected void putMachineStateEvent(MachineStateEventOutput ev) {
      Logger.track();
      
      if (id.equals(ev.getServerId())) {
         CoreEventManager.post(new MachineStateChangedEvent(getId(), ev.getMachine()));
      }
      
   }
   
   @Handler
   protected final void putServerShutdownEvent(ServerShutdownEventOutput ev) {
      Logger.track();
      
      if (id.equals(ev.getServerId())) {
         disconnect();
      }
   }
   
   @Override
   public _GuestReader getGuest(String machineUuid) {
      // TODO Add machine validation
      return new GuestReader(this, machineUuid);
   }
   
   @Override
   public List<PermissionOutput> listPermissions(UserInput usrIn) {
      Transaction trans = getTransaction(new Request(Command.HBOX, HyperboxTasks.PermissionList, usrIn));
      if (!trans.sendAndWait()) {
         throw new HyperboxRuntimeException("Unable to retrieve list of permissions : " + trans.getError());
      }
      
      List<PermissionOutput> permOutList = trans.extractItems(PermissionOutput.class);
      return permOutList;
   }
   
}
