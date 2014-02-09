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

package org.altherian.hboxd.core.action.machine;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.input.Action;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.NetworkInterfaceInput;
import org.altherian.hbox.comm.input.StorageControllerInput;
import org.altherian.hbox.comm.input.StorageDeviceAttachmentInput;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hboxd.comm.io.factory.SettingIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.ASingleTaskAction;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.storage._RawMediumAttachment;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.hypervisor.vm.device._RawNetworkInterface;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.tool.logging.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public final class MachineModifyAction extends ASingleTaskAction {
   
   private void createMedium(_Hyperbox hbox, _RawVM vm, _RawStorageController sc, StorageDeviceAttachmentInput sdaIn) {
      Logger.track();
      
      String fullLocation = sdaIn.getMedium().getLocation();
      if (!new File(fullLocation).isAbsolute()) {
         fullLocation = vm.getLocation() + "/" + fullLocation;
      }
      String medFormat = sdaIn.getMedium().getFormat();
      Long medSize = sdaIn.getMedium().getLogicalSize();
      _RawMedium rawMed = hbox.getHypervisor().createHardDisk(fullLocation, medFormat, medSize);
      sc.attachMedium(rawMed, sdaIn.getPortId(), sdaIn.getDeviceId());
   }
   
   private void replaceMedium(_Hyperbox hbox, _RawStorageController sc, StorageDeviceAttachmentInput sdaIn) {
      Logger.track();
      
      _RawMedium rawMed = hbox.getHypervisor().getMedium(sdaIn.getMedium().getUuid());
      sc.attachMedium(rawMed, sdaIn.getPortId(), sdaIn.getDeviceId());
   }
   
   private void removeMedium(_Hyperbox hbox, _RawStorageController sc, StorageDeviceAttachmentInput sdaIn) {
      Logger.track();
      
      sc.detachMedium(sdaIn.getPortId(), sdaIn.getDeviceId());
   }
   
   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.VBOX.getId() + HypervisorTasks.MachineModify.getId());
   }
   
   @Override
   public boolean isQueueable() {
      return true;
   }
   
   @Override
   public void run(Request request, _Hyperbox hbox) {
      MachineInput mIn = request.get(MachineInput.class);
      mIn.removeSetting(MachineAttributes.ServerId);
      _RawVM vm = hbox.getHypervisor().getMachine(mIn.getUuid());
      
      boolean success = false;
      
      vm.lock();
      
      try {
         vm.setSetting(SettingIoFactory.getListIo(mIn.listSettings()));
         
         for (NetworkInterfaceInput nIn : mIn.listNetworkInterface()) {
            _RawNetworkInterface nic = vm.getNetworkInterface(nIn.getNicId());
            nic.setSetting(SettingIoFactory.getListIo(nIn.listSettings()));
         }
         
         for (StorageControllerInput scIn : mIn.listStorageController()) {
            _RawStorageController sc = null;
            
            if (scIn.getAction().equals(Action.Delete) || scIn.getAction().equals(Action.Replace)) {
               vm.removeStorageController(scIn.getId());
            }
            if (scIn.getAction().equals(Action.Create) || scIn.getAction().equals(Action.Replace)) {
               sc = vm.addStorageController(scIn.getType(), scIn.getId());
            }
            if (scIn.getAction().equals(Action.Create) || scIn.getAction().equals(Action.Modify) || scIn.getAction().equals(Action.Replace)) {
               sc = vm.getStorageController(scIn.getId());
               sc.setSetting(SettingIoFactory.getListIo(scIn.listSettings()));
               
               for (StorageDeviceAttachmentInput sdaIn : scIn.listAttachments()) {
                  if (sdaIn.getAction().equals(Action.Delete)) {
                     sc.detachDevice(sdaIn.getPortId(), sdaIn.getDeviceId());
                  }
                  
                  if (sdaIn.getAction().equals(Action.Create)) {
                     sc.attachDevice(sdaIn.getDeviceType(), sdaIn.getPortId(), sdaIn.getDeviceId()); // TODO evaluate if still needed
                     if (sdaIn.hasMedium()) {
                        if (sdaIn.getMedium().getAction().equals(Action.Create)) {
                           createMedium(hbox, vm, sc, sdaIn);
                        } else {
                           replaceMedium(hbox, sc, sdaIn);
                        }
                     }
                  }
                  if (sdaIn.getAction().equals(Action.Modify)) {
                     _RawMediumAttachment rawMedAtt = sc.getMediumAttachment(sdaIn.getPortId(), sdaIn.getDeviceId());
                     if (rawMedAtt == null) {
                        Logger.track();
                        SessionContext.getClient().putAnswer(new Answer(request, AnswerType.WARNING, "Trying to modify a storage attachment that doesn't exist, skipping"));
                     } else {
                        // We either want to replace or create or change nothing if UUID are the same
                        if (sdaIn.hasMedium() && (sdaIn.getMedium().getAction() == Action.Create)) {
                           createMedium(hbox, vm, sc, sdaIn);
                        }
                        
                        // We attach since there is nothing attached yet
                        else if (sdaIn.hasMedium() && !rawMedAtt.hasMedium()) {
                           replaceMedium(hbox, sc, sdaIn);
                        }
                        
                        // We only want to modify if the UUID is different
                        else if (sdaIn.hasMedium() && (sdaIn.getMedium().getAction() == Action.Modify) && !sdaIn.getMedium().getUuid().contentEquals(rawMedAtt.getMedium().getUuid())) {
                           replaceMedium(hbox, sc, sdaIn);
                        }
                        
                        // We want to remove the current medium if the ACtion is set to Delete or if the input has no medium
                        // TODO need explicit change via Action.Delete or Action.Replace
                        else if ((sdaIn.hasMedium() && (sdaIn.getMedium().getAction() == Action.Delete)) || (rawMedAtt.hasMedium() && !sdaIn.hasMedium())) {
                           removeMedium(hbox, sc, sdaIn);
                        } else {
                           Logger.debug("No medium in the current config or the input, skipping");
                        }
                     }
                  }
               }
            }
         }
         success = true;
      } catch (RuntimeException e) {
         throw e;
      } finally {
         vm.unlock(success);
      }
   }
   
}
