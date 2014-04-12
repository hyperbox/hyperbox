package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.host.HostOutput;

public interface _HostReceiver extends _WorkerDataReceiver {
   
   public void put(HostOutput hostOut);

}
