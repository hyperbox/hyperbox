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

package org.altherian.hboxd.vbox.hypervisor.ws4_3.vm;

import org.altherian.hbox.constant.SnapshotAttributes;
import org.altherian.hbox.settings.vbox.snapshot.SnapshotDescriptionSetting;
import org.altherian.hbox.settings.vbox.snapshot.SnapshotNameSetting;
import org.altherian.hboxd.event.EventManager;
import org.altherian.hboxd.event.snapshot.SnapshotChangedEvent;
import org.altherian.hboxd.hypervisor.vm.snapshot._RawSnapshot;
import org.altherian.hboxd.settings._Setting;
import org.altherian.hboxd.vbox4_3.ws.factory.ConnectionManager;
import org.altherian.hboxd.vbox4_3.ws.manager.VbSettingManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.virtualbox_4_3.IMachine;
import org.virtualbox_4_3.ISnapshot;

public class VBoxSnapshot implements _RawSnapshot {
   
   private String uuid;
   private String machineUuid;
   
   public VBoxSnapshot(ISnapshot rawSnap) {
      uuid = rawSnap.getId();
      machineUuid = rawSnap.getMachine().getId();
   }
   
   private IMachine getVm() {
      return ConnectionManager.findMachine(getMachineId());
   }
   
   private ISnapshot getSnap() {
      return getVm().findSnapshot(getUuid());
   }
   
   @Override
   public List<_Setting> listSettings() {
      return VbSettingManager.list(this);
   }
   
   @Override
   public _Setting getSetting(Object getName) {
      return VbSettingManager.get(this, getName);
   }
   
   @Override
   public void setSetting(_Setting s) {
      setSetting(Arrays.asList(s));
   }
   
   @Override
   public void setSetting(List<_Setting> s) {
      VbSettingManager.set(this, s);
      EventManager.post(new SnapshotChangedEvent(uuid, machineUuid));
   }
   
   @Override
   public String getUuid() {
      return uuid;
   }
   
   @Override
   public String getName() {
      return getSetting(SnapshotAttributes.Name).getString();
   }
   
   @Override
   public void setName(String name) {
      setSetting(new SnapshotNameSetting(name));
   }
   
   @Override
   public String getDescription() {
      return getSetting(SnapshotAttributes.Description).getString();
   }
   
   @Override
   public void setDescription(String description) {
      setSetting(new SnapshotDescriptionSetting(description));
   }
   
   @Override
   public Date getCreationTime() {
      return new Date(getSetting(SnapshotAttributes.CreationTime).getNumber());
   }
   
   @Override
   public boolean isOnline() {
      return getSetting(SnapshotAttributes.IsOnline).getBoolean();
   }
   
   @Override
   public String getMachineId() {
      return machineUuid;
   }
   
   @Override
   public boolean hasParent() {
      return getSetting(SnapshotAttributes.HasParent).getBoolean();
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
      return getSetting(SnapshotAttributes.HasChildren).getBoolean();
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
