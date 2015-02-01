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

import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.RefreshAction;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class RefreshUtil {
   
   protected RefreshUtil() {
      // static only
   }
   
   private static KeyStroke refreshKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
   
   public static void set(JComponent comp, _Refreshable r) {
      comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(refreshKeyStroke, "refresh");
      comp.getActionMap().put("refresh", new RefreshAction(r));
   }
   
   public static void unset(JComponent comp) {
      comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(refreshKeyStroke);
      comp.getActionMap().remove("refresh");
   }
   
}
