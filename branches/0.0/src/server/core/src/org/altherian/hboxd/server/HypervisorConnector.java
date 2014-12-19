/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxd.server;

import org.altherian.hboxd.hypervisor._HypervisorConnector;

public class HypervisorConnector implements _HypervisorConnector {
   
   private String hypervisorId;
   private String options;
   private Boolean autoConnect;
   
   public HypervisorConnector(String hypId, String options, Boolean autoConn) {
      setHypervisorId(hypId);
      setOptions(options);
      setAutoConnect(autoConn);
   }
   
   @Override
   public String getHypervisorId() {
      return hypervisorId;
   }
   
   @Override
   public String getOptions() {
      return options;
   }
   
   @Override
   public Boolean isAutoConnect() {
      return autoConnect;
   }
   
   @Override
   public void setHypervisorId(String hypervisorId) {
      this.hypervisorId = hypervisorId;
   }
   
   @Override
   public void setOptions(String options) {
      this.options = options;
   }
   
   @Override
   public void setAutoConnect(Boolean autoConnect) {
      this.autoConnect = autoConnect;
   }
   
}
