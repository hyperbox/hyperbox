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

import org.altherian.hboxd.HBoxServer;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.module.ModuleDisabledEvent;
import org.altherian.hboxd.event.module.ModuleEnabledEvent;
import org.altherian.hboxd.event.module.ModuleLoadedEvent;
import org.altherian.hboxd.exception.ModuleException;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Module implements _Module {
   
   private String id;
   private String descFile;
   private String base;
   private String name;
   private String version;
   private String vendor;
   private String website;
   private Map<String, String> providers = new HashMap<String, String>();
   
   private _ModuleClassLoader loader;
   private boolean isEnabled = true;
   private Map<Class<?>, Class<?>> providerClasses;
   
   public Module(String id, File descFile, File base, String name, String version, String vendor, String desc, String website,
         Map<String, String> providers) {
      this.id = id;
      this.descFile = descFile.getAbsolutePath();
      this.base = base.getAbsolutePath();
      this.name = name;
      this.version = version;
      this.website = website;
      this.providers = providers == null ? this.providers : providers;
   }
   
   @Override
   public String getId() {
      return id;
   }
   
   @Override
   public String getDescriptor() {
      return descFile;
   }
   
   @Override
   public String getName() {
      return name;
   }
   
   @Override
   public String getLocation() {
      return base;
   }
   
   @Override
   public String getVersion() {
      return version;
   }
   
   @Override
   public String getVendor() {
      return vendor;
   }
   
   @Override
   public String getUrl() {
      return website;
   }
   
   @Override
   public String toString() {
      return "Module ID " + getId() + " (" + getName() + ")";
   }
   
   @Override
   public void enable() {
      if (!isEnabled()) {
         isEnabled = true;
         EventManager.post(new ModuleEnabledEvent(this));
      }
   }
   
   @Override
   public void disable() {
      if (isEnabled()) {
         if (isLoaded()) {
            unload();
         }
         
         isEnabled = false;
         EventManager.post(new ModuleDisabledEvent(this));
      }
   }
   
   @Override
   public boolean isEnabled() {
      return isEnabled;
   }
   
   @Override
   public void load() throws ModuleException {
      if (isLoaded()) {
         throw new ModuleException("Module is already loaded");
      }
      if (!isEnabled()) {
         throw new ModuleException("Module must be enabled to be loaded");
      }
      
      loader = new ModuleClassLoader();
      loader.load(getLocation());
      
      try {
         Map<Class<?>, Class<?>> providersRaw = new HashMap<Class<?>, Class<?>>();
         for (String typeName : providers.keySet()) {
            Class<?> type = loader.createClass(typeName);
            Class<?> provider = loader.createClass(providers.get(typeName));
            providersRaw.put(type, provider);
         }
         providerClasses = providersRaw;
      } catch (Throwable t) {
         throw new ModuleException("Unable to create provider classes: " + t.getClass().getSimpleName() + ": " + t.getMessage());
      }
      
      HBoxServer.reload(getRessources(), loader.getClassLoader());
      EventManager.post(new ModuleLoadedEvent(this));
   }
   
   @Override
   public final void unload() {
      throw new ModuleException("This module cannot be unloaded: Not implemented");
      
      /*
       * There is currently a leak with ModuleClassLoader: unloaded modules are not being GC.
       * Until fix, we do not allow unloading in the default implementation.
       * 
      if (isLoaded()) {
         HBoxServer.remove(getRessources());
         
         mainClass = null;
         typeClass = null;
         
         loader.unload();
         loader = null;
         
         EventManager.post(new ModuleUnloadedEvent(this));
      }
       */
   }
   
   @Override
   public boolean isLoaded() {
      return (loader != null) && (providers != null);
   }
   
   @Override
   public boolean isReady() {
      return isEnabled() && isLoaded();
   }
   
   @Override
   public Set<URL> getRessources() {
      if (!isReady()) {
         throw new ModuleException("Module must be enabled and loaded before retrieving list of ressources");
      }
      
      return loader.getRessources();
   }
   
   @Override
   public Set<Class<?>> getTypes() {
      if (!isReady()) {
         throw new ModuleException("Module is not ready");
      }
      
      return new HashSet<Class<?>>(providerClasses.keySet());
   }
   
   @Override
   public Class<?> getProvider(Class<?> type) throws ModuleException {
      if (!isReady()) {
         throw new ModuleException("Module is not ready");
      }
      
      if (!providerClasses.containsKey(type)) {
         throw new ModuleException("No such provider type found: " + type.getName());
      }
      
      return providerClasses.get(type);
   }
   
   @Override
   public Object buildProvider(Class<?> type) throws ModuleException {
      if (!isReady()) {
         throw new ModuleException("Module is not ready");
      }
      
      try {
         return getProvider(type).newInstance();
      } catch (InstantiationException e) {
         throw new ModuleException("Couldn't create an instance: " + e.getMessage());
      } catch (IllegalAccessException e) {
         throw new ModuleException("Couldn't create an instance: " + e.getMessage());
      }
   }
   
}
