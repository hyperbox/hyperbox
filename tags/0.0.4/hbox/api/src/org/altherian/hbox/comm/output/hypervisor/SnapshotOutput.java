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

package org.altherian.hbox.comm.output.hypervisor;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.ObjectOutput;
import org.altherian.hbox.constant.SnapshotAttributes;

import java.util.ArrayList;
import java.util.List;

public class SnapshotOutput extends ObjectOutput {
   
   private String parentUuid;
   private List<String> childrenUuid;
   
   @SuppressWarnings("unused")
   private SnapshotOutput() {
      // Used for serialization
   }
   
   public SnapshotOutput(String uuid) {
      super(uuid);
      setSetting(new StringSettingIO(SnapshotAttributes.Uuid, uuid));
   }
   
   public SnapshotOutput(String uuid, String name, String desc) {
      this(uuid);
      setSetting(new StringSettingIO(SnapshotAttributes.Name, name));
      setSetting(new StringSettingIO(SnapshotAttributes.Description, desc));
   }
   
   public SnapshotOutput(String uuid, String name, String desc, String parent) {
      this(uuid, name, desc);
      setParent(parent);
   }
   
   public SnapshotOutput(String uuid, String name, String desc, List<String> children) {
      this(uuid, name, desc);
      setChildren(children);
   }
   
   public SnapshotOutput(String uuid, String name, String desc, String parent, List<String> children) {
      this(uuid, name, desc);
      setParent(parent);
      setChildren(children);
   }
   
   public SnapshotOutput(String uuid, List<SettingIO> settings, String parent, List<String> children) {
      super(uuid);
      setSetting(settings);
      setParent(parent);
      setChildren(children);
   }
   
   private void setParent(String parentUuid) {
      this.parentUuid = parentUuid;
   }
   
   private void setChildren(List<String> children) {
      this.childrenUuid = new ArrayList<String>(children);
   }
   
   public String getUuid() {
      return getSetting(SnapshotAttributes.Uuid).getString();
   }
   
   public String getName() {
      return getSetting(SnapshotAttributes.Name).getString();
   }
   
   public String getDescription() {
      return getSetting(SnapshotAttributes.Description).getString();
   }
   
   public boolean hasParent() {
      return parentUuid != null;
   }
   
   public boolean hasChildren() {
      return (childrenUuid != null) && !childrenUuid.isEmpty();
   }
   
   public String getParentUuid() {
      return parentUuid;
   }
   
   public List<String> getChildrenUuid() {
      return new ArrayList<String>(childrenUuid);
   }
   
   public Boolean isOnline() {
      return getSetting(SnapshotAttributes.IsOnline).getBoolean();
   }
   
   /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = (prime * result) + ((getUuid() == null) ? 0 : getUuid().hashCode());
      result = (prime * result) + ((parentUuid == null) ? 0 : parentUuid.hashCode());
      return result;
   }
   
   /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!super.equals(obj)) {
         return false;
      }
      if (!(obj instanceof SnapshotOutput)) {
         return false;
      }
      SnapshotOutput other = (SnapshotOutput) obj;
      if (getId() == null) {
         if (other.getId() != null) {
            return false;
         }
      } else if (!getId().equals(other.getId())) {
         return false;
      }
      if (parentUuid == null) {
         if (other.getParentUuid() != null) {
            return false;
         }
      } else if (!parentUuid.equals(other.getParentUuid())) {
         return false;
      }
      return true;
   }
   
}
