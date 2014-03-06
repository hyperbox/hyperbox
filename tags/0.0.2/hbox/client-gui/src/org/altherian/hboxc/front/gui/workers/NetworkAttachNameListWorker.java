package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.input.NetworkAttachModeInput;
import org.altherian.hbox.comm.output.network.NetworkAttachNameOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import java.util.List;

import javax.swing.SwingWorker;

public class NetworkAttachNameListWorker extends SwingWorker<Void, NetworkAttachNameOutput> {
   
   private _NetworkAttachNameReceiver recv;
   private String serverId;
   private String netAttachModeId;
   
   public NetworkAttachNameListWorker(_NetworkAttachNameReceiver recv, String serverId, String netAttachModeId) {
      this.recv = recv;
      this.serverId = serverId;
      this.netAttachModeId = netAttachModeId;
      recv.loadingStarted();
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      for (NetworkAttachNameOutput nanOut : Gui.getServer(serverId).listNetworkAttachNames(new NetworkAttachModeInput(netAttachModeId))) {
         publish(nanOut);
      }
      
      return null;
   }
   
   @Override
   protected void process(List<NetworkAttachNameOutput> nanOut) {
      recv.add(nanOut);
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         get();
         recv.loadingFinished(true, null);
      } catch (Throwable e) {
         recv.loadingFinished(false, e.getMessage());
      }
   }
   
   public static void run(_NetworkAttachNameReceiver recv, String serverId, String netAttachModeId) {
      new NetworkAttachNameListWorker(recv, serverId, netAttachModeId).execute();
   }
   
}
