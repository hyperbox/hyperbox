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

package org.altherian.hbox.comm.out;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.constant.Entity;
import org.altherian.hbox.constant.ModuleAttribute;

import java.util.Collection;

public class ModuleOut extends ObjectOut {
   
   protected ModuleOut() {
      // used for (de)serialisation
   }
   
   public ModuleOut(String id) {
      super(Entity.Module, id);
   }
   
   public ModuleOut(String id, Collection<SettingIO> settings) {
      super(Entity.Module, id, settings);
   }
   
   public String getDescriptorFile() {
      return getSetting(ModuleAttribute.DescriptorFile).getString();
   }
   
   public String getName() {
      return getSetting(ModuleAttribute.Name).getString();
   }
   
   public String getVersion() {
      return getSetting(ModuleAttribute.Version).getString();
   }
   
   public boolean isEnabled() {
      return getSetting(ModuleAttribute.isEnabled).getBoolean();
   }
   
   public boolean isLoaded() {
      return getSetting(ModuleAttribute.isLoaded).getBoolean();
   }
   
   @Override
   public String toString() {
      return "Module ID " + getId() + " (" + getName() + ")";
   }
   
}
