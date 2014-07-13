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

package org.altherian.vbox.utils;

/**
 * This class is used to provide workarounds for various bugs or very-desirable features in the Virtualbox API.
 * 
 * @author noteirak
 */
public class InconsistencyUtils {
   
   /**
    * <p>
    * Workaround for Bug Ticket <a href="https://www.virtualbox.org/ticket/11316">#11316</a>
    * </p>
    * <p>
    * This will keep any value within {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE} by reducing the {@link Long} value if necessary.<br/>
    * We assume that it is not possible to reach any value outside these boundaries within the methods of the API for anything ID-like.
    * </p>
    * 
    * @param value a Long from within Virtualbox's API
    * @return a int value.
    */
   public static Integer getAndTruncate(long value) {
      Integer finalInt = 0;
      
      if (value < Integer.MIN_VALUE) {
         finalInt = Integer.MIN_VALUE;
      } else if (value > Integer.MAX_VALUE) {
         finalInt = Integer.MAX_VALUE;
      } else {
         finalInt = (int) value;
      }
      
      return finalInt;
   }
   
}
