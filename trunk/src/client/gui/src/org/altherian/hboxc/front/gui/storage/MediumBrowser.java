/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxc.front.gui.storage;

import org.altherian.hbox.comm.in.MediumIn;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.StoreItemOut;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.store.utils.StoreItemChooser;

public class MediumBrowser {
   
   private static MediumOut getMedium(ServerOut srvOut, StoreItemOut siOut, String deviceType) {
      MediumIn medIn = new MediumIn();
      medIn.setLocation(siOut.getPath());
      medIn.setType(deviceType);
      MediumOut medOut = Gui.getServer(srvOut).getMedium(medIn);
      return medOut;
   }
   
   public static MediumOut browse(ServerOut srvOut, String deviceType) {
      StoreItemOut siOut = StoreItemChooser.getExisitingFile(srvOut.getId());
      if ((siOut == null) || siOut.isContainer()) {
         return null;
      }
      return getMedium(srvOut, siOut, deviceType);
   }
   
   public static MediumOut browse(ServerOut srvOut, String storeId, String deviceType) {
      return browse(srvOut, deviceType);
   }
   
}
