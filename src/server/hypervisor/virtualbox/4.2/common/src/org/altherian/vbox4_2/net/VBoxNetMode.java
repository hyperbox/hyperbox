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

import org.altherian.hbox.constant.NetServiceType;
import org.altherian.hbox.hypervisor.net._NetMode;
import org.altherian.hboxd.exception.net.InvalidNetworkModeException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public enum VBoxNetMode implements _NetMode {

   Bridged(new HashSet<String>(), true, false, false, false),
   Generic(new HashSet<String>(), false, true, false, false),
   HostOnly(new HashSet<String>(Arrays.asList(NetServiceType.IPv4.getId(), NetServiceType.DHCP.getId())), true, false, true, true),
   Internal(new HashSet<String>(), false, true, false, false),
   NAT(new HashSet<String>(Arrays.asList(NetServiceType.NAT.getId())), false, false, false, false);
   
   protected String id;
   protected String label;
   protected Set<String> services;
   protected boolean canLinkAdaptor;
   protected boolean canLinkNetworkName;
   protected boolean canAddAdaptor;
   protected boolean canRemoveAdaptor;

   private VBoxNetMode(Set<String> services, boolean canLinkAdaptor, boolean canLinkNetworkName, boolean canAddAdaptor, boolean canRemoveAdaptor) {
      this.services = new HashSet<String>(services);
      this.canLinkAdaptor = canLinkAdaptor;
      this.canLinkNetworkName = canLinkNetworkName;
      this.canAddAdaptor = canAddAdaptor;
      this.canRemoveAdaptor = canRemoveAdaptor;
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
   public boolean canLinkAdaptor() {
      return canLinkAdaptor;
   }
   
   @Override
   public boolean canLinkNetworkName() {
      return canLinkNetworkName;
   }
   
   @Override
   public boolean canAddAdaptor() {
      return canAddAdaptor;
   }
   
   @Override
   public boolean canRemoveAdaptor() {
      return canRemoveAdaptor;
   }
   
   public static VBoxNetMode getEnum(String modeId) {
      try {
         return VBoxNetMode.valueOf(modeId);
      } catch (IllegalArgumentException e) {
         throw new InvalidNetworkModeException(modeId);
      }
   }
   
}
