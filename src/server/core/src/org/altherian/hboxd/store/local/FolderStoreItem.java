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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.store.local;

import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.store._Store;
import org.altherian.hboxd.store._StoreItem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class FolderStoreItem implements _StoreItem {

   private _Store store;
   private File location;

   public FolderStoreItem(_Store store, File path) {
      path = path.getAbsoluteFile();
      if (!path.exists()) {
         throw new HyperboxRuntimeException(path + " does not exist");
      }
      if (!path.isDirectory()) {
         throw new HyperboxRuntimeException(path + " is not a folder");
      }
      if (!path.isAbsolute()) {
         throw new HyperboxRuntimeException(path + " must be a full path");
      }

      this.store = store;
      location = path;
   }

   @Override
   public String getName() {
      return location.getName();
   }

   @Override
   public boolean isContainer() {
      return true;
   }

   @Override
   public String getPath() {
      return location.getAbsolutePath();
   }

   @Override
   public long getSize() {
      return 0;
   }

   @Override
   public List<_StoreItem> listItems() {
      List<_StoreItem> list = new ArrayList<_StoreItem>();
      if (location.isDirectory()) {
         for (File f : location.listFiles()) {
            if (f.isDirectory()) {
               list.add(new FolderStoreItem(store, f));
            } else if (f.isFile()) {
               list.add(new FileStoreItem(store, f));
            } else {
               list.add(new LinkStoreItem(store, f));
            }
         }
      }
      return list;
   }

   @Override
   public _Store getStore() {
      return store;
   }

}
