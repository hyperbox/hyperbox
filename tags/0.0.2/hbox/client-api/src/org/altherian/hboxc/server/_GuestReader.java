package org.altherian.hboxc.server;

import org.altherian.hbox.comm.output.hypervisor.GuestNetworkInterfaceOutput;

public interface _GuestReader {
   
   public GuestNetworkInterfaceOutput findNetworkInterface(String macAddress);

}
