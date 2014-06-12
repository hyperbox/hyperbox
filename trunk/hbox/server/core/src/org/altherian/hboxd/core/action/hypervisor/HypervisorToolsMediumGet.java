/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxd.core.action.hypervisor;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hboxd.comm.io.factory.MediumIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.tool.logging.Logger;

import java.util.Arrays;
import java.util.List;

public class HypervisorToolsMediumGet extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.ToolsMediumGet.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      MediumOutput medOut = null;
      
      if (hbox.getHypervisor().hasToolsMedium()) {
         _RawMedium med = hbox.getHypervisor().getToolsMedium();
         Logger.debug("Hypervisor has tools medium - Location: " + med.getLocation());
         medOut = MediumIoFactory.get(med);
      } else {
         Logger.debug("Hypervisor does not have tools medium");
      }
      
      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, MediumOutput.class, medOut));
   }
   
}
