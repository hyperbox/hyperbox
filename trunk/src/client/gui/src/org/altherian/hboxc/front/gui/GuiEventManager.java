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

package org.altherian.hboxc.front.gui;

import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.event.DefaultEventManager;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class GuiEventManager extends DefaultEventManager {
   
   public GuiEventManager(String string) {
      super(string);
      
   }
   
   @Override
   public void start() throws HyperboxException {
      super.start();
      eventBus.addErrorHandler(new ErrorDisplay());
   }
   
   @Override
   protected void publish(final Object event) throws InterruptedException, InvocationTargetException {
      SwingUtilities.invokeLater(new Runnable() {
         
         @Override
         public void run() {
            GuiEventManager.this.send(event);
         }
         
      });
   }
   
}
