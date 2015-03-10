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

package org.altherian.hboxc.controller;

import org.altherian.hbox.Configuration;
import org.altherian.hbox.HyperboxAPI;
import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._AnswerReceiver;
import org.altherian.hbox.comm._RequestReceiver;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.Hyperbox;
import org.altherian.hboxc.HyperboxClient;
import org.altherian.hboxc.PreferencesManager;
import org.altherian.hboxc.controller.action._ClientControllerAction;
import org.altherian.hboxc.core.ClientCore;
import org.altherian.hboxc.core.CoreReader;
import org.altherian.hboxc.event.EventManager;
import org.altherian.hboxc.exception.ServerDisconnectedException;
import org.altherian.hboxc.front._Front;
import org.altherian.hboxc.front.minimal.MiniUI;
import org.altherian.tool.logging.LogLevel;
import org.altherian.tool.logging.Logger;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class Controller implements _ClientMessageReceiver, _RequestReceiver {
   
   private ClientCore core;
   private _Front front = new MiniUI();
   
   private RequestWorker msgWorker;
   private Map<String, _ClientControllerAction> actionsMap;
   
   static {
      Logger.raw(getHeader());
      try {
         if (new File(Hyperbox.getConfigFilePath()).exists()) {
            Configuration.init(Hyperbox.getConfigFilePath());
         } else {
            Logger.debug("Default config file does not exist, skipping: " + Hyperbox.getConfigFilePath());
         }
      } catch (HyperboxException e) {
         Logger.error(e);
         System.exit(1);
      }
   }
   
   private void loadActions() throws HyperboxException {
      actionsMap = new HashMap<String, _ClientControllerAction>();
      
      ShutdownAction.c = this;
      
      for (_ClientControllerAction ac : HyperboxClient.getAllOrFail(_ClientControllerAction.class)) {
         actionsMap.put(ac.getRegistration().toString(), ac);
      }
   }
   
   public static String getHeader() {
      return HyperboxAPI.getLogHeader(Hyperbox.getFullVersion());
   }
   
   private void loadFront() throws HyperboxException {
      String classToLoad = MiniUI.class.getName();
      
      if (Configuration.hasSetting("view.class")) {
         classToLoad = Configuration.getSetting("view.class");
      } else if (!GraphicsEnvironment.isHeadless()) {
         classToLoad = "org.altherian.hboxc.front.gui.Gui";
      } else {
         // TODO create Console view
      }
      
      try {
         front = HyperboxClient.loadClass(_Front.class, classToLoad);
      } catch (HyperboxRuntimeException e) {
         throw new HyperboxException(e);
      }
   }
   
   public void start() throws HyperboxException {
      
      
      try {
         PreferencesManager.init();
         
         loadFront();
         
         String logLevel = Configuration.getSetting("log.level", LogLevel.Info.toString());
         Logger.setLevel(LogLevel.valueOf(logLevel));
         
         String defaultLogFilePath = PreferencesManager.getUserPrefPath() + File.separator + "log" + File.separator + "hbox.log";
         try {
            String logFile = Configuration.getSetting("log.file", defaultLogFilePath);
            if (!logFile.toLowerCase().contentEquals("none")) {
               Logger.log(logFile, 4);
            }
         } catch (IOException e) {
            front.postError(e, "Launch error: " + e.getMessage());
            System.exit(1);
         }
         
         for (String name : System.getenv().keySet()) {
            Logger.debug(name + ": " + System.getenv(name));
         }
         
         EventManager.get().start();
         
         loadActions();
         
         msgWorker = new RequestWorker();
         msgWorker.start();
         
         front.start();
         front.setRequestReceiver(this);
         
         core = new ClientCore();
         core.init();
         front.setCoreReader(new CoreReader(core));
         core.start();
         
         HyperboxClient.initView(front);
      } catch (Throwable t) {
         Logger.exception(t);
         front.postError(t, "Fatal error when starting: " + t.getMessage());
         stop();
      }
   }
   
   public void stop() {
      
      
      try {
         if (core != null) {
            core.stop();
            core.destroy();
         }
         if (front != null) {
            front.stop();
         }
         if (msgWorker != null) {
            msgWorker.stop();
         }
         
         EventManager.get().stop();
         System.exit(0);
      } catch (Throwable t) {
         System.exit(1);
      }
   }
   
   @Override
   public void post(MessageInput mIn) {
      
      
      msgWorker.post(mIn);
   }
   
   @Override
   public void putRequest(Request request) {
      
      
      msgWorker.post(new MessageInput(request));
   }
   
   private class RequestWorker implements _ClientMessageReceiver, Runnable {
      
      private boolean running;
      private Thread thread;
      private BlockingQueue<MessageInput> queue;
      
      @Override
      public void post(MessageInput mIn) {
         
         
         if (!queue.offer(mIn)) {
            throw new HyperboxRuntimeException("Couldn't queue the request : queue is full (" + queue.size() + " messages)");
         } else {
            
         }
      }
      
      public void start() throws HyperboxException {
         
         
         running = true;
         queue = new LinkedBlockingQueue<MessageInput>();
         thread = new Thread(this, "FRQMGR");
         thread.setDaemon(true);
         thread.start();
      }
      
      public void stop() {
         
         
         running = false;
         thread.interrupt();
         try {
            thread.join(5000);
         } catch (InterruptedException e) {
            Logger.debug("Worker thread didn't stop on request after 5 sec");
         }
      }
      
      @Override
      public void run() {
         Logger.verbose("RequestWorker Thread started");
         while (running) {
            try {
               MessageInput mIn = queue.take();
               
               Request req = mIn.getRequest();
               _AnswerReceiver recv = mIn.getReceiver();
               
               try {
                  if (actionsMap.containsKey(mIn.getRequest().getName())) {
                     
                     _ClientControllerAction action = actionsMap.get(mIn.getRequest().getName());
                     recv.putAnswer(new Answer(mIn.getRequest(), action.getStartReturn()));
                     action.run(core, front, req, recv);
                     recv.putAnswer(new Answer(mIn.getRequest(), action.getFinishReturn()));
                  } else {
                     
                     if (req.has(ServerIn.class)) {
                        core.getServer(req.get(ServerIn.class).getId()).sendRequest(req);
                     } else if (req.has(MachineIn.class)) {
                        core.getServer(req.get(MachineIn.class).getServerId()).sendRequest(req);
                     } else {
                        throw new HyperboxRuntimeException("Server ID or Machine ID is required for generic requests");
                     }
                  }
               } catch (ServerDisconnectedException e) {
                  Logger.error(e);
               } catch (HyperboxException e) {
                  Logger.error("Unable to perform the request [ " + req.getName() + " ] : " + e.getMessage());
                  front.postError(e);
               } catch (HyperboxRuntimeException e) {
                  Logger.error("Unable to perform the request [ " + req.getName() + " ] : " + e.getMessage());
                  front.postError(e);
               }
            } catch (InterruptedException e) {
               
               Logger.debug("Got interupted, halting");
               running = false;
            } catch (Throwable e) {
               
               Logger.error("Unknown error : " + e.getMessage());
               Logger.exception(e);
               front.postError(e, "Unexpected error occured: " + e.getMessage());
            }
         }
         
         Logger.verbose("RequestWorker Thread halted");
      }
      
   }
   
}
