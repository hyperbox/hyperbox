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

package org.altherian.hboxc.core;

import org.altherian.setting.SettingManager;
import org.altherian.setting._Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Entity implements _Entity {
   
   private String id;
   private SettingManager mgr = new SettingManager();
   
   public Entity(String id) {
      this.id = id;
   }
   
   @Override
   public List<_Setting> getSettings() {
      return mgr.list();
   }
   
   @Override
   public _Setting getSetting(String settingId) {
      return mgr.get(settingId);
   }
   
   @Override
   public void setSetting(_Setting setting) {
      mgr.set(setting);
   }
   
   @Override
   public void setSetting(List<_Setting> settings) {
      mgr.set(settings);
   }
   
   @Override
   public boolean hasSetting(String settingId) {
      return mgr.has(settingId);
   }
   
   @Override
   public void unsetSetting(String id) {
      mgr.unset(id);
   }
   
   @Override
   public void unsetSettings(Set<String> ids) {
      mgr.unset(new ArrayList<String>(ids));
   }
   
   @Override
   public String getId() {
      return id;
   }
   
}
