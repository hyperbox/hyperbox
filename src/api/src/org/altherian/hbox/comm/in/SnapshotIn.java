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

import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.SnapshotAttribute;
import java.util.UUID;

public class SnapshotIn extends ObjectIn<EntityType> {

   public SnapshotIn() {
      super(EntityType.Snapshot);
   }

   public SnapshotIn(String id) {
      this();
      try {
         UUID uuid = UUID.fromString(id);
         setUuid(uuid.toString());
      } catch (IllegalArgumentException e) {
         setName(id);
      }
   }

   public String getUuid() {
      return getSetting(SnapshotAttribute.Uuid).getString();
   }

   public void setUuid(String uuid) {
      setSetting(new StringSettingIO(SnapshotAttribute.Uuid, uuid));
   }

   public String getName() {
      return getSetting(SnapshotAttribute.Name).getString();
   }

   public void setName(String name) {
      setSetting(new StringSettingIO(SnapshotAttribute.Name, name));
   }

   public String getDescription() {
      return getSetting(SnapshotAttribute.Description).getString();
   }

   public void setDescription(String desc) {
      setSetting(new StringSettingIO(SnapshotAttribute.Description, desc));
   }

}
