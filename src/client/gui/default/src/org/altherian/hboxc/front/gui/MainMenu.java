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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.front.gui;

import org.altherian.hboxc.front.gui.action.AboutAction;
import org.altherian.hboxc.front.gui.action.ClientExitAction;
import org.altherian.hboxc.front.gui.action.connector.ConnectorAddAction;
import org.altherian.hboxc.front.gui.action.settings.SettingsShowAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public final class MainMenu {

   private JMenuBar menuBar;

   private JMenu fileMenu;
   private JMenuItem exitMenuItem;

   private JMenu editMenu;
   private JMenuItem settingsMenuItem;

   private JMenu srvMenu;
   private JMenuItem srvConnectMenuItem;

   private JMenu helpMenu;
   private JMenuItem aboutItem;

   public MainMenu() {
      exitMenuItem = new JMenuItem(new ClientExitAction());
      fileMenu = new JMenu("File");
      fileMenu.add(exitMenuItem);

      settingsMenuItem = new JMenuItem(new SettingsShowAction());
      editMenu = new JMenu("Edit");
      editMenu.add(settingsMenuItem);

      srvConnectMenuItem = new JMenuItem(new ConnectorAddAction());
      srvMenu = new JMenu("Server");
      srvMenu.add(srvConnectMenuItem);

      aboutItem = new JMenuItem(new AboutAction());
      helpMenu = new JMenu("Help");
      helpMenu.add(aboutItem);

      menuBar = new JMenuBar();
      menuBar.add(fileMenu);
      menuBar.add(editMenu);
      menuBar.add(srvMenu);
      menuBar.add(helpMenu);
   }

   public JMenuBar getComponent() {
      return menuBar;
   }

}
