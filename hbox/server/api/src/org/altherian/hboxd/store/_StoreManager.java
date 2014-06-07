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

package org.altherian.hboxd.store;

import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxd.persistence._StorePersistor;

import java.util.List;

public interface _StoreManager {
   
   public void init(_StorePersistor persistor) throws HyperboxException;
   
   public void start() throws HyperboxException;
   
   public void stop();
   
   public List<_Store> listStores();
   
   public _Store getStore(String id);
   
   /**
    * Will create the store using the location given, register it under the given label and open the store
    * 
    * @param location Location of the store
    * @param label The label to apply
    * @return The newly created & registered Store
    */
   public _Store createStore(String location, String label);
   
   /**
    * Will register the store using the path given under the given label and open the store
    * 
    * @param location Full path for the store - must be a directory
    * @param label The label to apply
    * @return The newly registered Store
    */
   public _Store registerStore(String location, String label);
   
   /**
    * <p>
    * Will close the store and unregister it from the store list.<br/>
    * This operation will not delete the actual implementation.
    * </p>
    * 
    * @param id The Store ID to unregister
    */
   public void unregisterStore(String id);
   
   /**
    * <p>
    * Will close the store, unregister it and try to delete the implementation.<br/>
    * This call could fail if the store is not empty and the implementation doesn't allow the removal of non-empty containers.<br/>
    * </p>
    * 
    * @param id The Store ID to unregister & delete
    */
   public void deleteStore(String id);
   
}
