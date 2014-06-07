package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.network.NetworkAttachModeOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import java.util.List;

import javax.swing.SwingWorker;

public class NetworkAttachModeListWorker extends SwingWorker<Void, NetworkAttachModeOutput> {
   
   private _NetworkAttachModeReceiver recv;
   private String serverId;
   
   public NetworkAttachModeListWorker(_NetworkAttachModeReceiver recv, String serverId) {
      this.recv = recv;
      this.serverId = serverId;
      recv.loadingStarted();
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      for (NetworkAttachModeOutput ostOut : Gui.getServer(serverId).listNetworkAttachModes()) {
         publish(ostOut);
      }
      
      return null;
   }
   
   @Override
   protected void process(List<NetworkAttachModeOutput> ostOutList) {
      recv.add(ostOutList);
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
   
   public static void run(_NetworkAttachModeReceiver recv, String serverId) {
      new NetworkAttachModeListWorker(recv, serverId).execute();
   }
   
}
