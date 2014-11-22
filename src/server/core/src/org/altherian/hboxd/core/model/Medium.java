/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.exception.FeatureNotImplementedException;
import org.altherian.hboxd.hypervisor._Hypervisor;
import org.altherian.hboxd.hypervisor.storage._RawMedium;
import org.altherian.hboxd.server._Server;
import org.altherian.hboxd.task._ProgressTracker;
import org.altherian.setting._Setting;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Medium implements _Medium {
   
   private _Server server;
   private _Hypervisor hypervisor;
   private _RawMedium rawMed;
   
   public Medium(_Server server, _Hypervisor hypervisor, _RawMedium rawMed) {
      this.server = server;
      this.hypervisor = hypervisor;
      this.rawMed = rawMed;
   }
   
   @Override
   public List<_Setting> getSettings() {
      return rawMed.listSettings();
   }
   
   @Override
   public _Setting getSetting(String settingId) {
      return rawMed.getSetting(settingId);
   }
   
   @Override
   public void setSetting(_Setting setting) {
      rawMed.setSetting(setting);
   }
   
   @Override
   public void setSetting(List<_Setting> settings) {
      rawMed.setSetting(settings);
   }
   
   @Override
   public boolean hasSetting(String settingId) {
      try {
         rawMed.getSetting(settingId);
         return true;
      } catch (Throwable t) {
         // TODO catch better
         return false;
      }
   }
   
   @Override
   public String getId() {
      return server.getId() + "/" + EntityType.Medium + "/" + rawMed.getUuid();
   }
   
   @Override
   public String getUuid() {
      return rawMed.getUuid();
   }

   @Override
   public String getDescription() {
      return rawMed.getDescription();
   }
   
   @Override
   public void setDescription(String desc) {
      rawMed.setDescription(desc);
   }
   
   @Override
   public String getState() {
      return rawMed.getState();
   }
   
   @Override
   public String getVariant() {
      return rawMed.getVariant();
   }
   
   @Override
   public String getLocation() {
      return rawMed.getLocation();
   }
   
   @Override
   public void setLocation(String path) {
      rawMed.setLocation(path);
   }
   
   @Override
   public String getName() {
      return rawMed.getName();
   }
   
   @Override
   public String getDeviceType() {
      return rawMed.getDeviceType();
   }
   
   @Override
   public long getSize() {
      return rawMed.getSize();
   }
   
   @Override
   public String getFormat() {
      return rawMed.getFormat();
   }
   
   @Override
   public String getMediumFormat() {
      return rawMed.getMediumFormat();
   }
   
   @Override
   public String getType() {
      return rawMed.getType();
   }
   
   @Override
   public void setType(String type) {
      rawMed.setType(type);
   }
   
   @Override
   public boolean hasParent() {
      return rawMed.hasParent();
   }
   
   @Override
   public _Medium getParent() {
      return new Medium(server, hypervisor, rawMed.getParent());
   }
   
   @Override
   public boolean hasChild() {
      return rawMed.hasChild();
   }
   
   @Override
   public Set<_Medium> getChild() {
      Set<_Medium> mediumList = new HashSet<_Medium>();
      for (_RawMedium med : rawMed.getChild()) {
         mediumList.add(new Medium(server, hypervisor, med));
      }
      return mediumList;
   }
   
   @Override
   public _Medium getBase() {
      return new Medium(server, hypervisor, rawMed.getBase());
   }
   
   @Override
   public boolean isReadOnly() {
      return rawMed.isReadOnly();
   }
   
   @Override
   public long getLogicalSize() {
      return rawMed.getLogicalSize();
   }
   
   @Override
   public boolean isAutoReset() {
      return rawMed.isAutoReset();
   }
   
   @Override
   public String lastAccessError() {
      return "";
   }
   
   @Override
   public Set<_Machine> getLinkedMachines() {
      // TODO Auto-generated method stub
      return new HashSet<_Machine>();
   }
   
   @Override
   public void close() {
      rawMed.close();
   }
   
   @Override
   public _ProgressTracker delete() {
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public _ProgressTracker deleteForce() {
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public void refresh() {
      // TODO Auto-generated method stub
      rawMed.refresh();
   }
   
   @Override
   public _ProgressTracker clone(String path) {
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public _ProgressTracker clone(_RawMedium toMedium) {
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public _ProgressTracker clone(String path, String variantType) {
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public _ProgressTracker clone(_RawMedium toMedium, String variantType) {
      throw new FeatureNotImplementedException();
   }
   
   @Override
   public _ProgressTracker compact() {
      return rawMed.compact();
   }
   
   @Override
   public _ProgressTracker create(long size) {
      return rawMed.create(size);
   }
   
   @Override
   public _ProgressTracker create(long size, String variantType) {
      return rawMed.create(size, variantType);
   }
   
   @Override
   public _ProgressTracker resize(long size) {
      return rawMed.resize(size);
   }
   
   @Override
   public void reset() {
      rawMed.reset();
   }
   
   
}
