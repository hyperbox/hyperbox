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

package org.altherian.helper.swing;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class JTextFieldUtils {

   public static JTextField createNonEditable() {
      JTextField field = new JTextField();
      field.setEditable(false);
      return field;
   }

   public static JTextField createAsLabel(String initialText) {
      JTextField field = new JTextField(initialText);
      field.setEditable(false);
      field.setBorder(BorderFactory.createEmptyBorder());
      return field;
   }

   public static boolean hasValue(JTextField... fields) {
      for (JTextField field : fields) {
         if (field.isEnabled() && !field.getText().isEmpty()) {
            return true;
         }
      }
      return false;
   }

}
