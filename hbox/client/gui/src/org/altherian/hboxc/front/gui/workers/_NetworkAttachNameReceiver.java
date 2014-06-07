package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.network.NetworkAttachNameOutput;

import java.util.List;

public interface _NetworkAttachNameReceiver extends _WorkerDataReceiver {
   
   public void add(List<NetworkAttachNameOutput> nanOut);
   
}
