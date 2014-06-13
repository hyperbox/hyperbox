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

package org.altherian.hboxc.comm.input;

import org.altherian.hbox.comm.input.ObjectInput;
import org.altherian.hbox.comm.input.UserInput;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hboxc.constant.ConnectorAttributes;

public class ConnectorInput extends ObjectInput {
   
   public ConnectorInput() {
      
   }
   
   public ConnectorInput(String id) {
      super(id);
   }
   
   public String getAddress() {
      return getSetting(ConnectorAttributes.Address).getString();
   }
   
   public void setAddress(String address) {
      setSetting(new StringSettingIO(ConnectorAttributes.Address, address));
   }
   
   public String getBackendId() {
      return getSetting(ConnectorAttributes.BackendId).getString();
   }
   
   public void setBackendId(String backendId) {
      setSetting(new StringSettingIO(ConnectorAttributes.BackendId, backendId));
   }
   
   public UserInput getCredentials() {
      return (UserInput) getSetting(ConnectorAttributes.Credentials).getRawValue();
   }
   
   public void setCredentials(UserInput usrIn) {
      setSetting(new SettingIO(ConnectorAttributes.Credentials, usrIn));
   }
   
}
