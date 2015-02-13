/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.hboxc.front.gui.workers;

import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AxSwingWorker;
import org.altherian.hboxc.front.gui.worker.receiver._ConnectorReceiver;
import java.util.concurrent.ExecutionException;

public class ConnectorGetWorker extends AxSwingWorker<_ConnectorReceiver, ConnectorOutput, Void> {
   
   private String conId;
   
   public ConnectorGetWorker(_ConnectorReceiver recv, String conId) {
      super(recv);
      this.conId = conId;
   }
   
   @Override
   protected ConnectorOutput doInBackground() throws Exception {
      return Gui.getReader().getConnector(conId);
   }
   
   @Override
   protected void innerDone() throws InterruptedException, ExecutionException {
      getReceiver().put(get());
   }
   
   public static void execute(_ConnectorReceiver recv, String conId) {
      (new ConnectorGetWorker(recv, conId)).execute();
   }
   
}
