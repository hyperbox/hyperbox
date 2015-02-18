/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.hboxd.core.action.network;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.NetAdaptorIn;
import org.altherian.hbox.comm.io.NetServiceIO;
import org.altherian.hbox.hypervisor.net._NetService;
import org.altherian.hboxd.comm.io.factory.NetServiceIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ServerAction;
import org.altherian.hboxd.server._Server;
import org.altherian.hboxd.session.SessionContext;
import java.util.Arrays;
import java.util.List;

public class NetServiceGetAction extends ServerAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.NetServiceGet.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   protected void run(Request request, _Hyperbox hbox, _Server srv) {
      NetAdaptorIn adaptIn = request.get(NetAdaptorIn.class);
      NetServiceIO netSvcIn = request.get(NetServiceIO.class);
      _NetService netSvc = srv.getHypervisor().getNetAdaptor(adaptIn.getModeId(), adaptIn.getId()).getService(netSvcIn.getType());
      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, NetServiceIO.class, NetServiceIoFactory.get(netSvc)));
   }
   
}
