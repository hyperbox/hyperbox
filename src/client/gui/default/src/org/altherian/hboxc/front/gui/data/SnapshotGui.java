/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxc.front.gui.data;

import org.altherian.hbox.comm.out.hypervisor.SnapshotOut;
import org.altherian.tool.AxStrings;

public class SnapshotGui extends EntityGui {

   private String name;

   public SnapshotGui(SnapshotOut snapOut) {
      super(snapOut.getEntityTypeId(), snapOut.getId());
      name = snapOut.getName();
   }

   @Override
   public String toString() {
      return AxStrings.isEmpty(name) ? super.toString() : name;
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

}
