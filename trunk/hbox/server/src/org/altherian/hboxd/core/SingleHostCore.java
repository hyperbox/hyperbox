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

import org.altherian.hbox.Configuration;
import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._Client;
import org.altherian.hbox.comm.input.HypervisorInput;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.states.ServerState;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.Hyperbox;
import org.altherian.hboxd.core.action._ActionManager;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.hypervisor.HypervisorConnectedEvent;
import org.altherian.hboxd.event.hypervisor.HypervisorDisconnectedEvent;
import org.altherian.hboxd.event.system.SystemStateEvent;
import org.altherian.hboxd.exception.ServerNotFoundException;
import org.altherian.hboxd.factory.SecurityManagerFactory;
import org.altherian.hboxd.front._RequestReceiver;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor._HypervisorLoader;
import org.altherian.hboxd.persistence.H2SqlPersistor;
import org.altherian.hboxd.persistence._Persistor;
import org.altherian.hboxd.security.RootUser;
import org.altherian.hboxd.security.SecurityContext;
import org.altherian.hboxd.security._SecurityManager;
import org.altherian.hboxd.security._User;
import org.altherian.hboxd.server._Server;
import org.altherian.hboxd.server._ServerManager;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.hboxd.session.SessionManager;
import org.altherian.hboxd.session._RootSessionManager;
import org.altherian.hboxd.session._SessionManager;
import org.altherian.hboxd.store.StoreManager;
import org.altherian.hboxd.store._StoreManager;
import org.altherian.hboxd.task.TaskManager;
import org.altherian.hboxd.task._TaskManager;
import org.altherian.tool.BooleanUtils;
import org.altherian.tool.SystemUtils;
import org.altherian.tool.logging.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SingleHostCore implements _Hyperbox, _Server, _ServerManager {
   
   private ServerState state;
   
   private _RootSessionManager sessMgr;
   private _SecurityManager secMgr;
   private _ActionManager actionMgr;
   private _TaskManager taskMgr;
   private _StoreManager storeMgr;
   private _Persistor persistor;
   
   private Map<String, _HypervisorLoader> hypervisors;
   private _Hypervisor hypervisor;
   
   private _Client system = new System();
   private _User rootUsr = new RootUser();
   
   public static final String CFGKEY_SRV_ID = "server.id";
   public static final String CFGKEY_SRV_NAME = "server.name";
   public static final String CFGKEY_CORE_HYP_ID = "core.hypervisor.id";
   public static final String CFGKEY_CORE_HYP_OPTS = "core.hypervisor.options";
   public static final String CFGKEY_CORE_HYP_AUTO = "core.hypervisor.autoconnect";
   
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
      
      persistor = HBoxServer.loadClass(_Persistor.class, Configuration.getSetting("core.persistor.class", H2SqlPersistor.class.getName()));
      persistor.init();
   }
   
   @Override
   public void init() throws HyperboxException {
      Logger.track();
      
      HBoxServer.initServer(this);
      
      EventManager.start();
      EventManager.register(this);
      
      loadPersistors();
      
      secMgr = SecurityManagerFactory.get();
      secMgr.init(persistor);
      
      actionMgr = new DefaultActionManager();
      taskMgr = new TaskManager();
      
      storeMgr = new StoreManager();
      storeMgr.init(persistor);
      
      sessMgr = new SessionManager();
      
      loadHypervisors();
   }
   
   @Override
   public _RootSessionManager start() throws HyperboxException {
      Logger.track();
      
      setState(ServerState.Starting);
      
      persistor.start();
      HBoxServer.initPersistor(persistor);
      
      if (!HBoxServer.hasSetting(CFGKEY_SRV_ID)) {
         Logger.verbose("Generating new Server ID");
         HBoxServer.setSetting(CFGKEY_SRV_ID, UUID.randomUUID());
         Logger.verbose("Generating default Server name");
         HBoxServer.setSetting(CFGKEY_SRV_NAME, SystemUtils.getHostname());
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
      
      SessionContext.setClient(system);
      SecurityContext.setUser(rootUsr);
      
      if (HBoxServer.hasSetting(CFGKEY_CORE_HYP_ID)) {
         Logger.info("Loading Hypervisor configuration");
         HypervisorInput in = new HypervisorInput(HBoxServer.getSetting(CFGKEY_CORE_HYP_ID));
         in.setConnectionOptions(HBoxServer.getSetting(CFGKEY_CORE_HYP_OPTS));
         in.setAutoConnect(BooleanUtils.get(HBoxServer.getSetting(CFGKEY_CORE_HYP_AUTO)));
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
      return sessMgr;
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      setState(ServerState.Stopping);
      
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
   }
   
   @Override
   public String getType() {
      return "Server";
   }
   
   @Override
   public String getVersion() {
      return Hyperbox.getVersionFull();
   }
   
   private void loadHypervisors() throws HyperboxException {
      Logger.track();
      
      hypervisors = new HashMap<String, _HypervisorLoader>();
      
      Set<_HypervisorLoader> subTypes = HBoxServer.getQuiet(_HypervisorLoader.class);
      if (subTypes.isEmpty()) {
         Logger.error("Found no hypervisor module - make sure your classpath is correct and the Hypervisor module is properly located");
         Logger.warning("Hyperbox will not be able to connect to any hypervisor and will require a restart if this is needed");
      }

      for (_HypervisorLoader hypLoader : subTypes) {
         for (String scheme : hypLoader.getSupportedSchemes()) {
            try {
               hypervisors.put(scheme, hypLoader);
               Logger.debug("Loaded " + hypLoader.getHypervisorClass().getSimpleName() + " for " + scheme + " scheme");
            } catch (Exception e) {
               throw new HyperboxException("Failed to load Hypervior Class : " + e.getLocalizedMessage(), e);
            }
         }
      }
      
   }
   
   @Override
   public void connect(String hypervisorId, String options) {
      if (!hypervisors.containsKey(hypervisorId)) {
         throw new HyperboxRuntimeException("Invalid Hypervisor ID");
      }
      
      try {
         _Hypervisor hypervisor = (_Hypervisor) hypervisors.get(hypervisorId).getHypervisorClass().getConstructors()[0].newInstance();
         hypervisor.setEventManager(EventManager.get());
         hypervisor.connect(options);
         this.hypervisor = hypervisor;
         HBoxServer.setSetting(CFGKEY_CORE_HYP_ID, hypervisorId);
         HBoxServer.setSetting(CFGKEY_CORE_HYP_OPTS, options);
         HBoxServer.setSetting(CFGKEY_CORE_HYP_AUTO, 1);
         EventManager.post(new HypervisorConnectedEvent(this));
      } catch (IllegalArgumentException e) {
         throw new HyperboxRuntimeException("Hypervisor cannot be loaded due to bad format: " + e.getMessage(), e);
      } catch (SecurityException e) {
         throw new HyperboxRuntimeException("Hypervisor cannot be loaded due to bad format: " + e.getMessage(), e);
      } catch (InstantiationException e) {
         throw new HyperboxRuntimeException("Hypervisor cannot be loaded due to bad format: " + e.getMessage(), e);
      } catch (IllegalAccessException e) {
         throw new HyperboxRuntimeException("Hypervisor cannot be loaded due to bad format: " + e.getMessage(), e);
      } catch (InvocationTargetException e) {
         throw new HyperboxRuntimeException("Hypervisor cannot be loaded due to bad format: " + e.getMessage(), e);
      }
   }
   
   @Override
   public void disconnect() {
      hypervisor.disconnect();
      hypervisor = null;
      EventManager.post(new HypervisorDisconnectedEvent(this));
   }
   
   @Override
   public boolean isConnected() {
      return (hypervisor != null);
   }
   
   @Override
   public _Hypervisor getHypervisor() {
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
   public List<_HypervisorLoader> listHypervisors() {
      return new ArrayList<_HypervisorLoader>(hypervisors.values());
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
   
}
