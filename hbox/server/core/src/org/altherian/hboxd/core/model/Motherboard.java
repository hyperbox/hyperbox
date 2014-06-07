package org.altherian.hboxd.core.model;

import org.altherian.hboxd.hypervisor.vm.device._RawMotherboard;
import org.altherian.hboxd.settings._Setting;

import java.util.List;

public class Motherboard implements _Motherboard {
   
   public Motherboard(_RawMotherboard motherboard) {
      // TODO Auto-generated constructor stub
   }
   
   @Override
   public List<_Setting> listSettings() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public _Setting getSetting(Object id) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public void setSetting(_Setting s) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public boolean isAcpiEnabled() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public void setAcpi(boolean isEnabled) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public boolean isIoApicEnabled() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public void setIoApic(boolean isEnabled) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public String getHardwareUuid() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public void setHardwareUuid(String uuid) {
      // TODO Auto-generated method stub
      
   }
   
}
