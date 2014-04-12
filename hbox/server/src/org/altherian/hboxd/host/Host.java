package org.altherian.hboxd.host;

import org.altherian.hboxd.hypervisor.host._RawHost;


public class Host implements _Host {
   
   private _RawHost rawHost;
   
   public Host(_RawHost rawHost) {
      this.rawHost = rawHost;
   }
   
   @Override
   public String getHostname() {
      return rawHost.getHostname();
   }
   
   @Override
   public String getOSName() {
      return rawHost.getOSName();
   }
   
   @Override
   public String getOSVersion() {
      return rawHost.getOSVersion();
   }
   
   @Override
   public long getMemorySize() {
      return rawHost.getMemorySize();
   }
   
   @Override
   public long getMemoryAvailable() {
      return rawHost.getMemoryAvailable();
   }
   
}
