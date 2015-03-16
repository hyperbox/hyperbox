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

import java.net.URL;
import java.util.Set;

/**
 * A Module Class Loader is the magic behind the scenes that allow java code to be added, reloaded or removed at runtime without any packages
 * conflicts or others known initial limitations of Java.
 * <p>
 * Module ClassLoader will be internally made of a custom implementation of a ClassLoader. For an example, see the Server implementation for the
 * default ModuleClassLoader class.<br/>
 * The Module ClassLoader should scan a base path and all its sub-paths for any java code (typically, in .jar, .zip and .class files) to be able to
 * provide the following essentials services :
 * <ul>
 * <li>Load & unload implementation.
 * <li>Return a Class implementation give its full qualified name (e.g. org.altherian.hboxd.module._ModuleClassLoader) if such class can be found
 * within the module files.</li>
 * <li>Return the set of all found Java code in a Set of URL pointing to the Java files.</li>
 * </ul>
 * </p>
 * 
 * @author noteirak
 * 
 */
public interface _ModuleClassLoader {

   /**
    * Initialise this module class loader with the give base path and search for Java resources.
    * 
    * @param basePath Path (file or directory) given as String. Supported values are implementation specific.
    */
   public void load(String basePath);

   /**
    * Returns the internal implementation of {@link ClassLoader} for this particular ModuleClassLoader instance.
    * 
    * @return Custom implementation of {@link ClassLoader}.
    */
   public ClassLoader getClassLoader();

   /**
    * Return the list of Java code also called resources as a set of URL pointing to the relevant files.
    * 
    * @return a {@link Set} of {@link URL} for all the Java resources found during {@link #load(String)}.
    */
   public Set<URL> getRessources();

   /**
    * Returns this module implementation of the given {@link Class} name, or its parent if none is found.
    * <p>
    * The implementation will prefer a Class within the module over the Parent ClassLoader's one to be consistent with the notion of module.
    * </p>
    * 
    * @param name The fully qualified name for the class in the format <code>com.package.class.name</code>
    * @return The implementation within the module, or the parent class loader (typically, the JVM one).
    * @throws ClassNotFoundException If no class under that name was found wihtin the module or in the parent class loader.
    */
   public Class<?> createClass(String name) throws ClassNotFoundException;

   /**
    * Unload and release any resources linked by this ModuleClassLoader, including its own internal ClassLoader.
    */
   public void unload();

}
