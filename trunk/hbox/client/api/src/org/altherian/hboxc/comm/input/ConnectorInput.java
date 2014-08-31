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

import org.altherian.hbox.comm.in.ObjectIn;
import org.altherian.hbox.comm.in.UserIn;
import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.constant.Entity;
import org.altherian.hboxc.constant.ConnectorAttributes;

public class ConnectorInput extends ObjectIn<Entity> {
   
   public ConnectorInput() {
      super(Entity.Connector);
   }
   
   public ConnectorInput(String id) {
      super(Entity.Connector, id);
   }
   
   public String getAddress() {
      return getSetting(ConnectorAttributes.Address).getString();
   }
   
   public void setAddress(String address) {
      setSetting(new StringSettingIO(ConnectorAttributes.Address, address));
   }
   
   public String getLabel() {
      return getSetting(ConnectorAttributes.Label).getString();
   }
   
   public void setLabel(String label) {
      setSetting(new StringSettingIO(ConnectorAttributes.Label, label));
   }
   
   public String getBackendId() {
      return getSetting(ConnectorAttributes.BackendId).getString();
   }
   
   public void setBackendId(String backendId) {
      setSetting(new StringSettingIO(ConnectorAttributes.BackendId, backendId));
   }
   
   public UserIn getCredentials() {
      return (UserIn) getSetting(ConnectorAttributes.Credentials).getRawValue();
   }
   
   public void setCredentials(UserIn usrIn) {
      setSetting(new SettingIO(ConnectorAttributes.Credentials, usrIn));
   }
   
}
