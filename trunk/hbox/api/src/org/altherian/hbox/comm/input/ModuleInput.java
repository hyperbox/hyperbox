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

package org.altherian.hbox.comm.input;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.ModuleAttributes;

import java.util.List;

public class ModuleInput extends ObjectInput {
   
   public ModuleInput() {
      super();
   }
   
   public ModuleInput(String id) {
      super(id);
   }
   
   public ModuleInput(String id, List<SettingIO> settings) {
      super(id, settings);
   }
   
   public String getDescriptorFile() {
      return getSetting(ModuleAttributes.DescriptorFile).getString();
   }
   
   public void setDescriptorFile(String filePath) {
      setSetting(new StringSettingIO(ModuleAttributes.DescriptorFile, filePath));
   }

   public String getName() {
      return getSetting(ModuleAttributes.Name).getString();
   }
   
   public void setName(String name) {
      setSetting(new StringSettingIO(ModuleAttributes.Name, name));
   }
   
   public String getVersion() {
      return getSetting(ModuleAttributes.Version).getString();
   }
   
   public void setVersion(String version) {
      setSetting(new StringSettingIO(ModuleAttributes.Version, version));
   }
   
   public boolean isEnabled() {
      return getSetting(ModuleAttributes.isEnabled).getBoolean();
   }
   
   public void setEnabled(boolean isEnabled) {
      setSetting(new BooleanSettingIO(ModuleAttributes.isEnabled, isEnabled));
   }
   
   public boolean isLoaded() {
      return getSetting(ModuleAttributes.isLoaded).getBoolean();
   }
   
   public void setLoaded(boolean isLoaded) {
      setSetting(new BooleanSettingIO(ModuleAttributes.isLoaded, isLoaded));
   }
   
}
