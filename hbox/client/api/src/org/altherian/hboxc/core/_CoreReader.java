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

package org.altherian.hboxc.core;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hboxc.comm.input.ConnectorInput;
import org.altherian.hboxc.comm.output.BackendOutput;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.comm.output.ConsoleViewerOutput;
import org.altherian.hboxc.server._ServerReader;
import org.altherian.hboxc.state.CoreState;

import java.util.List;

public interface _CoreReader {
   
   public CoreState getCoreState();
   
   public List<ConnectorOutput> listConnectors();
   
   public ConnectorOutput getConnector(ConnectorInput srvIn);
   
   public ConnectorOutput getConnector(String id);
   
   public ServerOutput getServer(ConnectorInput conIn);
   
   public ServerOutput getServerInfo(String id);

   public _ServerReader getServerReader(String id);
   
   public List<ConsoleViewerOutput> listConsoleViewers();
   
   public List<BackendOutput> listBackends();
   
}
