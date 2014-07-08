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

package org.altherian.hboxd.controller;

import org.altherian.hbox.Configuration;
import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm._Client;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.event.server.ServerShutdownEventOutput;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.Hyperbox;
import org.altherian.hboxd.comm.io.factory.ServerIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ShutdownAction;
import org.altherian.hboxd.factory.ModelFactory;
import org.altherian.hboxd.front._Front;
import org.altherian.hboxd.security.SecurityContext;
import org.altherian.hboxd.security.SystemUser;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.tool.logging.LogLevel;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public final class Controller implements _Controller {
   
   private int exitCode = 0;
   private _Hyperbox model;
   private List<_Front> fronts = new ArrayList<_Front>();
   
   private Thread shutdownHook;
   
   {
      shutdownHook = new Thread() {
         @Override
         public void run() {
            SecurityContext.setUser(new SystemUser());
            SessionContext.setClient(new Client());
            Controller.this.stop();
         }
      };
      Runtime.getRuntime().addShutdownHook(shutdownHook);
   }
   
   public static String getHeader() {
      StringBuilder header = new StringBuilder();
      header.append("Hyperbox " + Hyperbox.getVersion() + " r" + Hyperbox.getRevision());
      header.append(" - ");
      header.append("Java " + System.getProperty("java.version") + " " + System.getProperty("java.vm.name") + " "
            + System.getProperty("java.vm.version"));
      header.append(" - ");
      header.append(System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
      return header.toString();
   }
   
   private void startBack() {
      Logger.track();
      
      try {
         model = ModelFactory.get();
         model.init();
         model.start();
      } catch (HyperboxException e) {
         Logger.error(e);
         Logger.exception(e);
         stop();
      }
   }
   
   private void startFront() throws HyperboxException {
      Logger.track();
      
      Set<_Front> subTypes = HBoxServer.getAtLeastOneOrFail(_Front.class);
      for (_Front test : subTypes) {
         fronts.add(test);
      }
      
      Logger.info("Starting Front-ends");
      for (final _Front f : fronts) {
         try {
            f.start(model.getReceiver());
            Logger.info(f.getClass().getSimpleName() + " has started");
         } catch (HyperboxException e1) {
            Logger.info(f.getClass().getSimpleName() + " failed to start");
            throw new HyperboxRuntimeException(e1);
         }
      }
      Logger.info("Done starting Front-ends");
      
   }
   
   @Override
   public void start() {
      Logger.track();
      
      try {
         Long startTime = System.currentTimeMillis();
         
         SecurityContext.init();
         SecurityContext.addAdminThread(shutdownHook);
         
         Configuration.init();
         
         String logLevel = Configuration.getSetting("log.level", LogLevel.Info.toString());
         Logger.setLevel(LogLevel.valueOf(logLevel));
         
         String logFilename = Configuration.getSetting("log.file", "log/hboxd.log");
         if (!logFilename.contentEquals("none")) {
            Logger.log(logFilename, 4);
         }
         
         Logger.put(getHeader());
         Logger.info("Hyperbox Init Sequence started");
         
         ShutdownAction.setController(this);
         
         for (String name : System.getenv().keySet()) {
            Logger.debug(name + ": " + System.getenv(name));
         }
         
         startBack();
         startFront();
         Long endTime = System.currentTimeMillis();
         Logger.verbose("Hyperbox started in " + (endTime - startTime) + "ms");
         Logger.info("-------> Hyperbox is running <-------");
      } catch (Throwable e) {
         Logger.error("Hyperbox Init Sequence failed!");
         exitCode = 1;
         Logger.exception(e);
         stop();
      }
      
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      Long startTime = System.currentTimeMillis();
      Logger.info("-------> Hyperbox is stopping <-------");
      try {
         stopFront();
         stopBack();
      } catch (Throwable e) {
         Logger.error("Exception when stopping Hyperbox: " + e.getMessage());
         Logger.exception(e);
      } finally {
         Long endTime = System.currentTimeMillis();
         Logger.verbose("Hyperbox Stop Sequence finished in " + (endTime - startTime) + "ms");
         Logger.info("-------> Hyperbox halt <-------");
         
         if (!Thread.currentThread().equals(shutdownHook)) {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
            System.exit(exitCode);
         }
      }
      
   }
   
   private void stopFront() {
      Logger.track();
      
      Logger.info("Stopping front-ends");
      EventOutput evOut = new ServerShutdownEventOutput(new Date(), ServerIoFactory.get(model.getServerManager().getServer()));
      for (_Front f : fronts) {
         f.broadcast(evOut);
         f.stop();
         Logger.info("\t" + f.getClass().getSimpleName() + " stopped");
      }
      Logger.info("Finished stopping front-ends");
   }
   
   private void stopBack() {
      Logger.track();
      
      Logger.info("Stopping back-ends");
      if (model != null) {
         model.stop();
      }
      Logger.info("Finished stopping back-ends");
   }
   
   private class Client implements _Client {
      
      @Override
      public void putAnswer(Answer ans) {
         // stub
      }
      
      @Override
      public String getId() {
         return "System";
      }
      
      @Override
      public String getAddress() {
         return "System";
      }
      
      @Override
      public void post(EventOutput evOut) {
         // stub
      }
      
   }
   
}
