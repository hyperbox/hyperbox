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

package org.altherian.hboxd.session;

import org.altherian.hbox.comm._Client;
import org.altherian.hbox.exception.HyperboxException;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.front._RequestReceiver;
import java.util.List;

public interface _SessionManager extends _RequestReceiver {

   public void start(_Hyperbox hbox) throws HyperboxException;

   public void stop();

   public boolean hasSession(_Client c);

   public List<_Session> list();

   public _Session getSession(_Client c);

   public void closeSession(_Client c);

}
