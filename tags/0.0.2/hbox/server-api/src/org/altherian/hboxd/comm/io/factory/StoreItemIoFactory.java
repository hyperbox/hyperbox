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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.output.StoreItemOutput;
import org.altherian.hboxd.store._StoreItem;

import java.util.ArrayList;
import java.util.List;

public final class StoreItemIoFactory {
   
   public static StoreItemOutput get(_StoreItem si) {
      return new StoreItemOutput(si.getStore().getId(), si.getName(), si.getPath(), si.getSize(), si.isContainer());
   }
   
   public static List<StoreItemOutput> get(List<_StoreItem> siList) {
      List<StoreItemOutput> siOutList = new ArrayList<StoreItemOutput>();
      for (_StoreItem si : siList) {
         siOutList.add(get(si));
      }
      return siOutList;
   }
   
}
