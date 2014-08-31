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

package org.altherian.hbox.comm.out;

import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.Entity;
import org.altherian.hbox.constant.StoreAttribute;
import org.altherian.hbox.states.StoreState;

//TODO add getters/setters
public final class StoreOut extends ObjectOut {
   
   @SuppressWarnings("unused")
   private StoreOut() {
      // used for (de)serialisation
   }
   
   public StoreOut(String id, String label, String location, StoreState state) {
      super(Entity.Store, id);
      setSetting(new StringSettingIO(StoreAttribute.Label, label));
      setSetting(new StringSettingIO(StoreAttribute.Location, location));
      setSetting(new StringSettingIO(StoreAttribute.State, state.toString()));
   }
   
   public String getLabel() {
      return getSetting(StoreAttribute.Label).getString();
   }
   
   public String getLocation() {
      return getSetting(StoreAttribute.Location).getString();
   }
   
   public StoreState getState() {
      return StoreState.valueOf(getSetting(StoreAttribute.State).getString());
   }
   
   @Override
   public String toString() {
      return getLabel();
   }
   
}
