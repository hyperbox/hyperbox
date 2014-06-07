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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui.utils;

import org.altherian.hboxc.front.gui.workers._WorkerDataReceiver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public abstract class HSwingWorker<T, V, K extends _WorkerDataReceiver> extends SwingWorker<T, V> {
   
   private K recv;
   
   public HSwingWorker(final K recv) {
      this.recv = recv;
      
      addPropertyChangeListener(new PropertyChangeListener() {
         
         @Override
         public void propertyChange(PropertyChangeEvent ev) {
            // FIXME change
            if ("state".equals(ev.getPropertyName()) && (SwingWorker.StateValue.STARTED == ev.getNewValue())) {
               if (SwingUtilities.isEventDispatchThread()) {
                  recv.loadingStarted();
               } else {
                  SwingUtilities.invokeLater(new Runnable() {
                     
                     @Override
                     public void run() {
                        recv.loadingStarted();
                     }
                  });
               }
            }
         }
      });
   }
   
   protected K getReceiver() {
      return recv;
   }
   
}
