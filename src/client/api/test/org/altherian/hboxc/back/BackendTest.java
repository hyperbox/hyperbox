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

package org.altherian.hboxc.back;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.MediumIn;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.in.TaskIn;
import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.comm.out.security.UserOut;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hbox.comm.output.MachineOutputTest;
import org.altherian.hbox.comm.output.MediumOutputTest;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxc.comm.utils.Transaction;
import org.altherian.hboxc.event.EventManager;
import org.altherian.tool.logging.LogLevel;
import org.altherian.tool.logging.Logger;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public abstract class BackendTest {
   
   private static _Backend backend;
   
   public static void init(_Backend b, ServerIn srvIn) throws HyperboxException {
      Logger.setLevel(LogLevel.Tracking);
      
      BackendTest.backend = b;
      
      EventManager.get().start();
      b.start();
      assertFalse(b.isConnected());
      assertTrue(b.isConnected());
      b.disconnect();
      assertFalse(b.isConnected());
   }
   
   @Before
   public void before() throws HyperboxException {
      assertFalse(backend.isConnected());
      assertTrue(backend.isConnected());
   }
   
   @After
   public void after() {
      backend.disconnect();
      assertFalse(backend.isConnected());
   }
   
   @AfterClass
   public static void afterClass() {
      EventManager.get().stop();
   }
   
   @Test
   public void helloTest() {
      Transaction t = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.Hello));
      assertTrue(t.sendAndWait());
      Queue<Answer> answers = t.getBody();
      assertTrue("We must have only one reply", answers.size() == 1);
      Answer ans = answers.poll();
      assertTrue("The answer must containt the Hello data", ans.has("Hello"));
      assertNotNull("The Hello data must be a String", ans.get("Hello").toString());
      System.out.println("Server banner : " + ans.get("Hello"));
   }
   
   @Test
   public void listVmTest() {
      String newUuid = UUID.randomUUID().toString();
      MachineIn mIn = new MachineIn(newUuid);
      mIn.setName(Long.toString(System.currentTimeMillis()));
      
      Transaction t = new Transaction(backend, new Request(Command.VBOX, HypervisorTasks.MachineCreate, mIn));
      t.sendAndWaitForTask();
      assertFalse(t.getError(), t.hasFailed());
      
      t = new Transaction(backend, new Request(Command.VBOX, HypervisorTasks.MachineList));
      t.sendAndWait();
      assertFalse(t.getError(), t.hasFailed());
      assertFalse(t.getBody().isEmpty());
      Queue<Answer> answers = t.getBody();
      
      for (Answer ans : answers) {
         assertTrue(ans.has(MachineOut.class));
         MachineOut mOut = ans.get(MachineOut.class);
         MachineOutputTest.validateSimple(mOut);
      }
   }
   
   @Test
   public void listUsers() {
      Transaction t = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.UserList));
      assertTrue(t.sendAndWait());
      Queue<Answer> answers = t.getBody();
      for (Answer ans : answers) {
         assertTrue(ans.has(UserOut.class));
         UserOut objOut = ans.get(UserOut.class);
         assertNotNull(objOut);
         assertFalse(objOut.getId().isEmpty());
         assertFalse(objOut.getUsername().isEmpty());
      }
   }
   
   @Test
   public void listTasks() {
      Transaction t = null;
      
      String newUuid = UUID.randomUUID().toString();
      MachineIn mIn = new MachineIn(newUuid);
      mIn.setName(Long.toString(System.currentTimeMillis()));
      
      t = new Transaction(backend, new Request(Command.VBOX, HypervisorTasks.MachineCreate, mIn));
      t.sendAndWaitForTask();
      assertFalse(t.getError(), t.hasFailed());
      
      t = new Transaction(backend, new Request(Command.VBOX, HypervisorTasks.MachinePowerOn, mIn));
      t.sendAndWaitForTask();
      assertFalse(t.getError(), t.hasFailed());
      
      t = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.GuestShutdown, mIn));
      t.sendAndWait();
      assertFalse(t.getError(), t.hasFailed());
      TaskOut tOutFinal = t.extractItem(TaskOut.class);
      assertNotNull(tOutFinal);
      
      try {
         t = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.TaskList));
         t.sendAndWait();
         assertFalse(t.getError(), t.hasFailed());
         assertFalse(t.getBody().isEmpty());
         
         Queue<Answer> answers = t.getBody();
         assertTrue(answers.size() >= 1);
         for (Answer ans : answers) {
            assertTrue(ans.has(TaskOut.class));
            TaskOut tOut = ans.get(TaskOut.class);
            assertNotNull("TaskOutput shouldn't be null", tOut);
            assertFalse(tOut.getId().isEmpty());
            assertNotNull(tOut.getUser());
         }
      } finally {
         t = new Transaction(backend, new Request(Command.HBOX, HyperboxTasks.TaskCancel, new TaskIn(tOutFinal.getId())));
         t.sendAndWait();
         assertFalse(t.getError(), t.hasFailed());
      }
   }
   
   @Test
   public void listMediums() {
      Transaction t = new Transaction(backend, new Request(Command.VBOX, HypervisorTasks.MediumList));
      t.sendAndWait();
      assertFalse(t.getError(), t.hasFailed());
      
      List<MediumOut> medOutList = t.extractItems(MediumOut.class);
      if (medOutList.isEmpty()) {
         t = new Transaction(backend, new Request(Command.VBOX, HypervisorTasks.MediumGet, new MediumIn("/usr/share/VBoxGuestAdditions")));
         t.sendAndWait();
         assertFalse(t.getError(), t.hasFailed());
         MediumOut newMedOut = t.extractItem(MediumOut.class);
         MediumOutputTest.validateFull(newMedOut);
      } else {
         for (MediumOut medOut : medOutList) {
            MediumOutputTest.validateSimple(medOut);
            t = new Transaction(backend, new Request(Command.VBOX, HypervisorTasks.MediumGet, new MediumIn(medOut.getUuid())));
            t.sendAndWait();
            assertFalse(t.getError(), t.hasFailed());
            MediumOut newMedOut = t.extractItem(MediumOut.class);
            MediumOutputTest.validateFull(newMedOut);
         }
      }
   }
   
   @Test(expected = HyperboxRuntimeException.class)
   public void sendDisconnectedFail() {
      backend.disconnect();
      backend.putRequest(new Request(Command.HBOX, HyperboxTasks.Hello));
   }
   
}
