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

package org.altherian.hbox.comm;

import org.altherian.hbox.comm.in.HypervisorIn;
import org.altherian.hbox.comm.in.ServerIn;
import org.altherian.hbox.comm.out.ServerOut;
import org.altherian.hbox.comm.out.hypervisor.HypervisorOut;

public enum HyperboxTasks {
   
   /**
    * Retrieve the list of servers managed by the one we are connected to.
    * <p>
    * Request Object: None<br/>
    * Answer Object: {@link ServerOut}<br/>
    * Answer Type : Multi<br/>
    * Queueable: No
    * </p>
    */
   ServerList,
   /**
    * Retrieve the information about a server
    * <p>
    * Request Object: {@link ServerIn} for a specific server, none for the current server<br/>
    * Answer Object: {@link ServerOut}<br/>
    * Answer Type: Single<br/>
    * Queueable: No
    * </p>
    */
   ServerGet,
   ServerConfigure,
   ServerShutdown,
   ServerLogLevelList,
   
   /**
    * Connect a server to an hypervisor.
    * <p>
    * Request Objects: {@link ServerIn} for a specific server, none for the current server<br/>
    * {@link HypervisorIn} for the hypervisor to connect to<br/>
    * Answer Object: {@link HypervisorOut}<br/>
    * Answer Type: Single<br/>
    * Queueable: Yes
    * </p>
    */
   ServerConnectHypervisor,
   
   /**
    * Disconnect the server from its hypervisor.
    * <p>
    * Request Objects: {@link ServerIn} for a specific server, none for the current server<br/>
    * Answer Type: Single<br/>
    * Queueable: Yes
    * </p>
    */
   ServerDisconnectHypervisor,
   
   HypervisorList,
   HypervisorGet,
   HypervisorAdd,
   HypervisorRemove,
   HypervisorConfigure,
   HypervisorConnect,
   HypervisorDisconnect,
   
   HostGet,
   
   Login,
   Logout,
   
   StoreList,
   StoreGet,
   StoreCreate,
   StoreModify,
   StoreDelete,
   
   StoreRegister,
   StoreUnregister,
   
   /**
    * Not implemented
    */
   StoreItemList,
   /**
    * Not implemented
    */
   StoreItemGet,
   /**
    * Not implemented
    */
   StoreItemCreate,
   /**
    * Not implemented
    */
   StoreItemModify,
   /**
    * Not implemented
    */
   StoreItemDelete,
   /**
    * Not implemented
    */
   StoreItemDownload,
   /**
    * Not implemented
    */
   StoreItemUpload,
   
   SessionList,
   SessionGet,
   SessionClose,
   
   TaskList,
   TaskGet,
   TaskCancel,
   TaskPause,
   TaskStart,
   
   UserList,
   UserGet,
   UserCreate,
   UserModify,
   UserDelete,
   
   GuestRestart,
   GuestShutdown,
   
   PermissionSet,
   PermissionDelete,
   PermissionList,
   PermissionGet,
   
   ModuleRefresh,
   ModuleList,
   ModuleGet,
   ModuleRegister,
   ModuleUnregister,
   ModuleEnable,
   ModuleDisable,
   ModuleLoad,
   ModuleUnload,
   
   Hello;
   
   public String getId() {
      return toString();
   }
   
}
