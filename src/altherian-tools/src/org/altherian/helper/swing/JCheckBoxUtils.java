/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.helper.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

public class JCheckBoxUtils {

   public static void link(final JCheckBox box, final JComponent... comps) {
      update(box, comps);
      box.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            update(box, comps);
         }

      });
   }

   private static void update(JCheckBox box, JComponent... comps) {
      for (JComponent comp : comps) {
         comp.setEnabled(box.isSelected());
      }
   }

}
