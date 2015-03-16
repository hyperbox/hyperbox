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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hbox.kryonet;

import org.altherian.tool.logging.Logger;
import java.lang.Thread.UncaughtExceptionHandler;

public class KryoUncaughtExceptionHandler implements UncaughtExceptionHandler {

   @Override
   public void uncaughtException(Thread arg0, Throwable arg1) {
      Logger.error("Uncaught exception in Kryonet Update Thread: " + arg1.getMessage());
      Logger.exception(arg1);
   }
}
