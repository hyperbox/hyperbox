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

package org.altherian.hboxc.front.gui.builder;

import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.out.hypervisor.SnapshotOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hbox.constant.StorageControllerType;
import org.altherian.hbox.states.MachineStates;
import org.altherian.hboxc.comm.output.ConnectorOutput;
import org.altherian.hboxc.controller.ClientTasks;
import org.altherian.tool.logging.Logger;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class IconBuilder {
   
   private final static String ICONS_PATH = "icons/";
   private final static ImageIcon unknownElement = new ImageIcon(ICONS_PATH + "help.png");
   
   private static ImageIcon hbIcon = new ImageIcon(ICONS_PATH + "hyperbox-icon_16px.png");
   private static ImageIcon hbLogo = new ImageIcon(ICONS_PATH + "hyperbox-logo.png");
   private static ImageIcon loginHeader = new ImageIcon(ICONS_PATH + "login-header.png");
   
   public static final ImageIcon AddIcon = new ImageIcon(ICONS_PATH + "add.png");
   public static final ImageIcon DelIcon = new ImageIcon(ICONS_PATH + "delete.png");
   
   private static Map<HypervisorTasks, ImageIcon> vbTasks;
   private static Map<HyperboxTasks, ImageIcon> hbTasks;
   private static Map<ClientTasks, ImageIcon> clientTasks;
   
   private static Map<MachineStates, ImageIcon> machineStates;
   
   private static Map<String, ImageIcon> entTypes;
   private static ImageIcon unknownEntType;
   
   private static Map<String, ImageIcon> scTypes;
   private static ImageIcon unknownScType;
   
   private IconBuilder() {
      // static only
   }
   
   static {
      initClientTasks();
      initHbTasks();
      initVbTasks();
      initMachineStates();
      initStorageControllerTypes();
      initEntityTypes();
   }
   
   private static void initClientTasks() {
      clientTasks = new EnumMap<ClientTasks, ImageIcon>(ClientTasks.class);
      clientTasks.put(ClientTasks.ConnectorConnect, new ImageIcon(ICONS_PATH + "server_connect.png"));
      clientTasks.put(ClientTasks.ConnectorGet, new ImageIcon(ICONS_PATH + "details.png"));
      clientTasks.put(ClientTasks.NotificationClose, new ImageIcon(ICONS_PATH + "cross.png"));
      clientTasks.put(ClientTasks.Exit, new ImageIcon(ICONS_PATH + "exit.png"));
   }
   
   private static void initHbTasks() {
      hbTasks = new EnumMap<HyperboxTasks, ImageIcon>(HyperboxTasks.class);
      hbTasks.put(HyperboxTasks.UserCreate, new ImageIcon(ICONS_PATH + "user_create.png"));
      hbTasks.put(HyperboxTasks.UserDelete, new ImageIcon(ICONS_PATH + "user_delete.png"));
      hbTasks.put(HyperboxTasks.UserModify, new ImageIcon(ICONS_PATH + "user_edit.png"));
      
      hbTasks.put(HyperboxTasks.StoreGet, new ImageIcon(ICONS_PATH + "store_browse.png"));
      hbTasks.put(HyperboxTasks.StoreCreate, new ImageIcon(ICONS_PATH + "store_create.png"));
      hbTasks.put(HyperboxTasks.StoreDelete, new ImageIcon(ICONS_PATH + "store_delete.png"));
      hbTasks.put(HyperboxTasks.StoreRegister, new ImageIcon(ICONS_PATH + "store_register.png"));
      hbTasks.put(HyperboxTasks.StoreUnregister, new ImageIcon(ICONS_PATH + "store_unregister.png"));
   }
   
   private static void initVbTasks() {
      vbTasks = new EnumMap<HypervisorTasks, ImageIcon>(HypervisorTasks.class);
      vbTasks.put(HypervisorTasks.MachineAcpiPowerButton, new ImageIcon(ICONS_PATH + "control_power_blue.png"));
      vbTasks.put(HypervisorTasks.MachineCreate, new ImageIcon(ICONS_PATH + "computer_add.png"));
      vbTasks.put(HypervisorTasks.MachineDelete, new ImageIcon(ICONS_PATH + "computer_delete.png"));
      vbTasks.put(HypervisorTasks.MachineModify, new ImageIcon(ICONS_PATH + "computer_edit.png"));
      vbTasks.put(HypervisorTasks.MachineRegister, new ImageIcon(ICONS_PATH + "computer_link.png"));
      vbTasks.put(HypervisorTasks.MachineReset, new ImageIcon(ICONS_PATH + "reload.png"));
      vbTasks.put(HypervisorTasks.MachinePowerOn, new ImageIcon(ICONS_PATH + "control_play_blue.png"));
      vbTasks.put(HypervisorTasks.MachinePowerOff, new ImageIcon(ICONS_PATH + "control_power.png"));
      vbTasks.put(HypervisorTasks.MachineUnregister, new ImageIcon(ICONS_PATH + "computer_stop.png"));
      
      vbTasks.put(HypervisorTasks.SnapshotDelete, new ImageIcon(ICONS_PATH + "camera_stop.png"));
      vbTasks.put(HypervisorTasks.SnapshotGet, new ImageIcon(ICONS_PATH + "camera.png"));
      vbTasks.put(HypervisorTasks.SnapshotGetRoot, new ImageIcon(ICONS_PATH + "camera.png"));
      vbTasks.put(HypervisorTasks.SnapshotModify, new ImageIcon(ICONS_PATH + "camera_edit.png"));
      vbTasks.put(HypervisorTasks.SnapshotRestore, new ImageIcon(ICONS_PATH + "camera_go.png"));
      vbTasks.put(HypervisorTasks.SnapshotTake, new ImageIcon(ICONS_PATH + "camera_add.png"));
      
      vbTasks.put(HypervisorTasks.StorageControllerAdd, new ImageIcon(ICONS_PATH + "tab_add.png"));
      vbTasks.put(HypervisorTasks.StorageControllerRemove, new ImageIcon(ICONS_PATH + "tab_delete.png"));
      vbTasks.put(HypervisorTasks.StorageControllerMediumAttachmentAdd, new ImageIcon(ICONS_PATH + "drive_add.png"));
      vbTasks.put(HypervisorTasks.StorageControllerMediumAttachmentRemove, new ImageIcon(ICONS_PATH + "drive_delete.png"));
      
      vbTasks.put(HypervisorTasks.MediumMount, new ImageIcon(ICONS_PATH + "cd_add.png"));
      vbTasks.put(HypervisorTasks.MediumUnmount, new ImageIcon(ICONS_PATH + "cd_delete.png"));
      vbTasks.put(HypervisorTasks.MediumCreate, new ImageIcon(ICONS_PATH + "database_add.png"));
      vbTasks.put(HypervisorTasks.MediumRegister, new ImageIcon(ICONS_PATH + "database_link.png"));
      vbTasks.put(HypervisorTasks.MediumModify, new ImageIcon(ICONS_PATH + "cd_modify.png"));
      
      vbTasks.put(HypervisorTasks.NetAdaptorAdd, new ImageIcon(ICONS_PATH + "add.png"));
      vbTasks.put(HypervisorTasks.NetAdaptorModify, new ImageIcon(ICONS_PATH + "edit.png"));
      vbTasks.put(HypervisorTasks.NetAdaptorRemove, new ImageIcon(ICONS_PATH + "cross.png"));
   }
   
   private static void initMachineStates() {
      machineStates = new EnumMap<MachineStates, ImageIcon>(MachineStates.class);
      
      machineStates.put(MachineStates.Aborted, new ImageIcon(ICONS_PATH + "cross.png"));
      machineStates.put(MachineStates.Inaccessible, new ImageIcon(ICONS_PATH + "delete.png"));
      machineStates.put(MachineStates.Paused, new ImageIcon(ICONS_PATH + "pause_blue.png"));
      machineStates.put(MachineStates.PoweredOff, new ImageIcon(ICONS_PATH + "stop_red.png"));
      machineStates.put(MachineStates.Restoring, new ImageIcon(ICONS_PATH + "disk_upload.png"));
      machineStates.put(MachineStates.Starting, new ImageIcon(ICONS_PATH + "play_blue.png"));
      machineStates.put(MachineStates.Running, new ImageIcon(ICONS_PATH + "play_green.png"));
      machineStates.put(MachineStates.Saved, new ImageIcon(ICONS_PATH + "disk.png"));
      machineStates.put(MachineStates.Saving, new ImageIcon(ICONS_PATH + "disk_download.png"));
      machineStates.put(MachineStates.Stuck, new ImageIcon(ICONS_PATH + "delete.png"));
   }
   
   private static void initStorageControllerTypes() {
      scTypes = new HashMap<String, ImageIcon>();
      unknownScType = new ImageIcon(ICONS_PATH + "help.png");
      
      scTypes.put(StorageControllerType.Floppy.getId(), new ImageIcon(ICONS_PATH + "controller.png"));
      scTypes.put(StorageControllerType.IDE.getId(), new ImageIcon(ICONS_PATH + "controller.png"));
      scTypes.put(StorageControllerType.SAS.getId(), new ImageIcon(ICONS_PATH + "controller.png"));
      scTypes.put(StorageControllerType.SATA.getId(), new ImageIcon(ICONS_PATH + "controller.png"));
      scTypes.put(StorageControllerType.SCSI.getId(), new ImageIcon(ICONS_PATH + "controller.png"));
   }
   
   private static void initEntityTypes() {
      entTypes = new HashMap<String, ImageIcon>();
      entTypes.put(EntityType.Hyperbox.getId(), getHyperbox());
      entTypes.put(EntityType.Guest.getId(), new ImageIcon(ICONS_PATH + "monitor.png"));
      entTypes.put(EntityType.Machine.getId(), new ImageIcon(ICONS_PATH + "computer.png"));
      entTypes.put(EntityType.DVD.getId(), new ImageIcon(ICONS_PATH + "cd.png"));
      entTypes.put(EntityType.HardDisk.getId(), new ImageIcon(ICONS_PATH + "harddisk.png"));
      entTypes.put(EntityType.Floppy.getId(), new ImageIcon(ICONS_PATH + "disk.png"));
      entTypes.put(EntityType.Server.getId(), new ImageIcon(ICONS_PATH + "server.png"));
      entTypes.put(EntityType.Display.getId(), new ImageIcon(ICONS_PATH + "monitor.png"));
      entTypes.put(EntityType.CPU.getId(), new ImageIcon(ICONS_PATH + "shape_shadow.png"));
      entTypes.put(EntityType.Audio.getId(), new ImageIcon(ICONS_PATH + "sound.png"));
      entTypes.put(EntityType.Network.getId(), new ImageIcon(ICONS_PATH + "network.png"));
      entTypes.put(EntityType.DiskDrive.getId(), new ImageIcon(ICONS_PATH + "drive.png"));
      entTypes.put(EntityType.DvdDrive.getId(), new ImageIcon(ICONS_PATH + "drive_cd.png"));
      entTypes.put(EntityType.FloppyDrive.getId(), new ImageIcon(ICONS_PATH + "drive_disk.png"));
      entTypes.put(EntityType.User.getId(), new ImageIcon(ICONS_PATH + "user.png"));
      entTypes.put(EntityType.Store.getId(), new ImageIcon(ICONS_PATH + "store.png"));
      entTypes.put(EntityType.Task.getId(), new ImageIcon(ICONS_PATH + "task.png"));
   }
   
   public static ImageIcon getHyperbox() {
      return hbIcon;
   }
   
   public static ImageIcon getLogo() {
      return hbLogo;
   }
   
   public static ImageIcon getLoginHeader() {
      return loginHeader;
   }
   
   public static ImageIcon getTask(HypervisorTasks task) {
      if (vbTasks.containsKey(task)) {
         return vbTasks.get(task);
      } else {
         Logger.debug("No icon found for VirtualboxTask: " + task);
         return unknownElement;
      }
   }
   
   public static ImageIcon getTask(HyperboxTasks task) {
      if (hbTasks.containsKey(task)) {
         return hbTasks.get(task);
      } else {
         Logger.debug("No icon found for HyperboxTask: " + task);
         return unknownElement;
      }
   }
   
   public static ImageIcon getTask(ClientTasks task) {
      if ((task != null) && clientTasks.containsKey(task)) {
         return clientTasks.get(task);
      } else {
         Logger.debug("No icon found for ClientTask: " + task);
         return unknownElement;
      }
   }
   
   public static ImageIcon getMachineState(String state) {
      if (state == null) {
         return unknownElement;
      }
      
      try {
         return getMachineState(MachineStates.valueOf(state));
      }
      // TODO catch the proper exception
      catch (Throwable t) {
         return unknownElement;
      }
   }
   
   public static ImageIcon getMachineState(MachineStates state) {
      if ((state != null) && machineStates.containsKey(state)) {
         return machineStates.get(state);
      } else {
         Logger.debug("No icon found for Machine State: " + state);
         return unknownElement;
      }
   }
   
   public static ImageIcon getStorageControllerType(String type) {
      if ((type != null) && scTypes.containsKey(type)) {
         return scTypes.get(type);
      } else {
         Logger.debug("No icon found for Storage Controller Type: " + type);
         return unknownScType;
      }
   }
   
   public static ImageIcon getEntityType(EntityType type) {
      return getEntityType(type.getId());
   }
   
   public static ImageIcon getEntityType(String type) {
      if ((type != null) && entTypes.containsKey(type)) {
         return entTypes.get(type);
      } else {
         Logger.debug("No icon found for Entity Type: " + type);
         return unknownEntType;
      }
   }
   
   public static ImageIcon getDeviceType(String type) {
      return getEntityType(EntityType.valueOf(type));
   }
   
   public static ImageIcon getSnapshot(SnapshotOut snapOut) {
      if (snapOut.isOnline()) {
         return new ImageIcon(ICONS_PATH + "camera_start.png");
      } else {
         return new ImageIcon(ICONS_PATH + "camera_stop.png");
      }
   }
   
   public static ImageIcon getConnector(ConnectorOutput conOut) {
      if (conOut.isConnected()) {
         if (conOut.getServer().isHypervisorConnected()) {
            return new ImageIcon(ICONS_PATH + "server_start.png");
         } else {
            return new ImageIcon(ICONS_PATH + "server_stop.png");
         }
      } else {
         return new ImageIcon(ICONS_PATH + "disconnect.png");
      }
   }
   
   public static ImageIcon getSettings() {
      return new ImageIcon(ICONS_PATH + "computer_wrench.png");
   }
   
}
