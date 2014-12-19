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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.comm.io.NullSettingIO;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.tool.AxStrings;
import org.altherian.tool.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ObjectIn<T extends Enum<?>> {
   
   private T entityTypeId;
   private String id = "";
   private boolean changed = false;
   private Map<String, SettingIO> settings = new HashMap<String, SettingIO>(5);
   
   private void setEntityTypeId(T entityTypeId) {
      this.entityTypeId = entityTypeId;
   }
   
   private void setInitSetting(List<SettingIO> settings) {
      setSetting(settings);
      changed = false;
   }
   
   public ObjectIn(T entityTypeId) {
      if (entityTypeId == null) {
         throw new IllegalArgumentException("Entity Type cannot be null");
      }
      
      setEntityTypeId(entityTypeId);
   }
   
   public ObjectIn(T entityTypeId, String id) {
      this(entityTypeId);
      
      if (AxStrings.isEmpty(id)) {
         throw new IllegalArgumentException("ID must have a value - Current value: " + id);
      }
      
      this.id = id;
   }
   
   public ObjectIn(T entityTypeId, List<SettingIO> settings) {
      this(entityTypeId);
      setInitSetting(settings);
   }
   
   public ObjectIn(T entityTypeId, String id, List<SettingIO> settings) {
      this(entityTypeId, id);
      setInitSetting(settings);
   }
   
   @Override
   public String toString() {
      return getId();
   }
   
   /**
    * See {@link EntityType} for natives values
    * 
    * @return Entity Type ID
    */
   public T getEntityTypeId() {
      return entityTypeId;
   }
   
   /**
    * Get the most significant ID value for this object. The significance can change depending on the state of the object.<br/>
    * The default behaviour returns the id value given in the constructors, or an empty string if none was given
    * 
    * @return The most significant ID as a String, or an empty string if no ID exists within the object.
    */
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
   // TODO add a check in equals() on SettingIO to keep track if a setting was set with an actual different value - related to hasNewData()
   public void setSetting(SettingIO sIo) {
      settings.put(sIo.getName(), sIo);
      changed = true;
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
      if (name.equalsIgnoreCase("id")) {
         return new StringSettingIO(name, id);
      }
      if (!hasSetting(name)) {
         Logger.debug(getClass().getName() + " does not containg the setting " + name + "; NullSetting will be returned instead");
         return new NullSettingIO(name);
      }
      return settings.get(name);
   }
   
   /**
    * Retrieve the setting for the given machine settings name.
    * 
    * @param name The MachineSettings enum id to use
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
      changed = true;
   }
   
   public void removeSetting(Enum<?> name) {
      removeSetting(name.toString());
   }
   
   public List<SettingIO> listSettings() {
      return new ArrayList<SettingIO>(settings.values());
   }
   
   /**
    * Check if this object settings changed from the last time they were set
    * 
    * @return true if any setting changed, false if not
    */
   // TODO revamp this so it performs the check better
   public boolean hasNewData() {
      return changed;
   }
   
}
