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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.ModuleOutput;
import org.altherian.hbox.constant.ModuleAttributes;
import org.altherian.hboxd.module._Module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModuleIoFactory {
   
   private ModuleIoFactory() {
      // static class, cannot be instantiated
   }
   
   public static ModuleOutput get(_Module mod) {
      List<SettingIO> settings = new ArrayList<SettingIO>();
      settings.add(new StringSettingIO(ModuleAttributes.DescriptorFile, mod.getDescriptor()));
      settings.add(new StringSettingIO(ModuleAttributes.Name, mod.getName()));
      settings.add(new StringSettingIO(ModuleAttributes.Version, mod.getVersion()));
      settings.add(new BooleanSettingIO(ModuleAttributes.isEnabled, mod.isEnabled()));
      settings.add(new BooleanSettingIO(ModuleAttributes.isLoaded, mod.isLoaded()));
      return new ModuleOutput(mod.getId(), settings);
   }
   
   public static List<ModuleOutput> get(Collection<_Module> mods) {
      List<ModuleOutput> modsOut = new ArrayList<ModuleOutput>();
      for (_Module mod : mods) {
         modsOut.add(get(mod));
      }
      return modsOut;
   }
   
}
