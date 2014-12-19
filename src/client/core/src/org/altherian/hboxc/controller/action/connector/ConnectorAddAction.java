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

package org.altherian.hboxc.controller.action.connector;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm._AnswerReceiver;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxc.comm.input.ConnectorInput;
import org.altherian.hboxc.comm.io.factory.ConnectorIoFactory;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.hboxc.controller.action.AbstractClientControllerSingleAction;
import org.altherian.hboxc.core._Core;
import org.altherian.hboxc.core.connector._Connector;
import org.altherian.hboxc.front._Front;

public class ConnectorAddAction extends AbstractClientControllerSingleAction {
   
   @Override
   public Enum<?> getRegistration() {
      return ClientTasks.ConnectorAdd;
   }
   
   @Override
   public void run(_Core core, _Front view, Request req, _AnswerReceiver recv) throws HyperboxException {
      ConnectorInput conIn = req.get(ConnectorInput.class);
      UserIn usrIn = req.get(UserIn.class);
      _Connector conn = core.addConnector(conIn, usrIn);
      recv.putAnswer(new Answer(req, AnswerType.DATA, ConnectorIoFactory.get(conn)));
   }
   
}
