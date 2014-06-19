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

package org.altherian.hbox.comm.output;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.constant.ModuleAttributes;

import java.util.Collection;

public class ModuleOutput extends ObjectOutput {
   
   protected ModuleOutput() {
      // used for (de)serialisation
   }
   
   public ModuleOutput(String id) {
      super(id);
   }
   
   public ModuleOutput(String id, Collection<SettingIO> settings) {
      super(id, settings);
   }
   
   public String getDescriptorFile() {
      return getSetting(ModuleAttributes.DescriptorFile).getString();
   }

   public String getName() {
      return getSetting(ModuleAttributes.Name).getString();
   }
   
   public String getVersion() {
      return getSetting(ModuleAttributes.Version).getString();
   }
   
   public boolean isEnabled() {
      return getSetting(ModuleAttributes.isEnabled).getBoolean();
   }
   
   public boolean isLoaded() {
      return getSetting(ModuleAttributes.isLoaded).getBoolean();
   }
   
   @Override
   public String toString() {
      return "Module ID " + getId() + " (" + getName() + ")";
   }
   
}
