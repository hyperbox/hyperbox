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

package org.altherian.hboxc.core;

import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.event.CoreEventManager;
import org.altherian.hboxc.event.FrontEventManager;

import org.junit.BeforeClass;

public final class DefaultCoreTest extends CoreTest {
   
   @BeforeClass
   public static void beforeClass() throws HyperboxException {
      FrontEventManager.get().start();
      CoreEventManager.get().start();
      ServerIn srvIn = new ServerIn();
      init(new ClientCore(), srvIn, new UserIn());
   }
   
}
