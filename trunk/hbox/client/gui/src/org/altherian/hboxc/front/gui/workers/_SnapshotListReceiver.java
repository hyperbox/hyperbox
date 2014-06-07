package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.hypervisor.SnapshotOutput;

import java.util.List;

public interface _SnapshotListReceiver extends _WorkerDataReceiver {
   
   public void add(List<SnapshotOutput> mOutList);

}
