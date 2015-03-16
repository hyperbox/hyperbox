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

package org.altherian.hboxc.factory;

import org.altherian.hbox.Configuration;
import org.altherian.hboxc.HyperboxClient;
import org.altherian.hboxc.updater.Updater;
import org.altherian.hboxc.updater._Updater;

public class UpdaterFactory {

   public static final String CFGKEY_UPDATER_CLASS = "updater.class";
   public static final String CFGVAL_UPDATER_CLASS = Updater.class.getName();

   private UpdaterFactory() {
      // only static
   }

   public static _Updater get() {
      return HyperboxClient.loadClass(_Updater.class, Configuration.getSetting(CFGKEY_UPDATER_CLASS, CFGVAL_UPDATER_CLASS));
   }

}
