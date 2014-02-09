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

package org.altherian.hbox.kryonet;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.Message;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.input.Action;
import org.altherian.hbox.comm.input.MachineInput;
import org.altherian.hbox.comm.input.MediumInput;
import org.altherian.hbox.comm.input.NetworkAttachModeInput;
import org.altherian.hbox.comm.input.NetworkAttachNameInput;
import org.altherian.hbox.comm.input.NetworkInterfaceInput;
import org.altherian.hbox.comm.input.NetworkInterfaceTypeInput;
import org.altherian.hbox.comm.input.OsTypeInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.input.SessionInput;
import org.altherian.hbox.comm.input.SnapshotInput;
import org.altherian.hbox.comm.input.StorageControllerInput;
import org.altherian.hbox.comm.input.StorageControllerSubTypeInput;
import org.altherian.hbox.comm.input.StorageControllerTypeInput;
import org.altherian.hbox.comm.input.StorageDeviceAttachmentInput;
import org.altherian.hbox.comm.input.StoreInput;
import org.altherian.hbox.comm.input.StoreItemInput;
import org.altherian.hbox.comm.input.TaskInput;
import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.PositiveNumberSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.EnumOutput;
import org.altherian.hbox.comm.output.ExceptionOutput;
import org.altherian.hbox.comm.output.MachineMetricOutput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.SessionOutput;
import org.altherian.hbox.comm.output.StoreItemOutput;
import org.altherian.hbox.comm.output.StoreOutput;
import org.altherian.hbox.comm.output.TaskOutput;
import org.altherian.hbox.comm.output.UserOutput;
import org.altherian.hbox.comm.output.event.UnknownEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineDataChangeEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineRegistrationEventOutput;
import org.altherian.hbox.comm.output.event.machine.MachineStateEventOutput;
import org.altherian.hbox.comm.output.event.session.SessionStateEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotDeletedEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotModifiedEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotRestoredEventOutput;
import org.altherian.hbox.comm.output.event.snapshot.SnapshotTakenEventOutput;
import org.altherian.hbox.comm.output.event.store.StoreStateEventOutput;
import org.altherian.hbox.comm.output.event.system.SystemStateEventOutput;
import org.altherian.hbox.comm.output.event.task.TaskQueueEventOutput;
import org.altherian.hbox.comm.output.event.task.TaskStateEventOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineSessionOutput;
import org.altherian.hbox.comm.output.hypervisor.OsTypeOutput;
import org.altherian.hbox.comm.output.hypervisor.SnapshotOutput;
import org.altherian.hbox.comm.output.network.NetworkAttachModeOutput;
import org.altherian.hbox.comm.output.network.NetworkAttachNameOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceTypeOutput;
import org.altherian.hbox.comm.output.storage.MediumOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerSubTypeOutput;
import org.altherian.hbox.comm.output.storage.StorageControllerTypeOutput;
import org.altherian.hbox.comm.output.storage.StorageDeviceAttachmentOutput;
import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.constant.StorageControllerSubType;
import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.constant.StoreAttributes;
import org.altherian.hbox.constant.StoreItemAttributes;
import org.altherian.hbox.states.ACPI;
import org.altherian.hbox.states.MachineSessionStates;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hbox.states.ModuleState;
import org.altherian.hbox.states.ProgressTrackerState;
import org.altherian.hbox.states.ServerState;
import org.altherian.hbox.states.SessionStates;
import org.altherian.hbox.states.StoreState;
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
      kryo.register(EnumOutput.class);
      kryo.register(Action.class);
      kryo.register(ExceptionOutput.class);
      
      /*
       * Register Hyperbox API IO classes
       */
      kryo.register(StringSettingIO.class);
      kryo.register(BooleanSettingIO.class);
      kryo.register(PositiveNumberSettingIO.class);
      
      kryo.register(MachineInput.class);
      kryo.register(MachineOutput.class);
      
      kryo.register(MachineMetricOutput.class);
      
      kryo.register(MachineSessionOutput.class);
      
      kryo.register(StoreInput.class);
      kryo.register(StoreOutput.class);
      
      kryo.register(StoreItemInput.class);
      kryo.register(StoreItemOutput.class);
      
      kryo.register(MediumInput.class);
      kryo.register(MediumOutput.class);
      
      kryo.register(NetworkInterfaceInput.class);
      kryo.register(NetworkInterfaceOutput.class);
      
      kryo.register(NetworkAttachModeInput.class);
      kryo.register(NetworkAttachModeOutput.class);
      
      kryo.register(NetworkAttachNameInput.class);
      kryo.register(NetworkAttachNameOutput.class);
      
      kryo.register(NetworkInterfaceTypeInput.class);
      kryo.register(NetworkInterfaceTypeOutput.class);
      
      kryo.register(StorageControllerInput.class);
      kryo.register(StorageControllerOutput.class);
      
      kryo.register(StorageControllerTypeInput.class);
      kryo.register(StorageControllerTypeOutput.class);
      
      kryo.register(StorageControllerSubTypeInput.class);
      kryo.register(StorageControllerSubTypeOutput.class);
      
      kryo.register(StorageDeviceAttachmentInput.class);
      kryo.register(StorageDeviceAttachmentOutput.class);
      
      kryo.register(SnapshotInput.class);
      kryo.register(SnapshotOutput.class);
      
      kryo.register(UserInput.class);
      kryo.register(UserOutput.class);
      
      kryo.register(SessionOutput.class);
      kryo.register(SessionInput.class);
      
      kryo.register(ServerInput.class);
      kryo.register(ServerOutput.class);
      
      kryo.register(TaskInput.class);
      kryo.register(TaskOutput.class);
      
      kryo.register(OsTypeInput.class);
      kryo.register(OsTypeOutput.class);
      
      /*
       * Events
       */
      kryo.register(MachineDataChangeEventOutput.class);
      kryo.register(MachineRegistrationEventOutput.class);
      kryo.register(MachineStateEventOutput.class);
      kryo.register(SessionStateEventOutput.class);
      kryo.register(SnapshotModifiedEventOutput.class);
      kryo.register(SnapshotDeletedEventOutput.class);
      kryo.register(SnapshotRestoredEventOutput.class);
      kryo.register(SnapshotTakenEventOutput.class);
      kryo.register(StoreStateEventOutput.class);
      kryo.register(SystemStateEventOutput.class);
      kryo.register(TaskQueueEventOutput.class);
      kryo.register(TaskStateEventOutput.class);
      kryo.register(UnknownEventOutput.class);
      
      /*
       * Register Hyperbox API business classes
       */
      kryo.register(ACPI.class);
      kryo.register(EntityTypes.class);
      kryo.register(MachineSessionStates.class);
      kryo.register(MachineStates.class);
      kryo.register(ModuleState.class);
      kryo.register(ProgressTrackerState.class);
      kryo.register(SessionStates.class);
      kryo.register(StoreState.class);
      kryo.register(StoreAttributes.class);
      kryo.register(StoreItemAttributes.class);
      kryo.register(ServerState.class);
      kryo.register(TaskQueueEvents.class);
      kryo.register(TaskState.class);
      kryo.register(StorageControllerSubType.class);
      kryo.register(StorageControllerType.class);
   }
   
}
