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

package org.altherian.hboxc.core.server;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.DeviceIn;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.factory.SettingIoFactory;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hboxc.comm.utils.Transaction;
import org.altherian.hboxc.server._Console;
import org.altherian.hboxc.server._Machine;

public class Console extends Device implements _Console {
   
   public Console(_Machine machine) {
      super(machine, EntityType.Console.getId());
      refresh();
   }
   
   @Override
   public String getType() {
      return EntityType.Console.getId();
   }
   
   @Override
   public void refresh() {
      Request req = new Request(Command.VBOX, HypervisorTasks.DevicePropertyList);
      req.set(MachineIn.class, new MachineIn(getMachine().getId()));
      req.set(DeviceIn.class, new DeviceIn(getId()));
      Transaction t = getMachine().getServer().sendRequest(req);
      setSetting(SettingIoFactory.getListIo(t.extractItems(SettingIO.class)));
   }
   
}