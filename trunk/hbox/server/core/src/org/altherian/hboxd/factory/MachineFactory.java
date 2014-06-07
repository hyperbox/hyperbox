package org.altherian.hboxd.factory;

import org.altherian.hboxd.core.model.Machine;
import org.altherian.hboxd.core.model._Machine;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.server._Server;

public class MachineFactory {
   
   private MachineFactory() {
      
   }
   
   public static _Machine get(_Server server, _Hypervisor hypervisor, _RawVM rawVm) {
      return new Machine(server, hypervisor, rawVm);
   }
   
}
