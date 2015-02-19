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

package org.altherian.hbox.comm.io;

import org.altherian.hbox.hypervisor.net._NATRule;

public class NATRuleIO extends ObjectIO implements _NATRule {

   private boolean enabled;
   private String name;
   private String comment;
   private String protocol;
   private String publicIp;
   private String publicPort;
   private String privateIp;
   private String privatePort;
   
   /**
    * @return the enabled
    */
   @Override
   public boolean isEnabled() {
      return enabled;
   }
   
   /**
    * @param enabled the enabled to set
    */
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }
   
   /**
    * @return the name
    */
   @Override
   public String getName() {
      return name;
   }
   
   /**
    * @param name the name to set
    */
   public void setName(String name) {
      this.name = name;
   }
   
   /**
    * @return the comment
    */
   @Override
   public String getComment() {
      return comment;
   }
   
   /**
    * @param comment the comment to set
    */
   public void setComment(String comment) {
      this.comment = comment;
   }

   public String getProtocol() {
      return protocol;
   }

   public void setProtocol(String protocol) {
      this.protocol = protocol;
   }
   
   /**
    * @return the publicIp
    */
   @Override
   public String getPublicIp() {
      return publicIp;
   }
   
   /**
    * @param publicIp the publicIp to set
    */
   public void setPublicIp(String publicIp) {
      this.publicIp = publicIp;
   }
   
   /**
    * @return the publicPort
    */
   @Override
   public String getPublicPort() {
      return publicPort;
   }
   
   /**
    * @param publicPort the publicPort to set
    */
   public void setPublicPort(String publicPort) {
      this.publicPort = publicPort;
   }
   
   /**
    * @return the privateIp
    */
   @Override
   public String getPrivateIp() {
      return privateIp;
   }
   
   /**
    * @param privateIp the privateIp to set
    */
   public void setPrivateIp(String privateIp) {
      this.privateIp = privateIp;
   }
   
   /**
    * @return the privatePort
    */
   @Override
   public String getPrivatePort() {
      return privatePort;
   }
   
   /**
    * @param privatePort the privatePort to set
    */
   public void setPrivatePort(String privatePort) {
      this.privatePort = privatePort;
   }
   
}
