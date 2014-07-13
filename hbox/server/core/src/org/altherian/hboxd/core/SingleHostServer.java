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

package org.altherian.hboxd.core;

import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.Configuration;
import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.SecurityAction;
import org.altherian.hbox.comm.SecurityItem;
import org.altherian.hbox.comm._Client;
import org.altherian.hbox.comm.input.HypervisorInput;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.constant.ServerAttributes;
import org.altherian.hbox.constant.ServerType;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.states.ServerConnectionState;
import org.altherian.hbox.states.ServerState;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.Hyperbox;
import org.altherian.hboxd.core.action._ActionManager;
import org.altherian.hboxd.core.model.Medium;
import org.altherian.hboxd.core.model._Machine;
import org.altherian.hboxd.core.model._Medium;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.hypervisor.HypervisorDisconnectedEvent;
import org.altherian.hboxd.event.module.ModuleEvent;
import org.altherian.hboxd.event.server.ServerConnectionStateEvent;
import org.altherian.hboxd.event.server.ServerPropertyChangedEvent;
import org.altherian.hboxd.event.system.SystemStateEvent;
import org.altherian.hboxd.exception.hypervisor.HypervisorNotConnectedException;
import org.altherian.hboxd.exception.server.ServerLogLevelInvalidException;
import org.altherian.hboxd.exception.server.ServerNotFoundException;
import org.altherian.hboxd.factory.MachineFactory;
import org.altherian.hboxd.factory.ModuleManagerFactory;
import org.altherian.hboxd.factory.SecurityManagerFactory;
import org.altherian.hboxd.front._RequestReceiver;
import org.altherian.hboxd.host.Host;
import org.altherian.hboxd.host._Host;
import org.altherian.hboxd.hypervisor.Hypervisor;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.module._ModuleManager;
import org.altherian.hboxd.persistence._Persistor;
import org.altherian.hboxd.persistence.sql.h2.H2SqlPersistor;
import org.altherian.hboxd.security.SecurityContext;
import org.altherian.hboxd.security._SecurityManager;
import org.altherian.hboxd.server._Server;
import org.altherian.hboxd.server._ServerManager;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.hboxd.session.SessionManager;
import org.altherian.hboxd.session._SessionManager;
import org.altherian.hboxd.store.StoreManager;
import org.altherian.hboxd.store._StoreManager;
import org.altherian.hboxd.task.TaskManager;
import org.altherian.hboxd.task._TaskManager;
import org.altherian.tool.AxBooleans;
import org.altherian.tool.AxSystems;
import org.altherian.tool.logging.LogLevel;
import org.altherian.tool.logging.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.io.Files;

public class SingleHostServer implements _Hyperbox, _Server {
   
   private ServerState state;
   
   private _SessionManager sessMgr;
   private _SecurityManager secMgr;
   private _ActionManager actionMgr;
   private _TaskManager taskMgr;
   private _StoreManager storeMgr;
   private _Persistor persistor;
   private _ModuleManager modMgr;
   
   private Map<String, Class<? extends _Hypervisor>> hypervisors;
   private _Hypervisor hypervisor;
   
   private _Client system = new System();
   
   private String id;
   private String name;
   
   private void setState(ServerState state) {
      if (this.state != state) {
         this.state = state;
         EventManager.post(new SystemStateEvent(state));
      }
   }
   
   public ServerState getState() {
      return state;
   }
   
   private void loadPersistors() throws HyperboxException {
      Logger.track();
      
      persistor = HBoxServer.loadClass(_Persistor.class, Configuration.getSetting(CFGKEY_CORE_PERSISTOR_CLASS, H2SqlPersistor.class.getName()));
      persistor.init();
   }
   
   @Override
   public void init() throws HyperboxException {
      Logger.track();
      
      SessionContext.setClient(system);
      
      HBoxServer.initServer(this);
      
      EventManager.start();
      EventManager.register(this);
      
      loadPersistors();
      
      secMgr = SecurityManagerFactory.get();
      SecurityContext.setAdminUser(secMgr.init(persistor));
      
      actionMgr = new DefaultActionManager();
      taskMgr = new TaskManager();
      
      storeMgr = new StoreManager();
      storeMgr.init(persistor);
      
      sessMgr = new SessionManager();
      
      modMgr = ModuleManagerFactory.get();
   }
   
