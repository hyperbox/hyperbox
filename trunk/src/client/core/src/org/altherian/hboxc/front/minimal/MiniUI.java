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

package org.altherian.hboxc.front.minimal;

import org.altherian.hbox.comm._RequestReceiver;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.core._CoreReader;
import org.altherian.hboxc.front._Front;

import java.awt.GraphicsEnvironment;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * This class provides a minimal UI during load of the client, in case something breaks very early, even before the full UI is initialised.
 * 
 * @author noteirak
 * 
 */
public final class MiniUI implements _Front {
   
   @Override
   public void start() throws HyperboxException {
      // stub
   }
   
   @Override
   public void stop() {
      // stub
   }
   
   @Override
   public void postError(String description, Throwable t) {
      if (GraphicsEnvironment.isHeadless()) {
         System.out.println("Fatal error occured during startup: " + t.getMessage());
      } else {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Throwable t1) {
            // we don't care, we just tried in case of
         }
         JOptionPane.showMessageDialog(null, "Fatal error occured during startup: " + t.getMessage(), "Fatal error", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   @Override
   public void postError(Throwable t) {
      // stub
   }
   
   @Override
   public void postError(String s) {
      // stub
   }
   
   @Override
   public void postError(Throwable t, String s) {
      // stub
   }
   
   @Override
   public void setRequestReceiver(_RequestReceiver reqRec) {
      // stub
   }
   
   @Override
   public void setCoreReader(_CoreReader reader) {
      // stub
   }
   
}
