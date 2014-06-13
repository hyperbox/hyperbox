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

package org.altherian.hboxd.core.model;

import java.util.Set;

public interface _StorageController extends _Device {
   
   @Override
   public String getId();
   
   public String getMachineUuid();
   
   public String getName();
   
   public void setName(String name);
   
   /**
    * IDE, SATA, ...
    * 
    * @return ID for the type
    */
   public String getControllerType();
   
   /**
    * AHCI, etc
    * 
    * @return ID for the precise type
    */
   public String getControllerSubType();
   
   /**
    * AHCI, etc
    * 
    * @param subType ID for the precise type
    */
   public void setSubType(String subType);
   
   public long getPortCount();
   
   public void setPortCount(long portCount);
   
   public long getMaxPortCount();
   
   public long getMaxDeviceCount();
   
   public void attachDevice(String deviceId, long portNb, long deviceNb);
   
   public void detachDevice(long portNb, long deviceNb);
   
   public Set<_Medium> listMedium();
   
   public Set<_MediumAttachment> listMediumAttachment();
   
   public void attachMedium(_Medium medium);
   
   public void attachMedium(_Medium medium, long portNb, long deviceNb);
   
   public void detachMedium(_Medium medium);
   
   /**
    * Will force if the media is locked
    * 
    * @param portNb The port number to use
    * @param deviceNb the device number to use
    */
   public void detachMedium(long portNb, long deviceNb);
   
   public boolean isSlotTaken(long portNb, long deviceNb);
   
   public _MediumAttachment getMediumAttachment(long portNb, long deviceNb);

}
