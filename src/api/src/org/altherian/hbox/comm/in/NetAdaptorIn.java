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

package org.altherian.hbox.comm.in;

import org.altherian.hbox.constant.EntityType;
import java.util.ArrayList;
import java.util.List;

public class NetAdaptorIn extends ObjectIn<EntityType> {
   
   private String label;
   private String modeId;
   private List<NetServiceIn> services = new ArrayList<NetServiceIn>();
   
   public NetAdaptorIn() {
      super(EntityType.NetAdaptor);
   }
   
   public NetAdaptorIn(String modeId, String id) {
      this();
      setId(id);
      setModeId(modeId);
   }
   
   public String getLabel() {
      return label;
   }
   
   public void setLabel(String label) {
      this.label = label;
   }
   
   public String getModeId() {
      return modeId;
   }
   
   public void setModeId(String modeId) {
      this.modeId = modeId;
   }
   
   public List<NetServiceIn> getServices() {
      return services;
   }
   
   public void setServices(List<NetServiceIn> services) {
      this.services = services;
   }
   
   public void addService(NetServiceIn service) {
      services.add(service);
   }
   
}
