package org.altherian.hboxd.core.model;

import org.altherian.hboxd.hypervisor.vm.device._RawCPU;
import org.altherian.hboxd.settings._Setting;

import java.util.List;

public class CPU implements _CPU {
   
   public CPU(_RawCPU rawCpu) {
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
   public long getAmount() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public void setAmount(long amount) {
      // TODO Auto-generated method stub
      
   }
   
}
