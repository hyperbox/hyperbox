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

import net.engio.mbassy.IPublicationErrorHandler;
import net.engio.mbassy.PublicationError;

import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ErrorDisplay implements IPublicationErrorHandler {
   
   public static void display(String description, Throwable t) {
      JTextArea textArea = new JTextArea();
      textArea.setEditable(false);
      StringWriter writer = new StringWriter();
      t.printStackTrace(new PrintWriter(writer));
      textArea.setText(writer.toString());
      
      JScrollPane scrollPane = new JScrollPane(textArea);
      scrollPane.setPreferredSize(new Dimension(750, 300));
      
      JOptionPane.showMessageDialog(null, scrollPane, "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
   }
   
   @Override
   public void handleError(PublicationError error) {
      display(error.getMessage(), error.getCause());
   }
   
}