   @Override
   public void start() throws HyperboxException {
      Logger.track();
      
      setState(ServerState.Starting);
      
      persistor.start();
      HBoxServer.initPersistor(persistor);
      
      if (!HBoxServer.hasSetting(CFGKEY_SRV_ID)) {
         Logger.verbose("Generating new Server ID");
         HBoxServer.setSetting(CFGKEY_SRV_ID, UUID.randomUUID());
         Logger.verbose("Generating default Server name");
         HBoxServer.setSetting(CFGKEY_SRV_NAME, AxSystems.getHostname());
      }
      id = HBoxServer.getSettingOrFail(CFGKEY_SRV_ID);
      name = HBoxServer.getSettingOrFail(CFGKEY_SRV_NAME);
      
      Logger.info("Server ID: " + id);
      Logger.info("Server Name: " + name);
      
      actionMgr.start();
      secMgr.start();
      SecurityContext.initSecurityManager(secMgr);
      taskMgr.start(this);
      sessMgr.start(this);
      storeMgr.start();
      modMgr.start();
      
      loadHypervisors();
      
      if (HBoxServer.hasSetting(CFGKEY_CORE_HYP_ID)) {
         Logger.info("Loading Hypervisor configuration");
         HypervisorInput in = new HypervisorInput(HBoxServer.getSetting(CFGKEY_CORE_HYP_ID));
         in.setConnectionOptions(HBoxServer.getSetting(CFGKEY_CORE_HYP_OPTS));
         in.setAutoConnect(AxBooleans.get(HBoxServer.getSetting(CFGKEY_CORE_HYP_AUTO)));
         Logger.info("Hypervisor ID: " + in.getId());
         Logger.info("Hypervisor options: " + in.getConnectOptions());
         Logger.info("Hypervisor AutoConnect: " + in.getAutoConnect());
         if (in.getAutoConnect()) {
            Logger.info("Hyperbox will auto-connect to the Hypervisor");
            taskMgr.process(new Request(Command.HBOX, HyperboxTasks.HypervisorConnect, in));
         } else {
            Logger.info("Hypervisor is not set to auto-connect, skipping");
         }
      } else {
         Logger.info("No Hypervisor configuration found, skipping");
      }
      
      setState(ServerState.Running);
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      secMgr.authorize(SecurityItem.Server, SecurityAction.Stop);
      
      setState(ServerState.Stopping);
      
      if (modMgr != null) {
         modMgr.stop();
      }
      if (sessMgr != null) {
         sessMgr.stop();
      }
      if (taskMgr != null) {
         taskMgr.stop();
      }
      
      if (isConnected()) {
         disconnect();
      }
      if (secMgr != null) {
         secMgr.stop();
      }
      if (actionMgr != null) {
         actionMgr.stop();
      }
      if (storeMgr != null) {
         storeMgr.stop();
      }
      
      if (persistor != null) {
         persistor.stop();
      }
      
      hypervisors = null;
      setState(ServerState.Stopped);
      EventManager.stop();
   }
   
   @Override
   public _RequestReceiver getReceiver() {
      return sessMgr;
   }
   
   @Override
   public _TaskManager getTaskManager() {
      return taskMgr;
   }
   
   @Override
   public _SessionManager getSessionManager() {
      return sessMgr;
   }
   
   @Override
   public _SecurityManager getSecurityManager() {
      return secMgr;
   }
   
   @Override
   public _ActionManager getActionManager() {
      return actionMgr;
   }
   
   @Override
   public _StoreManager getStoreManager() {
      return storeMgr;
   }
   
   @Override
   public _ModuleManager getModuleManager() {
      return modMgr;
   }
   
   @Override
   public _ServerManager getServerManager() {
      return this;
   }
   
