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
import org.altherian.hboxc.server._HypervisorReader;
import org.altherian.hboxc.server._Server;

public class HypervisorReader implements _HypervisorReader {
   
   private _Server srv;
   private HypervisorOutput hypData;
   
   private void refresh() {
      Transaction t = srv.sendRequest(new Request(Command.HBOX, HyperboxTasks.HypervisorGet));
      hypData = t.extractItem(HypervisorOutput.class);
   }
   
   public HypervisorReader(_Server srv) {
      this.srv = srv;
      if (srv.isHypervisorConnected()) {
         refresh();
      }
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
      hypData = null;
   }
   
   @Handler
   protected void putHypervisorEvent(HypervisorEventOutput ev) {
      refresh();
   }
   
}
