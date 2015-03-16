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

package org.altherian.hboxc.front.gui.builder;

import net.miginfocom.swing.MigLayout;
import org.altherian.hboxc.front.gui.MainView;
import org.altherian.hboxc.front.gui.utils.JDialogUtils;
import java.awt.Dialog.ModalityType;
import java.awt.Image;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

public class JDialogBuilder {

   private JDialogBuilder() {
      // only static
   }

   /**
    * Build a JDialog with no title name, the default Hyperbox icon and no default button
    *
    * @return JDialog instance
    */
   public static JDialog get() {
      return get(null, IconBuilder.getHyperbox().getImage(), null);
   }

   /**
    * Build a JDialog with no title name, the given icon and no default button
    *
    * @param icon The Image object to use as icon for the JDialog
    * @return JDialog instance
    */
   public static JDialog get(Image icon) {
      return get(null, icon, null);
   }

   /**
    * Build a JDialog with no title name, the default Hyperbox icon and then given default button
    *
    * @param defaultButton The JButton to use as default button
    * @return JDialog instance
    */
   public static JDialog get(JButton defaultButton) {
      return get(null, IconBuilder.getHyperbox().getImage(), defaultButton);
   }

   /**
    * Build a JDialog with the given title name, the default Hyperbox icon and no default button
    *
    * @param title The Title to set as String
    * @return JDialog instance
    */
   public static JDialog get(String title) {
      return get(title, IconBuilder.getHyperbox().getImage(), null);
   }

   /**
    * Build a JDialog with the given title name, the default Hyperbox icon and then given default button
    *
    * @param title The Title to set as String
    * @param defaultButton The JButton to use as default button
    * @return JDialog instance
    */
   public static JDialog get(String title, JButton defaultButton) {
      return get(title, IconBuilder.getHyperbox().getImage(), defaultButton);
   }

   /**
    * Build a JDialog with the given title name, the given icon and no default button
    *
    * @param title The Title to set as String
    * @param icon The Image object to use as icon for the JDialog
    * @return JDialog instance
    */
   public static JDialog get(String title, Image icon) {
      return get(title, icon, null);
   }

   /**
    * Build a JDialog with the given title name, the given icon and the given default button
    *
    * @param title The Title to set as String
    * @param img The Image object to use as icon for the JDialog
    * @param defaultButton The JButton to use as default button
    * @return JDialog instance
    */
   public static JDialog get(String title, Image img, JButton defaultButton) {
      JDialog dialog = new JDialog(MainView.getMainFrame());
      dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.setIconImage(img);
      dialog.setTitle(title);
      dialog.setLayout(new MigLayout());
      dialog.getRootPane().setDefaultButton(defaultButton);
      JDialogUtils.setCloseOnEscapeKey(dialog, true);

      return dialog;
   }

}
