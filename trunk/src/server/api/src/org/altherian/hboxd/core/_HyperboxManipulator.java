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

package org.altherian.hboxd.core;

import org.altherian.hboxd.core.action._ActionManager;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.module._ModuleManager;
import org.altherian.hboxd.persistence._Persistor;
import org.altherian.hboxd.security._SecurityManager;
import org.altherian.hboxd.server._ServerManager;
import org.altherian.hboxd.session._SessionManager;
import org.altherian.hboxd.store._StoreManager;
import org.altherian.hboxd.task._TaskManager;

public interface _HyperboxManipulator {
   
   public _ServerManager getServerManager();
   
   public _TaskManager getTaskManager();
   
   public _SessionManager getSessionManager();
   
   public _SecurityManager getSecurityManager();
   
   public _ActionManager getActionManager();
   
   public _StoreManager getStoreManager();
   
   public _ModuleManager getModuleManager();

   public _Persistor getPersistor();
   
   public _Hypervisor getHypervisor();
   
}
