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

import org.altherian.hbox.Configuration;
import org.altherian.hboxc.exception.UpdaterNoNewUpdateException;
import org.altherian.hboxc.exception.UpdaterNotEnabledException;
import org.altherian.hboxc.exception.UpdaterScheduleException;
import org.altherian.tool.AxBooleans;
import org.altherian.tool.AxStrings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Use ScheduledExecutorService, Executors & Future
 * http://stackoverflow.com/questions/18365795/best-way-to-create-a-background-thread-in-java
 */
public class Updater implements _Updater {
   
   @Override
   public String getChannel() {
      return Configuration.getSetting(CFGKEY_UPDATER_CHANNEL, CFGVAL_UPDATER_CHANNEL);
   }
   
   @Override
   public void setChannel(String channel) {
      if (AxStrings.isEmpty(channel)) {
         setChannel(CFGVAL_UPDATER_CHANNEL);
      }
      
      Configuration.setSetting(CFGKEY_UPDATER_CHANNEL, channel);
   }
   
   @Override
   public void setChannel(Enum<?> channel) {
      setChannel(channel.toString().toLowerCase());
   }
   
   @Override
   public boolean isScheduleEnable() {
      return AxBooleans.get(Configuration.getSetting(CFGKEY_UPDATER_ENABLE, CFGVAL_UPDATER_ENABLE));
   }
   
   @Override
   public void setScheduleEnable(boolean isEnable) {
      Configuration.setSetting(CFGKEY_UPDATER_ENABLE, isEnable);
   }
   
   @Override
   public long getScheduleInterval() {
      return Long.parseLong(Configuration.getSetting(CFGKEY_UPDATER_INTERVAL, CFGVAL_UPDATER_INTERVAL));
   }
   
   @Override
   public void setScheduleInterval(long interval) throws IllegalArgumentException, UpdaterScheduleException {
      if (interval <= 1) {
         throw new IllegalArgumentException("Interval must be equal or greater to 1");
      }
      
      Configuration.setSetting(CFGKEY_UPDATER_INTERVAL, interval);
   }
   
   @Override
   public boolean isLastScheduleSuccessful() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public Date getLastScheduleDate() {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public Date getNextScheduleDate() throws UpdaterNotEnabledException {
      // TODO Auto-generated method stub
      throw new UpdaterNotEnabledException();
   }
   
   @Override
   public List<String> getLastScheduleErrors() {
      // TODO Auto-generated method stub
      return new ArrayList<String>();
   }
   
   @Override
   public void scheduleNext(long timestamp) throws UpdaterScheduleException {
      // TODO Auto-generated method stub
   }
   
   @Override
   public boolean hasUpdate() {
      // TODO Auto-generated method stub
      return false;
   }
   
   @Override
   public _Release getUpdate() throws UpdaterNoNewUpdateException {
      // TODO Auto-generated method stub
      throw new UpdaterNoNewUpdateException();
   }
   
   @Override
   public _Release checkForUpdate() throws UpdaterNoNewUpdateException {
      // TODO Auto-generated method stub
      throw new UpdaterNoNewUpdateException();
   }
   
}
