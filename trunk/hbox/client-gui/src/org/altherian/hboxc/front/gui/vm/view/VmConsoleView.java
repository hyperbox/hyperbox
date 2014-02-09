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

package org.altherian.hboxc.front.gui.vm.view;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.hypervisor.ScreenshotOutput;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.action.RefreshAction;
import org.altherian.hboxc.front.gui.workers.MachineGetScreenshotWorker;
import org.altherian.hboxc.front.gui.workers._MachineScreenshotReceiver;
import org.altherian.helper.swing.AncestorAdaptor;
import org.altherian.helper.swing.JLabelIconAutoResize;
import org.altherian.tool.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;

public class VmConsoleView implements _Refreshable, _MachineScreenshotReceiver {
   
   private Timer timer;
   private boolean isVisible;
   
   private JPanel panel;
   private JLabelIconAutoResize screenlabel;
   private JLabel statusLabel;
   private MachineOutput mOut;
   
   private boolean isDisplayAvailable() {
      return ((mOut != null) && mOut.getState().equalsIgnoreCase("running"));
   }
   
   public VmConsoleView() {
      screenlabel = new JLabelIconAutoResize();
      statusLabel = new JLabel("No output available");
      statusLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.addAncestorListener(new AncestorAdaptor() {
         @Override
         public void ancestorAdded(AncestorEvent event) {
            isVisible = true;
            if (isDisplayAvailable()) {
               start();
            }
         }
         
         @Override
         public void ancestorRemoved(AncestorEvent event) {
            isVisible = false;
            stop();
         }
      });
      
      panel.add(statusLabel, "center,growx,pushx,wrap");
      panel.add(screenlabel, "dock center,grow");
      timer = new Timer(3000, new RefreshAction(this));
   }
   
   private void start() {
      Logger.track();
      
      if (isVisible) {
         timer.start();
         refresh();
      }
   }
   
   private void stop() {
      Logger.track();
      
      timer.stop();
      screenlabel.setIcon(null);
   }
   
   public void show(MachineOutput mOut) {
      this.mOut = mOut;
      if (isDisplayAvailable()) {
         start();
      }
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   public void clear() {
      mOut = null;
      stop();
      panel.validate();
   }
   
   @Override
   public void refresh() {

      if (isDisplayAvailable()) {
         MachineGetScreenshotWorker.get(this, mOut);
      } else {
         statusLabel.setText("No output available");
         stop();
      }
      panel.validate();
   }
   
   public void loadingEnded() {
      panel.validate();
   }
   
   @Override
   public void loadingStarted() {
      statusLabel.setText("Refreshing...");
   }
   
   @Override
   public void loadingFinished() {
      statusLabel.setText(null);
   }
   
   @Override
   public void loadingFailed(String message) {
      statusLabel.setText("Error when trying to get the Console: " + message);
      stop();
   }
   
   @Override
   public void put(ScreenshotOutput scrOut) {
      screenlabel.setIcon(new ImageIcon(scrOut.getData()));
   }
   
}