   @Override
   public _Persistor getPersistor() {
      return persistor;
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
   public void setName(String name) {
      Logger.track();
      
      HBoxServer.setSetting(CFGKEY_SRV_NAME, name);
      this.name = name;
      EventManager.post(new ServerPropertyChangedEvent(this, ServerAttributes.Name, name));
   }
   
   @Override
   public ServerType getType() {
      return ServerType.Host;
   }
   
   @Override
   public String getVersion() {
      return Hyperbox.getVersionFull();
   }
   
   private void loadHypervisors() throws HyperboxException {
      Logger.track();
      
      Map<String, Class<? extends _Hypervisor>> hyps = new HashMap<String, Class<? extends _Hypervisor>>();
      Set<Class<? extends _Hypervisor>> subTypes = HBoxServer.getAnnotatedSubTypes(_Hypervisor.class, Hypervisor.class);
      
      for (Class<? extends _Hypervisor> hypLoader : subTypes) {
         for (String scheme : hypLoader.getAnnotation(Hypervisor.class).schemes()) {
            try {
               hyps.put(scheme, hypLoader);
               Logger.verbose("Loaded " + hypLoader.getSimpleName() + " for " + scheme + " scheme");
            } catch (Exception e) {
               throw new HyperboxException("Failed to load Hypervior Class : " + e.getLocalizedMessage(), e);
            }
         }
      }
      
      hypervisors = hyps;
   }
   
   @Override
   public void connect(String hypervisorId, String options) {
      secMgr.authorize(SecurityItem.Hypervisor, SecurityAction.Connect);
      
      if (!hypervisors.containsKey(hypervisorId)) {
         throw new HyperboxRuntimeException("No Hypervisor is registered under this ID: " + hypervisorId);
      }
      
      try {
         Class<? extends _Hypervisor> hypClass = hypervisors.get(hypervisorId);
         Logger.debug("Loading " + hypClass.getName() + " using " + hypClass.getClassLoader().getClass().getName());
         _Hypervisor hypervisor = hypervisors.get(hypervisorId).newInstance();
         hypervisor.setEventManager(EventManager.get());
         hypervisor.start(options);
         this.hypervisor = hypervisor;
         HBoxServer.setSetting(CFGKEY_CORE_HYP_ID, hypervisorId);
         HBoxServer.setSetting(CFGKEY_CORE_HYP_OPTS, options);
         HBoxServer.setSetting(CFGKEY_CORE_HYP_AUTO, 1);
         EventManager.post(new ServerConnectionStateEvent(this, ServerConnectionState.Connected));
      } catch (IllegalArgumentException e) {
         throw new HyperboxRuntimeException("Hypervisor cannot be loaded due to bad format: " + e.getMessage(), e);
      } catch (SecurityException e) {
         throw new HyperboxRuntimeException("Hypervisor cannot be loaded due to bad format: " + e.getMessage(), e);
      } catch (InstantiationException e) {
         throw new HyperboxRuntimeException("Hypervisor cannot be loaded due to bad format: " + e.getMessage(), e);
      } catch (IllegalAccessException e) {
         throw new HyperboxRuntimeException("Hypervisor cannot be loaded due to bad format: " + e.getMessage(), e);
      }
   }
   
   @Override
   public void disconnect() {
      secMgr.authorize(SecurityItem.Hypervisor, SecurityAction.Disconnect);
      
      if (isConnected()) {
         hypervisor.stop();
         hypervisor = null;
         EventManager.post(new ServerConnectionStateEvent(this, ServerConnectionState.Disconnected));
      }
   }
   
   @Override
   public boolean isConnected() {
      return ((hypervisor != null) && hypervisor.isRunning());
   }
   
   @Override
   public _Hypervisor getHypervisor() {
      if (!isConnected()) {
         throw new HypervisorNotConnectedException();
      }
      
      return hypervisor;
   }
   
   @Override
   public _Server getServer() {
      return this;
   }
   
   @Override
   public _Server getServer(String uuid) {
      if (!uuid.contentEquals(getId())) {
         throw new ServerNotFoundException(uuid);
      }
      return this;
   }
   
   @Override
   public List<_Server> listServer() {
      return Arrays.asList((_Server) this);
   }
   
   @Override
   public List<Class<? extends _Hypervisor>> listHypervisors() {
      return new ArrayList<Class<? extends _Hypervisor>>(hypervisors.values());
   }
   
   private class System implements _Client {
      
      @Override
      public void putAnswer(Answer ans) {
         // we ignore this
      }
      
      @Override
      public String getId() {
         return "System";
      }
      
      @Override
      public String getAddress() {
         return "";
      }
      
      @Override
      public void post(EventOutput evOut) {
         Logger.verbose("[ Event ] " + evOut);
      }
      
   }
   
   @Override
   public List<_Machine> listMachines() {
      if (!isConnected()) {
         throw new HyperboxRuntimeException("Server is not connected");
      }
      
      List<_Machine> vms = new ArrayList<_Machine>();
      for (_RawVM rawVm : hypervisor.listMachines()) {
         if (secMgr.isAuthorized(SecurityItem.Machine, SecurityAction.List, rawVm.getUuid())) {
            vms.add(MachineFactory.get(this, hypervisor, rawVm));
         }
      }
      
      return vms;
   }
   
   @Override
   public _Machine getMachine(String id) {
      secMgr.authorize(SecurityItem.Machine, SecurityAction.Get, id);
      return MachineFactory.get(this, hypervisor, hypervisor.getMachine(id));
   }
   
   @Override
   public _Machine findMachine(String idOrName) {
      return getMachine(idOrName);
   }
   
   @Override
   public void unregisterMachine(String id) {
      hypervisor.unregisterMachine(id);
   }
   
   @Override
   public void deleteMachine(String id) {
      hypervisor.deleteMachine(id);
   }
   
   @Override
   public _Medium createMedium(String location, String format, Long logicalSize) {
      Logger.debug("Creating a new hard disk at location [" + location + "] with format [" + format + "] and size ["
            + logicalSize + "]");
      Logger.debug("File extension: " + Files.getFileExtension(location));
      if (Files.getFileExtension(location).isEmpty()) {
         Logger.debug("Will add extention to filename: " + format.toLowerCase());
         location = location + "." + format.toLowerCase();
      } else {
         Logger.debug("No need to add extension");
      }
      _RawMedium rawMed = hypervisor.createHardDisk(location, format, logicalSize);
      _Medium med = new Medium(this, hypervisor, rawMed);
      return med;
   }
   
   @Override
   public _Medium createMedium(String vmId, String filename, String format, Long logicalSize) {
      if (!new File(filename).isAbsolute()) {
         filename = getMachine(vmId).getLocation() + "/" + filename;
      }
      return createMedium(filename, format, logicalSize);
   }
   
   @Override
   public _Medium getMedium(String medId) {
      return new Medium(this, hypervisor, hypervisor.getMedium(medId));
   }
   
   @Override
   public _Host getHost() {
      if (!isConnected()) {
         throw new HyperboxRuntimeException("Hypervisor is not connected - cannot retrieve host information");
      }
      
      return new Host(hypervisor.getHost());
   }
   
   @Handler
   protected void putHypervisorDisconnectEvent(HypervisorDisconnectedEvent ev) {
      Logger.track();
      
      if (ev.getHypervisor().equals(hypervisor) && isConnected()) {
         Logger.debug("Hypervisor disconnected, cleaning up");
         disconnect();
      }
   }
   
   @Handler
   protected void putModuleEvent(ModuleEvent ev) {
      Logger.track();
      
      try {
         loadHypervisors();
      } catch (HyperboxException e) {
         Logger.error("Error when trying to refresh hypervisors: " + e.getMessage());
         Logger.exception(e);
      }
   }
   
   @Override
   public String getLogLevel() {
      return Logger.getLevel().toString();
   }
   
   @Override
   public Set<String> listLogLevel() {
      Set<String> levels = new HashSet<String>();
      for (LogLevel level : LogLevel.values()) {
         levels.add(level.toString());
      }
      return levels;
   }
   
   @Override
   public void setLogLevel(String logLevel) throws ServerLogLevelInvalidException {
      
      try {
         Logger.setLevel(LogLevel.valueOf(logLevel));
         EventManager.post(new ServerPropertyChangedEvent(this, ServerAttributes.LogLevel, logLevel));
      } catch (IllegalArgumentException e) {
         throw new ServerLogLevelInvalidException(logLevel);
      }
   }
   
   @Override
   public void save() {
      // stub, nothing to save for now
   }
   
}
