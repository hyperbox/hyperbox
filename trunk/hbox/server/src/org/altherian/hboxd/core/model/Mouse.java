package org.altherian.hboxd.core.model;

import org.altherian.hboxd.hypervisor.vm.device._RawMouse;
import org.altherian.hboxd.settings._Setting;

import java.util.List;

public class Mouse implements _Mouse {
   
   public Mouse(_RawMouse mouse) {
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
   public String getMode() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public void setMode(String modeId) {
      // TODO Auto-generated method stub
      
   }
   
}
