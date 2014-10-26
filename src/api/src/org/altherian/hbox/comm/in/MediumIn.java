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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.comm._Actionnable;
import org.altherian.hbox.comm.io.PositiveNumberSettingIO;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.Entity;
import org.altherian.hbox.constant.MediumAttribute;
import org.altherian.tool.AxStrings;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MediumIn extends ObjectIn<Entity> implements _Actionnable {
   
   private Action action = Action.Create;
   private String parentUuid = null;
   private Set<String> childUuid = new HashSet<String>();
   
   public MediumIn() {
      super(Entity.Medium);
   }
   
   public MediumIn(String uuid) {
      super(Entity.Medium, uuid);
      setUuid(uuid);
      setAction(Action.Modify);
   }
   
   public MediumIn(String path, String type) {
      super(Entity.Medium, path);
      setLocation(path);
      setType(type);
   }
   
   public MediumIn(StoreItemIn siIn, String type) {
      this(siIn.getPath(), type);
   }
   
   public MediumIn(String uuid, List<SettingIO> settings) {
      super(Entity.Medium, uuid, settings);
      setUuid(uuid);
      setAction(Action.Modify);
   }
   
   public MediumIn(String uuid, String location, String type) {
      this(uuid);
      setLocation(location);
      setType(type);
   }
   
   public MediumIn(String uuid, String location, String type, String parentUuid, Set<String> childUuid) {
      this(uuid, location, type);
      if (parentUuid != null) {
         this.parentUuid = parentUuid;
      }
      if (childUuid != null) {
         this.childUuid.addAll(childUuid);
      }
   }
   
   /**
    * Will return in preference of order : UUID, Location then an empty string if none of the previous are set
    */
   @Override
   public String getId() {
      if (!AxStrings.isEmpty(getUuid())) {
         return getUuid();
      } else if (!AxStrings.isEmpty(getLocation())) {
         return getLocation();
      } else {
         return "";
      }
   }
   
   public String getUuid() {
      return getSetting(MediumAttribute.UUID).getString();
   }
   
   public void setUuid(String uuid) {
      setSetting(new StringSettingIO(MediumAttribute.UUID, uuid));
   }
   
   public String getName() {
      return getSetting(MediumAttribute.Name).getString();
   }
   
   public void setName(String name) {
      setSetting(new StringSettingIO(MediumAttribute.Name, name));
   }
   
   public void setFormat(String format) {
      setSetting(new StringSettingIO(MediumAttribute.Format, format));
   }
   
   public String getFormat() {
      return getSetting(MediumAttribute.Format).getString();
   }
   
   public boolean hasParent() {
      return (parentUuid != null);
   }
   
   public String getLocation() {
      return getSetting(MediumAttribute.Location).getString();
   }
   
   public void setLocation(String path) {
      setSetting(new StringSettingIO(MediumAttribute.Location, path));
   }
   
   public String getDeviceType() {
      return getSetting(MediumAttribute.DeviceType).getString();
   }
   
   public void setDeviceType(String id) {
      setSetting(new StringSettingIO(MediumAttribute.DeviceType, id));
   }
   
   public String getType() {
      return getSetting(MediumAttribute.Type).getString();
   }
   
   public void setType(String devType) {
      setSetting(new StringSettingIO(MediumAttribute.Type, devType));
   }
   
   public String getParentUuid() {
      return parentUuid;
   }
   
   public boolean hasChild() {
      return ((childUuid != null) && childUuid.isEmpty());
   }
   
   public Set<String> getChildUuid() {
      return childUuid;
   }
   
   public String getBaseUuid() {
      return getSetting(MediumAttribute.BaseUUID).getString();
   }
   
   public boolean isReadOnly() {
      return getSetting(MediumAttribute.ReadOnly).getBoolean();
   }
   
   public Long getLogicalSize() {
      return getSetting(MediumAttribute.LogicalSize).getNumber();
   }
   
   public void setLogicalSize(Long logicalSize) {
      setSetting(new PositiveNumberSettingIO(MediumAttribute.LogicalSize, logicalSize));
   }
   
   @Override
   public String toString() {
      if (hasSetting(MediumAttribute.Name)) {
         return getSetting(MediumAttribute.Name).getString();
      } else if (hasSetting(MediumAttribute.UUID)) {
         return getUuid().toString();
      } else if (hasSetting(MediumAttribute.Location)) {
         return new File(getLocation()).getName();
      } else {
         return getId();
      }
   }
   
   @Override
   public Action getAction() {
      return action;
   }
   
   @Override
   public void setAction(Action action) {
      this.action = action;
   }
   
}
