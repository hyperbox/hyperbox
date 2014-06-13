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

package org.altherian.hboxd.store.local;

import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.store._Store;
import org.altherian.hboxd.store._StoreItem;

import java.io.File;
import java.util.List;

public final class FileStoreItem implements _StoreItem {
   
   private _Store store;
   private File location;
   
   public FileStoreItem(_Store store, String path) {
      this.store = store;
      location = new File(path);
      if (!location.exists()) {
         throw new HyperboxRuntimeException(path + " does not exist");
      }
      if (location.isDirectory()) {
         throw new HyperboxRuntimeException(path + " is a folder");
      }
      if (!location.isAbsolute()) {
         throw new HyperboxRuntimeException(path + " must be a full path");
      }
   }
   
   @Override
   public String getName() {
      return location.getName();
   }
   
   @Override
   public boolean isContainer() {
      return false;
   }
   
   @Override
   public String getPath() {
      return location.getAbsolutePath();
   }
   
   @Override
   public long getSize() {
      return location.length();
   }
   
   @Override
   public List<_StoreItem> listItems() {
      throw new HyperboxRuntimeException("Cannot list items, this is not a container");
   }
   
   @Override
   public _Store getStore() {
      return store;
   }
   
}
