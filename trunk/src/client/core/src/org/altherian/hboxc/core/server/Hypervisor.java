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

import net.engio.mbassy.listener.Handler;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.NetAdaptorIn;
import org.altherian.hbox.comm.in.NetModeIn;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorDisconnectedEventOut;
import org.altherian.hbox.comm.out.event.hypervisor.HypervisorEventOut;
import org.altherian.hbox.comm.out.hypervisor.HypervisorOut;
import org.altherian.hbox.comm.out.network.NetAdaptorOut;
import org.altherian.hbox.comm.out.network.NetModeOut;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hboxc.comm.utils.Transaction;
import org.altherian.hboxc.event.EventManager;
import org.altherian.hboxc.server._Hypervisor;
import org.altherian.hboxc.server._Server;
import org.altherian.hboxd.exception.net.InvalidNetworkModeException;
import org.altherian.hboxd.exception.net.NetworkAdaptorNotFoundException;
import org.altherian.tool.logging.Logger;
import java.util.List;

public class Hypervisor implements _Hypervisor {
   
   private _Server srv;
   private HypervisorOut hypData;
   
   private void refresh() {
      if (srv.isHypervisorConnected()) {
         Transaction t = srv.sendRequest(new Request(Command.HBOX, HyperboxTasks.HypervisorGet));
         hypData = t.extractItem(HypervisorOut.class);
      } else {
         hypData = null;
      }
      
   }
   
   public Hypervisor(_Server srv) {
      this.srv = srv;
      EventManager.register(this);
      refresh();
   }
   
   @Override
   public HypervisorOut getInfo() {
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
   public MediumOut getToolsMedium() {
      Transaction t = srv.sendRequest(new Request(Command.VBOX, HypervisorTasks.ToolsMediumGet));
      return t.extractItem(MediumOut.class);
   }
   
   @Handler
   protected void putHypervisorDisconnectedEvent(HypervisorDisconnectedEventOut ev) {
      Logger.track();
      
      hypData = null;
   }
   
   @Handler
   protected void putHypervisorEvent(HypervisorEventOut ev) {
      Logger.track();
      
      refresh();
   }
   
   @Override
   public List<NetModeOut> listNetworkModes() {
      Transaction t = srv.sendRequest(new Request(Command.VBOX, HypervisorTasks.NetModeList));
      return t.extractItems(NetModeOut.class);
   }
   
   @Override
   public NetModeOut getNetworkMode(String id) {
      Transaction t = srv.sendRequest(new Request(Command.VBOX, HypervisorTasks.NetModeGet, new NetModeIn(id)));
      return t.extractItem(NetModeOut.class);
   }
   
   @Override
   public List<NetAdaptorOut> listAdaptors() {
      Transaction t = srv.sendRequest(new Request(Command.VBOX, HypervisorTasks.NetAdaptorList));
      return t.extractItems(NetAdaptorOut.class);
   }
   
   @Override
   public List<NetAdaptorOut> listAdaptors(String modeId) throws InvalidNetworkModeException {
      Transaction t = srv.sendRequest(new Request(Command.VBOX, HypervisorTasks.NetAdaptorList, new NetModeIn(modeId)));
      return t.extractItems(NetAdaptorOut.class);
   }
   
   @Override
   public NetAdaptorOut createAdaptor(String modeId, String name) throws InvalidNetworkModeException {
      NetAdaptorIn adaptIn = new NetAdaptorIn();
      adaptIn.setModeId(modeId);
      adaptIn.setLabel(name);
      Transaction t = srv.sendRequest(new Request(Command.VBOX, HypervisorTasks.NetAdaptorAdd, adaptIn));
      return t.extractItem(NetAdaptorOut.class);
   }
   
   @Override
   public void removeAdaptor(String modeId, String adaptorId) throws InvalidNetworkModeException {
      srv.sendRequest(new Request(Command.VBOX, HypervisorTasks.NetAdaptorRemove, new NetAdaptorIn(modeId, adaptorId)));
   }
   
   @Override
   public NetAdaptorOut getNetAdaptor(String modeId, String adaptorId) throws NetworkAdaptorNotFoundException {
      Transaction t = srv.sendRequest(new Request(Command.VBOX, HypervisorTasks.NetAdaptorGet, new NetAdaptorIn(modeId, adaptorId)));
      return t.extractItem(NetAdaptorOut.class);
   }
   
   @Override
   public String getServerId() {
      return srv.getId();
   }
   
}
