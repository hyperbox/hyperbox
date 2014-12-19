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

package org.altherian.hboxd.core.action.medium;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.StorageControllerIn;
import org.altherian.hboxd.comm.io.factory.MediumAttachmentIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.AbstractHyperboxMultiTaskAction;
import org.altherian.hboxd.hypervisor.storage._RawMediumAttachment;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;
import org.altherian.hboxd.session.SessionContext;
import java.util.Arrays;
import java.util.List;

public final class MediumAttachmentListAction extends AbstractHyperboxMultiTaskAction {
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.StorageControllerMediumAttachmentList.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return false;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      if (request.has(StorageControllerIn.class)) {
         StorageControllerIn scIn = request.get(StorageControllerIn.class);
         _RawStorageController sc = hbox.getHypervisor().getMachine(scIn.getMachineUuid()).getStorageController(scIn.getId());
         
         for (_RawMediumAttachment ma : sc.listMediumAttachment()) {
            SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, MediumAttachmentIoFactory.get(ma)));
         }
         
      } else {
         MachineIn mIn = request.get(MachineIn.class);
         for (_RawStorageController sc : hbox.getHypervisor().getMachine(mIn.getUuid()).listStoroageControllers()) {
            for (_RawMediumAttachment ma : sc.listMediumAttachment()) {
               SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, MediumAttachmentIoFactory.get(ma)));
            }
         }
      }
   }
}
