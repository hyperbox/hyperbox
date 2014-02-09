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

package org.altherian.hboxd.hypervisor;


import java.util.List;

/**
 * This interface must be implemented for any new version of Virtualbox that needs to be supported.<br/>
 * This interface is the key link to any virtualbox implementation, Hyperbox using only abstract objects.<br/>
 * If support for a custom version is needed, this is what should be implemented.
 * 
 * @author noteirak
 */
// TODO replace with annotation
public interface _HypervisorLoader {
   
   public Class<? extends _Hypervisor> getHypervisorClass();
   
   /**
    * Return a list of schemes handled by the URI - If a version separation is needed, this must be put into the scheme (e.g. vboxws-4.2.4)
    * 
    * @return a List of Strings, each containing a scheme URI (scheme://...)
    */
   public List<String> getSupportedSchemes();
   
   public String getProduct();
   
   public String getVendor();
   
   public String getTypeId();

}
