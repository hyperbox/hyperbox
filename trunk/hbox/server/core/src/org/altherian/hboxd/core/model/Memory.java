package org.altherian.hboxd.core.model;

import org.altherian.hboxd.hypervisor.vm.device._RawMemory;
import org.altherian.hboxd.settings._Setting;

import java.util.List;

public class Memory implements _Memory {
   
   public Memory(_RawMemory memory) {
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
   public long getAmount() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public void setAmount(long amount) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public boolean isLargePageEnabled() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public void setLargePage(boolean isEnabled) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public boolean isPageFusionEnabled() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public void setPageFusion(boolean isEnabled) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public boolean isNestedPagingEnabled() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public void setNestedPaging(boolean isEnabled) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public boolean isVTxvpidEnabled() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public void setVtxvpid(boolean isEnabled) {
      // TODO Auto-generated method stub
      
   }
   
}
