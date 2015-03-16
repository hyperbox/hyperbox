package org.altherian.hbox.comm.io.factory;

import org.altherian.hbox.comm.io.MachineLogFileIO;
import org.altherian.hbox.hypervisor._MachineLogFile;

public class MachineLogFileIoFactory {

   private MachineLogFileIoFactory() {
      // only static
   }

   public static MachineLogFileIO get(_MachineLogFile log) {
      return new MachineLogFileIO(log.getId(), log.getFileName(), log.getLog());
   }

}
