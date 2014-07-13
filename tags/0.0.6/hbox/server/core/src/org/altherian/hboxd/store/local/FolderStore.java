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
import org.altherian.hbox.states.StoreState;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.store.StoreStateEvent;
import org.altherian.hboxd.exception.store.StoreNotOpenException;
import org.altherian.hboxd.store._Store;
import org.altherian.hboxd.store._StoreItem;

import java.io.File;

public final class FolderStore implements _Store {
   
   private String id;
   private String name;
   private File location;
   private StoreState state;
   
   public FolderStore(String id, String name, File path) {
      state = StoreState.Closed;
      this.id = id;
      this.name = name;
      location = path;
   }
   
   public FolderStore(String id, String name, File path, StoreState state) {
      this(id, name, path);
      this.state = state;
   }
   
   private void setState(StoreState s) {
      state = s;
      EventManager.post(new StoreStateEvent(this));
   }
   
   @Override
   public String getId() {
      return id;
   }
   
   @Override
   public String getType() {
      return "localFolder";
   }
   
   @Override
   public String getLabel() {
      return name;
   }
   
   @Override
   public void open() {
      if (!getState().equals(StoreState.Closed)) {
         throw new HyperboxRuntimeException("Cannot open a store that is not closed.");
      }
      setState(StoreState.Opening);
      try {
         if (!location.exists()) {
            throw new HyperboxRuntimeException(location.getAbsolutePath() + " does not exist");
         }
         if (!location.isDirectory()) {
            throw new HyperboxRuntimeException(location.getAbsolutePath() + " is not a folder");
         }
         if (!location.isAbsolute()) {
            throw new HyperboxRuntimeException(location.getAbsolutePath() + " must be a full path");
         }
      } catch (HyperboxRuntimeException e) {
         close();
         throw e;
      }
      setState(StoreState.Open);
   }
   
   @Override
   public void close() {
      if (!isOpen()) {
         throw new StoreNotOpenException();
      }
      
      setState(StoreState.Closed);
   }
   
   @Override
   public _StoreItem getContainer() {
      if (!isOpen()) {
         throw new StoreNotOpenException();
      }
      
      return new FolderStoreItem(this, location.getAbsolutePath());
   }
   
   @Override
   public StoreState getState() {
      return state;
   }
   
   @Override
   public String getLocation() {
      return location.getAbsolutePath();
   }
   
   @Override
   public _StoreItem getItem(String path) {
      if (!isOpen()) {
         throw new StoreNotOpenException();
      }
      
      File newItemPath = path.startsWith(getLocation()) ? new File(path) : new File(getLocation() + path);
      if (!newItemPath.exists()) {
         throw new HyperboxRuntimeException(newItemPath.getAbsolutePath() + " is not a valid location");
      }
      if (!newItemPath.canRead()) {
         throw new HyperboxRuntimeException(newItemPath.getAbsolutePath() + " is not readable");
      }
      if (newItemPath.isDirectory()) {
         return new FolderStoreItem(this, newItemPath.getAbsolutePath());
      } else {
         return new FileStoreItem(this, newItemPath.getAbsolutePath());
      }
   }
   
   @Override
   public boolean isOpen() {
      return state.equals(StoreState.Open);
   }
   
}
