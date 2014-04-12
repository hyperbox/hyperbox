package org.altherian.hboxd.host;


public interface _Host {
   
   public String getHostname();

   public String getOSName();
   
   public String getOSVersion();
   
   public long getMemorySize();
   
   public long getMemoryAvailable();
   
}
