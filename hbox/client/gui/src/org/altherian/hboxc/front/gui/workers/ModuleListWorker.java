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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.out.ModuleOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AnSwingWorker;
import org.altherian.tool.logging.Logger;

import java.util.List;

public class ModuleListWorker extends AnSwingWorker<Void, ModuleOut, _ModuleListReceiver> {
   
   private String srvId;
   
   public ModuleListWorker(_ModuleListReceiver recv, String srvId) {
      super(recv);
      this.srvId = srvId;
   }
   
   @Override
   protected Void doInBackground() throws Exception {
      for (ModuleOut modOut : Gui.getServer(srvId).listModules()) {
         publish(modOut);
      }

      return null;
   }
   
   @Override
   protected void process(List<ModuleOut> objOutList) {
      getReceiver().add(objOutList);
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         get();
         getReceiver().loadingFinished(true, null);
      } catch (Throwable e) {
         Logger.exception(e);
         getReceiver().loadingFinished(false, e.getMessage());
      }
   }
   
   public static void run(_ModuleListReceiver recv, String srvId) {
      new ModuleListWorker(recv, srvId).execute();
   }
   
}
