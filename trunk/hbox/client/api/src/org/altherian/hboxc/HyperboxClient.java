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

package org.altherian.hboxc;

import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.front._Front;
import org.altherian.tool.logging.Logger;

import java.lang.reflect.InvocationTargetException;
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

public class HyperboxClient {
   
   private static Reflections classes;
   private static _Front view;
   
   static {
      Set<URL> rawURls = ClasspathHelper.forJavaClassPath();
      Set<URL> urls = new HashSet<URL>();
      for (URL url : rawURls) {
         if (url.getFile().endsWith(".jar") || url.getFile().endsWith(".class") || url.getFile().endsWith("/")) {
            urls.add(url);
         }
      }
      classes = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
            .setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())));
   }
   
   @SuppressWarnings("unchecked")
   public static <T> T loadClass(Class<T> itWannabeClass, String it) {
      try {
         Class<T> itClass = (Class<T>) Class.forName(it);
         T itInstance = (T) itClass.getConstructors()[0].newInstance();
         Logger.debug("Loaded " + itInstance.getClass().getSimpleName());
         return itInstance;
      } catch (Exception e) {
         Logger.error("Failed to load " + it + " : " + e.getLocalizedMessage());
         Logger.exception(e);
         throw new HyperboxRuntimeException(e);
      }
   }
   
   public static <T> Set<Class<? extends T>> getSubTypes(final Class<T> type) {
      return classes.getSubTypesOf(type);
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
         } catch (InstantiationException e) {
            Logger.error("Failed to load " + rawObject.getSimpleName() + " : " + e.getLocalizedMessage());
            Logger.exception(e);
         } catch (IllegalAccessException e) {
            Logger.error("Failed to load " + rawObject.getSimpleName() + " : " + e.getLocalizedMessage());
            Logger.exception(e);
         } catch (IllegalArgumentException e) {
            Logger.error("Failed to load " + rawObject.getSimpleName() + " : " + e.getLocalizedMessage());
            Logger.exception(e);
         } catch (InvocationTargetException e) {
            Logger.error("Failed to load " + rawObject.getSimpleName() + " : " + e.getLocalizedMessage());
            Logger.exception(e);
         } catch (SecurityException e) {
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
   
   // TODO re-evalute the pertinence of using HyperboxException for this
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
      } catch (IllegalArgumentException e) {
         Logger.exception(e);
         throw new HyperboxException("Failed to load " + type.getSimpleName(), e);
      } catch (InstantiationException e) {
         Logger.exception(e);
         throw new HyperboxException("Failed to load " + type.getSimpleName(), e);
      } catch (IllegalAccessException e) {
         Logger.exception(e);
         throw new HyperboxException("Failed to load " + type.getSimpleName(), e);
      } catch (InvocationTargetException e) {
         Logger.exception(e);
         throw new HyperboxException("Failed to load " + type.getSimpleName(), e);
      } catch (SecurityException e) {
         Logger.exception(e);
         throw new HyperboxException("Failed to load " + type.getSimpleName(), e);
      }
   }
   
   public static void initView(_Front view) {
      if (HyperboxClient.view == null) {
         HyperboxClient.view = view;
      }
   }
   
   public static _Front getView() {
      return view;
   }
   
}
