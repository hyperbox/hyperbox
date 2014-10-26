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

package org.altherian.hboxc.front.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class ErrorDisplay {
   
   private static boolean init = false;
   private static JDialog errorDialog;
   private static JLabel descLabel;
   private static JTextArea errorStack;
   
   private static void init() {
      errorDialog = new JDialog(MainView.getMainFrame());
      errorDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
      
      descLabel = new JLabel();
      errorStack = new JTextArea(10, 30);
      
      errorDialog.add(descLabel, BorderLayout.NORTH);
      errorDialog.add(errorStack, BorderLayout.SOUTH);
      
      init = true;
   }
   
   public static void display(String description, Throwable t) {
      if (!init) {
         init();
      }
      descLabel.setText(description);
      errorStack.setText(null);
      for (StackTraceElement stEl : t.getStackTrace()) {
         errorStack.append(stEl.toString() + "\n");
      }
      errorDialog.pack();
      errorDialog.setVisible(true);
   }
   
}
