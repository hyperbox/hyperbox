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

package org.altherian.hboxc.front.gui.builder;

import org.altherian.hbox.comm.output.ServerOutput;
import org.altherian.hbox.comm.output.StoreOutput;
import org.altherian.hbox.comm.output.hypervisor.MachineOutput;
import org.altherian.hbox.comm.output.storage.StorageDeviceAttachmentOutput;
import org.altherian.hbox.constant.EntityTypes;
import org.altherian.hbox.states.StoreState;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.front.gui.action.connector.ConnectorConnectAction;
import org.altherian.hboxc.front.gui.action.connector.ConnectorDisconnectAction;
import org.altherian.hboxc.front.gui.action.connector.ConnectorModifyAction;
import org.altherian.hboxc.front.gui.action.connector.ConnectorRemoveAction;
import org.altherian.hboxc.front.gui.action.guest.GuestRestartAction;
import org.altherian.hboxc.front.gui.action.guest.GuestShutdownAction;
import org.altherian.hboxc.front.gui.action.hypervisor.HypervisorConnectAction;
import org.altherian.hboxc.front.gui.action.hypervisor.HypervisorDisconnectAction;
import org.altherian.hboxc.front.gui.action.machine.MachineAcpiPowerAction;
import org.altherian.hboxc.front.gui.action.machine.MachineCreateAction;
import org.altherian.hboxc.front.gui.action.machine.MachineDeleteAction;
import org.altherian.hboxc.front.gui.action.machine.MachineEditAction;
import org.altherian.hboxc.front.gui.action.machine.MachinePauseAction;
import org.altherian.hboxc.front.gui.action.machine.MachineRegisterAction;
import org.altherian.hboxc.front.gui.action.machine.MachineResetAction;
import org.altherian.hboxc.front.gui.action.machine.MachineResumeAction;
import org.altherian.hboxc.front.gui.action.machine.MachineSaveStateAction;
import org.altherian.hboxc.front.gui.action.machine.MachineStartAction;
import org.altherian.hboxc.front.gui.action.machine.MachineStopAction;
import org.altherian.hboxc.front.gui.action.machine.MachineUnregisterAction;
import org.altherian.hboxc.front.gui.action.server.ServerShutdownAction;
import org.altherian.hboxc.front.gui.action.storage.HypervisorToolsMediumAttachAction;
import org.altherian.hboxc.front.gui.action.storage.MediumAttachAction;
import org.altherian.hboxc.front.gui.action.storage.MediumDettachAction;
import org.altherian.hboxc.front.gui.action.store.StoreBrowseAction;
import org.altherian.hboxc.front.gui.action.store.StoreCloseAction;
import org.altherian.hboxc.front.gui.action.store.StoreDeleteAction;
import org.altherian.hboxc.front.gui.action.store.StoreOpenAction;
import org.altherian.hboxc.front.gui.action.store.StoreUnregisterAction;
import org.altherian.hboxc.front.gui.connector._ConnectorSelector;
import org.altherian.hboxc.front.gui.server._ServerSelector;
import org.altherian.hboxc.front.gui.store._StoreSelector;
import org.altherian.hboxc.front.gui.vm._VmSelector;
import org.altherian.tool.logging.Logger;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

public class PopupMenuBuilder {
   
   public static JPopupMenu get(String serverId, StorageDeviceAttachmentOutput sdaOut) {
      JPopupMenu stoMenuActions = new JPopupMenu();
      stoMenuActions.add(new JMenuItem(new HypervisorToolsMediumAttachAction(serverId, sdaOut)));
      stoMenuActions.add(new JMenuItem(new MediumAttachAction(serverId, sdaOut)));
      stoMenuActions.add(new JMenuItem(new MediumDettachAction(serverId, sdaOut, sdaOut.hasMediumInserted())));
      return stoMenuActions;
   }
   
