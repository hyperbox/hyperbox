/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.hbox.hypervisor.net;

import org.altherian.hbox.comm.io._ObjectIO;

public interface _NATRule extends _ObjectIO {

   /**
    * @return the enabled
    */
   public boolean isEnabled();

   public String getName();
   
   /**
    * @param name the name to set
    */
   public void setName(String name);
   
   /**
    * @return the comment
    */
   public String getComment();
   
   /**
    * @param comment the comment to set
    */
   public void setComment(String comment);

   public String getProtocol();

   public void setProtocol(String protocol);
   
   /**
    * @return the publicIp
    */
   public String getPublicIp();
   
   /**
    * @param publicIp the publicIp to set
    */
   public void setPublicIp(String publicIp);
   
   /**
    * @return the publicPort
    */
   public String getPublicPort();
   
   /**
    * @param publicPort the publicPort to set
    */
   public void setPublicPort(String publicPort);
   
   /**
    * @return the privateIp
    */
   public String getPrivateIp();
   
   /**
    * @param privateIp the privateIp to set
    */
   public void setPrivateIp(String privateIp);
   
   /**
    * @return the privatePort
    */
   public String getPrivatePort();
   
   /**
    * @param privatePort the privatePort to set
    */
   public void setPrivatePort(String privatePort);

}
