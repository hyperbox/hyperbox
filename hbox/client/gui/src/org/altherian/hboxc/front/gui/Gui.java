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

import net.engio.mbassy.listener.Handler;

import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._RequestReceiver;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.controller.MessageInput;
import org.altherian.hboxc.core._CoreReader;
import org.altherian.hboxc.event.CoreStateEvent;
import org.altherian.hboxc.event.FrontEventManager;
import org.altherian.hboxc.front._Front;
import org.altherian.hboxc.server._ServerReader;
import org.altherian.hboxc.state.CoreState;
import org.altherian.tool.logging.Logger;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public final class Gui implements _Front {
   
   private static _RequestReceiver reqRecv;
   private static _CoreReader reader;
   
   private MainView mainView;
   
   @Override
   public void start() throws HyperboxException {
      Logger.track();
      
      FrontEventManager.register(this);
      
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
         Logger.error("Couldn't switch to the System Look & Feel");
      }
      EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      queue.push(new EventQueueProxy());
      mainView = new MainView();
   }
   
   @Override
   public void stop() {
      Logger.track();
      
      if (mainView != null) {
         mainView.hide();
      }
   }
   
   @Override
   public void postError(Throwable t) {
      Logger.track();
      
      if (t.getCause() != null) {
         postError(t.getCause().getMessage());
      } else {
         postError(t.getMessage());
      }
      
   }
   
   @Override
   public void postError(String s) {
      Logger.track();
      
      JOptionPane.showMessageDialog(null,
            s,
            "Error",
            JOptionPane.ERROR_MESSAGE);
   }
   
   @Override
   public void postError(Throwable t, String s) {
      Logger.track();
      
      postError(s);
   }
   
   @Override
   public void postError(String s, Throwable t) {
      postError(s + ": " + t.getMessage());
   }
   
   @Handler
   public void getCoreState(CoreStateEvent event) {
      Logger.track();
      
      CoreState state = event.get(CoreState.class);
      Logger.debug("Got CoreState event : " + state);
      
      if (state == CoreState.Started) {
         mainView.show();
      }
      
      if (state == CoreState.Stopped) {
         stop();
      }
   }
   
   private class EventQueueProxy extends EventQueue {
      
      @Override
      protected void dispatchEvent(AWTEvent newEvent) {
         try {
            super.dispatchEvent(newEvent);
         } catch (Throwable t) {
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            StringWriter writer = new StringWriter();
            t.printStackTrace(new PrintWriter(writer));
            textArea.setText(writer.toString());
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(750, 300));
            
            JOptionPane.showMessageDialog(null, scrollPane, "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
         }
      }
   }
   
   @Override
   public void setRequestReceiver(_RequestReceiver reqRecv) {
      Gui.reqRecv = reqRecv;
   }
   
   @Override
   public void setCoreReader(_CoreReader reader) {
      Gui.reader = reader;
   }
   
   public static void post(MessageInput msgIn) {
      getReqRecv().putRequest(msgIn.getRequest());
   }
   
   public static void post(Request req) {
      getReqRecv().putRequest(req);
   }
   
   public static _RequestReceiver getReqRecv() {
      return reqRecv;
   }
   
   public static _CoreReader getReader() {
      return reader;
   }
   
   public static _ServerReader getServer(String id) {
      return getReader().getServerReader(id);
   }
   
   public static ServerOutput getServerInfo(String id) {
      return reader.getServerInfo(id);
   }
   
   public static _ServerReader getServer(ServerOutput srvOut) {
      return getServer(srvOut.getId());
   }
   
   public static void exit() {
      post(new MessageInput(new Request(Command.CUSTOM, ClientTasks.Exit)));
   }
   
}
