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
import static org.junit.Assert.assertTrue;

import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.constant.MachineAttributes;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hbox.settings.vbox.cpu.CpuExecCapSetting;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.vbox.hypervisor.ws4_3.VirtualboxHypervisor;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

public final class HypervisorTest_VBWS43 extends HypervisorTest {
   
   @BeforeClass
   public static void beforeClass() throws HyperboxException {
      hypervisor = new VirtualboxHypervisor();
      hypervisor.setEventManager(EventManager.get());
   }
   
   @Test
   public void modifyVmWhileRunning() {
      Long execCap = 43l;
      MachineInput mIn = new MachineInput(UUID.randomUUID().toString());
      mIn.setName(Long.toString(System.currentTimeMillis()));
      
      _RawVM rawVm = createVm(mIn);
      
      rawVm.powerOn();
      rawVm.setSetting(new CpuExecCapSetting(execCap));
      
      rawVm = hypervisor.getMachine(rawVm.getUuid());
      assertTrue(rawVm.getSetting(MachineAttributes.CpuExecCap).getNumber().equals(execCap));
      
      rawVm.powerOff();
      
      rawVm = hypervisor.getMachine(rawVm.getUuid());
      assertTrue(rawVm.getSetting(MachineAttributes.CpuExecCap).getNumber().equals(execCap));
      
      rawVm.setSetting(new CpuExecCapSetting(100l));
      rawVm = hypervisor.getMachine(rawVm.getUuid());
      assertTrue(rawVm.getSetting(MachineAttributes.CpuExecCap).getNumber().equals(100l));
   }
   
   @Test(expected = HypervisorException.class)
   public void modifyVmRunning() {
      MachineInput mIn = new MachineInput(UUID.randomUUID().toString());
      mIn.setName(Long.toString(System.currentTimeMillis()));
      
      _RawVM rawVm = createVm(mIn);
      assertFalse(rawVm.getDisplay().getVideoMemoryAmount() == 187l);
      
      rawVm.powerOn();
      assertTrue(rawVm.getState().equals(MachineStates.Running));
      rawVm.lock();
      rawVm.getDisplay().setVideoMemoryAmount(187l);
      rawVm.saveChanges();
      rawVm.unlock();
      
      rawVm = hypervisor.getMachine(mIn.getUuid());
      assertTrue(rawVm.getDisplay().getVideoMemoryAmount() == 187l);
   }
   
}
