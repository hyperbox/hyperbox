/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.io.factory.SettingIoFactory;
import org.altherian.hbox.comm.out.hypervisor.HypervisorLoaderOut;
import org.altherian.hbox.comm.out.hypervisor.HypervisorOut;
import org.altherian.hbox.constant.HypervisorAttribute;
import org.altherian.hboxd.hypervisor.Hypervisor;
import org.altherian.hboxd.hypervisor._Hypervisor;
import java.util.ArrayList;
import java.util.List;

public class HypervisorIoFactory {
   
   private HypervisorIoFactory() {
      // static only
   }
   
   public static HypervisorOut getOut(_Hypervisor hyp) {
      List<SettingIO> settings = new ArrayList<SettingIO>();
      settings.add(new StringSettingIO(HypervisorAttribute.Type, hyp.getTypeId()));
      settings.add(new StringSettingIO(HypervisorAttribute.Vendor, hyp.getVendor()));
      settings.add(new StringSettingIO(HypervisorAttribute.Product, hyp.getProduct()));
      if (hyp.isRunning()) {
         settings.add(new StringSettingIO(HypervisorAttribute.Version, hyp.getVersion()));
         settings.add(new StringSettingIO(HypervisorAttribute.Revision, hyp.getRevision()));
      }
      settings.addAll(SettingIoFactory.getList(hyp.getSettings()));
      return new HypervisorOut(hyp.getId(), settings);
   }
   
   public static List<HypervisorLoaderOut> getOut(Class<? extends _Hypervisor> loader) {
      List<HypervisorLoaderOut> listOut = new ArrayList<HypervisorLoaderOut>();
      Hypervisor metadata = loader.getAnnotation(Hypervisor.class);
      if (metadata != null) {
         for (String id : metadata.schemes()) {
            listOut.add(new HypervisorLoaderOut(metadata.vendor(), metadata.product(), metadata.typeId(), id));
         }
      }
      return listOut;
   }
   
}
