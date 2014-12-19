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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.state.CoreState;
import org.altherian.tool.logging.LogLevel;
import org.altherian.tool.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

public abstract class CoreTest {
   
   @Rule
   public TestName name = new TestName();
   
   private static _Core core;
   
   public static void init(_Core coreToTest, ServerIn sInToTest, UserIn uInToTest) throws HyperboxException {
      Logger.setLevel(LogLevel.Tracking);
      
      core = coreToTest;
      assertTrue(core.getCoreState().equals(CoreState.Stopped));
      core.start();
      assertTrue(core.getCoreState().equals(CoreState.Started));
      
      assertNotNull(core.getCoreState());
      
      // Validate connection & deconnection before going further
   }
   
   @Before
   public void before() throws HyperboxException {
      System.out.println("-----------------------------------------------------------------");
      System.out.println(name.getMethodName());
      System.out.println("-----------------------------------------------------------------");
   }
   
   @After
   public void after() {
      System.out.println("-----------------------------------------------------------------");
      System.out.println();
   }
   
   /*
   @Test
   public void vmListAndGetTest() {
      for (MachineOutput mOut : core.getReader().listMachines()) {
         MachineOutputTest.validateSimple(mOut);
         MachineOutput newVmOut = core.getReader().getMachine(new MachineInput(mOut.getUuid()));
         MachineOutputTest.validateFull(newVmOut);
         MachineOutputTest.compareSimple(mOut, newVmOut);
      }
   }
   
   // TODO rewrite
   public void vmModifyTest() throws InterruptedException {
      String uuid = UUID.randomUUID().toString();
      String name = Long.toString(System.currentTimeMillis());
      String osType = "Other";
      
      // -------------- Validate Machine creation ----------------- //
      MachineInput mIn = new MachineInput(uuid);
      mIn.setName(name);
      mIn.setSetting(new StringSettingIO(MachineAttributes.OsType, osType));
      
      core.getServer().sendRequest(new Request(Command.VBOX, VirtualboxTasks.MachineCreate, mIn), RequestProcessType.WaitForTask);
      MachineOutput mOut = core.getReader().getMachine(mIn);
      MachineOutputTest.validateFull(mOut);
      mOut.getUuid().contentEquals(uuid);
      mOut.getName().contentEquals(name);
      mOut.getSetting(MachineAttributes.OsType).getString().contentEquals(osType);
      // ---------------------------------------------------------- //
      
      // ---------------- Validate Machine Modification ------------------------ //
      String sc01Name = "Test01";
      StorageControllerType sc01Type = StorageControllerType.IDE;
      StorageControllerSubType sc01SubType = StorageControllerSubType.PIIX4;
      
      String sc02Name = "Test02";
      StorageControllerType sc02Type = StorageControllerType.SATA;
      StorageControllerSubType sc02SubType = StorageControllerSubType.IntelAhci;
      
      StorageControllerInput scIn01 = new StorageControllerInput(mOut.getUuid(), sc01Name, sc01Type.getId());
      scIn01.setSubType(sc01SubType.getId());
      mIn.addStorageController(scIn01);
      StorageControllerInput scIn02 = new StorageControllerInput(mOut.getUuid(), sc02Name, sc02Type.getId());
      scIn02.setSubType(sc02SubType.getId());
      mIn.addStorageController(scIn02);
      mIn.setSetting(new PositiveNumberSettingIO(MachineAttributes.Memory, 256));
      mIn.setSetting(new StringSettingIO(MachineAttributes.KeyboardMode, KeyboardModes.Usb.getId()));
      
      core.getServer().sendRequest(new Request(Command.VBOX, VirtualboxTasks.MachineModify, mIn), RequestProcessType.WaitForTask);
      
      mOut = core.getReader().getMachine(mIn);
      long ram = mOut.getSetting(MachineAttributes.Memory).getNumber();
      assertTrue("RAM Value: " + ram, ram == 256);
      assertTrue(((StringSettingIO) mOut.getSetting(MachineAttributes.KeyboardMode)).getValue().contentEquals(KeyboardModes.Usb.getId()));
      assertTrue("Nb of Storage Controllers : " + mOut.listStorageController().size(), mOut.listStorageController().size() == 2);
      assertTrue(mOut.getStorageController(scIn01.getId()).getName().contentEquals(sc01Name));
      assertTrue(mOut.getStorageController(scIn01.getId()).getType().contentEquals(sc01Type.getId()));
      assertTrue(mOut.getStorageController(scIn01.getId()).getSubType().contentEquals(sc01SubType.getId()));
      assertTrue(mOut.getStorageController(scIn02.getId()).getName().contentEquals(sc02Name));
      assertTrue(mOut.getStorageController(scIn02.getId()).getType().contentEquals(sc02Type.getId()));
      assertTrue(mOut.getStorageController(scIn02.getId()).getSubType().contentEquals(sc02SubType.getId()));
      // ----------------------------------------------------------------------- //
   }
   
   @Test
   public void vmModifyOnlineTest() {
      String uuid = UUID.randomUUID().toString();
      String name = Long.toString(System.currentTimeMillis());
      String osType = "Windows2003_64";
      Long execCap = 47l;
      
      MachineInput mIn = new MachineInput(uuid);
      mIn.setName(name);
      mIn.setSetting(new StringSettingIO(MachineAttributes.OsType, osType));
      core.getServer().sendRequest(new Request(Command.VBOX, VirtualboxTasks.MachineCreate, mIn), RequestProcessType.WaitForTask);
      
      core.getServer().sendRequest(new Request(Command.VBOX, VirtualboxTasks.MachinePowerOn, mIn), RequestProcessType.WaitForTask);
      assertTrue(core.getReader().getMachine(mIn).getState().contentEquals(MachineStates.Running.getId()));
      
      mIn = new MachineInput(uuid);
      mIn.setSetting(new PositiveNumberSettingIO(MachineAttributes.CpuExecCap, execCap));
      
      core.getServer().sendRequest(new Request(Command.VBOX, VirtualboxTasks.MachineModify, mIn), RequestProcessType.WaitForTask);
      assertTrue(core.getReader().getMachine(mIn).getSetting(MachineAttributes.CpuExecCap).getNumber().equals(execCap));
      
      core.getServer().sendRequest(new Request(Command.VBOX, VirtualboxTasks.MachinePowerOff, mIn), RequestProcessType.WaitForTask);
      assertTrue(core.getReader().getMachine(mIn).getState().contentEquals(MachineStates.PoweredOff.getId()));
      assertTrue(core.getReader().getMachine(mIn).getSetting(MachineAttributes.CpuExecCap).getNumber().equals(execCap));
   }
   
   @Test(expected = HyperboxRuntimeException.class)
   public void vmModifyVmOnlineTestFail() {
      String uuid = UUID.randomUUID().toString();
      String name = Long.toString(System.currentTimeMillis());
      String osType = "Windows2003_64";
      String newName = Long.toString(System.currentTimeMillis());
      
      MachineInput mIn = new MachineInput(uuid);
      mIn.setName(name);
      mIn.setSetting(new StringSettingIO(MachineAttributes.OsType, osType));
      core.getServer().sendRequest(new Request(Command.VBOX, VirtualboxTasks.MachineCreate, mIn), RequestProcessType.WaitForTask);
      
      core.getServer().sendRequest(new Request(Command.VBOX, VirtualboxTasks.MachinePowerOn, mIn), RequestProcessType.WaitForTask);
      assertTrue(core.getReader().getMachine(mIn).getState().contentEquals(MachineStates.Running.getId()));
      
      mIn = new MachineInput(uuid);
      mIn.setName(newName);
      
      core.getServer().sendRequest(new Request(Command.VBOX, VirtualboxTasks.MachineModify, mIn), RequestProcessType.WaitForTask);
   }
    */
   
   @AfterClass
   public static void afterClass() {
      if (core.getCoreState().equals(CoreState.Started)) {
         core.stop();
      }
   }
   
}
