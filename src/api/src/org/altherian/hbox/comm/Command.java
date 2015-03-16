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

/**
 * Represent the different kind of commands that can be sent via a Request message.<br/>
 * Commands are actions that are to be executed directly on the server, opposed to Tasks who are queued & executed in a FIFO order.
 * 
 * @author noteirak
 */
public enum Command {

   /**
    * Perform the given Security task (authenticate, authorise, etc)
    */
   SECURITY,
   /**
    * Perform the given Virtualbox task
    */
   VBOX,
   /**
    * Perform the given Hyperbox task
    */
   HBOX,
   /**
    * Perform the given custom task originating from modules
    */
   CUSTOM,
   /**
    * Reserved
    */
   MODULE,
   /**
    * Reserved
    */
   SERVICE;

   public String getId() {
      return toString();
   }

}
