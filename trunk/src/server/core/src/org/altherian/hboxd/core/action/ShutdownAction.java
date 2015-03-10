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

package org.altherian.hboxd.core.action;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hboxd.controller._Controller;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.security.SecurityContext;
import org.altherian.tool.logging.Logger;
import java.util.Arrays;
import java.util.List;

public class ShutdownAction extends ASingleTaskAction {
   
   private static _Controller c;
   
   public static void setController(_Controller c) {
      
      if (ShutdownAction.c == null) {
         ShutdownAction.c = c;
      }
   }
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.HBOX.getId() + HyperboxTasks.ServerShutdown.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return true;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      
      Logger.info("Server shutdown requested by " + SecurityContext.getUser().getDomainLogonName());
      c.stop();
   }
   
}
