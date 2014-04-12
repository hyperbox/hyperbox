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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.hypervisor.HypervisorLoaderOutput;
import org.altherian.hbox.comm.output.hypervisor.HypervisorOutput;
import org.altherian.hbox.constant.HypervisorAttributes;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor._HypervisorLoader;

import java.util.ArrayList;
import java.util.List;

public class HypervisorIoFactory {
   
   private HypervisorIoFactory() {
      // static only
   }
   
   public static HypervisorOutput getOut(_Hypervisor hyp) {
      List<SettingIO> settings = new ArrayList<SettingIO>();
      settings.add(new StringSettingIO(HypervisorAttributes.Type, hyp.getTypeId()));
      settings.add(new StringSettingIO(HypervisorAttributes.Vendor, hyp.getVendor()));
      settings.add(new StringSettingIO(HypervisorAttributes.Product, hyp.getProduct()));
      settings.add(new StringSettingIO(HypervisorAttributes.Version, hyp.getVersion()));
      settings.add(new StringSettingIO(HypervisorAttributes.Revision, hyp.getRevision()));
      settings.addAll(SettingIoFactory.getList(hyp.getSettings()));
      return new HypervisorOutput(hyp.getId(), settings);
   }
   
   public static List<HypervisorLoaderOutput> getOut(_HypervisorLoader loader) {
      List<HypervisorLoaderOutput> listOut = new ArrayList<HypervisorLoaderOutput>();
      for (String id : loader.getSupportedSchemes()) {
         listOut.add(new HypervisorLoaderOutput(loader.getVendor(), loader.getProduct(), loader.getTypeId(), id));
      }
      return listOut;
   }
   
}
