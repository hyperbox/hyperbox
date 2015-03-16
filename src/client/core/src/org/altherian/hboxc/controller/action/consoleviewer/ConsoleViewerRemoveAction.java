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

package org.altherian.hboxc.controller.action.consoleviewer;

import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._AnswerReceiver;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.comm.input.ConsoleViewerInput;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.controller.action.AbstractClientControllerSingleAction;
import org.altherian.hboxc.core._Core;
import org.altherian.hboxc.front._Front;

public class ConsoleViewerRemoveAction extends AbstractClientControllerSingleAction {

   @Override
   public Enum<?> getRegistration() {
      return ClientTasks.ConsoleViewerRemove;
   }

   @Override
   public void run(_Core core, _Front view, Request req, _AnswerReceiver recv) throws HyperboxException {
      ConsoleViewerInput coreViewInput = req.get(ConsoleViewerInput.class);
      core.removeConsoleViewer(coreViewInput.getId());
   }

}
