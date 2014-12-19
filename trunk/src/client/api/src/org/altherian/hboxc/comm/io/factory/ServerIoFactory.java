/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxc.comm.io.factory;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.constant.ServerAttribute;
import org.altherian.hboxc.server._Server;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServerIoFactory {
   
   public static ServerOut get(_Server srv) {
      List<SettingIO> settings = new ArrayList<SettingIO>();
      settings.add(new StringSettingIO(ServerAttribute.Name, srv.getName()));
      settings.add(new StringSettingIO(ServerAttribute.Type, srv.getType()));
      settings.add(new StringSettingIO(ServerAttribute.Version, srv.getVersion()));
      settings.add(new BooleanSettingIO(ServerAttribute.IsHypervisorConnected, srv.isHypervisorConnected()));
      settings.add(new StringSettingIO(ServerAttribute.NetProtocolVersion, srv.getProtocolVersion()));
      settings.add(new StringSettingIO(ServerAttribute.LogLevel, srv.getLogLevel()));
      ServerOut srvOut = new ServerOut(srv.getId(), settings);
      return srvOut;
   }
   
   public static List<ServerOut> getList(Collection<_Server> objList) {
      List<ServerOut> listOut = new ArrayList<ServerOut>();
      for (_Server obj : objList) {
         listOut.add(get(obj));
      }
      return listOut;
   }
   
}
