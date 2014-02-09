package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.input.TaskInput;
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import javax.swing.SwingWorker;

public class TaskGetWorker extends SwingWorker<Void, Void> {
   
   private _TaskReceiver recv;
   private TaskOutput objOut;
   
   public TaskGetWorker(_TaskReceiver recv, TaskOutput objOut) {
      this.recv = recv;
      this.objOut = objOut;
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      recv.loadingStarted();
      TaskOutput newObjOut = Gui.getServer(objOut.getServerId()).getTask(new TaskInput(objOut));
      recv.put(newObjOut);
      
      return null;
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         get();
         recv.loadingFinished();
      } catch (Throwable e) {
         Logger.exception(e);
         recv.loadingFailed(e.getMessage());
      }
   }
   
   public static void get(_TaskReceiver recv, TaskOutput objOut) {
      new TaskGetWorker(recv, objOut).execute();
   }
   
}
