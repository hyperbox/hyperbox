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

package org.altherian.vbox;

import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.exception.net.InvalidNetworkModeException;
import org.altherian.hbox.hypervisor.net._NetMode;
import java.util.HashSet;
import java.util.Set;

public enum VBoxNetMode implements _NetMode {
   
   Bridged(true, false, false, false, false),
   
   Generic(false, true, false, false, false),
   
   HostOnly(true, false, true, true, false, NetServiceType.IPv4, NetServiceType.IPv6, NetServiceType.DHCP_IPv4),
   
   Internal(false, true, false, false, false),
   
   NAT(false, false, false, false, false, NetServiceType.NAT_IPv4),
   
   NATNetwork(true, false, true, true, true,
         NetServiceType.IPv4_NetCIDR,
         NetServiceType.DHCP_IPv4,
         NetServiceType.IPv6,
         NetServiceType.IPv6_Gateway,
         NetServiceType.NAT_IPv6,
         NetServiceType.NAT_IPv4);
   
   protected String id;
   protected String label;
   protected Set<String> services = new HashSet<String>();
   protected boolean canUseAdaptor;
   protected boolean canUseNetworkName;
   protected boolean canAddAdaptor;
   protected boolean canRemoveAdaptor;
   protected boolean canRenameAdaptor;
   
   private VBoxNetMode(boolean canUseAdaptor, boolean canUseNetworkName, boolean canAddAdaptor, boolean canRemoveAdaptor, boolean canRenameAdaptor,
         NetServiceType... services) {
      for (NetServiceType type : services) {
         this.services.add(type.getId());
      }
      this.canUseAdaptor = canUseAdaptor;
      this.canUseNetworkName = canUseNetworkName;
      this.canAddAdaptor = canAddAdaptor;
      this.canRemoveAdaptor = canRemoveAdaptor;
      this.canRenameAdaptor = canRenameAdaptor;
   }
   
   @Override
   public String getId() {
      return toString();
   }
   
   @Override
   public String getLabel() {
      return toString();
   }
   
   @Override
   public Set<String> getSupportedServices() {
      return new HashSet<String>(services);
   }
   
   @Override
   public boolean canUseAdaptor() {
      return canUseAdaptor;
   }
   
   @Override
   public boolean canUseNetworkName() {
      return canUseNetworkName;
   }
   
   @Override
   public boolean canAddAdaptor() {
      return canAddAdaptor;
   }
   
   @Override
   public boolean canRemoveAdaptor() {
      return canRemoveAdaptor;
   }
   
   @Override
   public boolean canRenameAdaptor() {
      return canRenameAdaptor;
   }
   
   public static VBoxNetMode getEnum(String modeId) {
      try {
         return VBoxNetMode.valueOf(modeId);
      } catch (IllegalArgumentException e) {
         throw new InvalidNetworkModeException(modeId);
      }
   }
   
   public boolean is(String modeId) {
      try {
         return getId().contentEquals(modeId);
      } catch (InvalidNetworkModeException e) {
         return false;
      }
   }
   
}
