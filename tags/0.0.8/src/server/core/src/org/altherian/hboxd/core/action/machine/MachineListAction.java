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

package org.altherian.hboxd.core.action.machine;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.comm.io.factory.MachineIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.AbstractHyperboxMultiTaskAction;
import org.altherian.hboxd.core.model._Machine;
import org.altherian.hboxd.session.SessionContext;

import java.util.Arrays;
import java.util.List;

public final class MachineListAction extends AbstractHyperboxMultiTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.MachineList.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      if (!request.has(ServerIn.class)) {
         throw new HyperboxRuntimeException("missing serverinput");
      }
      ServerIn srvIn = request.get(ServerIn.class);
      
      List<_Machine> vms = hbox.getServer(srvIn.getId()).listMachines();

      for (_Machine vm : vms) {
         SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, MachineIoFactory.getSimple(vm)));
      }
   }
   
}
