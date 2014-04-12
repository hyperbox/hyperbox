package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import javax.swing.SwingWorker;

public class ServerGetWorker extends SwingWorker<ServerOutput, Void> {
   
   private _ServerReceiver recv;
   private String srvId;
   
   public ServerGetWorker(_ServerReceiver recv, String srvId) {
      this.recv = recv;
      this.srvId = srvId;
   }
   
   @Override
   protected ServerOutput doInBackground() throws Exception {
      recv.loadingStarted();
      ServerOutput newSrvOut = Gui.getServerInfo(srvId);
      return newSrvOut;
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         ServerOutput srvOut = get();
         recv.put(srvOut);
         recv.loadingFinished(true, null);
      } catch (Throwable e) {
         recv.loadingFinished(false, e.getMessage());
      }
   }
   
   public static void get(_ServerReceiver recv, String srvId) {
      new ServerGetWorker(recv, srvId).execute();
   }
   
}
