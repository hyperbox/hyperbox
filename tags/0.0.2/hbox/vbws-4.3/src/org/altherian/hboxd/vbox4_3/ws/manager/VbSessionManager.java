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

package org.altherian.hboxd.vbox4_3.ws.manager;

import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.exception.HypervisorException;
import org.altherian.hboxd.vbox4_3.ws.factory.ConnectionManager;
import org.altherian.hboxd.vbox4_3.ws.utils.EventBusFactory;
import org.altherian.tool.logging.Logger;

import java.util.HashMap;
import java.util.Map;

import org.virtualbox_4_3.IMachine;
import org.virtualbox_4_3.IMachineStateChangedEvent;
import org.virtualbox_4_3.ISession;
import org.virtualbox_4_3.ISessionStateChangedEvent;
import org.virtualbox_4_3.LockType;
import org.virtualbox_4_3.MachineState;
import org.virtualbox_4_3.SessionState;
import org.virtualbox_4_3.SessionType;
import org.virtualbox_4_3.VBoxException;

public class VbSessionManager implements _RawSessionManager {
   
   private static ThreadLocal<VbSessionManager> instance = new ThreadLocal<VbSessionManager>();
   private ThreadLocal<Map<String, ISession>> sessions;
   private ThreadLocal<Map<String, Boolean>> userLocking;
   
   public static _RawSessionManager get() {
      if (instance.get() == null) {
         instance.set(new VbSessionManager());
      }
      return instance.get();
   }
   
   public VbSessionManager() {
      sessions = new ThreadLocal<Map<String, ISession>>();
      sessions.set(new HashMap<String, ISession>());
      
      userLocking = new ThreadLocal<Map<String, Boolean>>();
      userLocking.set(new HashMap<String, Boolean>());
      
      EventBusFactory.subscribe(this);
   }
   
   @Handler
   public void getMachineStateEvent(IMachineStateChangedEvent ev) {
      Logger.track();
      
      if (ev.getState().equals(MachineState.Stopping)) {
         unlock(ev.getMachineId(), false);
      }
   }
   
   @Handler
   public void getMachineSessionStateEvent(ISessionStateChangedEvent ev) {
      Logger.track();
      
      if (ev.getState().equals(SessionState.Unlocked) || ev.getState().equals(SessionState.Unlocking)) {
         unlock(ev.getMachineId(), false);
      }
   }
   
   @Override
   public ISession lock(String uuid, LockType lockType) {
      return lock(uuid, lockType, true);
   }
   
   private ISession lock(String uuid, LockType lockType, boolean userRequest) {
      Logger.track();
      
      try {
         if (sessions.get().containsKey(uuid) && (lockType.equals(LockType.Shared) ||
               (sessions.get().get(uuid).getState().equals(SessionState.Locked) && sessions.get().get(uuid).getType().equals(SessionType.WriteLock)))) {
            Logger.debug("Found a session for [" + uuid + "] that fits the locktype [" + lockType + "]");
         } else {
            Logger.debug("Creating new " + lockType + " lock on " + uuid);
            ISession session = ConnectionManager.getSession();
            IMachine roMachine = ConnectionManager.findMachine(uuid);
            roMachine.lockMachine(session, lockType);
            sessions.get().put(uuid, session);
         }
         
         if (!userLocking.get().containsKey(uuid) || !userLocking.get().get(uuid)) {
            userLocking.get().put(uuid, userRequest);
         }
         
         return sessions.get().get(uuid);
      } catch (VBoxException e) {
         throw new HypervisorException(e.getMessage(), e);
      }
   }
   
   @Override
   public void unlock(String uuid, boolean saveSettings) {
      unlock(uuid, true, saveSettings);
   }
   
   @Override
   public void unlock(String uuid) {
      unlock(uuid, true);
   }
   
   private void unlock(String uuid, boolean userRequest, boolean saveSettings) {
      Logger.track();
      
      if (sessions.get().containsKey(uuid) && (userRequest || (userLocking.get().containsKey(uuid) && !userLocking.get().get(uuid)))) {
         Logger.debug("Found a session for VM #" + uuid + " and the unlock is authorized.");
         try {
            if (sessions.get().get(uuid).getState().equals(SessionState.Locked)) {
               Logger.debug("SaveSettings: " + saveSettings);
               if (saveSettings) {
                  try {
                     sessions.get().get(uuid).getMachine().saveSettings();
                  } catch (VBoxException e) {
                     /*
                      * In case of PowerDown or SaveState, the session lock could be removed between the time we check for the session state
                      * and the save settings, so ignoring this possible error.
                      * In other case, throwing the exception again.
                      */
                     if (!e.getMessage().contains("80070005")) {
                        throw e;
                     }
                  }
               }
               sessions.get().get(uuid).unlockMachine();
               Logger.debug("VM #" + uuid + " was unlocked");
            } else {
               Logger.debug("VM #" + uuid + " wasn't locked, will clean up");
            }
         } catch (VBoxException e) {
            /*
             * In case of PowerDown or SaveState, the session lock could be removed between the time we check for the session state and the unlock,
             * so ignoring this possible error.
             * In other case, throwing the exception again.
             */
            if (!e.getMessage().toUpperCase().contains("8000FFFF")) {
               throw e;
            }
         }
         sessions.get().remove(uuid);
         userLocking.get().remove(uuid);
      } else {
         Logger.debug("Not unlocking " + uuid + ".");
      }
   }
   
   @Override
   public void unlockAuto(String uuid) {
      unlock(uuid, false, false);
   }
   
   @Override
   public void unlockAuto(String uuid, boolean saveSettings) {
      unlock(uuid, false, saveSettings);
   }
   
   @Override
   public ISession getLock(String uuid) {
      
      return sessions.get().get(uuid);
   }
   
   @Override
   public IMachine getCurrent(String uuid) {
      if (sessions.get().containsKey(uuid)) {
         ISession session = getLock(uuid);
         if (session.getState().equals(SessionState.Unlocking) || session.getState().equals(SessionState.Unlocked)) {
            unlock(uuid, false);
         } else {
            return session.getMachine();
         }
      }
      return ConnectionManager.findMachine(uuid);
   }
   
   @Override
   public ISession lockAuto(String uuid) {
      try {
         return lockAuto(uuid, LockType.Write);
      } catch (HypervisorException e) {
         Logger.debug("Failed to create Write lock: " + e.getLocalizedMessage());
         Logger.debug("Trying Shared lock instead");
         return lockAuto(uuid, LockType.Shared);
      }
   }
   
   @Override
   public ISession lockAuto(String uuid, LockType lockType) {
      return lock(uuid, lockType, false);
   }
   
}
