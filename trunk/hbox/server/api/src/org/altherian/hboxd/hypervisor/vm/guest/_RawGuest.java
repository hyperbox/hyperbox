package org.altherian.hboxd.hypervisor.vm.guest;

import java.util.List;

public interface _RawGuest {
   
   public boolean hasHypervisorTools();
   
   public _RawHypervisorTools getHypervisorTools();
   
   public List<_RawGuestNetworkInterface> listNetworkInterfaces();
   
   public _RawGuestNetworkInterface getNetworkInterfaceById(String id);
   
   public _RawGuestNetworkInterface getNetworkInterfaceByMac(String macAddress);

}
