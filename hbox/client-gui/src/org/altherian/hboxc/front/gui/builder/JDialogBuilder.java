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

package org.altherian.hboxc.front.gui.builder;

import net.miginfocom.swing.MigLayout;

import org.altherian.hboxc.front.gui.MainView;

import java.awt.Dialog.ModalityType;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JDialog;

public class JDialogBuilder {
   
   private JDialogBuilder() {
   }
   
   public static JDialog get() {
      return get(null, IconBuilder.getHyperbox().getImage(), null);
   }
   
   public static JDialog get(Image icon) {
      return get(null, icon, null);
   }
   
   public static JDialog get(JButton defaultButton) {
      return get(null, IconBuilder.getHyperbox().getImage(), defaultButton);
   }
   
   public static JDialog get(String title) {
      return get(title, IconBuilder.getHyperbox().getImage(), null);
   }
   
   public static JDialog get(String title, JButton defaultButton) {
      return get(title, IconBuilder.getHyperbox().getImage(), defaultButton);
   }
   
   public static JDialog get(String title, Image icon) {
      return get(title, icon, null);
   }
   
   public static JDialog get(String title, Image img, JButton defaultButton) {
      JDialog dialog = new JDialog(MainView.getMainFrame());
      dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setIconImage(img);
      dialog.setTitle(title);
      dialog.setLayout(new MigLayout());
      dialog.getRootPane().setDefaultButton(defaultButton);
      
      return dialog;
   }
   
}
