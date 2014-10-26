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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxd.module;

import org.altherian.hboxd.exception.ModuleException;

import java.net.URL;
import java.util.Set;

/**
 * A Module allows to provide custom services to Hyperbox like extra Hypervisors connectors, new Store backends, etc.
 * <p>
 * A Module is implemented into a Descriptor File which is an XML file located within the Server Modules Directory.<br/>
 * It can provide any kind of service, referring to them via the interface(s) that these implements.<br/>
 * The Descriptor file will be picked up by the {@link _ModuleManager}. Module ID and Descriptor file path are linked and are both considered valid
 * unique identifiers, so it wouldn't be possible to have two modules registered under different IDs with the same Descriptor file or the other way
 * around.
 * </p>
 * <p>
 * A typical descriptor file will look like this:
 * 
 * <pre>
 * &lt;module&gt;
 *    &lt;id&gt;vbox-4.3-xpcom&lt;/id&gt;
 *    &lt;path&gt;vbox-4.3-xpcom&lt;/path&gt;
 *    &lt;name&gt;VirtualBox 4.3 XPCOM Connector&lt;/name&gt;
 *    &lt;desc&gt;This module provides the UNIX-based XPCOM connector for VirtualBox 4.3.&lt;/desc&gt;
 *    &lt;version&gt;1&lt;/version&gt;
 *    &lt;vendor&gt;Altherian&lt;/vendor&gt;
 *    &lt;url&gt;http://hyperbox.altherian.org&lt;/url&gt;
 *    &lt;providers&gt;
 *       &lt;provider&gt;
 *          &lt;type&gt;org.altherian.hboxd.hypervisor._Hypervisor&lt;/type&gt;
 *          &lt;impl&gt;org.altherian.hboxd.vbox4_3.xpcom.VBoxXpcomHypervisor&lt;/impl&gt;
 *       &lt;/provider&gt;
 *    &lt;/providers&gt;
 * &lt;/module&gt;
 * 
 * </pre>
 * 
 * See the template in the sources for a more detail description of each possible element.<br/>
 * </p>
 * <p>
 * A Module can either be enabled or disabled, and loaded or not. These states can only flow this way :
 * <ul>
 * <li>Disabled & Unloaded -> Enabled & Unloaded</li>
 * <li>Enabled & Unloaded -> Enabled & Loaded</li>
 * <li>Enabled & Loaded -> Enabled & Unloaded</li>
 * <li>Enabled & Unloaded -> Disabled & Unloaded</li>
 * </ul>
 * </p>
 * 
 * @author noteirak
 * 
 */
public interface _Module {
   
   /**
    * Return this module's ID.
    * 
    * @return a String uniquely identifying the module.
    */
   public String getId();
   
   /**
    * Return this module's Name, which is used for display purpose.
    * 
    * @return a String for this module name, or null/empty String if none was given.
    */
   public String getName();
   
   /**
    * Return this module file descriptor absolute path.
    * 
    * @return the absolute path to the file descriptor as String
    */
   public String getDescriptor();
   
   /**
    * Return this module location, either base path or single file.
    * 
    * @return The absolute path to this module file(s)..
    */
   public String getLocation();
   
   /**
    * Return the module version. The format will solely depend on the module.
    * 
    * @return version as String.
    */
   public String getVersion();
   
   /**
    * Return the module's vendor given in the Descriptor File.
    * 
    * @return String of the module vendor, or null/empty String if none was provided.
    */
   public String getVendor();
   
   /**
    * Return the module's website given in the Descriptor File.
    * 
    * @return String of the module's website or null/empty String if none was provided.
    */
   public String getUrl();
   
   /**
    * Return the resources that compose this module.
    * 
    * @return {@link Set} of {@link URL} pointing to the Java code for this module.
    */
   public Set<URL> getRessources();
   
   /**
    * Return the set of providers type this module offers.
    * 
    * @return a {@link Set} of {@link URL} for the generic type of providers.
    */
   public Set<Class<?>> getTypes();
   
   /**
    * Return the specific provider for the generic type.
    * 
    * @param type The generic type of the provider.
    * @return The {@link Class} of the provider.
    * @throws ModuleException If an error occurred during Class initialisation.
    */
   public Class<?> getProvider(Class<?> type) throws ModuleException;
   
   /**
    * Return an object instance of the provider for the given generic type using the {@link _ModuleClassLoader} within this module instance.
    * 
    * @param type The generic type of the provider.
    * @return An object instance of the provider.
    * @throws ModuleException If an error occurred during the instance creation.
    */
   public Object buildProvider(Class<?> type) throws ModuleException;
   
   /**
    * Load this module, generating the provider type's classes and the provider's classes.
    * 
    * @throws ModuleException If an error occurred during the load process.
    */
   public void load() throws ModuleException;
   
   /**
    * Unload the module.
    * 
    * @throws ModuleException If an error occurred during unload or if the module does not support it.
    */
   public void unload() throws ModuleException;
   
   /**
    * Indicates if the module is loaded.
    * 
    * @return true if the module is loaded, else false.
    */
   public boolean isLoaded();
   
   /**
    * Enable this module.
    * 
    * @throws ModuleException If an error occurred.
    */
   public void enable() throws ModuleException;
   
   /**
    * Disable this module. This will unload the module if it is loaded.
    */
   public void disable();
   
   /**
    * Indicates if the module is enabled.
    * 
    * @return true if the module is enabled, else false.
    */
   public boolean isEnabled();
   
   /**
    * Indicates if the module is enabled, loaded and ready for usage.
    * 
    * @return true if the module is ready, else false.
    */
   public boolean isReady();
   
}
