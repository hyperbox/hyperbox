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

package org.altherian.hbox.comm.io;

import org.altherian.hbox.exception.ObjectNotFoundException;
import org.altherian.hbox.hypervisor.net._NATRule;
import org.altherian.hbox.hypervisor.net._NetService_NAT;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetService_NAT_IO extends NetServiceIO implements _NetService_NAT {
   
   private Map<String, _NATRule> rules = new HashMap<String, _NATRule>();
   
   protected NetService_NAT_IO() {
      
   }
   
   public NetService_NAT_IO(NetService_NAT_IO io) {
      super(io);
   }
   
   public NetService_NAT_IO(String type, boolean isEnabled) {
      super(type, isEnabled);
   }
   
   @Override
   public List<_NATRule> getRules() {
      return new ArrayList<_NATRule>(rules.values());
   }
   
   @Override
   public void addRule(_NATRule rule) {
      rules.put(rule.getId(), rule);
   }
   
   @Override
   public _NATRule getRule(String id) throws ObjectNotFoundException {
      return rules.get(id);
   }
   
   @Override
   public void removeRule(String id) throws ObjectNotFoundException {
      rules.remove(id);
   }
   
}
