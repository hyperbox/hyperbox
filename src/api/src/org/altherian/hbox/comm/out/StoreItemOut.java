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

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.PositiveNumberSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.StoreItemAttribute;

public final class StoreItemOut extends ObjectOut {

   @SuppressWarnings("unused")
   private StoreItemOut() {
      // used for (de)serialisation
   }

   public StoreItemOut(String storeId, String name, String path, long size, boolean isContainer) {
      super(EntityType.StoreItem, name + "|" + path);
      setSetting(new StringSettingIO(StoreItemAttribute.StoreId, storeId));
      setSetting(new StringSettingIO(StoreItemAttribute.Name, name));
      setSetting(new StringSettingIO(StoreItemAttribute.Path, path));
      setSetting(new PositiveNumberSettingIO(StoreItemAttribute.Size, size));
      setSetting(new BooleanSettingIO(StoreItemAttribute.IsContainer, isContainer));
   }

   public String getStoreId() {
      return getSetting(StoreItemAttribute.StoreId).getString();
   }

   public String getName() {
      return getSetting(StoreItemAttribute.Name).getString();
   }

   public String getPath() {
      return getSetting(StoreItemAttribute.Path).getString();
   }

   public Long getSize() {
      return getSetting(StoreItemAttribute.Size).getNumber();
   }

   public Boolean isContainer() {
      return getSetting(StoreItemAttribute.IsContainer).getBoolean();
   }

   @Override
   public String toString() {
      return getName();
   }

}
