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

package org.altherian.hboxd.service;

import org.altherian.hbox.exception.ServiceException;
import org.altherian.hbox.states.ServiceState;

/**
 * <p>
 * A service for Hyperbox is a module that run in its own thread(s) and is used to perform continuous and/or lengthy background activities that shouldn't be run on the main core thread.<br/>
 * A service is expected to manage its own life-cycle and events registration(s) if any is required.
 * </p>
 * <p>
 * Hyperbox will :
 * <ul>
 * <li>Request start/stop/pause/unpause/reload actions on the service</li>
 * </ul>
 * The service must :
 * <ul>
 * <li>Have the HyperboxService annotation</li>
 * <li>Perform initialisation and thread(s) creation into start() and return with no further delay</li>
 * <li>Run background code into an internal method</li>
 * <li>Register a default uncaughtExceptionHandler on the thread(s) and ensure none becomes zombie</li>
 * <li>Check if the service thread(s) is/are still running in isRunning()</li>
 * </ul>
 * </p>
 * <p>
 * Skeleton classes exists to ease service implementation : SkeletonService & SimpleLoopService.
 * </p>
 * 
 * @author noteirak
 * @see SkeletonService
 * @see SimpleLoopService
 */
// TODO review some service framework instead of using custom impl
public interface _Service {
   
   /**
    * Returns the service ID. This ID must be unique in a server
    * 
    * @return String of the service ID
    */
   public String getId();
   
   /**
    * Returns the current service state
    * 
    * @return the current service state
    */
   public ServiceState getState();
   
   /**
    * Requests the service to start. _Core expects the service to return directly after the service thread has been started.
    * 
    * @throws ServiceException If anything prevented the service to start
    */
   public void start() throws ServiceException;
   
   public void startAndRun() throws ServiceException;
   
   /**
    * Requests the service to stop. this method should ensure the service thread will stop (by setting a stop variable or sending an interprocess message per example) and then return.<br/>
    * After stop() has been called, _Core will either wait for the service to finish using isRunning() and mark the service as "stopped" or reach wait timeout and mark the service as zombie.
    * 
    * @throws ServiceException If anything prevented the service to stop
    */
   public void stop() throws ServiceException;
   
   /**
    * Requests the service to stop by calling {@link _Service#stop()} then waits for the service thread to stop for the given milliseconds<br/>
    * The wait is perform using @link {@link Thread#join(long)}.
    * 
    * @param timeout Time in milliseconds the call should wait for the service thread to stop
    * @return true if the service stopped within the timeframe given, or false if not
    */
   public boolean stopAndDie(int timeout);
   
   /**
    * Requests the service to pause until further call is made.
    * 
    * @throws UnsupportedOperationException If this operation is not supported by the service
    */
   public void pause() throws UnsupportedOperationException;
   
   /**
    * Requests the service to pause until further call is made.
    * 
    * @throws UnsupportedOperationException If this operation is not supported by the service
    */
   public void unpause() throws UnsupportedOperationException;
   
   /**
    * Requests the service to reload its config
    * 
    * @throws UnsupportedOperationException If this operation is not supported by the service
    */
   public void reload() throws UnsupportedOperationException;
   
   /**
    * Checks if the service is running
    * 
    * @return true if the service is running, false if not
    */
   public boolean isRunning();
   
}
