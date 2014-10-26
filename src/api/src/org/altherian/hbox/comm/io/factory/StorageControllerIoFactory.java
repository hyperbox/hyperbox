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

package org.altherian.hbox.comm.io.factory;

import org.altherian.hbox.comm.in.Action;
import org.altherian.hbox.comm.in.StorageControllerIn;
import org.altherian.hbox.comm.out.storage.StorageControllerOut;

public final class StorageControllerIoFactory {
   
   public static String getId(String machineUuid, String name) {
      return machineUuid + "|" + name;
   }
   
   /**
    * Create the equivalent in an Input object of this Storage Controller output object. The input object will have its {@link Action} set to {@link Action#Modify}
    * 
    * @param scOut The Storage Controller output object to transform
    * @return an input Storage Controller object labelled as {@link Action#Modify}
    * @see StorageControllerIn
    * @see StorageControllerOut
    * @see Action
    */
   public static StorageControllerIn get(StorageControllerOut scOut) {
      StorageControllerIn scIn = new StorageControllerIn(scOut.getMachineUuid(), scOut.getName(), scOut.getType(), scOut.listSettings());
      scIn.setAction(Action.Modify);
      return scIn;
   }
   
}
