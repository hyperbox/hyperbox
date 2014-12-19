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

package org.altherian.hboxd.hypervisor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.altherian.hbox.Configuration;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.HardDiskFormat;
import org.altherian.hbox.constant.KeyboardMode;
import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.hbox.constant.StorageControllerSubType;
import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hbox.exception.MachineDisplayNotAvailableException;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.hypervisor.snapshot.RawSnapshotTest;
import org.altherian.hboxd.hypervisor.storage.RawMediumTest;
import org.altherian.hboxd.hypervisor.storage.RawStorageControllerSubTypeTest;
import org.altherian.hboxd.hypervisor.storage.RawStorageControllerTest;
import org.altherian.hboxd.hypervisor.storage.RawStorageControllerTypeTest;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.hypervisor.storage._RawStorageController;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerSubType;
import org.altherian.hboxd.hypervisor.storage._RawStorageControllerType;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.hypervisor.vm.device._RawNetworkInterface;
import org.altherian.hboxd.hypervisor.vm.snapshot._RawSnapshot;
import org.altherian.setting.PositiveNumberSetting;
import org.altherian.setting.StringSetting;
import org.altherian.setting._Setting;
import org.altherian.tool.logging.LogLevel;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public abstract class HypervisorTest {
   
   @Rule
   public TestName testName = new TestName();
   
   private static boolean initialized = false;
   protected static _Hypervisor hypervisor;
   
   protected List<MachineIn> machines = new ArrayList<MachineIn>();
   
   public static void init(String options) throws HyperboxException {
      Logger.setLevel(LogLevel.Tracking);
      
      Configuration.setSetting("core.eventmgr.class", DummyEventManager.class.getName());
      hypervisor.start(options);
      initialized = true;
   }
   
   public static void init() throws HyperboxException {
      init("");
   }
   
   public _RawVM createVm(MachineIn mIn) {
      _RawVM rawVm = hypervisor.createMachine(mIn.getUuid(), mIn.getName(), null);
      assertNotNull(rawVm);
      machines.add(new MachineIn(rawVm.getUuid()));
      
      if (mIn.getUuid() != null) {
         assertTrue(mIn.getUuid().equals(rawVm.getUuid()));
      }
      assertTrue(mIn.getName().equals(rawVm.getName()));
      return rawVm;
   }
   
   public _RawVM createVm() {
      MachineIn mIn01 = new MachineIn(UUID.randomUUID().toString());
      mIn01.setName(Long.toString(System.currentTimeMillis()));
      return createVm(mIn01);
   }
   
   @Before
   public void before() {
      assertTrue("HypervisorTest is not initiazlied, call init() in @BeforeClass", initialized);
      System.out.println("--------------- START OF " + testName.getMethodName() + "-----------------------");
   }
   
   @After
   public void after() {
      List<MachineIn> deletedVms = new ArrayList<MachineIn>();
      for (MachineIn mIn : machines) {
         if (hypervisor.getMachine(mIn.getUuid()).getState().equals(MachineStates.Running)) {
            hypervisor.getMachine(mIn.getUuid()).powerOff();
         }
         hypervisor.deleteMachine(mIn.getUuid());
         deletedVms.add(mIn);
      }
      for (MachineIn mIn : deletedVms) {
         machines.remove(mIn);
         assertFalse(machines.contains(mIn));
      }
      assertTrue("Not all VMs created during the tests were deleted. Cleanup manually", machines.isEmpty());
      System.out.println("--------------- END OF " + testName.getMethodName() + "-----------------------");
   }
   
   @Test
   public void listVmsTest() {
      createVm();
      createVm();
      
      for (_RawVM rawVm : hypervisor.listMachines()) {
         assertNotNull(rawVm);
         _RawVM vm = hypervisor.getMachine(rawVm.getUuid());
         assertNotNull(vm);
         assertNotNull(vm.getConsole());
      }
   }
   
   @Test
   public void createAndDeleteSnapshotsTest() {
      String rootSnapName = "Darth Snapshot";
      String rootSnapDesc = "I am your father";
      String childSnapName = "Snap Skywalker";
      String childSnapDesc = "Nooooooooo";
      _RawVM vm = createVm();
      
      assertFalse(vm.hasSnapshot());
      
      _RawSnapshot rawSnap = vm.takeSnapshot(rootSnapName, rootSnapDesc);
      RawSnapshotTest.validateFull(rawSnap);
      _RawSnapshot rootSnap = vm.getRootSnapshot();
      assertTrue(rootSnap.getUuid().contentEquals(rawSnap.getUuid()));
      
      _RawSnapshot childSnap = vm.takeSnapshot(childSnapName, childSnapDesc);
      RawSnapshotTest.validateFull(childSnap);
      assertTrue(childSnap.getParent().getUuid().contentEquals(rawSnap.getUuid()));
      
      assertTrue(rawSnap.getChildren().size() == 1);
      assertTrue(rawSnap.getChildren().get(0).getUuid().contentEquals(childSnap.getUuid()));
      
      vm.deleteSnapshot(childSnap.getUuid());
      assertFalse(rawSnap.hasChildren());
      vm.deleteSnapshot(rawSnap.getUuid());
      assertNull(vm.getRootSnapshot());
   }
   
   @Test
   public void createAndDeleteSnapshotsOnlineTest() {
      _RawVM vm = createVm();
      assertFalse(vm.hasSnapshot());
      vm.powerOn();
      createAndDeleteSnapshotsTest();
      vm.powerOff();
   }
   
   @Test
   public void restoreSnapshotsTest() {
      String rootSnapName = "Darth Snapshot";
      String rootSnapDesc = "I am your father";
      String childSnapName = "Snap Skywalker";
      String childSnapDesc = "Nooooooooo";
      _RawVM vm = createVm();
      
      assertFalse(vm.hasSnapshot());
      
      _RawSnapshot rawSnap = vm.takeSnapshot(rootSnapName, rootSnapDesc);
      _RawSnapshot currentSnap = vm.getCurrentSnapshot();
      assertTrue(rawSnap.getUuid().contentEquals(currentSnap.getUuid()));
      
      _RawSnapshot childSnap = vm.takeSnapshot(childSnapName, childSnapDesc);
      currentSnap = vm.getCurrentSnapshot();
      assertTrue(childSnap.getUuid().contentEquals(currentSnap.getUuid()));
      
      vm.restoreSnapshot(rawSnap.getUuid());
      currentSnap = vm.getCurrentSnapshot();
      assertTrue(rawSnap.getUuid().contentEquals(currentSnap.getUuid()));
      
      vm.restoreSnapshot(childSnap.getUuid());
      currentSnap = vm.getCurrentSnapshot();
      assertTrue(childSnap.getUuid().contentEquals(currentSnap.getUuid()));
   }
   
   @Test
   public void createNoUuid() {
      MachineIn mIn = new MachineIn();
      mIn.setName(Long.toString(System.currentTimeMillis()));
      createVm(mIn);
   }
   
   @Test
   public void createStorageController() {
      _RawVM vm = createVm();
      
      _RawStorageController rawSc = null;
      
      vm.addStorageController(StorageControllerType.IDE, "ide");
      rawSc = vm.getStorageController("ide");
      RawStorageControllerTest.validateFull(rawSc);
      assertTrue(rawSc.getMachineUuid().contentEquals(vm.getUuid()));
      assertTrue(rawSc.getName().contentEquals("ide"));
      
      vm.addStorageController(StorageControllerType.SATA, "sata");
      rawSc = vm.getStorageController("sata");
      RawStorageControllerTest.validateFull(rawSc);
      assertTrue(rawSc.getMachineUuid().contentEquals(vm.getUuid()));
      assertTrue(rawSc.getName().contentEquals("sata"));
   }
   
   @Test
   public void modifyVm() {
      MachineIn mIn = new MachineIn(UUID.randomUUID().toString());
      mIn.setName(Long.toString(System.currentTimeMillis()));
      
      _RawVM rawVm = createVm(mIn);
      
      rawVm.lock();
      
      rawVm.getMemory().setAmount(256);
      rawVm.getKeyboard().setMode(KeyboardMode.Usb);
      _RawStorageController rawScIde = rawVm.addStorageController(StorageControllerType.IDE, "testIde");
      rawScIde.setSubType(StorageControllerSubType.PIIX3.getId());
      _RawStorageController rawScSata = rawVm.addStorageController(StorageControllerType.SATA, "testSata");
      rawScSata.setSubType(StorageControllerSubType.IntelAhci.getId());
      
      rawVm.saveChanges();
      rawVm.unlock();
      
      _RawVM resultVm = hypervisor.getMachine(mIn.getUuid());
      
      assertTrue(resultVm.getMemory().getAmount() == 256);
      assertTrue(resultVm.getKeyboard().getMode().contentEquals(KeyboardMode.Usb.getId()));
      assertTrue(((PositiveNumberSetting) resultVm.getSetting(MachineAttribute.Memory)).getValue() == 256);
      assertTrue(((StringSetting) resultVm.getSetting(MachineAttribute.KeyboardMode)).getValue().contentEquals(KeyboardMode.Usb.getId()));
      assertTrue(resultVm.listStoroageControllers().size() == 2);
      
      _RawStorageController resultSc01 = resultVm.getStorageController("testIde");
      assertTrue(resultSc01.getName().contentEquals("testIde"));
      assertTrue(resultSc01.getType().contentEquals(StorageControllerType.IDE.getId()));
      assertTrue(resultSc01.getSubType().contentEquals(StorageControllerSubType.PIIX3.getId()));
      
      _RawStorageController resultSc02 = resultVm.getStorageController("testSata");
      assertTrue(resultSc02.getName().contentEquals("testSata"));
      assertTrue(resultSc02.getType().contentEquals(StorageControllerType.SATA.getId()));
      assertTrue(resultSc02.getSubType().contentEquals(StorageControllerSubType.IntelAhci.getId()));
      
      for (_RawNetworkInterface resultNic : resultVm.listNetworkInterfaces()) {
         for (_Setting s : resultNic.listSettings()) {
            assertNotNull(resultNic.getSetting(s.getName()).getRawValue());
         }
      }
   }
   
   @Test
   public void listStorageControllerTypeTest() {
      List<_RawStorageControllerType> typeList = hypervisor.listStorageControllerType();
      assertNotNull(typeList);
      assertFalse(typeList.isEmpty());
      for (_RawStorageControllerType type : typeList) {
         RawStorageControllerTypeTest.validate(type);
         RawStorageControllerTypeTest.compare(type, hypervisor.getStorageControllerType(type.getId()));
         List<_RawStorageControllerSubType> subtypeList = hypervisor.listStorageControllerSubType(type.getId());
         assertNotNull(subtypeList);
         assertFalse(subtypeList.isEmpty());
         for (_RawStorageControllerSubType subtype : subtypeList) {
            RawStorageControllerSubTypeTest.validate(subtype);
         }
      }
   }
   
   @Test
   public void listMediums() {
      // TODO update for non-harcoded value
      _RawMedium guestAddMed = hypervisor.getToolsMedium();
      RawMediumTest.validate(guestAddMed);
      
      List<_RawMedium> rawMedList = hypervisor.listMediums();
      System.out.println("Number of mediums: " + rawMedList.size());
      for (_RawMedium rawMed : rawMedList) {
         RawMediumTest.validate(rawMed);
      }
   }
   
   @Test
   public void createAndDeleteMediumTest() {
      // Update for non hard-coded path & extension
      String path = "/tmp/test" + System.currentTimeMillis() + ".vdi";
      _RawMedium rawMed = hypervisor.createHardDisk(path, HardDiskFormat.VDI.getId(), 1535000l);
      RawMediumTest.validate(rawMed);
      _RawMedium otherMed = hypervisor.getMedium(rawMed.getUuid());
      RawMediumTest.validate(otherMed);
      assertTrue(otherMed.getLocation().contentEquals(path));
      hypervisor.deleteMedium(rawMed.getUuid());
      try {
         hypervisor.getMedium(path, EntityType.HardDisk);
         assertTrue(false);
      } catch (HypervisorException e) {
         assertTrue(true);
      }
   }
   
   @Test
   public void listAttachModeTest() {
      List<String> attachModes = hypervisor.listNicAttachModes();
      assertFalse(attachModes.isEmpty());
      for (String attachMode : attachModes) {
         assertNotNull(attachMode);
         assertFalse(attachMode.isEmpty());
      }
   }
   
   @Test
   public void correlateVmAttachModeAndAttachModeList() {
      _RawVM rawVm = createVm();
      
      List<String> attachModes = hypervisor.listNicAttachModes();
      for (_RawNetworkInterface rawNic : rawVm.listNetworkInterfaces()) {
         assertTrue("Unknown attach mode : " + rawNic.getAttachMode(), attachModes.contains(rawNic.getAttachMode()));
      }
   }
   
   @Test
   public void listAttachNameTest() {
      List<String> attachModes = hypervisor.listNicAttachModes();
      for (String attachMode : attachModes) {
         List<String> attachNames = hypervisor.listNicAttachNames(attachMode);
         for (String attachName : attachNames) {
            assertNotNull(attachName);
            assertFalse(attachName.isEmpty());
         }
      }
   }
   
   @Test
   public void takeScreenshot() {
      _RawVM vm = createVm();
      vm.powerOn();
      byte[] screenData = vm.takeScreenshot();
      assertNotNull(screenData);
      vm.powerOff();
   }
   
   @Test(expected = MachineDisplayNotAvailableException.class)
   public void takeScreenshotFail() {
      _RawVM vm = createVm();
      vm.takeScreenshot();
   }
   
   @AfterClass
   public static void afterClass() {
      hypervisor.stop();
   }
   
}
