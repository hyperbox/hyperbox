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

package org.altherian.hbox.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
   
   private Map<String, Machine> vmMap = new HashMap<String, Machine>();
   private Map<String, Task> taskMap = new HashMap<String, Task>();
   
   public void addMachine(Machine vm) {
      vmMap.put(vm.getUuid(), vm);
   }
   
   public void removeMachine(String uuid) {
      vmMap.remove(uuid);
   }
   
   public void updateMachine(Machine vm) {
      addMachine(vm);
   }
   
   public List<Machine> listMachines() {
      return new ArrayList<Machine>(vmMap.values());
   }
   
   public void addTask(Task t) {
      taskMap.put(t.getId(), t);
   }
   
   public void removeTask(String id) {
      taskMap.remove(id);
   }
   
   public void updateTask(Task t) {
      addTask(t);
   }
   
   public List<Task> listTasks() {
      return new ArrayList<Task>(taskMap.values());
   }
   
}
