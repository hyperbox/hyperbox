/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxd.module;

import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.Configuration;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.module.ModuleRegisteredEvent;
import org.altherian.hboxd.event.module.ModuleUnloadedEvent;
import org.altherian.hboxd.event.module.ModuleUnregisteredEvent;
import org.altherian.hboxd.exception.ModuleException;
import org.altherian.hboxd.exception.module.ModuleAlreadyRegisteredException;
import org.altherian.hboxd.factory.ModuleFactory;
import org.altherian.tool.AxBooleans;
import org.altherian.tool.logging.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModuleManager implements _ModuleManager {
   
   private boolean isStarted = false;
   private String[] baseDirs = new String[0];
   private String moduleExtention = "xml";
   private Map<String, _Module> modules;
   
   @Override
   public void start() {
      modules = new HashMap<String, _Module>();
      baseDirs = Configuration.getSetting(CFGKEY_MODULE_BASEPATH, CFGVAL_MODULE_BASEPATH).split(File.pathSeparator);
      refreshModules();
      isStarted = true;
      EventManager.register(this);
      Logger.verbose("Module Manager has started");
   }
   
   @Override
   public void stop() {
      EventManager.unregister(this);
      isStarted = false;
      unregisterModules();
      baseDirs = null;
      modules = null;
      Logger.verbose("Module manager has stopped");
   }
   
   @Override
   public void refreshModules() {
      Logger.verbose("Refreshing modules...");
      
      Logger.debug("Number of base module directories: " + baseDirs.length);
      for (String baseDir : baseDirs) {
         File baseDirFile = new File(baseDir);
         Logger.verbose("Searching in " + baseDirFile.getAbsolutePath() + " for modules...");
         if (baseDirFile.isDirectory() && baseDirFile.canRead()) {
            Logger.debug(baseDirFile.getAbsolutePath() + " is a readable directory, processing...");
            for (File file : baseDirFile.listFiles()) {
               if (isRegistered(file.getAbsolutePath())) {
                  Logger.debug(file.getAbsolutePath() + " is already registered for " + modules.get(file.getAbsolutePath()).getName());
                  continue;
               }
               if (!file.isFile()) {
                  continue;
               }
               if (!file.canRead()) {
                  Logger.debug(file.getAbsolutePath() + " is not readable, skipping.");
                  continue;
               }
               if (!file.getPath().endsWith("." + moduleExtention)) {
                  Logger.debug(file.getAbsolutePath() + " does not have the module extention, skipping.");
                  continue;
               }
               if (modules.containsKey(file.getAbsolutePath())) {
                  Logger.debug(file.getAbsolutePath() + " is already registered as " + modules.get(file.getAbsolutePath()));
                  continue;
               }
               
               Logger.verbose("Usable module descriptor file detected: " + file.getAbsolutePath());
               try {
                  registerModule(file.getAbsolutePath());
               } catch (ModuleAlreadyRegisteredException e) {
                  Logger.verbose("Module is already registered, skipping.");
               }
            }
         } else {
            Logger.error("Unable to refresh modules for Base Directory " + baseDirFile + ": either not a directory or cannot be read");
         }
      }
      Logger.verbose("Finished refreshing modules.");
   }
   
   @Override
   public void unregisterModules() {
      Logger.track();
      
      // needed not to cause an Exception
      for (_Module mod : new ArrayList<_Module>(modules.values())) {
         try {
            mod.unload();
            unregisterModule(mod);
         } catch (ModuleException e) {
            Logger.warning("Couldn't unload " + mod + ": " + e.getMessage());
         }
         
      }
   }
   
   @Override
   public void setModuleBasedir(String... basedir) {
      Logger.track();
      
      baseDirs = basedir;
      Logger.verbose("Module Base Dirs has been set to:");
      for (String s : basedir) {
         Logger.verbose("\t" + new File(s).getAbsolutePath());
      }
      unregisterModules();
      if (isStarted) {
         refreshModules();
      }
   }
   
   @Override
   public Set<_Module> listModules() {
      return new HashSet<_Module>(modules.values());
   }
   
   @Override
   public Set<_Module> listModules(Class<?> type) {
      Set<_Module> mods = new HashSet<_Module>();
      for (_Module mod : modules.values()) {
         if (mod.isLoaded() && mod.getTypes().contains(type)) {
            mods.add(mod);
         }
      }
      return mods;
   }
   
   @Override
   public _Module getModule(String moduleId) {
      return modules.get(moduleId);
   }
   
   @Override
   public boolean isRegistered(String idOrDescriptorPath) {
      return modules.containsKey(idOrDescriptorPath) || modules.containsKey(new File(idOrDescriptorPath).getAbsolutePath());
   }
   
   protected boolean isRegistered(_Module mod) {
      return isRegistered(mod.getId()) || isRegistered(mod.getDescriptor());
   }
   
   @Override
   public _Module registerModule(String moduleDescFile) {
      Logger.track();
      
      Logger.verbose("Attempting to add module with descriptor file: " + moduleDescFile);
      File xmlFile = new File(moduleDescFile);
      _Module mod = ModuleFactory.get(xmlFile);
      
      if (isRegistered(mod.getId()) || isRegistered(mod.getDescriptor())) {
         throw new ModuleAlreadyRegisteredException(mod.getId());
      }
      
      modules.put(mod.getId(), mod);
      modules.put(mod.getDescriptor(), mod);
      
      Logger.verbose("Module ID " + mod.getId() + " (" + mod.getName() + ") was successfully registered");
      EventManager.post(new ModuleRegisteredEvent(mod));
      
      if (AxBooleans.get(Configuration.getSetting(CFGKEY_MODULE_AUTOLOAD, CFGVAL_MODULE_AUTOLOAD))) {
         try {
            mod.load();
            Logger.verbose("Module ID " + mod.getId() + " (" + mod.getName() + ") was autoloaded");
         } catch (ModuleException e) {
            Logger.warning("Module ID " + mod.getId() + " (" + mod.getName() + ") failed to autoload: " + e.getMessage());
         }
      }
      
      return mod;
   }
   
   @Override
   public void unregisterModule(_Module mod) {
      Logger.track();
      
      mod = getModule(mod.getId());
      mod.unload();
      
      modules.remove(mod.getId());
      modules.remove(mod.getDescriptor());
      
      EventManager.post(new ModuleUnregisteredEvent(mod.getId()));
      Logger.verbose("Module ID " + mod.getId() + " (" + mod.getName() + ") was successfully unregistered");
   }
   
   @Override
   public _Module reloadModule(_Module mod) {
      if (isRegistered(mod)) {
         String path = mod.getLocation();
         unregisterModule(mod);
         return registerModule(path);
      } else {
         return getModule(mod.getId());
      }
   }
   
   @Handler
   protected void putModuleEvent(ModuleUnloadedEvent ev) {
      Logger.track();
      
      Logger.debug("Calling GC to release any ressources of the module");
      System.gc();
   }
   
   @Handler
   protected void putModuleEvent(ModuleUnregisteredEvent ev) {
      Logger.track();
      
      Logger.debug("Calling GC to release any ressources of the module");
      System.gc();
   }
   
}
