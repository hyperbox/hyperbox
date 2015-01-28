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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.altherian.hboxd.hypervisor.net;

import org.altherian.hbox.hypervisor.net._NetService;


public abstract class NetService implements _NetService {

   private String id;
   private String label;
   private String type;
   private boolean enabled;

   public NetService(String id, String label, String type, boolean enabled) {
      this.id = id;
      this.label = label;
      this.type = type;
      setEnabled(enabled);
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public String getLabel() {
      return label;
   }

   @Override
   public String getType() {
      return type;
   }

   @Override
   public boolean isEnabled() {
      return enabled;
   }

   @Override
   public void setEnabled(boolean isEnabled) {
      this.enabled = isEnabled;
   }

}
