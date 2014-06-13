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

package org.altherian.hboxd;

import org.altherian.hbox.Configuration;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.persistence._Persistor;
import org.altherian.hboxd.server._Server;
import org.altherian.tool.logging.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class HBoxServer {
   
   private static Reflections classes;
   private static _Persistor persistor;
   private static _Server srv;
   
   static {
      Long start = System.currentTimeMillis();
      Set<URL> rawURls = ClasspathHelper.forJavaClassPath();
      Set<URL> urls = new HashSet<URL>();
      for (URL url : rawURls) {
         if (url.getFile().endsWith(".jar") || url.getFile().endsWith(".class") || url.getFile().endsWith("/")) {
            urls.add(url);
         }
      }
      
      classes = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
            .setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())));
      Long end = System.currentTimeMillis();
      Long eslaped = end - start;
      Logger.verbose("Scanning for providers took " + eslaped + " ms");
   }
   
   @SuppressWarnings("unchecked")
   public static <T> T loadClass(Class<T> itWannabeClass, String it) {
      try {
         Class<T> itClass = (Class<T>) Class.forName(it);
         T itInstance = (T) itClass.getConstructors()[0].newInstance();
         Logger.verbose("Loaded " + itInstance.getClass().getSimpleName());
         return itInstance;
      } catch (Exception e) {
         Logger.error("Failed to load " + it + " : " + e.getLocalizedMessage());
         Logger.exception(e);
         throw new HyperboxRuntimeException(e);
      }
   }
   
   public static <T> Set<Class<? extends T>> getSubTypes(final Class<T> type) {
      Long start = System.currentTimeMillis();
      Set<Class<? extends T>> classList = classes.getSubTypesOf(type);
      Long end = System.currentTimeMillis();
      Long eslaped = end - start;
      Logger.verbose(type.getSimpleName() + " providers took " + eslaped + " ms to find");
      return classList;
   }
   
   public static <T> Set<Class<? extends T>> getAnnotatedSubTypes(Class<T> type, Class<? extends Annotation> note) {
      Long start = System.currentTimeMillis();
      Set<Class<? extends T>> classList = new HashSet<Class<? extends T>>();
      for (Class<? extends T> subType : classes.getSubTypesOf(type)) {
         Logger.track();
         if (Modifier.isAbstract(subType.getModifiers())) {
            Logger.debug(subType.getName() + " is abstract and was ignored");
         } else {
            Logger.debug("Found match for " + type.getName() + ": " + subType.getName());
            if (subType.getAnnotations().length == 0) {
               Logger.debug(subType.getName() + " has no annotation");
            }
            for (Annotation subTypeNote : subType.getAnnotations()) {
               if (!subTypeNote.annotationType().equals(note)) {
                  Logger.debug(subTypeNote.annotationType().getName() + " ignored, does not have annotation " + note.getName());
               } else {
                  classList.add(subType);
               }
            }
            
         }
      }
      Long end = System.currentTimeMillis();
      Long eslaped = end - start;
      Logger.verbose(type.getSimpleName() + " providers took " + eslaped + " ms to find");
      return classList;
   }
   
   @SuppressWarnings("unchecked")
   public static <T> Set<T> getQuiet(Class<T> type) {
      Set<T> loadedClasses = new HashSet<T>();
      
      Set<Class<? extends T>> classes = getSubTypes(type);
      for (Class<? extends T> rawObject : classes) {
         try {
            if (!Modifier.isAbstract(rawObject.getModifiers())) {
               T view = (T) rawObject.getConstructors()[0].newInstance();
               Logger.verbose("Loaded " + view.getClass().getSimpleName());
               loadedClasses.add(view);
            }
         } catch (Exception e) {
            Logger.error("Failed to load " + rawObject.getSimpleName() + " : " + e.getLocalizedMessage());
            Logger.exception(e);
         }
      }
      
      return loadedClasses;
   }
   
   public static <T> Set<T> getAtLeastOneOrFail(Class<T> type) throws HyperboxException {
      Set<T> objects = getQuiet(type);
      
      if (objects.isEmpty()) {
         throw new HyperboxException("Unable to find any match for " + type.getSimpleName());
      }
      
      return objects;
   }
   
   @SuppressWarnings("unchecked")
   public static <T> Set<T> getAllOrFail(Class<T> type) throws HyperboxException {
      try {
         Set<Class<? extends T>> classes = getSubTypes(type);
         Set<T> loadedClasses = new HashSet<T>();
         for (Class<? extends T> rawObject : classes) {
            if (!Modifier.isAbstract(rawObject.getModifiers())) {
               T view = (T) rawObject.getConstructors()[0].newInstance();
               Logger.verbose("Loaded " + view.getClass().getSimpleName());
               loadedClasses.add(view);
            }
         }
         return loadedClasses;
      } catch (Exception e) {
         Logger.exception(e);
         throw new HyperboxException("Failed to load " + type.getSimpleName(), e);
      }
   }
   
   public static void initPersistor(_Persistor persistor) {
      if (HBoxServer.persistor == null) {
         HBoxServer.persistor = persistor;
      }
   }
   
   public static void initServer(_Server srv) {
      HBoxServer.srv = srv;
   }
   
   public static _Server get() {
      return srv;
   }
   
   public static String getSetting(String key, String defaultValue) {
      try {
         if (Configuration.hasSetting(key)) {
            return Configuration.getSetting(key, defaultValue);
         } else {
            return persistor.loadSetting(key);
         }
      } catch (Throwable e) {
         return defaultValue;
      }
   }
   
   public static String getSetting(String key) {
      return getSetting(key, null);
   }
   
   public static boolean hasSetting(String key) {
      Logger.track();
      try {
         Logger.debug("Checking key \""+key+"\" in storage");
         return persistor.loadSetting(key) != null;
      } catch (Throwable e) {
         Logger.debug("Key is not in storage, checking in memory or env - Error: " + e.getMessage());
         return Configuration.hasSetting(key);
      }
   }
   
   public static String getSettingOrFail(String key) {
      if (!hasSetting(key)) {
         throw new HyperboxRuntimeException("Setting key not found: " + key);
      } else {
         return getSetting(key);
      }
   }
   
   public static void setSetting(String key, Object value) {
      persistor.storeSetting(key, value.toString());
      Configuration.setSetting(key, value);
   }
}
