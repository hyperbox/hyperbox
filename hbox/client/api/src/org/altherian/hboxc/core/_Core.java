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

import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.back._Backend;
import org.altherian.hboxc.comm.input.ConnectorInput;
import org.altherian.hboxc.core.connector._Connector;
import org.altherian.hboxc.server._Server;
import org.altherian.hboxc.state.CoreState;

import java.util.List;

public interface _Core {
   
   public void init() throws HyperboxException;
   
   public void start() throws HyperboxException;
   
   public void stop();
   
   public void destroy();
   
   public CoreState getCoreState();
   
   public _Backend getBackend(String id);
   
   public List<String> listBackends();
   
   public List<_Connector> listConnector();
   
   public _Connector getConnector(String id);
   
   public _Connector addConnector(ConnectorInput conIn, UserInput usrIn);
   
   public _Connector modifyConnector(ConnectorInput conIn, UserInput usrIn);
   
   public _Connector connect(String id);
   
   public void disconnect(String id);
   
   public void removeConnector(String id);
   
   public _ConsoleViewer addConsoleViewer(String hypervisorId, String moduleId, String viewerPath);
   
   public void removeConsoleViewer(String id);
   
   public _ConsoleViewer getConsoleViewer(String id);
   
   public _ConsoleViewer findConsoleViewer(String hypervisorId, String moduleId);
   
   public List<_ConsoleViewer> listConsoleViewer();
   
   public List<_ConsoleViewer> listConsoleViewer(String hypervisorTypeId);
   
   public _Server getServer(String serverId);
   
   public _Connector getConnectorForServer(String serverId);
   
}
