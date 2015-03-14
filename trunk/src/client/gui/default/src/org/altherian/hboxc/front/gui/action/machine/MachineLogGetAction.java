package org.altherian.hboxc.front.gui.action.machine;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.hypervisor._MachineLogFile;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.builder.IconBuilder;

public class MachineLogGetAction extends AbstractAction {
   
   private String _srvId;
   private String _vmId;
   private String _logId;
   
   public MachineLogGetAction(String srvId, String vmId, String logId) {
      super("View Log Files", IconBuilder.getTask(HypervisorTasks.MachineLogFileGet));
      
      _srvId = srvId;
      _vmId = vmId;
      _logId = logId;
      
      setEnabled(true);
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      _MachineLogFile logIo = Gui.getServer(_srvId).getHypervisor().getLogFile(_vmId, "0");
      
      JDialog dialog = new JDialog();
      JTextArea text = new JTextArea();
      for (String line : logIo.getLog()) {
         text.append(line);
      }
      dialog.getContentPane().add(text);
      dialog.setVisible(true);
   }
   
}
