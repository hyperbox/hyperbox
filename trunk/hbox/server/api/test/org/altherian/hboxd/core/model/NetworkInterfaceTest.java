package org.altherian.hboxd.core.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NetworkInterfaceTest {
   
   public static void validateFull(_NetworkInterface nic) {
      assertNotNull(nic);
      assertFalse(nic.getId().isEmpty());
      assertTrue(nic.getNicId() >= 0);
      assertNotNull(nic.getMachine());
      assertNotNull(nic.getType());
      assertFalse(nic.getMacAddress().isEmpty());
      assertFalse(nic.getAdapterType().isEmpty());
      assertFalse(nic.getAttachMode().isEmpty());
      assertNotNull(nic.getAttachName());
   }
   
}
