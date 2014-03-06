package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.output.TaskOutput;

public interface _TaskReceiver extends _WorkerDataReceiver {
   
   public void put(TaskOutput tskOut);
   
}
