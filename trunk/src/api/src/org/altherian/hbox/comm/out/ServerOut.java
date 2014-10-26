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

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.constant.Entity;
import org.altherian.hbox.constant.ServerAttribute;

import java.util.Collection;

public class ServerOut extends ObjectOut {
   
   protected ServerOut() {
      // used for (de)serialisation
   }
   
   public ServerOut(String id) {
      super(Entity.Server, id);
   }
   
   public ServerOut(String id, Collection<SettingIO> settings) {
      super(Entity.Server, id, settings);
   }
   
   public String getName() {
      return getSetting(ServerAttribute.Name).getString();
   }
   
   public String getType() {
      return getSetting(ServerAttribute.Type).getString();
   }
   
   public String getVersion() {
      return getSetting(ServerAttribute.Version).getString();
   }
   
   public String getNetworkProtocolVersion() {
      return getSetting(ServerAttribute.NetProtocolVersion).getString();
   }
   
   public Boolean isHypervisorConnected() {
      return getSetting(ServerAttribute.IsHypervisorConnected).getBoolean();
   }
   
   public String getLogLevel() {
      return getSetting(ServerAttribute.LogLevel).getString();
   }
   
   @Override
   public String toString() {
      return "Server ID #" + getId() + " (" + getName() + ")";
   }
   
}
