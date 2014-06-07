package org.altherian.hboxd.hypervisor.vm.guest;

public interface _RawGuestNetworkInterface {
   
   public int getId();
   
   public boolean isUp();
   
   public String getMacAddress();
   
   public String getIp4Address();
   
   public String getIp4Subnet();
   
   public String getIp6Address();
   
   public String getIp6Subnet();
   
}
