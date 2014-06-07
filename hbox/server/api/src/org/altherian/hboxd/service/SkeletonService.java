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

import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hbox.exception.ServiceException;
import org.altherian.tool.logging.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * <p>
 * Skeleton implementation of a service. This skeleton will handle the Thread creation as well as its basic management.
 * </p>
 * <p>
 * The thread startup & shutdown condition must be set in starting() and stopping().<br/>
 * Subclasses must handle interrupt on the service thread. This occurs when the service needs to stop.
 * </p>
 * <p>
 * 3 methods can be overwritten if functionality is needed :
 * <ul>
 * <li>pause() - If the service can be paused, any code allowing to do so must be put there.</li>
 * <li>unpause() - If the service can be paused, any code allowing it to unpause must be put there.</li>
 * <li>reload() - If the service configuration can be reloaded during runtime, the code can go here.</li>
 * </ul>
 * </p>
 * 
 * @author noteirak
 * @see SimpleLoopService
 */
public abstract class SkeletonService implements _Service, Runnable {
   
   protected Thread serviceThread;
   
   @Override
   public void start() throws HyperboxRuntimeException {
      Logger.track();
      
      serviceThread = new Thread(this, "Service ID " + getClass().getSimpleName());
      serviceThread.setUncaughtExceptionHandler(new ServiceExceptionHander());
      starting();
      serviceThread.start();
   }
   
   @Override
   public void startAndRun() throws ServiceException {
      start();
      while (!serviceThread.isAlive()) {
         try {
            Thread.sleep(50);
         } catch (InterruptedException e) {
            Logger.exception(e);
         }
      }
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      stopping();
      if ((serviceThread != null) && serviceThread.isAlive()) {
         serviceThread.interrupt();
      }
   }
   
   @Override
   public void stopAndDie(int timeout) throws ServiceException {
      if (isRunning()) {
         stop();
         try {
            serviceThread.join(timeout);
         } catch (InterruptedException e) {
            Logger.exception(e);
         }
      }
   }
   
   @Override
   public final boolean isRunning() {
      Logger.track();
      
      if ((serviceThread != null) && serviceThread.isAlive()) {
         return true;
      }
      
      serviceThread = null;
      return false;
      
   }
   
   /**
    * Override if you want to implement this
    */
   @Override
   public void pause() throws UnsupportedOperationException {
      throw new UnsupportedOperationException("This action is not supported by " + getClass().getSimpleName());
   }
   
   /**
    * Override if you want to implement this
    */
   @Override
   public void unpause() throws UnsupportedOperationException {
      throw new UnsupportedOperationException("This action is not supported by " + getClass().getSimpleName());
   }
   
   /**
    * Override if you want to implement this
    */
   @Override
   public void reload() throws UnsupportedOperationException {
      throw new UnsupportedOperationException("This action is not supported by " + getClass().getSimpleName());
   }
   
   /**
    * Must be used to set the start condition
    */
   protected abstract void starting();
   
   /**
    * Must be used to set the stop condition
    */
   protected abstract void stopping();
   
   private class ServiceExceptionHander implements UncaughtExceptionHandler {
      
      @Override
      public void uncaughtException(Thread arg0, Throwable arg1) {
         Logger.track();
         
         Logger.error("The service \"" + serviceThread.getName() + "\" has crashed.");
         Logger.error("Exception caught is : " + arg1.getClass().getSimpleName() + " - " + arg1.getMessage());
         Logger.error("The service will not be restarted.");
         Logger.exception(arg1);
         serviceThread = null;
      }
   }
   
}
