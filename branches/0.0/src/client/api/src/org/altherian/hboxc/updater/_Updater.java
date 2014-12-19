/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxc.updater;

import org.altherian.hboxc.exception.UpdaterNoNewUpdateException;
import org.altherian.hboxc.exception.UpdaterScheduleException;

import java.util.Date;
import java.util.List;

/**
 * Updater facility that checks regularly for a given release type for any new one.
 */
public interface _Updater {
   
   public final String CFGKEY_UPDATER_AUTORUN = "updater.autorun";
   public final String CFGVAL_UPDATER_AUTORUN = "1";
   
   public final String CFGKEY_UPDATER_CHANNEL = "updater.channel";
   public final String CFGVAL_UPDATER_CHANNEL = Channel.Fresh.toString();
   
   public final String CFGKEY_UPDATER_ENABLE = "updater.schedule.enable";
   public final String CFGVAL_UPDATER_ENABLE = "1";
   
   public final String CFGKEY_UPDATER_INTERVAL = "updater.schedule.interval";
   public final String CFGVAL_UPDATER_INTERVAL = "60";
   
   public final String CFGKEY_UPDATER_REPOSITORY_LOCATION = "updater.repository.location";
   public final String CFGVAL_UPDATER_REPOSITORY_LOCATION = "http://hyperbox.altherian.org/update/";
   
   public final String CFGKEY_UPDATER_REPOSITORY_EXTENSION = "updater.repository.extension";
   public final String CFGVAL_UPDATER_REPOSITORY_EXTENSION = "";
   
   public void start();
   
   public void stop();

   /**
    * Get the current configured channel for the Schedule check
    * 
    * @return Channel name
    * @see Channel
    */
   public String getChannel();
   
   /**
    * Set the channel for the Schedule check
    * <p>
    * This value can be set to anything but interpretation of this value is implementation-specific.
    * </p>
    * 
    * @param channel The Channel that will be used for the Schedule Check
    * @see Channel
    */
   public void setChannel(String channel);
   
   /**
    * Convenient method to pass an Enum instead of a String
    * <p>
    * This method will use .toString() on the enum then call {@link #setChannel(String)} as follow:<br/>
    * <code>setChannel(channel.toString())</code>
    * </p>
    * 
    * @param channel Enum as channel name
    * @see Channel
    */
   public void setChannel(Enum<?> channel);
   
   /**
    * Check if the updater regular check is enabled or not
    * 
    * @return Status of regular checks
    */
   public boolean isScheduleEnable();
   
   /**
    * Enable or disable the updater regular check
    * 
    * @param isEnable true to enable, false to disable
    */
   public void setScheduleEnable(boolean isEnable);
   
   /**
    * Get the interval between two automatic update check
    * 
    * @return Check interval in minutes
    */
   public long getScheduleInterval();
   
   /**
    * Set the interval between two automatic update check
    * 
    * @param interval Check interval in minutes.
    * @throws IllegalArgumentException If interval is not at least 1
    * @throws UpdaterScheduleException If an error occurred during the scheduling
    */
   public void setScheduleInterval(long interval) throws IllegalArgumentException, UpdaterScheduleException;
   
   /**
    * Check if the last schedule check was successful
    * 
    * @return true if it was, false if not
    */
   public boolean isLastScheduleSuccessful();
   
   /**
    * Get the time when the last scheduled check was made
    * 
    * @return Date object of the last scheduled check, or <code>null</code> if no scheduled check ever ran
    */
   public Date getLastScheduleDate();
   
   /**
    * Get the error(s) of the last schedule check
    * 
    * @return List of error messages, empty if no error occurred
    */
   public List<String> getLastScheduleErrors();
   
   /**
    * Check if an update is available
    * <p>
    * If no check has been made since the updater was started, the updater will check for it by calling {@link #checkForUpdate()}.<br/>
    * In case Scheduled checks are disabled, {@link #checkForUpdate()} must be called to refresh the update status.<br/>
    * {@link #getUpdate()} can then be called to retrieve the update information if any is available.
    * </p>
    * 
    * @return true if an update is available, false if not.
    */
   public boolean hasUpdate();
   
   /**
    * Get the latest new release for the default configured channel
    * <p>
    * This call is to get a newer version than the currently installed.
    * </p>
    * 
    * @return The latest update from the current version in the default configured channel
    * @throws UpdaterNoNewUpdateException If no update for the current version was found
    */
   public _Release getUpdate() throws UpdaterNoNewUpdateException;
   
   /**
    * Check for a new update in the default configured channel
    * <p>
    * This call will block for the duration of the check.
    * </p>
    * 
    * @return true if a new release is available, false if not
    * @throws UpdaterNoNewUpdateException If no update for the current version was found
    */
   public _Release checkForUpdate() throws UpdaterNoNewUpdateException;
   
}
