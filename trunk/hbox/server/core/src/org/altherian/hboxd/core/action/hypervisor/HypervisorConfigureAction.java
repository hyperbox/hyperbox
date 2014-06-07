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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxd.core.action.hypervisor;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.HypervisorInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hboxd.comm.io.factory.SettingIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.hypervisor.HypervisorConfigurationUpdateEvent;

import java.util.Arrays;
import java.util.List;

public class HypervisorConfigureAction extends ASingleTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.HBOX.getId() + HyperboxTasks.HypervisorConfigure.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return true;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      ServerInput srvIn = request.get(ServerInput.class);
      HypervisorInput hypIn = request.get(HypervisorInput.class);
      
      hbox.getServer(srvIn.getId()).getHypervisor().configure(SettingIoFactory.getListIo(hypIn.listSettings()));
      EventManager.post(new HypervisorConfigurationUpdateEvent(hbox.getServer(srvIn.getId())));
   }
   
}
