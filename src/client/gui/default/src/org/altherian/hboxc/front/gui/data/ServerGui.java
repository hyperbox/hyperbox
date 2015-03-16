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

import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.tool.AxStrings;

public class ServerGui extends EntityGui {

   private String address;
   private String name;
   private String label;
   private String state;

   public ServerGui(ConnectorOutput conOut, ServerOut srvOut) {
      super(srvOut.getEntityTypeId(), conOut.getId());
      address = conOut.getAddress();
      label = conOut.getLabel();
      name = srvOut.getName();
   }

   @Override
   public String toString() {
      if (!AxStrings.isEmpty(label)) {
         return label;
      } else if (!AxStrings.isEmpty(name)) {
         return name;
      } else if (!AxStrings.isEmpty(address)) {
         return address;
      } else {
         return super.toString();
      }
   }

   /**
    * @return the address
    */
   public String getAddress() {
      return address;
   }

   /**
    * @param address the address to set
    */
   public void setAddress(String address) {
      this.address = address;
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

   /**
    * @return the label
    */
   public String getLabel() {
      return label;
   }

   /**
    * @param label the label to set
    */
   public void setLabel(String label) {
      this.label = label;
   }

   /**
    * @return the state
    */
   public String getState() {
      return state;
   }

   /**
    * @param state the state to set
    */
   public void setState(String state) {
      this.state = state;
   }

}
