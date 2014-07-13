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

package org.altherian.hboxd.core.action.medium;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.MediumInput;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hbox.constant.MediumAttribute;
import org.altherian.hboxd.comm.io.factory.MediumIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.tool.AxStrings;
import org.altherian.tool.logging.Logger;

import java.util.Arrays;
import java.util.List;

public final class MediumGetAction extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.MediumGet.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      Logger.track();
      
      MediumInput medIn = request.get(MediumInput.class);
      
      MediumOutput medOut = null;
      if (!AxStrings.isEmpty(medIn.getUuid())) {
         medOut = MediumIoFactory.get(hbox.getHypervisor().getMedium(medIn.getUuid()));
      } else {
         medOut = MediumIoFactory.get(hbox.getHypervisor().getMedium(medIn.getLocation(), medIn.getSetting(MediumAttribute.Type).getString()));
      }
      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, medOut));
   }
   
}
