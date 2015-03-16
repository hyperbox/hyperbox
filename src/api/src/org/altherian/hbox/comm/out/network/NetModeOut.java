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

package org.altherian.hbox.comm.out.network;

import org.altherian.hbox.comm.out.ObjectOut;
import org.altherian.hbox.constant.EntityType;
import java.util.List;

public class NetModeOut extends ObjectOut {

   private String label;
   private List<String> netServices;
   private boolean canLinkAdaptor;
   private boolean canAddAdaptor;
   private boolean canRemoveAdaptor;
   private boolean canLinkNetworkName;
   private boolean canRenameAdaptor;

   protected NetModeOut() {
      // Serialization
   }

   public NetModeOut(String id, String label, List<String> netServices, boolean canLinkAdaptor, boolean canAddAdaptor, boolean canRemoveAdaptor,
         boolean canLinkNetworkName, boolean canRenameAdaptor) {
      super(EntityType.NetMode, id);
      this.label = label;
      this.netServices = netServices;
      this.canLinkAdaptor = canLinkAdaptor;
      this.canAddAdaptor = canAddAdaptor;
      this.canRemoveAdaptor = canRemoveAdaptor;
      this.canLinkNetworkName = canLinkNetworkName;
      this.canRenameAdaptor = canRenameAdaptor;
   }

   public String getLabel() {
      return label;
   }

   public List<String> getNetServices() {
      return netServices;
   }

   public boolean canLinkAdaptor() {
      return canLinkAdaptor;
   }

   public boolean canAddAdaptor() {
      return canAddAdaptor;
   }

   public boolean canRemoveAdaptor() {
      return canRemoveAdaptor;
   }

   public boolean canLinkNetworkName() {
      return canLinkNetworkName;
   }

   public boolean canRenameAdaptor() {
      return canRenameAdaptor;
   }

}
