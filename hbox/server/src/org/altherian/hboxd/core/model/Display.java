package org.altherian.hboxd.core.model;

import org.altherian.hboxd.hypervisor.vm.device._RawDisplay;
import org.altherian.hboxd.settings._Setting;

import java.util.List;

public class Display implements _Display {
   
   public Display(_RawDisplay display) {
      // TODO to complete
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
   public long getVideoMemoryAmount() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public void setVideoMemoryAmount(long amount) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public long getMonitorCount() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public void setMonitorCount(long monitor) {
      // TODO Auto-generated method stub
      
   }
   
}
