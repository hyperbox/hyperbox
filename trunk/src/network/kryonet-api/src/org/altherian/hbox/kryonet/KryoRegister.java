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

package org.altherian.hbox.kryonet;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.Message;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.Action;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.in.MediumIn;
import org.altherian.hbox.comm.in.NetworkAttachModeIn;
import org.altherian.hbox.comm.in.NetworkAttachNameIn;
import org.altherian.hbox.comm.in.NetworkInterfaceIn;
import org.altherian.hbox.comm.in.NetworkInterfaceTypeIn;
import org.altherian.hbox.comm.in.OsTypeIn;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.in.SessionIn;
import org.altherian.hbox.comm.in.SnapshotIn;
import org.altherian.hbox.comm.in.StorageControllerIn;
import org.altherian.hbox.comm.in.StorageControllerSubTypeIn;
import org.altherian.hbox.comm.in.StorageControllerTypeIn;
import org.altherian.hbox.comm.in.StorageDeviceAttachmentIn;
import org.altherian.hbox.comm.in.StoreIn;
import org.altherian.hbox.comm.in.StoreItemIn;
import org.altherian.hbox.comm.in.TaskIn;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.PositiveNumberSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.out.ExceptionOut;
import org.altherian.hbox.comm.out.MachineMetricOut;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.SessionOut;
import org.altherian.hbox.comm.out.StoreItemOut;
import org.altherian.hbox.comm.out.StoreOut;
import org.altherian.hbox.comm.out.TaskOut;
import org.altherian.hbox.comm.out.event.UnknownEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineDataChangeEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineRegistrationEventOut;
import org.altherian.hbox.comm.out.event.machine.MachineStateEventOut;
import org.altherian.hbox.comm.out.event.session.SessionStateEventOut;
import org.altherian.hbox.comm.out.event.snapshot.SnapshotDeletedEventOut;
import org.altherian.hbox.comm.out.event.snapshot.SnapshotModifiedEventOut;
import org.altherian.hbox.comm.out.event.snapshot.SnapshotRestoredEventOut;
import org.altherian.hbox.comm.out.event.snapshot.SnapshotTakenEventOut;
import org.altherian.hbox.comm.out.event.system.SystemStateEventOut;
import org.altherian.hbox.comm.out.event.task.TaskQueueEventOut;
import org.altherian.hbox.comm.out.event.task.TaskStateEventOut;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.comm.out.hypervisor.MachineSessionOut;
import org.altherian.hbox.comm.out.hypervisor.OsTypeOut;
import org.altherian.hbox.comm.out.hypervisor.SnapshotOut;
import org.altherian.hbox.comm.out.network.NetworkAttachModeOut;
import org.altherian.hbox.comm.out.network.NetworkAttachNameOut;
import org.altherian.hbox.comm.out.network.NetworkInterfaceOut;
import org.altherian.hbox.comm.out.network.NetworkInterfaceTypeOut;
import org.altherian.hbox.comm.out.security.UserOut;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hbox.comm.out.storage.StorageControllerOut;
import org.altherian.hbox.comm.out.storage.StorageControllerSubTypeOut;
import org.altherian.hbox.comm.out.storage.StorageControllerTypeOut;
import org.altherian.hbox.comm.out.storage.StorageDeviceAttachmentOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.StorageControllerSubType;
import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.constant.StoreAttribute;
import org.altherian.hbox.constant.StoreItemAttribute;
import org.altherian.hbox.states.ACPI;
import org.altherian.hbox.states.MachineSessionStates;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hbox.states.ModuleState;
import org.altherian.hbox.states.ProgressTrackerState;
import org.altherian.hbox.states.ServerState;
import org.altherian.hbox.states.SessionStates;
import org.altherian.hbox.states.TaskQueueEvents;
import org.altherian.hbox.states.TaskState;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import com.esotericsoftware.kryo.Kryo;

/**
 * Used to register classes that will be sent through the channel.
 * 
 * @author noteirak
 */
public class KryoRegister {
   
