package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.ServerOutput;

public interface _ServerReceiver extends _WorkerDataReceiver {
   
   public void put(ServerOutput srvOut);
   
}
