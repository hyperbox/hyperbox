package org.altherian.hboxd.vbox4_3.ws;

import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxd.hypervisor.DummyEventManager;
import org.altherian.hboxd.hypervisor.HypervisorTest;

import org.junit.BeforeClass;

public class VBoxWSTest extends HypervisorTest {
   
   @BeforeClass
   public static void beforeClass() throws HyperboxException {
      hypervisor = new VBoxWSHypervisor();
      hypervisor.setEventManager(new DummyEventManager());
      
      HypervisorTest.init();
   }
   
}
