/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
 * 
 * http://hyperbox.altherian.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.core.model;

import org.altherian.hboxd.hypervisor.vm.device._RawCPU;
import org.altherian.setting._Setting;
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
