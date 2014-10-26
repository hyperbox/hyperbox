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

package org.altherian.hboxc.comm.io.factory;

import org.altherian.hboxc.back._Backend;
import org.altherian.hboxc.comm.output.BackendOutput;

import java.util.ArrayList;
import java.util.List;

public class BackendIoFactory {
   
   private BackendIoFactory() {
      // static only
   }
   
   public static BackendOutput get(String id) {
      return new BackendOutput(id);
   }

   public static BackendOutput get(_Backend back) {
      return get(back.getId());
   }
   
   public static List<BackendOutput> getList(List<_Backend> backList) {
      List<BackendOutput> listOut = new ArrayList<BackendOutput>();
      for (_Backend back : backList) {
         listOut.add(get(back));
      }
      return listOut;
   }
   
   public static List<BackendOutput> getListId(List<String> backIdList) {
      List<BackendOutput> listOut = new ArrayList<BackendOutput>();
      for (String id : backIdList) {
         listOut.add(get(id));
      }
      return listOut;
   }
   
}
