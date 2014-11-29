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
import org.altherian.hboxc.Hyperbox;
import org.altherian.hboxc.event.EventManager;
import org.altherian.hboxc.event.updater.UpdaterCheckStarted;
import org.altherian.hboxc.event.updater.UpdaterCheckStopped;
import org.altherian.hboxc.event.updater.UpdaterUpdateAvailableEvent;
import org.altherian.hboxc.exception.UpdaterNoNewUpdateException;
import org.altherian.hboxc.exception.UpdaterRepositoryInvalidFormatException;
import org.altherian.hboxc.exception.UpdaterScheduleException;
import org.altherian.tool.AxBooleans;
import org.altherian.tool.AxStrings;
import org.altherian.tool.logging.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * Use ScheduledExecutorService, Executors & Future
 * http://stackoverflow.com/questions/18365795/best-way-to-create-a-background-thread-in-java
 */
public class Updater implements _Updater {
   
   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
   private boolean isRunning = false;
   private boolean isLastScheduleSuccessful = false;
   private Date scheduleLastDate;
   private _Release update;
   private List<String> errors = new ArrayList<String>();
   
   @Override
   public void start() {
      startSchedule();
   }
   
   @Override
   public void stop() {
      stopSchedule();
   }
   
   private void startSchedule() {
      errors.clear();
      scheduler.scheduleAtFixedRate(new Worker(), 0, Long.parseLong(Configuration.getSetting(CFGKEY_UPDATER_INTERVAL, CFGVAL_UPDATER_INTERVAL)),
            TimeUnit.MINUTES);
   }
   
   private void stopSchedule() {
      scheduler.shutdownNow();
   }
   
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
      if (isEnable) {
         startSchedule();
      } else {
         stopSchedule();
      }
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
      return isLastScheduleSuccessful;
   }
   
   @Override
   public Date getLastScheduleDate() {
      return scheduleLastDate;
   }
   
   @Override
   public List<String> getLastScheduleErrors() {
      return new ArrayList<String>(errors);
   }
   
   @Override
   public boolean hasUpdate() {
      return (update != null) && (update.getRevision().compareToIgnoreCase(Hyperbox.getRevision()) > 0);
   }
   
   @Override
   public _Release getUpdate() throws UpdaterNoNewUpdateException {
      if (!hasUpdate()) {
         throw new UpdaterNoNewUpdateException();
      }
      return update;
   }
   
   @Override
   public _Release checkForUpdate() throws UpdaterNoNewUpdateException {
      new Worker().run();
      return getUpdate();
   }
   
   private class Worker implements Runnable {
      
      @Override
      public void run() {
         if (!isRunning) {
            isRunning = true;
            EventManager.post(new UpdaterCheckStarted());
            String rep_base = Configuration.getSetting(CFGKEY_UPDATER_REPOSITORY_LOCATION, CFGVAL_UPDATER_REPOSITORY_LOCATION);
            String repo_channel = getChannel().toLowerCase();
            String repo_extension = Configuration.getSetting(CFGKEY_UPDATER_REPOSITORY_EXTENSION, CFGVAL_UPDATER_REPOSITORY_EXTENSION);
            String repoUrlRaw = rep_base + repo_channel + repo_extension;
            try {
               URL repoUrl = new URL(repoUrlRaw);
               URLConnection repoUrlConn = repoUrl.openConnection();
               BufferedReader in = new BufferedReader(new InputStreamReader(repoUrlConn.getInputStream()));
               String line = in.readLine();
               if (line == null) {
                  throw new UpdaterRepositoryInvalidFormatException("Update data is empty");
                  
               }
               String releaseRaw[] = line.split(" ");
               if (releaseRaw.length < 4) {
                  throw new UpdaterRepositoryInvalidFormatException("Invalid update data - expected at least 4 values, got " + releaseRaw.length);
               }
               
               String version = releaseRaw[0];
               String revision = releaseRaw[1];
               Date releaseDate;
               URL downloadUrl;
               URL changeLogUrl;
               
               try {
                  releaseDate = new Date(Long.parseLong(releaseRaw[2]) * 1000l);
               } catch (NumberFormatException e) {
                  throw new UpdaterRepositoryInvalidFormatException("Invalid timestamp");
               }
               
               try {
                  downloadUrl = new URL(releaseRaw[3]);
               } catch (MalformedURLException e) {
                  throw new UpdaterRepositoryInvalidFormatException("Invalid download URL");
               }
               
               try {
                  changeLogUrl = new URL(releaseRaw[4]);
               } catch (MalformedURLException e) {
                  throw new UpdaterRepositoryInvalidFormatException("Invalid changelog URL");
               }
               
               update = new Release(getChannel(), version, revision, releaseDate, downloadUrl, changeLogUrl);
               isLastScheduleSuccessful = true;
               Logger.verbose("Advertised version: " + update.getVersion() + "r" + update.getRevision());
               if (hasUpdate()) {
                  Logger.info("New update is available: " + getUpdate().getVersion());
                  EventManager.post(new UpdaterUpdateAvailableEvent());
               } else {
                  Logger.info("No update was found");
               }
            } catch (Throwable t) {
               Logger.exception(t);
               Logger.warning("Updater check to [ " + repoUrlRaw + " ] failed with " + t.getClass().getSimpleName() + ": " + t.getMessage());
               errors.add(t.getMessage());
               isLastScheduleSuccessful = false;
               EventManager.post(new UpdaterCheckStopped());
               setScheduleEnable(false);
            } finally {
               scheduleLastDate = new Date();
               isRunning = false;
               EventManager.post(new UpdaterCheckStopped());
            }
         } else {
            Logger.debug("Updater working is already running, skipping");
         }
      }
      
   }
   
}
