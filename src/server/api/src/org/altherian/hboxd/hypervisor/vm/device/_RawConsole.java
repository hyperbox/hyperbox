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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.hypervisor.vm.device;

import org.altherian.hboxd.hypervisor._RawItem;
import java.util.Set;

public interface _RawConsole extends _RawItem {

   public Boolean isEnabled();

   public void setEnabled(Boolean enable);

   public Boolean isActive();

   public String getAddress();

   public Long getPort();

   public String getProtocol();

   public String getAuthType();

   public void setAuthType(String authType);

   public String getAuthLibrary();

   public void setAuthLibrary(String library);

   public Long getAuthTimeout();

   public void setAuthTimeout(Long timeout);

   public Boolean getAllowMultiConnection();

   public void setAllowMultiConnection(Boolean allow);

   public Set<String> listProperties();

   public boolean hasProperty(String key);

   public String getProperty(String key);

   public void setProperty(String key, String value);

   public void unsetProperty(String key);

   /*
   public Boolean getReuseSingleConnection();
   
   public void setReuseSingleConnection(Boolean reuse);
   
   public String getModule();
   
   public void setModule(String module);
    */

}
