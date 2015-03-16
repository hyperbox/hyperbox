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
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.ServerAttribute;

public class ServerIn extends ObjectIn<EntityType> {

   public ServerIn() {
      super(EntityType.Server);
   }

   public ServerIn(String id) {
      super(EntityType.Server, id);
   }

   public ServerIn(ServerOut srvOut) {
      this(srvOut.getId());
   }

   public void setName(String name) {
      setSetting(new StringSettingIO(ServerAttribute.Name, name));
   }

   public void setLogLevel(String logLevel) {
      setSetting(new StringSettingIO(ServerAttribute.LogLevel, logLevel));
   }

}
