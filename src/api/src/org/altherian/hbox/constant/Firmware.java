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

/**
 * Firmware to use at boot of the VM
 * 
 * @author noteirak
 */
public enum Firmware {
   
   /**
    * BIOS
    */
   Bios,
   /**
    * EFI
    */
   Efi,
   /**
    * EFI 32 bits
    */
   Efi32,
   /**
    * EFI 64 bits
    */
   Efi64,
   
   EfiDual;
   
   public String getId() {
      return toString();
   }
   
}
