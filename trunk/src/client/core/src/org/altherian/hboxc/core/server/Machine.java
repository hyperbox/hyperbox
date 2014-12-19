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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.core.server;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.SnapshotIn;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hboxc.core.Entity;
import org.altherian.hboxc.server._Console;
import org.altherian.hboxc.server._Device;
import org.altherian.hboxc.server._Machine;
import org.altherian.hboxc.server._Server;
import java.util.HashMap;
import java.util.Map;

public class Machine extends Entity implements _Machine {
   
   private _Server srv;
   //private String id;
   //private MachineOut mOut;
   private Map<String, _Device> devices = new HashMap<String, _Device>();
   
   public Machine(_Server srv, String id) {
      super(id);
      this.srv = srv;
      refresh();
   }
   
   public void refresh() {
      /*
      Request req = new Request(Command.VBOX, HypervisorTasks.MachineGet);
      req.set(new MachineIn(id));
      Transaction t = srv.sendRequest(req);
      mOut = t.extractItem(MachineOut.class);
       */
   }
   
   @Override
   public _Console getConsole() {
      if (!devices.containsKey(EntityType.Console.getId())) {
         devices.put(EntityType.Console.getId(), new Console(this));
      }
      
      return (_Console) devices.get(EntityType.Console.getId());
   }
   
   @Override
   public _Server getServer() {
      return srv;
   }
   
   @Override
   public void takeSnapshot(SnapshotIn snapshotIn) {
      Request req = new Request(Command.VBOX, HypervisorTasks.SnapshotTake);
      req.set(MachineIn.class, new MachineIn(getId()));
      req.set(SnapshotIn.class, snapshotIn);
      srv.sendRequest(req);
   }
   
}
