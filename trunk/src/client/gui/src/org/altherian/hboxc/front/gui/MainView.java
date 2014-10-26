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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.front.gui;

import net.miginfocom.swing.MigLayout;

import org.altherian.hboxc.PreferencesManager;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.tasks.TaskListView;
import org.altherian.tool.logging.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public final class MainView {
   
   private static MainView instance;
   private final String FRAME_NAME = "Hyperbox Client";
   private JFrame mainFrame;
   private MainMenu mainMenu;
   
   private ServerMachineView vmListView;
   private JSplitPane vSplit;
   
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
      
      
      
      TaskListView taskList = new TaskListView();
      
      JPanel listView = new JPanel(new MigLayout("ins 0"));
      listView.add(vmListView.getComponent(), "grow,push,wrap");
      vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listView, taskList.getComponent());
      vSplit.setResizeWeight(1);
      vSplit.setDividerLocation(Integer.parseInt(PreferencesManager.get(MainView.class.getName()).getProperty(
            JSplitPane.DIVIDER_LOCATION_PROPERTY, "581")));
      vSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
         
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            PreferencesManager.get(MainView.class.getName()).setProperty(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt.getNewValue().toString());
         }
      });
      
      mainFrame.add(vSplit);
      FrontEventManager.register(this);
   }
   
   public void show() {
      Logger.track();
      
      int width = Integer.parseInt(PreferencesManager.get().getProperty(Config.MainFrameWidth.getId(), "990"));
      int height = Integer.parseInt(PreferencesManager.get(MainView.class.getName()).getProperty(Config.MainFrameHeight.getId(), "772"));
      mainFrame.setSize(width, height);
      
      // FIXME make sure the position still exists, in case we go from two screens to one screen.
      if (PreferencesManager.get().containsKey(Config.MainFramePosX.getId())
            && PreferencesManager.get().containsKey(Config.MainFramePosY.getId())) {
         int x = Integer.parseInt(PreferencesManager.get().getProperty(Config.MainFramePosX.getId()));
         int y = Integer.parseInt(PreferencesManager.get().getProperty(Config.MainFramePosY.getId()));
         mainFrame.setLocation(x, y);
      } else {
         mainFrame.setLocationRelativeTo(null);
      }
      
      mainFrame.setExtendedState(Integer.parseInt(PreferencesManager.get().getProperty(Config.MainFrameState.getId(),
            Integer.toString(JFrame.NORMAL))));
      mainFrame.setVisible(true);
   }
   
   public void hide() {
      Logger.track();
      
      mainFrame.setVisible(false);
   }
   
   private class WindowLManager extends WindowAdapter {
      
      @Override
      public void windowClosing(WindowEvent e) {
         Logger.track();
         
         mainFrame.setExtendedState(JFrame.NORMAL);
         PreferencesManager.get(MainView.class.getName()).setProperty(Config.MainFrameWidth.getId(), Integer.toString(mainFrame.getWidth()));
         PreferencesManager.get(MainView.class.getName()).setProperty(Config.MainFrameHeight.getId(), Integer.toString(mainFrame.getHeight()));
         PreferencesManager.get(MainView.class.getName()).setProperty(Config.MainFramePosX.getId(),
               Integer.toString(mainFrame.getLocationOnScreen().x));
         PreferencesManager.get(MainView.class.getName()).setProperty(Config.MainFramePosY.getId(),
               Integer.toString(mainFrame.getLocationOnScreen().y));
         PreferencesManager.get(MainView.class.getName()).setProperty(Config.MainFrameState.getId(), Integer.toString(mainFrame.getExtendedState()));
         
         Gui.exit();
      }
   }
   
}
