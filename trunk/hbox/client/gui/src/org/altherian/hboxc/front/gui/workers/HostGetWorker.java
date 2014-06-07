package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.host.HostOutput;
import org.altherian.hboxc.front.gui.Gui;

import javax.swing.SwingWorker;

public class HostGetWorker extends SwingWorker<HostOutput, Void> {
   
   private _HostReceiver recv;
   private String srvId;
   
   private HostGetWorker(_HostReceiver recv, String srvId) {
      this.recv = recv;
      this.srvId = srvId;
      recv.loadingStarted();
   }
   
   @Override
   protected HostOutput doInBackground() throws Exception {
      HostOutput hostOut = Gui.getServer(srvId).getHost();
      return hostOut;
   }
   
   @Override
   protected void done() {
      try {
         HostOutput hostOut = get();
         recv.put(hostOut);
         recv.loadingFinished(true, null);
      } catch (Throwable t) {
         recv.loadingFinished(false, t.getMessage());
      }
   }
   
   public static void get(_HostReceiver recv, String srvId) {
      new HostGetWorker(recv, srvId).execute();
   }

}
