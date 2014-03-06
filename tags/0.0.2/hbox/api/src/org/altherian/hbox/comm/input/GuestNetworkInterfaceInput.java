package org.altherian.hbox.comm.input;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.GuestNetworkInterfaceAttributes;

public class GuestNetworkInterfaceInput extends ObjectInput {
   
   public GuestNetworkInterfaceInput() {
      
   }
   
   public GuestNetworkInterfaceInput(String id) {
      super(id);
   }
   
   public boolean isUp() {
      return getSetting(GuestNetworkInterfaceAttributes.IsUp).getBoolean();
   }
   
   public void setUp(boolean isUp) {
      setSetting(new BooleanSettingIO(GuestNetworkInterfaceAttributes.IsUp, isUp));
   }
   
   public String getMacAddress() {
      return getSetting(GuestNetworkInterfaceAttributes.MacAddress).getString();
   }
   
   public GuestNetworkInterfaceInput setMacAddress(String macAddress) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.MacAddress, macAddress));
      return this;
   }
   
   public String getIp4Address() {
      return getSetting(GuestNetworkInterfaceAttributes.IP4Address).getString();
   }
   
   public void setIp4Address(String ip4Address) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP4Address, ip4Address));
   }
   
   public String getIp4Subnet() {
      return getSetting(GuestNetworkInterfaceAttributes.IP4Subnet).getString();
   }
   
   public void setIp4Subnet(String ip4Subnet) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP4Subnet, ip4Subnet));
   }
   
   public String getIp6Address() {
      return getSetting(GuestNetworkInterfaceAttributes.IP6Address).getString();
   }
   
   public void setIp6Address(String ip6Address) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP6Address, ip6Address));
   }
   
   public String getIp6Subnet() {
      return getSetting(GuestNetworkInterfaceAttributes.IP6Subnet).getString();
   }
   
   public void setIp6Subnet(String ip6Subnet) {
      setSetting(new StringSettingIO(GuestNetworkInterfaceAttributes.IP6Subnet, ip6Subnet));
   }
   
}
