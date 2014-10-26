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

package org.altherian.helper.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 * - Allow to be closed on escape key
 * 
 * @author noteirak
 * 
 */
@SuppressWarnings("serial")
public class JDialogImp extends JDialog {
   
   private KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
   
   public void setCloseOnEscapeKey(boolean close) {
      if (close) {
         getRootPane().registerKeyboardAction(new EscapeActionListener(), escapeKeyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
      } else {
         getRootPane().getInputMap().remove(escapeKeyStroke);
      }
   }
   
   private class EscapeActionListener implements ActionListener {
      
      @Override
      public void actionPerformed(ActionEvent ae) {
         setVisible(false);
      }
      
   }
   
}
