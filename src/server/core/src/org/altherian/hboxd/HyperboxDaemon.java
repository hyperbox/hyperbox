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

package org.altherian.hboxd;

import org.altherian.hboxd.controller.Controller;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public final class HyperboxDaemon implements Daemon {

   private static Controller c;
   private static DaemonContext _dc;

   @Override
   public void destroy() {
      // nothing to do here
   }

   @Override
   public void init(DaemonContext dc) throws DaemonInitException, Exception {
      _dc = dc;
      c = new Controller();
   }

   @Override
   public void start() throws Exception {
      c.start(_dc.getArguments());
   }

   @Override
   public void stop() throws Exception {
      c.stop();
   }

}
