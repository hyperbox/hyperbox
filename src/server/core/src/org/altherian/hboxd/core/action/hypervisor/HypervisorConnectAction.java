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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.core.action.hypervisor;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.HypervisorIn;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import java.util.Arrays;
import java.util.List;

public class HypervisorConnectAction extends ASingleTaskAction {

   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.HBOX.getId() + HyperboxTasks.HypervisorConnect.getId());
   }

   @Override
   public boolean isQueueable() {
      return true;
   }

   @Override
   public void run(Request request, _Hyperbox hbox) {
      HypervisorIn hypIn = request.get(HypervisorIn.class);
      hbox.getServerManager().getServer().connect(hypIn.getId(), hypIn.getConnectOptions());
   }

}
