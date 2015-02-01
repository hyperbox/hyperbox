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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.altherian.hboxc.front.gui.workers;

import org.altherian.hbox.comm.out.hypervisor.SnapshotOut;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.utils.AxSwingWorker;
import org.altherian.hboxc.front.gui.worker.receiver._SnapshotGetReceiver;
import java.util.concurrent.ExecutionException;

public class SnapshotGetWorker extends AxSwingWorker<_SnapshotGetReceiver, SnapshotOut, Void> {
   
   private String srvId;
   private String vmId;
   private String snapId;
   
   public SnapshotGetWorker(_SnapshotGetReceiver recv, String srvId, String vmId, String snapId) {
      super(recv);
      this.srvId = srvId;
      this.vmId = vmId;
      this.snapId = snapId;
   }
   
   @Override
   protected SnapshotOut doInBackground() throws Exception {
      return Gui.getServer(srvId).getSnapshot(vmId, snapId);
   }
   
   @Override
   protected void innerDone() throws InterruptedException, ExecutionException {
      getReceiver().put(srvId, vmId, get());
   }
   
   public static void execute(_SnapshotGetReceiver recv, String srvId, String vmId, String snapId) {
      (new SnapshotGetWorker(recv, srvId, vmId, snapId)).execute();
   }

}
