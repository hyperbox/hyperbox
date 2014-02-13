package org.altherian.hboxd.vbox.hypervisor.xpcom4_4.vm.guest;

import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.hypervisor.vm.guest._RawGuest;
import org.altherian.hboxd.hypervisor.vm.guest._RawGuestNetworkInterface;
import org.altherian.hboxd.hypervisor.vm.guest._RawHypervisorTools;
import org.altherian.hboxd.vbox.hypervisor.xpcom4_4.vm.VBoxMachine;
import org.altherian.hboxd.vbox4_4.xpcom.factory.ConnectionManager;
import org.altherian.hboxd.vbox4_4.xpcom.manager.VbSessionManager;
import org.altherian.tool.StringTools;

import java.util.ArrayList;
import java.util.List;

import org.virtualbox_4_4.AdditionsRunLevelType;
import org.virtualbox_4_4.IMachine;
import org.virtualbox_4_4.ISession;
import org.virtualbox_4_4.LockType;
import org.virtualbox_4_4.VBoxException;

public class VBoxGuest implements _RawGuest {
   
   private String machineUuid;
   private ISession session;
   
   public VBoxGuest(VBoxMachine vm) {
      machineUuid = vm.getUuid();
   }
   
   private IMachine getVm() {
      return ConnectionManager.getBox().findMachine(machineUuid);
   }
   
   private void lockAuto() {
      session = VbSessionManager.get().lockAuto(machineUuid, LockType.Shared);
   }
   
   private void unlockAuto() {
      unlockAuto(false);
   }
   
   private void unlockAuto(boolean saveSettings) {
      VbSessionManager.get().unlockAuto(machineUuid, saveSettings);
      session = null;
   }
   
   @Override
   public boolean hasHypervisorTools() {
      lockAuto();
      try {
         return !session.getConsole().getGuest().getAdditionsStatus(AdditionsRunLevelType.None);
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      } finally {
         unlockAuto();
      }
   }
   
   @Override
   public _RawHypervisorTools getHypervisorTools() {
      // TODO Auto-generated method stub
      lockAuto();
      try {
         return null;
      } catch (VBoxException e) {
         throw new HyperboxRuntimeException(e);
      } finally {
         unlockAuto();
      }
   }
   
   private int getNicCount() {
      return Integer.parseInt(StringTools.getNonEmpty(getVm().getGuestPropertyValue("/VirtualBox/GuestInfo/Net/Count"), "0"));
   }
   
   @Override
   public List<_RawGuestNetworkInterface> listNetworkInterfaces() {
      List<_RawGuestNetworkInterface> list = new ArrayList<_RawGuestNetworkInterface>();
      for (int i = 0; i < getNicCount(); i++) {
         list.add(new VBoxGuestNetworkInterface(machineUuid, i));
      }
      return list;
   }
   
   @Override
   public _RawGuestNetworkInterface getNetworkInterfaceById(String id) {
      // TODO check if ID is valid
      return new VBoxGuestNetworkInterface(machineUuid, Integer.parseInt(id));
   }
   
   @Override
   public _RawGuestNetworkInterface getNetworkInterfaceByMac(String macAddress) {
      macAddress = macAddress.replace(":", "").toUpperCase();
      for (int i = 0; i < getNicCount(); i++) {
         if (getVm().getGuestPropertyValue("/VirtualBox/GuestInfo/Net/" + i + "/MAC").contentEquals(macAddress.toUpperCase())) {
            return new VBoxGuestNetworkInterface(machineUuid, i);
         }
      }
      throw new HyperboxRuntimeException("No NIC matching MAC " + macAddress);
   }
   
}
