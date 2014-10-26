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

package org.altherian.vbox4_2.vm;

import org.altherian.hbox.constant.SnapshotAttribute;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.snapshot.SnapshotChangedEvent;
import org.altherian.hboxd.hypervisor.vm.snapshot._RawSnapshot;
import org.altherian.hboxd.settings._Setting;
import org.altherian.vbox.settings.snapshot.SnapshotDescriptionSetting;
import org.altherian.vbox.settings.snapshot.SnapshotNameSetting;
import org.altherian.vbox4_2.VBox;
import org.altherian.vbox4_2.manager.VBoxSettingManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.virtualbox_4_2.IMachine;
import org.virtualbox_4_2.ISnapshot;

public class VBoxSnapshot implements _RawSnapshot {
   
   private String uuid;
   private String machineUuid;
   
   public VBoxSnapshot(ISnapshot rawSnap) {
      uuid = rawSnap.getId();
      machineUuid = rawSnap.getMachine().getId();
   }
   
   private IMachine getVm() {
      return VBox.get().findMachine(getMachineId());
   }
   
   private ISnapshot getSnap() {
      return getVm().findSnapshot(getUuid());
   }
   
   @Override
   public List<_Setting> listSettings() {
      return VBoxSettingManager.list(this);
   }
   
   @Override
   public _Setting getSetting(Object getName) {
      return VBoxSettingManager.get(this, getName);
   }
   
   @Override
   public void setSetting(_Setting s) {
      setSetting(Arrays.asList(s));
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      VBoxSettingManager.set(this, s);
      EventManager.post(new SnapshotChangedEvent(uuid, machineUuid));
   }
   
   @Override
   public String getUuid() {
      return uuid;
   }
   
   @Override
   public String getName() {
      return getSetting(SnapshotAttribute.Name).getString();
   }
   
   @Override
   public void setName(String name) {
      setSetting(new SnapshotNameSetting(name));
   }
   
   @Override
   public String getDescription() {
      return getSetting(SnapshotAttribute.Description).getString();
   }
   
   @Override
   public void setDescription(String description) {
      setSetting(new SnapshotDescriptionSetting(description));
   }
   
   @Override
   public Date getCreationTime() {
      return new Date(getSetting(SnapshotAttribute.CreationTime).getNumber());
   }
   
   @Override
   public boolean isOnline() {
      return getSetting(SnapshotAttribute.IsOnline).getBoolean();
   }
   
   @Override
   public String getMachineId() {
      return machineUuid;
   }
   
   @Override
   public boolean hasParent() {
      return getSetting(SnapshotAttribute.HasParent).getBoolean();
   }
   
   @Override
   public _RawSnapshot getParent() {
      if (hasParent()) {
         return new VBoxSnapshot(getSnap().getParent());
      } else {
         return null;
      }
   }
   
   @Override
   public boolean hasChildren() {
      return getSetting(SnapshotAttribute.HasChildren).getBoolean();
   }
   
   @Override
   public List<_RawSnapshot> getChildren() {
      List<_RawSnapshot> snaps = new ArrayList<_RawSnapshot>();
      for (ISnapshot snap : getSnap().getChildren()) {
         snaps.add(new VBoxSnapshot(snap));
      }
      return snaps;
   }
   
}
