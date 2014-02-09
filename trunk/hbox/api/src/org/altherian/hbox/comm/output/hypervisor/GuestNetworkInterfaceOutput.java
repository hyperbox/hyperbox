package org.altherian.hbox.comm.output.hypervisor;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.ObjectOutput;
import org.altherian.hbox.constant.GuestNetworkInterfaceAttributes;

public class GuestNetworkInterfaceOutput extends ObjectOutput {
   
   protected GuestNetworkInterfaceOutput() {
      
   }
   
   public GuestNetworkInterfaceOutput(String id, boolean isUp, String macAddress) {
      super(id);
      setSetting(new BooleanSettingIO(GuestNetworkInterfaceAttributes.IsUp, isUp));
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.MacAddress, macAddress));
   }
   
   public GuestNetworkInterfaceOutput(String id, boolean isUp, String macAddress, String ip4Address, String ip4Subnet) {
      this(id, isUp, macAddress);
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP4Address, ip4Address));
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP4Subnet, ip4Subnet));
   }
   
   public GuestNetworkInterfaceOutput(String id, boolean isUp, String macAddress, String ip4Address, String ip4Subnet, String ip6Address,
         String ip6Subnet) {
      this(id, isUp, macAddress, ip4Address, ip4Subnet);
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP6Address, ip6Address));
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP6Subnet, ip6Subnet));
   }
   
   public boolean isUp() {
      return getSetting(GuestNetworkInterfaceAttributes.IsUp).getBoolean();
   }
   
   public String getMacAddress() {
      return getSetting(GuestNetworkInterfaceAttributes.MacAddress).getString();
   }
   
   public String getIp4Address() {
      return getSetting(GuestNetworkInterfaceAttributes.IP4Address).getString();
   }
   
   public String getIp4Subnet() {
      return getSetting(GuestNetworkInterfaceAttributes.IP4Subnet).getString();
   }
   
   public String getIp6Address() {
      return getSetting(GuestNetworkInterfaceAttributes.IP6Address).getString();
   }
   
   public String getIp6Subnet() {
      return getSetting(GuestNetworkInterfaceAttributes.IP6Subnet).getString();
   }
   
}
