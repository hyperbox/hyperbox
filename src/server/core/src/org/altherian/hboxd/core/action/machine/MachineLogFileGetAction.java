/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
 * <max@altherian.org>
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
package org.altherian.hboxd.core.action.machine;

import java.util.Arrays;
import java.util.List;
import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.io.MachineLogFileIO;
import org.altherian.hbox.comm.io.factory.MachineLogFileIoFactory;
import org.altherian.hbox.hypervisor._MachineLogFile;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ServerAction;
import org.altherian.hboxd.server._Server;
import org.altherian.hboxd.session.SessionContext;

public class MachineLogFileGetAction extends ServerAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.MachineLogFileGet.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   protected void run(Request request, _Hyperbox hbox, _Server srv) {
      MachineLogFileIO logfile = request.get(MachineLogFileIO.class);
      MachineIn machine = request.get(MachineIn.class);
      
      _MachineLogFile log = srv.getHypervisor().getLogFile(machine.getId(), Long.parseLong(logfile.getId()));
      MachineLogFileIO logIo = MachineLogFileIoFactory.get(log);
      
      // FIXME send(MachineLogFileIO.class, logIo);
      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, MachineLogFileIO.class, logIo));
   }
   
}
