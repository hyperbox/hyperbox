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

package org.altherian.hbox.comm.out;

import org.altherian.hbox.comm.io.NullSettingIO;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.utils.Settings;
import org.altherian.tool.logging.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ObjectOut {

   private String entityTypeId;
   private String id;
   private Map<String, SettingIO> settingsMap = new HashMap<String, SettingIO>(5);

   private void setEntityTypeId(String entityTypeId) {
      this.entityTypeId = entityTypeId;
   }

   private void setEntityTypeId(Enum<?> entityTypeId) {
      this.entityTypeId = entityTypeId.toString();
   }

   protected void setSetting(SettingIO sIo) {
      settingsMap.put(sIo.getName(), sIo);
   }

   protected void setSetting(Collection<SettingIO> sIoList) {
      for (SettingIO set : sIoList) {
         setSetting(set);
      }
   }

   protected ObjectOut() {
      setEntityTypeId(EntityType.Unknown);
   }

   public ObjectOut(String entityTypeId, String id) {
      setEntityTypeId(entityTypeId);
      this.id = id;
   }

   public ObjectOut(Enum<?> entityTypeId, String id) {
      setEntityTypeId(entityTypeId);
      this.id = id;
   }

   public ObjectOut(String entityTypeId, String id, Collection<SettingIO> settings) {
      this(entityTypeId, id);
      setSetting(settings);
   }

   public ObjectOut(Enum<?> entityTypeId, String id, Collection<SettingIO> settings) {
      this(entityTypeId, id);
      setSetting(settings);
   }

   /**
    * See {@link EntityType} for natives values
    *
    * @return Entity Type ID
    */
   public String getEntityTypeId() {
      return entityTypeId;
   }

   public String getId() {
      return id;
   }

   public SettingIO getSetting(String name) {
      if (name.equalsIgnoreCase("id")) {
         return new StringSettingIO(name, id);
      }
      if (!hasSetting(name)) {
         Logger.debug(getClass().getName() + " does not containg the setting " + name + "; NullSetting will be returned instead");
         return new NullSettingIO(name);
      }
      return settingsMap.get(name);
   }

   /**
    * Retrieve the setting linked to the given name.
    *
    * @param name The name of the wanted setting.
    * @return a SettingIO object that contains the setting data, or null if no setting under the ID could be found.
    */
   public SettingIO getSetting(Object name) {
      return getSetting(Settings.getUniqueId(name));
   }

   public boolean hasSetting(Object name) {
      if (name.toString().equalsIgnoreCase("id")) {
         return true;
      }

      return settingsMap.containsKey(Settings.getUniqueId(name));
   }

   /**
    * This return the list of settings names in this IO object
    *
    * @return a Set of setting ID as String
    */
   // TODO be clear that this is only for settings actually included in the object, and create a method to list possible settings
   public Set<String> listSettingsId() {
      return new HashSet<String>(settingsMap.keySet());
   }

   public List<SettingIO> listSettings() {
      return new ArrayList<SettingIO>(settingsMap.values());
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (id.contentEquals(obj.toString())) {
         return true;
      }
      if (!(obj instanceof ObjectOut)) {
         return false;
      }
      ObjectOut other = (ObjectOut) obj;
      if (id == null) {
         if (other.id != null) {
            return false;
         }
      } else if (!id.equals(other.id)) {
         return false;
      }
      return true;
   }

}
