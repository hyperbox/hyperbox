/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxd.vbox4_3.ws;

import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor._HypervisorLoader;
import org.altherian.hboxd.vbox.hypervisor.ws4_3.VirtualboxHypervisor;

import java.util.Arrays;
import java.util.List;


public class VBox_4_3_WS_Loader implements _HypervisorLoader {
   
   @Override
   public Class<? extends _Hypervisor> getHypervisorClass() {
      return VirtualboxHypervisor.class;
   }
   
   @Override
   public List<String> getSupportedSchemes() {
      return Arrays.asList(VirtualboxHypervisor.ID);
   }
   
   @Override
   public String getTypeId() {
      return VirtualboxHypervisor.TYPE_ID;
   }
   
   @Override
   public String getProduct() {
      return VirtualboxHypervisor.PRODUCT;
   }
   
   @Override
   public String getVendor() {
      return VirtualboxHypervisor.VENDOR;
   }
   
}
