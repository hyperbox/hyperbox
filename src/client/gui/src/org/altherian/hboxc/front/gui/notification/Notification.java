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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.front.gui.notification;

import net.miginfocom.swing.MigLayout;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class Notification extends JPanel {
   
   private JLabel textLabel = new JLabel();
   private JLabel closeIcon = new JLabel(IconBuilder.getTask(ClientTasks.NotificationClose));
   
   protected JLabel getLabel() {
      return textLabel;
   }
   
   protected void setText(String text) {
      textLabel.setText(text);
   }
   
   public Notification() {
      super(new MigLayout("ins 0"));
      
      Border insets = BorderFactory.createEmptyBorder(5, 5, 5, 5);
      Border line = BorderFactory.createLineBorder(new Color(0x00529B), 1);
      Border border = BorderFactory.createCompoundBorder(line, insets);
      setBorder(border);
      
      setOpaque(true);
      setBackground(new Color(0xBDE5F8));
      
      add(textLabel, "growx, pushx");
      add(closeIcon);
      
      closeIcon.addMouseListener(new MouseListener());
   }
   
   private class MouseListener extends MouseAdapter {
      
      @Override
      public void mouseClicked(MouseEvent ev) {
         if ((ev.getButton() == MouseEvent.BUTTON1) && (ev.getClickCount() == 1)) {
            setVisible(false);
         }
      }
      
   }
   
}
