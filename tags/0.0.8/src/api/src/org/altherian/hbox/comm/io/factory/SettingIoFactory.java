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

package org.altherian.hbox.comm.io.factory;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.PositiveNumberSettingIO;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.setting.BooleanSetting;
import org.altherian.setting.PositiveNumberSetting;
import org.altherian.setting.StringSetting;
import org.altherian.setting._Settable;
import org.altherian.setting._Setting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SettingIoFactory {
   
   public static SettingIO get(_Setting s) {
      if (s instanceof StringSetting) {
         return new StringSettingIO(s.getName(), ((StringSetting) s).getValue());
      } else if (s instanceof PositiveNumberSetting) {
         return new PositiveNumberSettingIO(s.getName(), ((PositiveNumberSetting) s).getValue());
      } else if (s instanceof BooleanSetting) {
         return new BooleanSettingIO(s.getName(), ((BooleanSetting) s).getValue());
      } else {
         // TODO add support for serializable
         return new StringSettingIO(s.getName(), s.getRawValue().toString());
      }
   }
   
   public static _Setting get(SettingIO sIo) {
      if (sIo instanceof StringSettingIO) {
         return new StringSetting(sIo.getName(), sIo.getString());
      } else if (sIo instanceof PositiveNumberSettingIO) {
         return new PositiveNumberSetting(sIo.getName(), sIo.getNumber());
      } else if (sIo instanceof BooleanSettingIO) {
         return new BooleanSetting(sIo.getName(), sIo.getBoolean());
      } else {
         // TODO add support for serializable
         return new StringSetting(sIo.getName(), sIo.getRawValue().toString());
      }
   }

   public static List<_Setting> getListIo(Collection<SettingIO> listIo) {
      List<_Setting> listSettings = new ArrayList<_Setting>();
      for (SettingIO settingIo : listIo) {
         listSettings.add(get(settingIo));
      }
      return listSettings;
   }
   
   public static List<SettingIO> getList(_Settable s) {
      return getList(s.getSettings());
   }
   
   public static List<SettingIO> getList(Collection<_Setting> settings) {
      List<SettingIO> settingsIo = new ArrayList<SettingIO>();
      for (_Setting setting : settings) {
         if (setting != null) {
            settingsIo.add(get(setting));
         }
      }
      return settingsIo;
   }
   
}
