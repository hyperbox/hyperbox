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

package org.altherian.hbox.states;

/**
 * Machine states of Virtualbox
 * 
 * @author noteirak
 */
public enum MachineStates {
   
   /**
    * Unknown state, most likely added and unsupported
    */
   UNKNOWN,
   
   /**
    * Unable to retrieve the Machine information. This represent a broken state.
    */
   Inaccessible,
   
   /**
    * Lengthy setup operation is in progress.
    */
   SettingUp,
   
   /**
    * Machine is being started after powering it on.
    */
   Starting,
   
   /**
    * Execution state of the machine is being restored from a file after powering it on from the saved execution state.
    */
   Restoring,
   
   /**
    * The machine is currently being executed.
    */
   Running,
   
   /**
    * Execution of the machine has been paused.
    */
   Paused,
   
   /**
    * Machine is being normally stopped powering it off, or after the guest OS has initiated a shutdown sequence.
    */
   Stopping,
   
   /**
    * Machine is saving its execution state to a file, or an online snapshot of the machine is being taken.
    */
   Saving,
   
   /**
    * The machine is not currently running, but the execution state of the machine has been saved to an external file when it was running, from where it can be resumed.
    */
   Saved,
   
   /**
    * The machine is not running and has no saved execution state; it has either never been started or been shut down successfully.
    */
   PoweredOff,
   
   /**
    * Execution of the machine has reached the "Guru Meditation" condition. This indicates a severe error in the hypervisor itself.
    */
   Stuck,
   
   /**
    * The process running the machine has terminated abnormally. This may indicate a crash of the VM process in host execution context, or the VM process has been terminated externally.
    */
   Aborted,
   
   /**
    * The machine is about to be teleported to a different host or process. It is possible to pause a machine in this state, but it will go to the TeleportingPausedVM state and it will not be possible
    * to resume it again unless the teleportation fails.
    */
   Teleporting,
   
   /**
    * The machine is being teleported to another host or process, but it is not running.
    */
   TeleportingPaused,
   
   /**
    * Receiving the teleportation of a machine state from another host or process.
    */
   TeleportingIn,
   
   /**
    * The machine was teleported to a different host (or process) and then powered off.
    */
   Teleported,
   
   /**
    * The machine is being synced with a fault tolerant VM running elsewhere.
    */
   FaultTolerantSyncing,
   
   /**
    * A live snapshot is being taken. The machine is running normally, but some of the runtime configuration options are inaccessible. Also, if paused while in this state it will transition to Saving
    * and it will not be resume the execution until the snapshot operation has completed.
    */
   SnapshotLive,
   
   /**
    * A machine snapshot is being deleted; this can take a long time since this may require merging differencing media. This value indicates that the machine is not running while the snapshot is being
    * deleted.
    */
   SnapshotDeletingOff,
   
   /**
    * Like SNAPSHOT_DELETING_OFF, but the merging of media is ongoing in the background while the machine is running.
    */
   SnapshotDeletingOnline,
   
   /**
    * Like SNAPSHOT_DELETING_OFF, but the machine was paused when the merging of differencing media was started.
    */
   SnapshotDeletingPaused,
   
   /**
    * A machine snapshot is being restored.
    */
   SnapshotRestoring;
   
   public String getId() {
      return toString();
   }
   
   public boolean match(String eq) {
      return getId().contentEquals(eq);
   }
   
}
