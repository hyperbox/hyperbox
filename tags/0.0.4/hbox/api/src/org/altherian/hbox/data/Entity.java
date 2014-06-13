/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hbox.data;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.exception.HyperboxRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Entity {
   
   private String id = "";
   private Map<String, SettingIO> settings = new HashMap<String, SettingIO>();
   
   public Entity() {
      
   }
   
   public Entity(String id) {
      this.id = id;
   }
   
   public Entity(List<SettingIO> settings) {
      setSetting(settings);
   }
   
   public Entity(String id, List<SettingIO> settings) {
      this(id);
      setSetting(settings);
   }
   
   @Override
   public String toString() {
      return getId();
   }
   
   public String getId() {
      return id;
   }
   
   public void setSetting(List<SettingIO> settings) {
      for (SettingIO setting : settings) {
         setSetting(setting);
      }
   }
   
   /**
    * Change this object config data according to the setting given.
    * 
    * @param sIo The SettingIO containing the setting data (name and value).
    */
   public void setSetting(SettingIO sIo) {
      settings.put(sIo.getName(), sIo);
   }
   
   /**
    * Retrieve the setting linked to the given name.
    * 
    * @param name The name of the wanted setting.
    * @return The setting object containing the value.
    * @throws HyperboxRuntimeException In case the setting does not exist.
    * @see #hasSetting(String)
    */
   public SettingIO getSetting(String name) {
      if (!hasSetting(name)) {
         throw new HyperboxRuntimeException("Setting [" + name + "] not found in " + this.getClass().getSimpleName());
      }
      return settings.get(name);
   }
   
   /**
    * Retrieve the setting for the given machine settings name.
    * 
    * @param name The Enum.toString() ID to use
    * @return a SettingIO object that contains the setting data.
    * @throws HyperboxRuntimeException In case the setting does not exist.
    * @see #hasSetting(Enum)
    */
   public SettingIO getSetting(Enum<?> name) {
      return getSetting(name.toString());
   }
   
   public boolean hasSetting(String name) {
      return settings.containsKey(name);
   }
   
   public boolean hasSetting(Enum<?> name) {
      return hasSetting(name.toString());
   }
   
   public void removeSetting(String name) {
      settings.remove(name);
   }
   
   public void removeSetting(Enum<?> name) {
      removeSetting(name.toString());
   }
   
   public List<SettingIO> listSettings() {
      return new ArrayList<SettingIO>(settings.values());
   }
   
}
