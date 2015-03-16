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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.io.factory.SettingIoFactory;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hboxd.core.model._Medium;
import org.altherian.hboxd.hypervisor.storage._RawMedium;

public class MediumIoFactory {

   private MediumIoFactory() {
      // static class
   }

   public static MediumOut get(_RawMedium m) {
      MediumOut mIo = new MediumOut(m.getUuid(), SettingIoFactory.getList(m.listSettings()));
      return mIo;
   }

   public static MediumOut get(_Medium m) {
      MediumOut mIo = new MediumOut(m.getUuid(), SettingIoFactory.getList(m.getSettings()));
      return mIo;
   }

}
