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

package org.altherian.hbox.comm;

import org.altherian.hbox.comm.input.HypervisorInput;
import org.altherian.hbox.comm.input.ServerInput;
import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.hypervisor.HypervisorOutput;

public enum HyperboxTasks {
   
   Hello,
   
   /**
    * Retrieve the list of servers managed by the one we are connected to.
    * <p>
    * Request Object: None<br/>
    * Answer Object: {@link ServerOutput}<br/>
    * Answer Type : Multi<br/>
    * Queueable: No
    * </p>
    */
   ServerList,
   /**
    * Retrieve the information about a server
    * <p>
    * Request Object: {@link ServerInput} for a specific server, none for the current server<br/>
    * Answer Object: {@link ServerOutput}<br/>
    * Answer Type: Single<br/>
    * Queueable: No
    * </p>
    */
   ServerGet,
   ServerShutdown,
   
   /**
    * Connect a server to an hypervisor.
    * <p>
    * Request Objects: {@link ServerInput} for a specific server, none for the current server<br/>
    * {@link HypervisorInput} for the hypervisor to connect to<br/>
    * Answer Object: {@link HypervisorOutput}<br/>
    * Answer Type: Single<br/>
    * Queueable: Yes
    * </p>
    */
   ServerConnectHypervisor,
   
   /**
    * Disconnect the server from its hypervisor.
    * <p>
    * Request Objects: {@link ServerInput} for a specific server, none for the current server<br/>
    * Answer Type: Single<br/>
    * Queueable: Yes
    * </p>
    */
   ServerDisconnectHypervisor,
   
   HypervisorList,
   HypervisorGet,
   /**
    * Retrieve the hypervisor information about a server
    * <p>
    * Request Object: {@link ServerInput} for a specific server, none for the current server<br/>
    * Answer Object: {@link HypervisorOutput}<br/>
    * Answer Type: Single<br/>
    * Queueable: No
    * </p>
    */
   HypervisorGetCurrent,
   HypervisorConfigure,
   HypervisorConnect,
   HypervisorDisconnect,
   
   Login,
   Logout,
   
   /**
    * Not implemented
    */
   StoreList,
   /**
    * Not implemented
    */
   StoreGet,
   /**
    * Not implemented
    */
   StoreCreate,
   /**
    * Not implemented
    */
   StoreModify,
   /**
    * Not implemented
    */
   StoreDelete,
   
   StoreRegister,
   StoreUnregister,
   
   /**
    * Not Implemented
    */
   StoreOpen,
   /**
    * Not Implemented
    */
   StoreClose,
   
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
   
   /**
    * Not implemented
    */
   MachineCreateWithSettings;
   
   public String getId() {
      return toString();
   }
   
}
