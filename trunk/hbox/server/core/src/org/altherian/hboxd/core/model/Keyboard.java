package org.altherian.hboxd.core.model;

import org.altherian.hbox.constant.KeyboardModes;
import org.altherian.hboxd.hypervisor.vm.device._RawKeyboard;
import org.altherian.hboxd.settings._Setting;

import java.util.List;

public class Keyboard implements _Keyboard {
   
   public Keyboard(_RawKeyboard keyboard) {
      // TODO complete
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
   public void setMode(String mode) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public void setMode(KeyboardModes mode) {
      // TODO Auto-generated method stub
      
   }
   
}
