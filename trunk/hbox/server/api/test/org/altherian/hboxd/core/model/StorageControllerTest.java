package org.altherian.hboxd.core.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StorageControllerTest {
   
   public static void validateSimple(_StorageController sto) {
      assertNotNull(sto);
      assertFalse(sto.getId().isEmpty());
      assertFalse(sto.getMachineUuid().isEmpty());
      assertNotNull(sto.getType());
   }
   
   public static void validateFull(_StorageController sto) {
      validateSimple(sto);
      assertFalse(sto.getName().isEmpty());
      assertNotNull(sto.getMachine());
      assertNotNull(sto.getControllerType());
      assertNotNull(sto.getControllerSubType());
      assertTrue(sto.getMaxDeviceCount() > 0);
      assertTrue(sto.getMaxPortCount() >= 0);
      assertTrue(sto.getPortCount() >= 0);
   }
   
}
