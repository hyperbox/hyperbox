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
 * The different modules states on Hyperbox Server.
 * 
 * @author noteirak
 */
public enum ModuleState {
   
   /**
    * The module is currently being loaded by the Hyperbox Server.
    */
   Loading,
   
   /**
    * The module has been loaded by the Hyperbox Server, but not yet ready for use.<br/>
    */
   Loaded,
   /**
    * The module is ready to be used.
    */
   Ready,
   /**
    * the module is currently being unloaded by the Hyperbox Server.
    */
   Unloading,
   
   /**
    * The module is not loaded in the Hyperbox Server, but can be requested.<br/>
    */
   Unloaded
   
}
