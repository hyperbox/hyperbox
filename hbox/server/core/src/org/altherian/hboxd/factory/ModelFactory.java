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

package org.altherian.hboxd.factory;

import org.altherian.hboxd.core.SingleHostServer;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.tool.logging.Logger;

public class ModelFactory {
   
   private static _Hyperbox hyperbox;
   
   private ModelFactory() {
      throw new RuntimeException("Not allowed");
   }
   
   public static _Hyperbox get() {
      Logger.track();
      
      if (hyperbox == null) {
         hyperbox = new SingleHostServer();
      }
      
      return hyperbox;
   }
   
}
