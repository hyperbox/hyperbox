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

package org.altherian.hboxc.front.gui;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.HyperboxAPI;
import org.altherian.hboxc.Hyperbox;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.helper.swing.JDialogImp;

import java.awt.Dialog.ModalityType;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class AboutDialog {
   
   private JDialogImp dialog;
   
   private JLabel logo;
   private JTextArea text;
   
   private AboutDialog() {
      dialog = new JDialogImp();
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setModalityType(ModalityType.APPLICATION_MODAL);
      dialog.setCloseOnEscapeKey(true);
      dialog.setTitle("About Hyperbox");
      dialog.setIconImage(IconBuilder.getHyperbox().getImage());
      
      logo = new JLabel();
      logo.setIcon(IconBuilder.getLogo());
      
      text = new JTextArea();
      text.setBorder(BorderFactory.createEmptyBorder());
      text.setBackground(dialog.getContentPane().getBackground());
      text.setOpaque(false);
      text.setEditable(false);
      text.setFont(UIManager.getFont("Label.font"));
      
      text.append("Hyperbox\n");
      text.append("Copyright 2013 Maxime Dor & Katalin Dor. All rights reserved.\n");
      text.append("This product is released under the GPL v3.\n");
      text.append("\n\n");
      text.append("API Version " + HyperboxAPI.getVersion() + " (r " + HyperboxAPI.getRevision() + ")\n");
      text.append("Client Version " + Hyperbox.getVersion() + " (r " + Hyperbox.getRevision() + ")\n");
      text.append("Network Protocol Version " + HyperboxAPI.getProtocolVersion() + "\n");
      text.append("\n\n");
      text.append("Hyperbox is an Enterprise Virtualization Manager.\n");
      text.append("For more information, please visit http://hyperbox.altherian.org\n");
      text.append("\n\n");
      text.append("This software is made possible thanks to several open-source projects.\n");
      text.append("Refer to the User Manual for detailed names and licensing information.");
      text.append("\n\n");
      text.append("Special thanks for their undying support and contribution to this project:\n");
      text.append("Perryg, klaus-vb");
      
      JPanel textPanel = new JPanel(new MigLayout("ins 50"));
      textPanel.setBackground(dialog.getContentPane().getBackground());
      textPanel.add(text);
      
      JPanel logoPanel = new JPanel(new MigLayout("ins 10 50 0 50"));
      logoPanel.setBackground(dialog.getContentPane().getBackground());
      logoPanel.add(logo);
      
      dialog.getContentPane().setLayout(new MigLayout("ins 0"));
      dialog.getContentPane().add(logoPanel, "growx, pushx, wrap");
      dialog.getContentPane().add(textPanel, "growx, pushx");
   }
   
   private void display() {
      dialog.pack();
      dialog.setLocationRelativeTo(MainView.getMainFrame());
      dialog.setVisible(true);
   }
   
   public static void show() {
      new AboutDialog().display();
   }
   
}
