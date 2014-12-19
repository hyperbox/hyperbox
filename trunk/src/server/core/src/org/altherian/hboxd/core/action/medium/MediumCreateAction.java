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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.core.action.medium;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MediumIn;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hboxd.comm.io.factory.MediumIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.core.model._Medium;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.tool.logging.Logger;
import java.util.Arrays;
import java.util.List;
import com.google.common.io.Files;

public final class MediumCreateAction extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.MediumCreate.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return true;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      ServerIn srvIn = request.get(ServerIn.class);
      MediumIn medIn = request.get(MediumIn.class);
      
      Logger.debug("Creating a new hard disk at location [" + medIn.getLocation() + "] with format [" + medIn.getFormat() + "] and size ["
            + medIn.getLogicalSize() + "]");
      Logger.debug("File extension: " + Files.getFileExtension(medIn.getLocation()));
      if (Files.getFileExtension(medIn.getLocation()).isEmpty()) {
         Logger.debug("Will add extention to filename: " + medIn.getFormat().toLowerCase());
         medIn.setLocation(medIn.getLocation() + "." + medIn.getFormat().toLowerCase());
      } else {
         Logger.debug("No need to add extension");
      }
      
      _Medium med = hbox.getServer(srvIn.getId()).createMedium(medIn.getLocation(), medIn.getFormat(), medIn.getLogicalSize());
      MediumOut medOut = MediumIoFactory.get(med);
      SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, medOut));
   }
   
}
