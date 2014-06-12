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

import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorDisconnectedEventOutput;
import org.altherian.hbox.comm.output.event.hypervisor.HypervisorEventOutput;
import org.altherian.hbox.comm.output.hypervisor.HypervisorOutput;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hboxc.comm.utils.Transaction;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.server._HypervisorReader;
import org.altherian.hboxc.server._Server;
import org.altherian.tool.logging.Logger;

public class HypervisorReader implements _HypervisorReader {
   
   private _Server srv;
   private HypervisorOutput hypData;
   
   private void refresh() {
      Logger.track();
      
      if (srv.isHypervisorConnected()) {
         Transaction t = srv.sendRequest(new Request(Command.HBOX, HyperboxTasks.HypervisorGet));
         hypData = t.extractItem(HypervisorOutput.class);
      } else {
         hypData = null;
      }
      
   }
   
   public HypervisorReader(_Server srv) {
      this.srv = srv;
      FrontEventManager.register(this);
      refresh();
   }
   
   @Override
   public HypervisorOutput getInfo() {
      return hypData;
   }
   
   @Override
   public String getType() {
      return hypData.getType();
   }
   
   @Override
   public String getVendor() {
      return hypData.getVendor();
   }
   
   @Override
   public String getProduct() {
      return hypData.getProduct();
   }
   
   @Override
   public String getVersion() {
      return hypData.getVersion();
   }
   
   @Override
   public String getRevision() {
      return hypData.getRevision();
   }
   
   @Override
   public boolean hasToolsMedium() {
      return getToolsMedium() != null;
   }
   
   @Override
   public MediumOutput getToolsMedium() {
      Transaction t = srv.sendRequest(new Request(Command.VBOX, HypervisorTasks.ToolsMediumGet));
      return t.extractItem(MediumOutput.class);
   }
   
   @Handler
   protected void putHypervisorDisconnectedEvent(HypervisorDisconnectedEventOutput ev) {
      Logger.track();
      
      hypData = null;
   }
   
   @Handler
   protected void putHypervisorEvent(HypervisorEventOutput ev) {
      Logger.track();
      
      refresh();
   }
   
}
