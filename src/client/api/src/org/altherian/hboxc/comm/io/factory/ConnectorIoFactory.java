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
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.constant.ConnectorAttributes;
import org.altherian.hboxc.core.connector._Connector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConnectorIoFactory {

   public static ConnectorOutput get(_Connector conn) {
      List<SettingIO> settings = new ArrayList<SettingIO>();
      settings.add(new StringSettingIO(ConnectorAttributes.Label, conn.getLabel()));
      settings.add(new StringSettingIO(ConnectorAttributes.Address, conn.getAddress()));
      settings.add(new StringSettingIO(ConnectorAttributes.BackendId, conn.getBackendId()));
      settings.add(new BooleanSettingIO(ConnectorAttributes.isConnected, conn.isConnected()));
      settings.add(new SettingIO(ConnectorAttributes.ConnectionState, conn.getState()));
      settings.add(new StringSettingIO(ConnectorAttributes.Username, conn.getUsername()));
      settings.add(new StringSettingIO(ConnectorAttributes.ServerId, conn.getServerId()));
      if (conn.isConnected()) {
         settings.add(new SettingIO(ConnectorAttributes.Server, ServerIoFactory.get(conn.getServer())));
      }
      ConnectorOutput srvOut = new ConnectorOutput(conn.getId(), settings);
      return srvOut;
   }

   public static List<ConnectorOutput> getList(Collection<_Connector> objList) {
      List<ConnectorOutput> listOut = new ArrayList<ConnectorOutput>();
      for (_Connector obj : objList) {
         listOut.add(get(obj));
      }
      return listOut;
   }

}
