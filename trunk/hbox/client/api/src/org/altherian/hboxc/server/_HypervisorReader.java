package org.altherian.hboxc.server;

import org.altherian.hbox.comm.output.hypervisor.HypervisorOutput;
import org.altherian.hbox.comm.output.storage.MediumOutput;

public interface _HypervisorReader {
   
   public HypervisorOutput getInfo();
   
   public String getType();
   
   public String getVendor();
   
   public String getProduct();
   
   public String getVersion();
   
   public String getRevision();
   
   public boolean hasToolsMedium();

   public MediumOutput getToolsMedium();
   
}
