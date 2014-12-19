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

import org.altherian.tool.logging.Logger;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

// TODO Use the urlList as cache to avoid scanning the file system everytime.
// TODO do the same for resource (if possible?)
public class ModuleClassLoader extends ClassLoader implements _ModuleClassLoader {
   
   private File basePathFile;
   private Set<URL> urlList = new HashSet<URL>();
   private Map<URL, ProtectionDomain> protectionDomains = new HashMap<URL, ProtectionDomain>();
   
   private URL getURL(File jar, JarEntry element) throws MalformedURLException {
      String sep = element.getName().startsWith("/") ? "!" : "!/";
      return new URL("jar:" + jar.toURI() + sep + element.getName());
   }
   
   private ProtectionDomain getDomain(URL url) {
      if (!protectionDomains.containsKey(url)) {
         CodeSource source = new CodeSource(url, new Certificate[] {});
         ProtectionDomain pd = new ProtectionDomain(source, null, this, null);
         protectionDomains.put(url, pd);
      }
      
      return protectionDomains.get(url);
   }
   
   // TODO use for the cache, not for every call
   private Class<?> findClassInDir(File path, String className) throws ClassNotFoundException {
      for (File item : path.listFiles()) {
         if (item.isDirectory()) {
            findClassInDir(item, className);
         } else {
            try {
               return findClassInFile(item, className);
            } catch (ClassNotFoundException e) {
               // we continue then...
            }
         }
      }
      throw new ClassNotFoundException();
   }
   
   // TODO use for the cache, not for every call
   private Class<?> findClassInFile(File path, String className) throws ClassNotFoundException {
      JarFile jar = null;
      try {
         jar = new JarFile(path);
         String classNamePath = className.replace(".", "/");
         JarEntry entry = jar.getJarEntry(classNamePath + ".class");
         if (entry == null) {
            throw new ClassNotFoundException(className);
         }
         InputStream is = jar.getInputStream(entry);
         ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
         int nextValue = is.read();
         while (-1 != nextValue) {
            byteStream.write(nextValue);
            nextValue = is.read();
         }
         is.close();
         
         byte classByte[] = byteStream.toByteArray();
         return defineClass(className, classByte, 0, classByte.length, getDomain(path.toURI().toURL()));
      } catch (ZipException e) {
         Logger.verbose(path.getAbsoluteFile() + " was skipped due to a JAR error: " + e.getMessage());
         throw new ClassNotFoundException(className);
      } catch (IOException e) {
         throw new RuntimeException(e);
      } finally {
         if (jar != null) {
            try {
               jar.close();
            } catch (IOException e) {
               Logger.warning("Error when trying to close JAR " + jar.getName() + ": " + e.getMessage());
            }
         }
      }
   }
   
   @Override
   protected Class<?> findClass(String className) throws ClassNotFoundException {
      try {
         return getParent().loadClass(className);
      } catch (ClassNotFoundException e) {
         if (basePathFile == null) {
            throw new RuntimeException("Not ready, use load() first");
         }
         
         return findClassInDir(basePathFile, className);
      }
      
   }
   
   // TODO use for the cache, not for every call
   private URL findRessource(File path, String ressource) {
      if (path.isDirectory()) {
         return findRessourceInDir(path, ressource);
      } else {
         return findRessourceInFile(path, ressource);
      }
      
   }
   
   // TODO use for the cache, not for every call
   private URL findRessourceInDir(File path, String ressource) {
      for (File item : path.listFiles()) {
         if (item.isDirectory()) {
            findRessourceInDir(item, ressource);
         } else {
            URL ressUrl = findRessourceInFile(item, ressource);
            if (ressUrl != null) {
               return ressUrl;
            }
         }
      }
      return null;
   }
   
   // TODO use for the cache, not for every call
   private URL findRessourceInFile(File path, String ressource) {
      JarFile jar = null;
      try {
         jar = new JarFile(path);
         JarEntry entry = jar.getJarEntry(ressource);
         if (entry == null) {
            return null;
         } else {
            return getURL(path, entry);
         }
      } catch (ZipException e) {
         Logger.verbose(path.getAbsoluteFile() + " was skipped due to a JAR error: " + e.getMessage());
         return null;
      } catch (IOException e1) {
         throw new RuntimeException(e1);
      } finally {
         try {
            if (jar != null) {
               jar.close();
            }
         } catch (IOException e1) {
            e1.printStackTrace();
         }
      }
   }
   
   // TODO use for the cache, not for every call
   private void generateClassPath(File path, Set<URL> list) {
      if (path.isDirectory()) {
         File[] items = path.listFiles();
         if (items != null) {
            for (File item : path.listFiles()) {
               generateClassPath(item, list);
            }
         }
      } else {
         if (path.getPath().endsWith(".jar") || path.getPath().endsWith(".zip") || path.getPath().endsWith(".class")) {
            try {
               list.add(path.toURI().toURL());
            } catch (MalformedURLException e) {
               // we skip
            }
         }
      }
   }
   
   @Override
   protected URL findResource(String ressource) {
      if (basePathFile == null) {
         throw new RuntimeException("Not ready, use load() first");
      }
      
      Logger.debug("Trying to find ressource for: " + ressource);
      URL url = getParent().getResource(ressource);
      return url == null ? findRessource(basePathFile, ressource) : url;
   }
   
   @Override
   public void load(String basePath) {
      File baseFile = new File(basePath);
      if (!baseFile.isDirectory()) {
         throw new RuntimeException("Path given is not a directory: " + basePath);
      }
      if (!baseFile.canRead()) {
         throw new RuntimeException("Path is not readable: " + basePath);
      }
      
      basePathFile = baseFile;
      generateClassPath(basePathFile, urlList);
   }
   
   @Override
   public void unload() {
      basePathFile = null;
      urlList.clear();
      protectionDomains.clear();
   }
   
   @Override
   public Set<URL> getRessources() {
      return new HashSet<URL>(urlList);
   }
   
   @Override
   public ClassLoader getClassLoader() {
      return this;
   }
   
   @Override
   public Class<?> createClass(String name) throws ClassNotFoundException {
      return findClass(name);
   }
   
}
