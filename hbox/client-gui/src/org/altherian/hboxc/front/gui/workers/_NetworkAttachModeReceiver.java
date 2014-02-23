package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.network.NetworkAttachModeOutput;

import java.util.List;

public interface _NetworkAttachModeReceiver extends _WorkerDataReceiver {
   
   public void add(List<NetworkAttachModeOutput> namOut);
   
}