   public static JPopupMenu get(_VmSelector select, MachineOutput mOut) {
      Logger.track();
      
      JMenu machineMenu = new JMenu("Machine");
      machineMenu.setIcon(IconBuilder.getEntityType(EntityTypes.Machine));
      JMenu guestMenu = new JMenu("Guest");
      machineMenu.setIcon(IconBuilder.getEntityType(EntityTypes.Guest));
      machineMenu.add(new JMenuItem(new MachineStartAction(select)));
      machineMenu.add(new JMenuItem(new MachineStopAction(select)));
      machineMenu.add(new JMenuItem(new MachineResetAction(select)));
      machineMenu.add(new JMenuItem(new MachineAcpiPowerAction(select)));
      machineMenu.add(new JSeparator());
      machineMenu.add(new JMenuItem(new MachineSaveStateAction(select)));
      machineMenu.add(new JMenuItem(new MachinePauseAction(select)));
      machineMenu.add(new JMenuItem(new MachineResumeAction(select)));
      machineMenu.add(new JSeparator());
      machineMenu.add(new JMenuItem(new MachineUnregisterAction(select)));
      machineMenu.add(new JMenuItem(new MachineDeleteAction(select)));
      
      guestMenu.add(new JMenuItem(new GuestRestartAction(select)));
      guestMenu.add(new JMenuItem(new GuestShutdownAction(select)));
      
      JPopupMenu vmPopupMenu = new JPopupMenu();
      vmPopupMenu.add(machineMenu);
      vmPopupMenu.add(guestMenu);
      vmPopupMenu.add(new JMenuItem(new MachineEditAction(select)));
      
      return vmPopupMenu;
   }
   
   public static JPopupMenu get(ServerOutput srvOut) {
      JPopupMenu menu = new JPopupMenu();
      menu.add(new JMenuItem("Not implemented"));
      return menu;
   }
   
   public static JPopupMenu get(_ConnectorSelector conSelect, _ServerSelector srvSelect, ConnectorOutput conOut) {
      JPopupMenu conPopupMenu = new JPopupMenu();
      if (conOut.isConnected()) {
         JMenu srvMenu = new JMenu("Server");
         srvMenu.add(new JMenuItem(new ServerShutdownAction(srvSelect)));
         conPopupMenu.add(srvMenu);
         JMenu hypActions = new JMenu("Hypervisor");
         if (conOut.getServer().isHypervisorConnected()) {
            hypActions.add(new JMenuItem(new HypervisorDisconnectAction(srvSelect)));
         } else {
            hypActions.add(new JMenuItem(new HypervisorConnectAction(srvSelect)));
         }
         conPopupMenu.add(hypActions);
         if (conOut.getServer().isHypervisorConnected()) {
            JMenu vmActions = new JMenu("Machine");
            vmActions.add(new JMenuItem(new MachineCreateAction(srvSelect)));
            vmActions.add(new JMenuItem(new MachineRegisterAction(srvSelect)));
            conPopupMenu.add(vmActions);
         }
      }
      if (conOut.isConnected()) {
         conPopupMenu.add(new JMenuItem(new ConnectorDisconnectAction(conSelect)));
      } else {
         conPopupMenu.add(new JMenuItem(new ConnectorConnectAction(conSelect)));
      }
      conPopupMenu.add(new JMenuItem(new ConnectorModifyAction(conSelect, !conOut.isConnected())));
      conPopupMenu.add(new JMenuItem(new ConnectorRemoveAction(conSelect)));
      
      return conPopupMenu;
   }
   
   public static JPopupMenu get(_StoreSelector stoSelect, StoreOutput stoOut) {
      JPopupMenu actions = new JPopupMenu();
      Action browse = new StoreBrowseAction(stoSelect);
      browse.setEnabled(stoOut.getState().equals(StoreState.Open));
      Action close = new StoreCloseAction(stoSelect);
      close.setEnabled(stoOut.getState().equals(StoreState.Open));
      Action open = new StoreOpenAction(stoSelect);
      open.setEnabled(stoOut.getState().equals(StoreState.Closed));
      Action unregister = new StoreUnregisterAction(stoSelect);
      Action delete = new StoreDeleteAction(stoSelect);
      
      actions.add(new JMenuItem(browse));
      actions.add(new JSeparator(JSeparator.HORIZONTAL));
      actions.add(new JMenuItem(close));
      actions.add(new JMenuItem(open));
      actions.add(new JSeparator(JSeparator.HORIZONTAL));
      actions.add(new JMenuItem(unregister));
      actions.add(new JMenuItem(delete));
      return actions;
   }
   
}
