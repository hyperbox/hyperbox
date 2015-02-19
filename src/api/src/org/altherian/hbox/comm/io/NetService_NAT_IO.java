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

import org.altherian.hbox.exception.FeatureNotImplementedException;
import org.altherian.hbox.exception.ObjectNotFoundException;
import org.altherian.hbox.hypervisor.net._NATRule;
import org.altherian.hbox.hypervisor.net._NetService_NAT;
import java.util.List;

public class NetService_NAT_IO extends ObjectIO implements _NetService_NAT {

   @Override
   public List<_NATRule> getRules() {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }

   @Override
   public void addRule(_NATRule rule) {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }

   @Override
   public _NATRule getRule(String id) throws ObjectNotFoundException {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }

   @Override
   public void removeRule(String id) throws ObjectNotFoundException {
      // TODO Auto-generated method stub
      throw new FeatureNotImplementedException();
   }

}
