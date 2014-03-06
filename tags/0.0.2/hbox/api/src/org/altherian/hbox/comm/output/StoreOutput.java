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

package org.altherian.hbox.comm.output;

import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.StoreAttributes;
import org.altherian.hbox.states.StoreState;

//TODO add getters/setters
public final class StoreOutput extends ObjectOutput {
   
   @SuppressWarnings("unused")
   private StoreOutput() {
      // used for (de)serialisation
   }
   
   public StoreOutput(String id, String label, String location, StoreState state) {
      super(id);
      setSetting(new StringSettingIO(StoreAttributes.Label, label));
      setSetting(new StringSettingIO(StoreAttributes.Location, location));
      setSetting(new StringSettingIO(StoreAttributes.State, state.toString()));
   }
   
   public String getLabel() {
      return getSetting(StoreAttributes.Label).getString();
   }
   
   public String getLocation() {
      return getSetting(StoreAttributes.Location).getString();
   }
   
   public StoreState getState() {
      return StoreState.valueOf(getSetting(StoreAttributes.State).getString());
   }
   
   @Override
   public String toString() {
      return getLabel();
   }
   
}
