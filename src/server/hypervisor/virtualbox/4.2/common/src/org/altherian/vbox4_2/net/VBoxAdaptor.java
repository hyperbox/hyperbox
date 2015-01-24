/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.vbox4_2.net;

import org.altherian.hboxd.hypervisor.net._NetAdaptor;
import org.altherian.hboxd.hypervisor.net._NetMode;
import org.altherian.hboxd.hypervisor.net._NetService;
import java.util.ArrayList;
import java.util.List;


public class VBoxAdaptor implements _NetAdaptor {
   
   private String id;
   private String label;
   private _NetMode mode;
   private List<_NetService> services;
   
   public VBoxAdaptor(String id, String label, _NetMode mode) {
      this(id, label, mode, new ArrayList<_NetService>());
   }
   
   public VBoxAdaptor(String id, String label, _NetMode mode, List<_NetService> services) {
      this.id = id;
      this.label = label;
      this.mode = mode;
      this.services = new ArrayList<_NetService>(services);
   }
   
   @Override
   public String getId() {
      return id;
   }
   
   @Override
   public String getLabel() {
      return label;
   }
   
   @Override
   public _NetMode getMode() {
      return mode;
   }
   
   @Override
   public List<_NetService> getServices() {
      return new ArrayList<_NetService>(services);
   }
   
}
