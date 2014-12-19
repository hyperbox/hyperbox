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

package org.altherian.hboxd.hypervisor.storage;

import org.altherian.hboxd.hypervisor._RawItem;
import org.altherian.hboxd.hypervisor.vm._RawVM;
import org.altherian.hboxd.task._ProgressTracker;
import java.util.Set;

public interface _RawMedium extends _RawItem {
   
   public String getUuid();
   
   public void setUuid(String newUuid);
   
   public void generateUuid();
   
   public String getDescription();
   
   public void setDescription(String desc);
   
   public String getState();
   
   public String getVariant();
   
   public String getLocation();
   
   public void setLocation(String path);
   
   public String getName();
   
   public String getDeviceType();
   
   /**
    * Get size In bytes
    * 
    * @return size in bytes
    */
   public long getSize();
   
   public String getFormat();
   
   public String getMediumFormat();
   
   public String getType();
   
   public void setType(String type);
   
   public boolean hasParent();
   
   public _RawMedium getParent();
   
   public boolean hasChild();
   
   public Set<_RawMedium> getChild();
   
   public _RawMedium getBase();
   
   public boolean isReadOnly();
   
   public long getLogicalSize();
   
   public boolean isAutoReset();
   
   public String lastAccessError();
   
   public Set<_RawVM> getLinkedMachines();
   
   public void close();
   
   public void refresh();
   
   public _ProgressTracker clone(String path);
   
   public _ProgressTracker clone(_RawMedium toMedium);
   
   public _ProgressTracker clone(String path, String variantType);
   
   public _ProgressTracker clone(_RawMedium toMedium, String variantType);
   
   public _ProgressTracker compact();
   
   /**
    * @param size in bytes
    * @return progress tracking object
    */
   public _ProgressTracker create(long size);
   
   /**
    * @param size in bytes
    * @param variantType see Virtualbox MediumVariant
    * @return progress tracking object
    */
   public _ProgressTracker create(long size, String variantType);
   
   /**
    * @param size in bytes
    * @return progress tracking object
    */
   public _ProgressTracker resize(long size);
   
   /**
    * Only for diff storage
    */
   public void reset();
   
}
