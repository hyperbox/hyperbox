package org.altherian.vbox4_3.vm.guest;

import org.altherian.hboxd.hypervisor.vm.guest._RawGuestNetworkInterface;
import org.altherian.tool.logging.Logger;
import org.altherian.vbox4_3.VBox;

import org.virtualbox_4_3.IMachine;

public class VBoxGuestNetworkInterface implements _RawGuestNetworkInterface {
   
   private String machineUuid;
   private int nicId;
   
   private IMachine getVm() {
      return VBox.get().findMachine(machineUuid);
   }
   
   private String getProperty(String name) {
      return getVm().getGuestPropertyValue("/VirtualBox/GuestInfo/Net/" + nicId + "/" + name);
   }
   
   public VBoxGuestNetworkInterface(String machineUuid, int nicId) {
      this.machineUuid = machineUuid;
      this.nicId = nicId;
   }
   
   @Override
   public int getId() {
      return nicId;
   }
   
   @Override
   public boolean isUp() {
      return getProperty("Status").equalsIgnoreCase("up");
   }
   
   @Override
   public String getMacAddress() {
      return getProperty("MAC");
   }
   
   @Override
   public String getIp4Address() {
      return getProperty("V4/IP");
   }
   
   @Override
   public String getIp4Subnet() {
      String subnet = getProperty("V4/Netmask");
      Logger.debug("Machine subnet :" + subnet);
      return subnet;
   }
   
   @Override
   public String getIp6Address() {
      // TODO Auto-generated method stub
      return "";
   }
   
   @Override
   public String getIp6Subnet() {
      // TODO Auto-generated method stub
      return "";
   }
   
}
