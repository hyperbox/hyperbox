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

package org.altherian.hboxc.front.gui.workers;

import org.altherian.hboxc.HyperboxClient;
import org.altherian.hboxc.controller.MessageInput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.tool.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

public class GenericSwingWorker extends SwingWorker<Object, Void> {
   
   private MessageInput finalMessageInput;
   private List<MessageInput> listInput;
   
   public GenericSwingWorker(MessageInput finalMessageInput) {
      this(new ArrayList<MessageInput>(), finalMessageInput);
   }
   
   public GenericSwingWorker(List<MessageInput> listInput, MessageInput finalMessageInput) {
      Logger.track();
      
      this.listInput = listInput;
      this.finalMessageInput = finalMessageInput;
      execute();
   }
   
   @Override
   protected Object doInBackground() {
      Logger.track();
      
      if (!listInput.isEmpty()) {
         for (MessageInput mIn : listInput) {
            Gui.post(mIn);
         }
      }
      
      Gui.post(finalMessageInput);
      
      return null;
   }
   
   @Override
   protected void done() {
      Logger.track();
      
      try {
         get();
      } catch (Throwable e) {
         HyperboxClient.getView().postError(e);
      }
   }
   
}
