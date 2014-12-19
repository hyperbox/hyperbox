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

package org.altherian.tool;

public class FileSizeNumber {
   
   private long byteSize;
   private long humanSize;
   private String humanUnit;
   
   public FileSizeNumber(long byteSize) {
      this.byteSize = byteSize;
      evaluate();
   }
   
   public FileSizeNumber(String byteSize) {
      this(Long.parseLong(byteSize));
   }
   
   private void evaluate() {
      if (byteSize >= 1125899906842624l) {
         humanUnit = "PB";
         humanSize = byteSize / 1125899906842624l;
      }
      else if (byteSize >= 1099511627776l) {
         humanUnit = "TB";
         humanSize = byteSize / 1099511627776l;
      }
      else if (byteSize >= 1073741824l) {
         humanUnit = "GB";
         humanSize = byteSize / 1073741824l;
      }
      else if (byteSize >= 1048576l) {
         humanUnit = "MB";
         humanSize = byteSize / 1048576l;
      }
      else if (byteSize >= 1024) {
         humanUnit = "KB";
         humanSize = byteSize / 1024;
      }
      else {
         humanUnit = "B";
         humanSize = byteSize;
      }
   }
   
   public long getHumanSize() {
      return humanSize;
   }
   
   public String getHumanUnit() {
      return humanUnit;
   }
   
}
