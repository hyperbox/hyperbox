package org.altherian.hboxd.core.model;

import org.altherian.hboxd.hypervisor.vm.device._RawUSB;
import org.altherian.hboxd.settings._Setting;

import java.util.List;

public class USB implements _USB {
   
   public USB(_RawUSB usb) {
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
   public boolean isEnabled() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public void setEnabled(boolean isEnabled) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public boolean isEhciEnabled() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public void setEhciEnabled(boolean isEnabled) {
      // TODO Auto-generated method stub
      
   }
   
}
