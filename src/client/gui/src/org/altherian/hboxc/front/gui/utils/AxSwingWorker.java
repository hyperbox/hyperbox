/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxc.front.gui.utils;

import org.altherian.hboxc.front.gui.workers._WorkerDataReceiver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public abstract class AxSwingWorker<K extends _WorkerDataReceiver, T, V> extends SwingWorker<T, V> {
   
   private K recv;
   
   public AxSwingWorker(K recv) {
      setReceiver(recv);
   }
   
   protected K getReceiver() {
      return recv;
   }
   
   protected void setReceiver(final K recv) {
      this.recv = recv;
      
      addPropertyChangeListener(new PropertyChangeListener() {
         
         @Override
         public void propertyChange(PropertyChangeEvent ev) {
            if ("state".equals(ev.getPropertyName()) && (SwingWorker.StateValue.STARTED == ev.getNewValue())) {
               recv.loadingStarted();
            }
         }
      });
   }
   
   @Override
   protected final void done() {
      try {
         innerDone();
         recv.loadingFinished(true, null);
      } catch (Throwable t) {
         recv.loadingFinished(false, t.getMessage());
      }
   }
   
   protected void innerDone() throws InterruptedException, ExecutionException {
      get();
   }
   
}
