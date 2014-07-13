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

package org.altherian.hbox.comm.input;

import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.SnapshotAttributes;

import java.util.UUID;

public class SnapshotInput extends ObjectInput {
   
   public SnapshotInput() {
   }
   
   public SnapshotInput(String id) {
      try {
         UUID uuid = UUID.fromString(id);
         setUuid(uuid.toString());
      } catch (IllegalArgumentException e) {
         setName(id);
      }
   }
   
   public String getUuid() {
      return getSetting(SnapshotAttributes.Uuid).getString();
   }
   
   public void setUuid(String uuid) {
      setSetting(new StringSettingIO(SnapshotAttributes.Uuid, uuid));
   }
   
   public String getName() {
      return getSetting(SnapshotAttributes.Name).getString();
   }
   
   public void setName(String name) {
      setSetting(new StringSettingIO(SnapshotAttributes.Name, name));
   }
   
   public String getDescription() {
      return getSetting(SnapshotAttributes.Description).getString();
   }
   
   public void setDescription(String desc) {
      setSetting(new StringSettingIO(SnapshotAttributes.Description, desc));
   }
   
}
