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

package org.altherian.hbox.comm.output.hypervisor;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.output.ObjectOutput;
import org.altherian.hbox.constant.HypervisorAttributes;

import java.util.List;

public class HypervisorOutput extends ObjectOutput {
   
   protected HypervisorOutput() {
      // Used for serialization
   }
   
   public HypervisorOutput(String id) {
      super(id);
   }
   
   public HypervisorOutput(String id, List<SettingIO> settings) {
      super(id, settings);
   }
   
   public String getType() {
      return getSetting(HypervisorAttributes.Type).getString();
   }
   
   public String getVendor() {
      return getSetting(HypervisorAttributes.Vendor).getString();
   }
   
   public String getProduct() {
      return getSetting(HypervisorAttributes.Product).getString();
   }
   
   public String getVersion() {
      return getSetting(HypervisorAttributes.Version).getString();
   }
   
   public String getRevision() {
      return getSetting(HypervisorAttributes.Revision).getString();
   }
   
}
