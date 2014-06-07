package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.output.hypervisor.GuestNetworkInterfaceOutput;
import org.altherian.hboxd.hypervisor.vm.guest._RawGuestNetworkInterface;

public class GuestNetworkInterfaceIoFactory {
   
   private GuestNetworkInterfaceIoFactory() {
      
   }
   
   public static GuestNetworkInterfaceOutput get(_RawGuestNetworkInterface rawGnic) {
      return new GuestNetworkInterfaceOutput(
            Integer.toString(rawGnic.getId()),
            rawGnic.isUp(),
            rawGnic.getMacAddress(),
            rawGnic.getIp4Address(),
            rawGnic.getIp4Subnet(),
            rawGnic.getIp6Address(),
            rawGnic.getIp6Subnet());
   }
   
}
