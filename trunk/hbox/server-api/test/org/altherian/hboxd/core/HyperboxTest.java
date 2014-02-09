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

package org.altherian.hboxd.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.CommObjets;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.MediumInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.input.StorageControllerSubTypeInput;
import org.altherian.hbox.comm.input.StorageControllerTypeInput;
import org.altherian.hbox.comm.input.StoreInput;
import org.altherian.hbox.comm.input.TaskInput;
import org.altherian.hbox.comm.io.PositiveNumberSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.ExceptionOutput;
import org.altherian.hbox.comm.output.HypervisorOutputTest;
import org.altherian.hbox.comm.output.MachineOutputTest;
import org.altherian.hbox.comm.output.MediumOutputTest;
import org.altherian.hbox.comm.output.OsTypeOutputTest;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.ServerOutputTest;
import org.altherian.hbox.comm.output.SessionOutput;
import org.altherian.hbox.comm.output.StorageControllerSubTypeOutputTest;
import org.altherian.hbox.comm.output.StorageControllerTypeOutputTest;
import org.altherian.hbox.comm.output.StoreOutput;
import org.altherian.hbox.comm.output.StoreOutputTest;
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hbox.comm.output.event.EventOutput;
import org.altherian.hbox.comm.output.hypervisor.HypervisorOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.OsTypeOutput;
import org.altherian.hbox.comm.output.network.NetworkAttachModeOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceOutput;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerSubTypeOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerTypeOutput;
import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.HardDiskFormats;
import org.altherian.hbox.constant.KeyboardModes;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hbox.utils.Transaction;
import org.altherian.hboxd.session._RootSession;
import org.altherian.tool.logging.LogLevel;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public abstract class HyperboxTest {
   
   protected static _Hyperbox core;
   protected static _RootSession session;
   protected static Transaction trans;
   
   private static boolean initialized = false;
   protected List<MachineInput> machines = new ArrayList<MachineInput>();
   
   public static void init() throws HyperboxException {
      Logger.setLevel(LogLevel.Tracking);
      
      trans = new Transaction();
      session = core.start().getRootSession(trans);
      trans.setRecv(session);
      
      initialized = true;
   }
   
   private MachineOutput getVm(String uuid) {
      boolean answerType = trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MachineGet, new MachineInput(uuid)));
      assertTrue(trans.getError(), answerType);
      return trans.extractItem(MachineOutput.class);
   }
   
   /*
   private StorageControllerOutput getStorageController(String machineUuid, String scId) {
      Request req = new Request(Command.VBOX, VirtualboxTasks.StorageControllerGet);
      req.set(new MachineInput(machineUuid));
      req.set(new StorageControllerInput(machineUuid, scId));
      trans.sendAndWait(req);
      assertFalse(trans.getError(), trans.hasFailed());
      StorageControllerOutput scOutFull = trans.extractItem(StorageControllerOutput.class);
      StorageControllerOutputTest.validateFull(scOutFull);
      return scOutFull;
   }
    */
   
   private void createVm(MachineInput mIn) {
      trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachineCreate, mIn));
      assertFalse("VM couldn't be created: " + trans.getError(), trans.hasFailed());
      machines.add(mIn);
   }
   
   @Before
   public void before() {
      assertTrue("HypervisorTest is not initiazlied, call init() in @BeforeClass", initialized);
      
      MachineInput mIn01 = new MachineInput(UUID.randomUUID().toString());
      mIn01.setName(Long.toString(System.currentTimeMillis()));
      mIn01.setSetting(new StringSettingIO(MachineAttributes.OsType, "Other"));
      createVm(mIn01);
      assertFalse(machines.isEmpty());
   }
   
   public void after() {
      List<MachineInput> deletedVms = new ArrayList<MachineInput>();
      for (MachineInput mIn : machines) {
         boolean status = trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachineDelete, mIn));
         assertTrue(trans.getError(), status);
         deletedVms.add(mIn);
      }
      for (MachineInput mIn : deletedVms) {
         machines.remove(mIn);
         assertFalse(machines.contains(mIn));
      }
      assertTrue("Not all VMs created during the tests were deleted. Cleanup manually", machines.isEmpty());
   }
   
   @Test
   public void correlateVmAttachModeAndAttachModeList() {
      MachineInput mIn = new MachineInput(UUID.randomUUID().toString());
      mIn.setName(Long.toString(System.currentTimeMillis()));
      mIn.setSetting(new StringSettingIO(MachineAttributes.OsType, "Windows2003_64"));
      
      createVm(mIn);
      
      assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.NetworkAttachModeList)));
      List<NetworkAttachModeOutput> listAttachModesOut = trans.extractItems(NetworkAttachModeOutput.class);
      List<String> attachIds = new ArrayList<String>();
      for (NetworkAttachModeOutput mode : listAttachModesOut) {
         attachIds.add(mode.getId());
      }
      boolean lastTrans = trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.NetworkInterfaceList, mIn));
      assertTrue(trans.getError(), lastTrans);
      List<NetworkInterfaceOutput> listNics = trans.extractItems(NetworkInterfaceOutput.class);
      for (NetworkInterfaceOutput nicOut : listNics) {
         assertTrue("Unknown attach mode : " + nicOut.getAttachMode(), attachIds.contains(nicOut.getAttachMode()));
      }
   }
   
   @Test
   public void sessionListTest() throws InterruptedException {
      assertTrue(trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.SessionList)));
      List<SessionOutput> sOutList = trans.extractItems(SessionOutput.class);
      assertTrue(sOutList.size() == 1);
   }
   
   @Test
   public void vmGetDetailsTest() {
      for (MachineInput mIn : machines) {
         MachineOutputTest.validateFull(getVm(mIn.getUuid()));
      }
   }
   
   @Test
   public void vmListTest() {
      assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MachineList)));
      List<MachineOutput> outList = trans.extractItems(MachineOutput.class);
      assertTrue(outList.size() > 0);
      
      for (MachineOutput mOut : outList) {
         MachineOutputTest.validateSimple(mOut);
      }
   }
   
   @Test
   public void vmModifyTestOnline() {
      MachineInput mIn = null;
      Long execCap = 47l;
      
      String uuid = machines.get(0).getUuid();
      assertTrue(getVm(uuid).getState().contentEquals(MachineStates.PoweredOff.toString()) || getVm(uuid).getState().contentEquals(MachineStates.Aborted.toString()));
      mIn = new MachineInput(uuid);
      
      assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOn, mIn)));
      assertTrue(getVm(mIn.getUuid()).getState(), getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Running.toString()));
      
      mIn.setSetting(new PositiveNumberSettingIO(MachineAttributes.CpuExecCap, execCap));
      
      assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachineModify, mIn)));
      
      getVm(mIn.getUuid()).getSetting(MachineAttributes.CpuExecCap).getNumber().equals(execCap);
      
      assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOff, mIn)));
      assertTrue(getVm(mIn.getUuid()).getState().contentEquals(MachineStates.PoweredOff.toString()));
      
      getVm(mIn.getUuid()).getSetting(MachineAttributes.CpuExecCap).getNumber().equals(execCap);
   }
   
   // TODO incorporate a way to create a VM without setting template, then un-comment all the sections
   @Test
   public void vmModifyTest() throws InterruptedException {
      MachineInput mIn = null;
      MachineOutput mOut = null;
      
      String uuid = machines.get(0).getUuid();
      assertTrue(getVm(uuid).getState().contentEquals(MachineStates.PoweredOff.toString()) || getVm(uuid).getState().contentEquals(MachineStates.Aborted.toString()));
      
      /*
      MediumInput medSc01P0D0In = new MediumInput("/usr/share/virtualbox/VBoxGuestAdditions.iso", DeviceTypes.DVD.getId());
      medSc01P0D0In.setAction(Action.Modify);
      trans.sendAndWait(new Request(Command.VBOX, VirtualboxTasks.MediumGet, medSc01P0D0In));
      assertFalse(trans.getError(), trans.hasFailed());
      MediumOutput medSc01P0D0Out = trans.extractItem(MediumOutput.class);
      medSc01P0D0In.setUuid(medSc01P0D0Out.getUuid());
       */
      
      mIn = new MachineInput(uuid);
      mIn.setSetting(new PositiveNumberSettingIO(MachineAttributes.Memory, 256));
      mIn.setSetting(new StringSettingIO(MachineAttributes.KeyboardMode, KeyboardModes.Usb.getId()));
      
      /*
      StorageControllerInput scIn01 = new StorageControllerInput(uuid, "Test01", StorageControllerType.IDE.getId());
      scIn01.setSubType(StorageControllerSubType.PIIX4.getId());
      mIn.addStorageController(scIn01);
      
      StorageDeviceAttachmentInput s01daIn = new StorageDeviceAttachmentInput(scIn01.getId(), 0l, 0l, DeviceTypes.DVD.getId(), medSc01P0D0In);
      assertTrue(scIn01.addMediumAttachment(s01daIn));
      
      StorageControllerInput scIn02 = new StorageControllerInput(uuid, "Test02", StorageControllerType.SATA.getId());
      scIn02.setSubType(StorageControllerSubType.IntelAhci.getId());
      mIn.addStorageController(scIn02);
      
      StorageDeviceAttachmentInput s02daIn = new StorageDeviceAttachmentInput(scIn02.getId(), 0l, 0l, DeviceTypes.HardDisk.getId());
      assertTrue(scIn02.addMediumAttachment(s02daIn));
       */
      
      trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachineModify, mIn));
      System.out.println(trans.getError());
      System.out.println(trans.getFooter().getType());
      assertFalse(trans.getError(), trans.hasFailed());
      
      mOut = getVm(uuid);
      assertTrue(mOut.getSetting(MachineAttributes.Memory).getNumber() == 256);
      assertTrue(mOut.getSetting(MachineAttributes.KeyboardMode).getString().contentEquals(KeyboardModes.Usb.getId()));
      
      /*
      assertTrue("Nb of Storage Controllers: " + mOut.listStorageController().size(), mOut.listStorageController().size() == 2);
      
      for (StorageControllerInput scIn : mIn.listStorageController()) {
         StorageControllerOutput scOutFull = getStorageController(mOut.getUuid(), scIn.getName());
         
         assertTrue(scOutFull.getMachineUuid().contentEquals(mOut.getUuid()));
         assertTrue(scOutFull.getName().contentEquals(scIn.getName()));
         assertTrue(scOutFull.getType().contentEquals(scIn.getType()));
         assertTrue(scOutFull.getSubType().contentEquals(scIn.getSubType()));
         
         Request req = new Request(Command.VBOX, VirtualboxTasks.StorageControllerMediumAttachmentList);
         req.set(new StorageControllerInput(mOut.getUuid(), scIn.getName()));
         trans.sendAndWait(req);
         assertFalse(trans.getError(), trans.hasFailed());
         
         // assertTrue(scIn.listAttachments().size() == trans.extractItems(StorageDeviceAttachmentOutput.class).size());
      }
      
      Request s01daP0D0Req = new Request(Command.VBOX, VirtualboxTasks.StorageControllerMediumAttachmentGet);
      s01daP0D0Req.set(new MachineInput(uuid));
      s01daP0D0Req.set(new StorageControllerInput(uuid, "Test01"));
      s01daP0D0Req.set(new StorageDeviceAttachmentInput(0l, 0l));
      trans.sendAndWait(s01daP0D0Req);
      assertFalse(trans.getError(), trans.hasFailed());
      StorageDeviceAttachmentOutput s01daP0D0Out = trans.extractItem(StorageDeviceAttachmentOutput.class);
      
      assertTrue(s01daP0D0Out.hasMediumInserted());
      assertTrue(s01daP0D0Out.getMediumUuid().contentEquals(medSc01P0D0In.getUuid()));
      
      
      /*
       * Creating and attaching a Hard Disk
       */
      /*
      MediumInput medSc02P0D0In = new MediumInput();
      medSc02P0D0In.setLocation("/tmp/test-" + Long.toString(System.currentTimeMillis()) + ".vdi");
      medSc02P0D0In.setFormat(HardDiskFormats.VDI.getId());
      medSc02P0D0In.setLogicalSize(5l * 1024 * 1024); // Logical size : 5 MB
      
      mIn = new MachineInput(uuid);
      scIn02 = new StorageControllerInput(uuid, "Test02", StorageControllerType.SATA.getId());
      mIn.modifyStorageController(scIn02);
      s02daIn = new StorageDeviceAttachmentInput("Test02", 0l, 0l, DeviceTypes.HardDisk.getId(), medSc02P0D0In);
      scIn02.addMediumAttachment(s02daIn);
      
      trans.sendAndWaitForTask(new Request(Command.VBOX, VirtualboxTasks.MachineModify, mIn));
      assertFalse(trans.getError(), trans.hasFailed());
      
      Request s02daP0D0Req = new Request(Command.VBOX, VirtualboxTasks.StorageControllerMediumAttachmentGet);
      s02daP0D0Req.set(new MachineInput(uuid));
      s02daP0D0Req.set(new StorageControllerInput(uuid, "Test02"));
      s02daP0D0Req.set(new StorageDeviceAttachmentInput(0l, 0l));
      trans.sendAndWait(s02daP0D0Req);
      assertFalse(trans.getError(), trans.hasFailed());
      StorageDeviceAttachmentOutput s02daP0D0Out = trans.extractItem(StorageDeviceAttachmentOutput.class);
      
      medSc02P0D0In.setType(DeviceTypes.HardDisk.getId());
      trans.sendAndWait(new Request(Command.VBOX, VirtualboxTasks.MediumGet, medSc02P0D0In));
      assertFalse(trans.getError(), trans.hasFailed());
      MediumOutput medSc02P0D0Out = trans.extractItem(MediumOutput.class);
      
      assertTrue(s02daP0D0Out.hasMediumInserted());
      assertTrue(s02daP0D0Out.getMediumUuid().contentEquals(medSc02P0D0Out.getUuid()));
      // -------------------------------------------------------------------//
      
      mIn = new MachineInput(uuid);
      mIn.removeStorageController("Test01");
      mIn.removeStorageController("Test02");
      assertTrue(mIn.getStorageController("Test01").getAction().toString(), mIn.getStorageController("Test01").getAction().equals(Action.Delete));
      assertTrue(mIn.getStorageController("Test02").getAction().toString(), mIn.getStorageController("Test02").getAction().equals(Action.Delete));
      mIn.setSetting(new PositiveNumberSettingIO(MachineAttributes.Memory, 128));
      mIn.setSetting(new StringSettingIO(MachineAttributes.KeyboardMode, KeyboardModes.Ps2.getId()));
      trans.sendAndWaitForTask(new Request(Command.VBOX, VirtualboxTasks.MachineModify, mIn));
      assertFalse(trans.getError(), trans.hasFailed());
      
      mOut = getVm(uuid);
      assertTrue(mOut.getSetting(MachineAttributes.Memory).getNumber() == 128);
      assertTrue(mOut.getSetting(MachineAttributes.KeyboardMode).getString().contentEquals(KeyboardModes.Ps2.getId()));
      assertTrue("Nb of Storage Controllers : " + mOut.listStorageController().size(), mOut.listStorageController().size() == 0);
      
      trans.sendAndWaitForTask(new Request(Command.VBOX, VirtualboxTasks.MediumDelete, new MediumInput(medSc02P0D0Out.getUuid())));
      assertFalse(trans.getError(), trans.hasFailed());
       */
   }
   
   @Test
   public void controlVmTest() throws InterruptedException {
      for (MachineInput mIn : machines) {
         assertTrue(getVm(mIn.getUuid()).getState().contentEquals(MachineStates.PoweredOff.toString()));
         
         assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOn, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState(), getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Running.toString()));
         
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MachinePause, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState(), getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Paused.toString()));
         
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MachineResume, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState(), getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Running.toString()));
         
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MachineReset, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState(), getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Running.toString()));
         
         assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOff, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState().contentEquals(MachineStates.PoweredOff.toString()));
         
         assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOn, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Running.toString()));
         
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MachineReset, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState(), getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Running.toString()));
         
         assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOff, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState().contentEquals(MachineStates.PoweredOff.toString()));
         
         assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOn, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Running.toString()));
         
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MachinePause, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState(), getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Paused.toString()));
         
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MachineResume, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState(), getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Running.toString()));
         
         assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOff, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState().contentEquals(MachineStates.PoweredOff.toString()));
         
         assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOn, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Running.toString()));
         
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MachinePause, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState(), getVm(mIn.getUuid()).getState().contentEquals(MachineStates.Paused.toString()));
         
         assertTrue(trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MachinePowerOff, mIn)));
         assertTrue(getVm(mIn.getUuid()).getState().contentEquals(MachineStates.PoweredOff.toString()));
      }
   }
   
   @Test
   public void listOsTypesTest() {
      for (MachineInput mIn : machines) {
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.OsTypeList, mIn)));
         List<OsTypeOutput> osOutList = trans.extractItems(OsTypeOutput.class);
         for (OsTypeOutput osOut : osOutList) {
            OsTypeOutputTest.validateSimple(osOut);
         }
      }
   }
   
   @Test
   public void listKeyboardModeTest() {
      for (MachineInput mIn : machines) {
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.KeyboardModeList, mIn)));
         for (Answer ans : trans.getBody()) {
            assertNotNull(ans.get(CommObjets.KeyboardMode));
         }
      }
   }
   
   @Test
   public void listMouseModeTest() {
      for (MachineInput mIn : machines) {
         assertTrue(trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MouseModeList, mIn)));
         for (Answer ans : trans.getBody()) {
            assertNotNull(ans.get(CommObjets.MouseMode));
         }
      }
   }
   
   @Test
   public void listStorageControllerTypeTest() {
      trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.StorageControllerTypeList));
      assertFalse(trans.getError(), trans.hasFailed());
      List<StorageControllerTypeOutput> sctOutList = trans.extractItems(StorageControllerTypeOutput.class);
      assertFalse(sctOutList.isEmpty());
      
      for (StorageControllerTypeOutput sctOut : sctOutList) {
         StorageControllerTypeOutputTest.validate(sctOut);
         
         trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.StorageControllerTypeGet, new StorageControllerTypeInput(sctOut.getId())));
         assertFalse(trans.getError(), trans.hasFailed());
         StorageControllerTypeOutput sctOutNew = trans.extractItem(StorageControllerTypeOutput.class);
         StorageControllerTypeOutputTest.validate(sctOutNew);
         StorageControllerTypeOutputTest.compare(sctOut, sctOutNew);
         
         trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.StorageControllerSubTypeList, new StorageControllerTypeInput(sctOut.getId())));
         assertFalse(trans.getError(), trans.hasFailed());
         List<StorageControllerSubTypeOutput> scstOutList = trans.extractItems(StorageControllerSubTypeOutput.class);
         assertFalse(scstOutList.isEmpty());
         
         for (StorageControllerSubTypeOutput scstOut : scstOutList) {
            StorageControllerSubTypeOutputTest.validate(scstOut);
            
            trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.StorageControllerSubTypeGet, new StorageControllerSubTypeInput(scstOut.getId())));
            assertFalse(trans.getError(), trans.hasFailed());
            StorageControllerSubTypeOutput scstOutNew = trans.extractItem(StorageControllerSubTypeOutput.class);
            StorageControllerSubTypeOutputTest.validate(scstOutNew);
            StorageControllerSubTypeOutputTest.compare(scstOut, scstOutNew);
         }
      }
   }
   
   @Test
   public void cancelTaskTest() throws InterruptedException {
      trans.sendAndWait(new Request(Command.HBOX, "dummy"));
      assertFalse("Task failed: " + trans.getError(), trans.hasFailed());
      // Wait for task to be started
      Thread.sleep(2000);
      assertTrue("Task list can be retrieved", trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.TaskList)));
      TaskOutput tOut = trans.extractItem(TaskOutput.class);
      // Let the task run for a bit
      Thread.sleep(5000);
      assertTrue("Task should be canceled", trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.TaskCancel, new TaskInput(tOut.getId()))));
      // Wait for task to be canceled
      Thread.sleep(2000);
      assertTrue("Task should be in history now", trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.TaskGet, new TaskInput(tOut.getId()))));
      tOut = trans.extractItem(TaskOutput.class);
      assertNull("We should not have an error set for this task", tOut.getError());
      assertTrue("Task list cannot be retrieved", trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.TaskList)));
      assertTrue("Task list size should be at least 1 but is " + trans.extractItems(TaskOutput.class).size(), trans.extractItems(TaskOutput.class).size() >= 1);
   }
   
   @Test
   public void failTaskTest() throws InterruptedException {
      trans.sendAndWaitForTask(new Request(Command.HBOX, DummyFailAction.ID));
      TaskOutput tOut = trans.extractItem(TaskOutput.class);
      assertNotNull(tOut);
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.TaskGet, new TaskInput(tOut.getId())));
      assertFalse(trans.hasFailed());
      TaskOutput failedTaskOut = trans.extractItem(TaskOutput.class);
      ExceptionOutput eOut = failedTaskOut.getError();
      assertNotNull(eOut);
   }
   
   @Test
   public void taskRunTest() {
      trans.sendAndWaitForTask(new Request(Command.HBOX, "dummy"));
      assertFalse("Task failed: " + trans.getError(), trans.hasFailed());
      assertTrue("Task list cannot be retrieved", trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.TaskList)));
      assertTrue("Task list size should be at least 1 but is " + trans.extractItems(TaskOutput.class).size(), trans.extractItems(TaskOutput.class).size() >= 1);
   }
   
   @Test
   public void storeListTest() {
      boolean result = trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreList));
      assertTrue("Stores list should be retrievable: " + trans.getError(), result);
      List<StoreOutput> sOutList = trans.extractItems(StoreOutput.class);
      for (StoreOutput sOut : sOutList) {
         StoreOutputTest.validateSimple(sOut);
      }
   }
   
   @Test
   public void storeGetTest() {
      assertTrue("Stores list should be retrievable", trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreList)));
      List<StoreOutput> sOutList = trans.extractItems(StoreOutput.class);
      for (StoreOutput sOut : sOutList) {
         assertTrue("Stores list should be retrievable", trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreGet, new StoreInput(sOut.getId()))));
         StoreOutput sOutDetail = trans.extractItem(StoreOutput.class);
         StoreOutputTest.validateFull(sOutDetail);
      }
   }
   
   @Test
   public void storeAddAndRemoveTest() {
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreList));
      assertFalse("Stores list should be retrievable: " + trans.getError(), trans.hasFailed());
      int firstStoreCount = trans.extractItems(StoreOutput.class).size();
      
      String label = Long.toString(System.currentTimeMillis());
      StoreInput stoIn = new StoreInput();
      stoIn.setLabel(label);
      stoIn.setLocation("C:/" + label);
      stoIn.setType("localFolder");
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreCreate, stoIn));
      assertFalse(trans.getError(), trans.hasFailed());
      StoreOutput firstStoOut = trans.extractItem(StoreOutput.class);
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreList));
      assertFalse("Stores list should be retrievable: " + trans.getError(), trans.hasFailed());
      int secondStoreCount = trans.extractItems(StoreOutput.class).size();
      
      assertTrue(secondStoreCount == (firstStoreCount + 1));
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreGet, new StoreInput(firstStoOut.getId())));
      assertFalse(trans.getError(), trans.hasFailed());
      StoreOutput secondStoOut = trans.extractItem(StoreOutput.class);
      assertTrue(secondStoOut.getId().equals(firstStoOut.getId()));
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreDelete, new StoreInput(firstStoOut.getId())));
      assertFalse(trans.getError(), trans.hasFailed());
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreList));
      assertFalse("Stores list should be retrievable: " + trans.getError(), trans.hasFailed());
      int thirdStoreCount = trans.extractItems(StoreOutput.class).size();
      
      assertTrue(secondStoreCount == (thirdStoreCount + 1));
   }
   
   @Test
   public void storeRegisterAndUnregisterTest() {
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreList));
      assertFalse("Stores list should be retrievable: " + trans.getError(), trans.hasFailed());
      int firstStoreCount = trans.extractItems(StoreOutput.class).size();
      
      String label = Long.toString(System.currentTimeMillis());
      StoreInput stoIn = new StoreInput();
      stoIn.setLabel(label);
      stoIn.setLocation("C:/" + label);
      stoIn.setType("localFolder");
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreCreate, stoIn));
      assertFalse(trans.getError(), trans.hasFailed());
      StoreOutput firstStoOut = trans.extractItem(StoreOutput.class);
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreList));
      assertFalse("Stores list should be retrievable: " + trans.getError(), trans.hasFailed());
      int secondStoreCount = trans.extractItems(StoreOutput.class).size();
      assertTrue(secondStoreCount == (firstStoreCount + 1));
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreGet, new StoreInput(firstStoOut.getId())));
      assertFalse(trans.getError(), trans.hasFailed());
      StoreOutput secondStoOut = trans.extractItem(StoreOutput.class);
      assertTrue(secondStoOut.getId().equals(firstStoOut.getId()));
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreUnregister, new StoreInput(firstStoOut.getId())));
      assertFalse(trans.getError(), trans.hasFailed());
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreList));
      assertFalse("Stores list should be retrievable: " + trans.getError(), trans.hasFailed());
      int thirdStoreCount = trans.extractItems(StoreOutput.class).size();
      assertTrue(thirdStoreCount == firstStoreCount);
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreRegister, stoIn));
      assertFalse(trans.getError(), trans.hasFailed());
      StoreOutput thirdStoOut = trans.extractItem(StoreOutput.class);
      assertTrue(thirdStoOut.getLocation().equalsIgnoreCase(firstStoOut.getLocation()));
      
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.StoreList));
      assertFalse("Stores list should be retrievable: " + trans.getError(), trans.hasFailed());
      int fourthStoreCount = trans.extractItems(StoreOutput.class).size();
      assertTrue(fourthStoreCount == secondStoreCount);
   }
   
   @Test
   public void storeItemListTest() {
      // TODO complete
   }
   
   @Test
   public void serverListAndGetTest() {
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.ServerList));
      assertFalse(trans.getError(), trans.hasFailed());
      List<ServerOutput> srvOutList = trans.extractItems(ServerOutput.class);
      assertFalse("Server list is empty", srvOutList.isEmpty());
      
      for (ServerOutput srvOut : srvOutList) {
         ServerOutputTest.validateSimple(srvOut);
         trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.ServerGet, new ServerInput(srvOut.getId())));
         assertFalse(trans.getError(), trans.hasFailed());
         ServerOutput srvOutNew = trans.extractItem(ServerOutput.class);
         ServerOutputTest.validateFull(srvOutNew);
      }
   }
   
   @Test
   public void serverGetCurrentTest() {
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.ServerGet));
      assertFalse(trans.getError(), trans.hasFailed());
      ServerOutput srvOutNew = trans.extractItem(ServerOutput.class);
      ServerOutputTest.validateFull(srvOutNew);
   }
   
   @Test
   public void hypervisorGetCurrentTest() {
      trans.sendAndWait(new Request(Command.HBOX, HyperboxTasks.HypervisorGetCurrent));
      assertFalse(trans.getError(), trans.hasFailed());
      HypervisorOutput hypOut = trans.extractItem(HypervisorOutput.class);
      HypervisorOutputTest.validateFull(hypOut);
   }
   
   @Test
   public void createAndDeleteHardDiskTest() {
      MediumInput medIn = new MediumInput();
      medIn.setLocation("/tmp/test-" + Long.toString(System.currentTimeMillis()) + ".vdi");
      medIn.setFormat(HardDiskFormats.VDI.getId());
      medIn.setLogicalSize(5l * 1024 * 1024 * 1024); // Logical size : 5 GB
      trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MediumCreate, medIn));
      assertFalse(trans.getError(), trans.hasFailed());
      
      medIn.setType(EntityTypes.HardDisk.getId());
      trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MediumGet, medIn));
      assertFalse(trans.getError(), trans.hasFailed());
      MediumOutput medOut = trans.extractItem(MediumOutput.class);
      MediumOutputTest.validateFull(medOut);
      assertTrue(medOut.getLocation().contentEquals(medIn.getLocation()));
      assertTrue(medOut.getLogicalSize() == medIn.getLogicalSize());
      
      trans.sendAndWaitForTask(new Request(Command.VBOX, HypervisorTasks.MediumDelete, new MediumInput(medOut.getUuid())));
      assertFalse(trans.getError(), trans.hasFailed());
      
      trans.sendAndWait(new Request(Command.VBOX, HypervisorTasks.MediumGet, new MediumInput(medOut.getUuid())));
      assertTrue(trans.hasFailed());
   }
   
   @Handler
   public void post(EventOutput eOut) {
      Logger.verbose(eOut.toString());
   }
   
   @AfterClass
   public static void afterClass() {
      core.stop();
   }
   
}
