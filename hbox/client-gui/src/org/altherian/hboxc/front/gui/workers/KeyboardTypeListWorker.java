package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class KeyboardTypeListWorker extends SwingWorker<Void, String> {
   
   private _KeyboardTypeListReceiver recv;
   private String serverId;
   private String machineId;
   
   public KeyboardTypeListWorker(_KeyboardTypeListReceiver recv, String serverId, String machineId) {
      this.recv = recv;
      this.serverId = serverId;
      this.machineId = machineId;
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      SwingUtilities.invokeLater(new Runnable() {
         
         @Override
         public void run() {
            recv.loadingStarted();
         }
         
      });
      
      for (String type : Gui.getServer(serverId).listKeyboardMode(new MachineInput(machineId))) {
         publish(type);
      }
      
      return null;
   }
   
   @Override
   protected void process(List<String> typeList) {
      recv.add(typeList);
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

   public static void get(_KeyboardTypeListReceiver recv, String serverId, String machineId) {
      new KeyboardTypeListWorker(recv, serverId, machineId).execute();
   }
   
}
