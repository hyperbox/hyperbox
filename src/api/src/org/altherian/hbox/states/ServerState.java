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

package org.altherian.hbox.states;

/**
 * The different Hyperbox system states.
 * 
 * @author noteirak
 */
public enum ServerState {

   /**
    * The server is under maintenance mode and will not accept any command except for going out of the mode or shutting down.
    */
   Maintenance,

   /**
    * The server is stopped. This status is only visible internally or after the server has been stopped.
    */
   Stopped,
   /**
    * When Hyperbox is loading services, modules, etc.
    */
   Starting,

   /**
    * When Hyperbox is running and ready for commands and tasks.
    */
   Running,

   /**
    * When Hyperbox is shutting down.
    */
   Stopping,

   /**
    * The server cannot be reached.
    */
   Unavailable

}
