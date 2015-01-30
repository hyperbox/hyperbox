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

import net.miginfocom.swing.MigLayout;
import org.altherian.hboxc.PreferencesManager;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.tasks.TaskListView;
import org.altherian.tool.Int;
import org.altherian.tool.logging.Logger;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public final class MainView {

   public static final String FRAME_NAME = "Hyperbox Client";
   public static final String CFGKEY_VSPLIT_POSITION = "gui.main.vsplit.pos";
   public static final String CFGVAL_VSPLIT_POSITION = "581";

   private static MainView instance;
   private JFrame mainFrame;
   private MainMenu mainMenu;

   private ServerMachineView vmListView;
   private JSplitPane vSplit;
   private JPanel notificationPanel;

   public static JFrame getMainFrame() {
      return instance.mainFrame;
   }

   {
      instance = this;
   }

   public MainView() {
      Logger.track();

      vmListView = new ServerMachineView();
      mainMenu = new MainMenu();

      mainFrame = new JFrame(FRAME_NAME);
      mainFrame.setIconImage(IconBuilder.getHyperbox().getImage());
      mainFrame.setJMenuBar(mainMenu.getComponent());
      mainFrame.addWindowListener(new WindowLManager());

      notificationPanel = new NotificationPanel();

      TaskListView taskList = new TaskListView();

      JPanel listView = new JPanel(new MigLayout("ins 0"));
      listView.add(vmListView.getComponent(), "grow, push, wrap");
      vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listView, taskList.getComponent());
      vSplit.setResizeWeight(1);
      vSplit.setDividerLocation(Integer.parseInt(PreferencesManager.get().getProperty(Config.MAIN_VIEW_VSPLIT_POS, CFGVAL_VSPLIT_POSITION)));
      vSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {

         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            PreferencesManager.get().setProperty(Config.MAIN_VIEW_VSPLIT_POS, evt.getNewValue().toString());
         }
      });

      mainFrame.add(notificationPanel, BorderLayout.NORTH);
      mainFrame.add(vSplit);
      ViewEventManager.register(this);
   }

   public void show() {
      if (!SwingUtilities.isEventDispatchThread()) {
         Logger.warning("Not EDT");
      }
      int width = Integer.parseInt(PreferencesManager.get().getProperty(Config.MainFrameWidth, "990"));
      int height = Integer.parseInt(PreferencesManager.get().getProperty(Config.MainFrameHeight, "772"));
      mainFrame.setSize(width, height);

      // FIXME make sure the position still exists, in case we go from two screens to one screen.
      if (PreferencesManager.get().containsKey(Config.MainFramePosX)
            && PreferencesManager.get().containsKey(Config.MainFramePosY)) {
         int x = Int.get(PreferencesManager.get().getProperty(Config.MainFramePosX));
         int y = Int.get(PreferencesManager.get().getProperty(Config.MainFramePosY));
         mainFrame.setLocation(x, y);
      } else {
         mainFrame.setLocationRelativeTo(null);
      }

      mainFrame.setExtendedState(Integer.parseInt(PreferencesManager.get().getProperty(Config.MainFrameState, Int.get(JFrame.NORMAL))));
      mainFrame.setVisible(true);
   }

   public void hide() {
      mainFrame.setVisible(false);
   }

   private class WindowLManager extends WindowAdapter {

      @Override
      public void windowClosing(WindowEvent e) {
         Logger.track();

         mainFrame.setExtendedState(JFrame.NORMAL);
         PreferencesManager.get().setProperty(Config.MainFrameWidth, Int.get(mainFrame.getWidth()));
         PreferencesManager.get().setProperty(Config.MainFrameHeight, Int.get(mainFrame.getHeight()));
         PreferencesManager.get().setProperty(Config.MainFramePosX, Int.get(mainFrame.getLocationOnScreen().x));
         PreferencesManager.get().setProperty(Config.MainFramePosY, Int.get(mainFrame.getLocationOnScreen().y));
         PreferencesManager.get().setProperty(Config.MainFrameState, Int.get(mainFrame.getExtendedState()));

         Gui.exit();
      }
   }

}
