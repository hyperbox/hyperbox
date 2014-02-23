package org.altherian.hboxd.core.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.altherian.hbox.constant.MachineAttributes;

public class MachineTest {
   
   public static void validateSimple(_Machine vm) {
      assertNotNull(vm);
      assertNotNull(vm.getUuid());
      assertFalse(vm.getUuid().isEmpty());
      assertNotNull(vm.getName());
      assertFalse(vm.getName().isEmpty());
      assertNotNull(vm.getState());
   }
   
   public static void validateFull(_Machine vm) {
      validateSimple(vm);
      assertFalse(vm.getSetting(MachineAttributes.OsType.toString()).getString().isEmpty());
      for (_StorageController sto : vm.listStorageControllers()) {
         StorageControllerTest.validateFull(sto);
      }
      for (_NetworkInterface nic : vm.listNetworkInterfaces()) {
         NetworkInterfaceTest.validateFull(nic);
      }
   }
   
   public static void compareSimple(_Machine vm1, _Machine vm2) {
      assertTrue(vm1.getUuid().contentEquals(vm2.getUuid()));
      assertTrue(vm1.getName().contentEquals(vm2.getName()));
      assertTrue(vm1.getState().equals(vm2.getState()));
   }
   
}
