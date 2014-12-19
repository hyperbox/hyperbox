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

package org.altherian.vbox4_3.manager;

import org.altherian.hboxd.exception.machine.MachineLockingException;

import org.virtualbox_4_3.IMachine;
import org.virtualbox_4_3.ISession;
import org.virtualbox_4_3.LockType;

public interface _RawSessionManager {
   
   /**
    * Get the current lock on the VM, or return null if no lock exists
    * 
    * @param uuid UUID of the VM
    * @return a ISession object with the current VM lock, or null if no lock exists
    */
   public ISession getLock(String uuid);
   
   /**
    * Get a lock of a given type for a VM, assuming that this lock is coming from a user command.<br/>
    * If a lock of at least equivalent level already exist, it will be returned.
    * 
    * @param uuid UUID of the VM to lock
    * @param lockType The type of lock needed
    * @return a ISession object with the requested lock
    * @throws MachineLockingException If an error occurred when trying to lock the machine
    */
   public ISession lock(String uuid, LockType lockType) throws MachineLockingException;
   
   /**
    * Get a lock of a given type for a VM, assuming that this lock is NOT coming from a user command.<br/>
    * If a lock of at least equivalent level already exist, it will be returned.
    * 
    * @param uuid UUID of the VM to lock
    * @param lockType The type of lock needed
    * @return a ISession object with the requested lock
    * @throws MachineLockingException If an error occurred when trying to lock the machine
    */
   public ISession lockAuto(String uuid, LockType lockType) throws MachineLockingException;
   
   /**
    * Get the most powerful lock possible for a VM, assuming that this lock is NOT coming from a user command.<br/>
    * This call will try every possible lock, from most powerful to least powerful until one succeed, and that one will be returned.<br/>
    * If a lock of already exist, it will be returned.
    * 
    * @param uuid UUID of the VM to lock
    * @return a ISession object with the requested lock
    * @throws MachineLockingException If an error occurred when trying to lock the machine
    */
   public ISession lockAuto(String uuid) throws MachineLockingException;
   
   /**
    * Save the settings and release a lock on the given VM, assuming that this lock release is coming from a user command.<br/>
    * If no lock exists, this will do nothing.
    * 
    * @param uuid UUID of the VM to unlock
    */
   public void unlock(String uuid);
   
   public void unlock(String uuid, boolean saveSettings);
   
   /**
    * Release a lock on the given VM, assuming that this lock release is *NOT* coming from a user command.<br/>
    * If no lock exists, or that the current lock has been from a user command, this will do nothing.<br/>
    * If VM settings have been changed, they will not be saved unless a manual call is performed on the VM.
    * 
    * @param uuid UUID of the VM to unlock
    */
   public void unlockAuto(String uuid);
   
   /**
    * Release a lock on the given VM, assuming that this lock release is *NOT* coming from a user command.<br/>
    * If no lock exists, or that the current lock has been from a user command, this will do nothing.<br/>
    * Depending on the parameter, machine settings will or will not be saved before releasing the lock.
    * 
    * @param uuid UUID of the VM to unlock
    * @param saveSettings if the settings should be saved or not
    */
   public void unlockAuto(String uuid, boolean saveSettings);
   
   /**
    * The "most" representative machine object currently in the system will be return by this method.
    * If a lock already exists for the given VM, the machine object coming for the session object will be returned.
    * If no lock exists, the read-only generic object will be returned.
    * 
    * @param uuid UUID of the VM to fetch
    * @return the "most" representative machine object known to this session manager
    */
   public IMachine getCurrent(String uuid);
   
}