   public static void register(Kryo kryo) {
      kryo.setRegistrationRequired(false);
      /*
       * Register Java API classes
       */
      kryo.register(ArrayList.class);
      kryo.register(HashMap.class);
      kryo.register(HashSet.class);
      kryo.register(Date.class);
      
      /*
       * Register Hyperbox API communication classes
       */
      kryo.register(Message.class);
      kryo.register(Request.class);
      kryo.register(Answer.class);
      kryo.register(AnswerType.class);
      kryo.register(Command.class);
      kryo.register(Action.class);
      kryo.register(ExceptionOut.class);
      
      /*
       * Register Hyperbox API IO classes
       */
      kryo.register(StringSettingIO.class);
      kryo.register(BooleanSettingIO.class);
      kryo.register(PositiveNumberSettingIO.class);
      
      kryo.register(MachineIn.class);
      kryo.register(MachineOut.class);
      
      kryo.register(MachineMetricOut.class);
      
      kryo.register(MachineSessionOut.class);
      
      kryo.register(StoreIn.class);
      kryo.register(StoreOut.class);
      
      kryo.register(StoreItemIn.class);
      kryo.register(StoreItemOut.class);
      
      kryo.register(MediumIn.class);
      kryo.register(MediumOut.class);
      
      kryo.register(NetworkInterfaceIn.class);
      kryo.register(NetworkInterfaceOut.class);
      
      kryo.register(NetworkAttachModeIn.class);
      kryo.register(NetworkAttachModeOut.class);
      
      kryo.register(NetworkAttachNameIn.class);
      kryo.register(NetworkAttachNameOut.class);
      
      kryo.register(NetworkInterfaceTypeIn.class);
      kryo.register(NetworkInterfaceTypeOut.class);
      
      kryo.register(StorageControllerIn.class);
      kryo.register(StorageControllerOut.class);
      
      kryo.register(StorageControllerTypeIn.class);
      kryo.register(StorageControllerTypeOut.class);
      
      kryo.register(StorageControllerSubTypeIn.class);
      kryo.register(StorageControllerSubTypeOut.class);
      
      kryo.register(StorageDeviceAttachmentIn.class);
      kryo.register(StorageDeviceAttachmentOut.class);
      
      kryo.register(SnapshotIn.class);
      kryo.register(SnapshotOut.class);
      
      kryo.register(UserIn.class);
      kryo.register(UserOut.class);
      
      kryo.register(SessionOut.class);
      kryo.register(SessionIn.class);
      
      kryo.register(ServerIn.class);
      kryo.register(ServerOut.class);
      
      kryo.register(TaskIn.class);
      kryo.register(TaskOut.class);
      
      kryo.register(OsTypeIn.class);
      kryo.register(OsTypeOut.class);
      
      /*
       * Events
       */
      kryo.register(MachineDataChangeEventOut.class);
      kryo.register(MachineRegistrationEventOut.class);
      kryo.register(MachineStateEventOut.class);
      kryo.register(SessionStateEventOut.class);
      kryo.register(SnapshotModifiedEventOut.class);
      kryo.register(SnapshotDeletedEventOut.class);
      kryo.register(SnapshotRestoredEventOut.class);
      kryo.register(SnapshotTakenEventOut.class);
      kryo.register(SystemStateEventOut.class);
      kryo.register(TaskQueueEventOut.class);
      kryo.register(TaskStateEventOut.class);
      kryo.register(UnknownEventOut.class);
      
      /*
       * Register Hyperbox API business classes
       */
      kryo.register(ACPI.class);
      kryo.register(EntityType.class);
      kryo.register(MachineSessionStates.class);
      kryo.register(MachineStates.class);
      kryo.register(ModuleState.class);
      kryo.register(ProgressTrackerState.class);
      kryo.register(SessionStates.class);
      kryo.register(StoreAttribute.class);
      kryo.register(StoreItemAttribute.class);
      kryo.register(ServerState.class);
      kryo.register(TaskQueueEvents.class);
      kryo.register(TaskState.class);
      kryo.register(StorageControllerSubType.class);
      kryo.register(StorageControllerType.class);
   }
   
}
