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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hbox.constant;

public enum MediumAttribute {
   UUID(true),
   ParentUUID(true),
   BaseUUID(true),
   Description,
   State(true),
   Location(true),
   Name,
   Size(true),
   LogicalSize(true),
   DeviceType(true),
   Type(true),
   ReadOnly,
   Variant(true),
   Format(true),
   MediumFormat(true),
   AutoReset,
   LastAccessError(true);
   
   private boolean isReadOnly;
   
   public boolean isReadOnly() {
      return isReadOnly;
   }
   
   private MediumAttribute() {
      this(false);
   }
   
   private MediumAttribute(boolean isReadOnly) {
      this.isReadOnly = isReadOnly;
   }
   
   public String getId() {
      return toString();
   }
   
}
