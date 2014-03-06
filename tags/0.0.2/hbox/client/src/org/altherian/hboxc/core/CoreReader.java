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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.core;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hboxc.comm.input.ConnectorInput;
import org.altherian.hboxc.comm.io.factory.BackendIoFactory;
import org.altherian.hboxc.comm.io.factory.ConnectorIoFactory;
import org.altherian.hboxc.comm.io.factory.ConsoleViewerIoFactory;
import org.altherian.hboxc.comm.io.factory.ServerIoFactory;
import org.altherian.hboxc.comm.output.BackendOutput;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.comm.output.ConsoleViewerOutput;
import org.altherian.hboxc.factory.BackendFactory;
import org.altherian.hboxc.server._ServerReader;
import org.altherian.hboxc.state.CoreState;

import java.util.List;

public class CoreReader implements _CoreReader {
   
   private _Core core;
   
   // private Map<_ServerReader, _ServerReader> cachedServerReaders = new WeakHashMap<_ServerReader, _ServerReader>();
   
   public CoreReader(_Core core) {
      this.core = core;
   }
   
   @Override
   public CoreState getCoreState() {
      return core.getCoreState();
   }
   
   @Override
   public List<ConnectorOutput> listConnectors() {
      return ConnectorIoFactory.getList(core.listConnector());
   }
   
   @Override
   public ConnectorOutput getConnector(ConnectorInput conIn) {
      return getConnector(conIn.getId());
   }
   
   @Override
   public _ServerReader getServerReader(String id) {
      // TODO think of caching data in the client
      /*
      _ServerReader cachedReader = cachedServerReaders.get(core.getServer(id));
      if (cachedReader == null) {
         cachedReader = new CachedServerReader(core.getServer(id));
         cachedServerReaders.put(core.getServer(id), cachedReader);
      }
       */
      return core.getServer(id);
   }
   
   @Override
   public List<ConsoleViewerOutput> listConsoleViewers() {
      return ConsoleViewerIoFactory.getOutList(core.listConsoleViewer());
   }
   
   @Override
   public List<BackendOutput> listBackends() {
      return BackendIoFactory.getListId(BackendFactory.list());
   }
   
   @Override
   public ServerOutput getServer(ConnectorInput conIn) {
      return ServerIoFactory.get(core.getConnector(conIn.getId()).getServer());
   }
   
   @Override
   public ConnectorOutput getConnector(String id) {
      return ConnectorIoFactory.get(core.getConnector(id));
   }
   
   @Override
   public ServerOutput getServerInfo(String id) {
      return ServerIoFactory.get(core.getServer(id));
   }
   
}
