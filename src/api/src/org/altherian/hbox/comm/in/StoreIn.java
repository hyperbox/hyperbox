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
import org.altherian.hbox.constant.StoreAttribute;

// TODO add getters/setters
public final class StoreIn extends ObjectIn<EntityType> {
   
   public StoreIn() {
      super(EntityType.Store, "-1");
   }
   
   public StoreIn(String id) {
      super(EntityType.Store, id);
   }
   
   public void setLabel(String label) {
      setSetting(new StringSettingIO(StoreAttribute.Label, label));
   }
   
   public void setType(String storeType) {
      setSetting(new StringSettingIO(StoreAttribute.Type, storeType));
   }
   
   public void setLocation(String location) {
      setSetting(new StringSettingIO(StoreAttribute.Location, location));
   }
   
}
