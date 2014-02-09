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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hbox.comm.output;

public class EnumOutput {
   
   private String id;
   private String name;
   private String value;
   
   /**
    * Used only for (de)serialisation
    */
   protected EnumOutput() {
      // used only for (de)serialisation
   }
   
   public EnumOutput(String id, String name, String value) {
      this.id = id;
      this.name = name;
      this.value = value;
   }
   
   /**
    * @return the id
    */
   public String getId() {
      return id;
   }
   
   /**
    * @return the name
    */
   public String getName() {
      return name;
   }
   
   /**
    * @return the value
    */
   public String getValue() {
      return value;
   }
   
   @Override
   public String toString() {
      return getName();
   }
   
   /*
    * (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((id == null) ? 0 : id.hashCode());
      return result;
   }
   
   /*
    * (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      EnumOutput other = (EnumOutput) obj;
      if (id == null) {
         if (other.id != null) {
            return false;
         }
      } else if (!id.equals(other.id)) {
         return false;
      }
      return true;
   }
   
}
